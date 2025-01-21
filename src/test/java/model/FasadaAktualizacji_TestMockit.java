package model;

import mockit.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Klasa testowa dla FasadaAktualizacji zawierająca testy jednostkowe
 * wykorzystujące framework JMockit.
 * W testach wykorzystywane są trzy główne techniki mockowania przy użyciu frameworka JMockit.
 * Podstawowe zależności (fasadaDane, grupaDAO, przedmiotDAO) są wstrzykiwane za pomocą
 * adnotacji @Injectable, a ich zachowanie jest definiowane w blokach Expectations.
 * Dla bardziej złożonych przypadków używany jest MockUp, który pozwala na pełną implementację
 * mockowanych metod z zachowaniem logiki biznesowej. System weryfikacji mocków umożliwia
 * sprawdzenie liczby i sposobu wywołań metod oraz ich parametrów.
 */
@Tag("model")
public class FasadaAktualizacji_TestMockit {

    @Tested
    private FasadaAktualizacji fasada;

    @Injectable
    private FasadaDane fasadaDane;

    @Injectable
    private GrupaDAO grupaDAO;

    @Injectable
    private PrzedmiotDAO przedmiotDAO;

    private Student student;
    private Grupa grupaZrodlowa;
    private Grupa grupaDocelowa;
    private Przedmiot przedmiot;
    private Kierunek kierunek;

    /**
     * Metoda inicjalizująca dane testowe przed każdym testem.
     * Tworzy przykładowego studenta, kierunek, przedmiot oraz grupy zajęciowe.
     */
    @BeforeEach
    void setUp() {
        // Inicjalizacja obiektów
        student = new Student(12345, "Jan", "Kowalski");
        kierunek = new Kierunek("INF", "Informatyka");
        przedmiot = new Przedmiot("Programowanie", "PRG001");
        przedmiot.setIdPrzedmiotu(1);
        Prowadzacy prowadzacy = new Prowadzacy("Pudzian", "Pudzianowski", "gigachad.");

        // Konfiguracja grupy źródłowej
        grupaZrodlowa = new Grupa(
                LocalTime.of(10, 0),
                LocalTime.of(11, 30),
                1,
                prowadzacy,
                20
        );
        grupaZrodlowa.setIdGrupy(1);
        grupaZrodlowa.setRodzajGrupy(RodzajGrupy.CWICZENIA);
        grupaZrodlowa.setPrzedmiot(przedmiot);

        // Konfiguracja grupy docelowej
        grupaDocelowa = new Grupa(
                LocalTime.of(12, 0),
                LocalTime.of(13, 30),
                1,
                prowadzacy,
                20
        );
        grupaDocelowa.setIdGrupy(2);
        grupaDocelowa.setRodzajGrupy(RodzajGrupy.CWICZENIA);
        grupaDocelowa.setPrzedmiot(przedmiot);

        przedmiot.setGrupy(Arrays.asList(grupaZrodlowa, grupaDocelowa));
        kierunek.setPrzedmioty(Arrays.asList(przedmiot));

        // Konfiguracja mocków dla inicjalizacji fasady
        new Expectations() {{
            fasadaDane.pobierzKierunki();
            result = Arrays.asList(kierunek);
            minTimes = 0;

            fasadaDane.pobierzTury(null);
            result = new ArrayList<TuraZapisow>();
            minTimes = 0;

            przedmiotDAO.pobierzGrupy((Przedmiot) any);
            result = Arrays.asList(grupaZrodlowa, grupaDocelowa);
            minTimes = 0;

            przedmiotDAO.pobierzWszystkiePrzedmioty();
            result = Arrays.asList(przedmiot);
            minTimes = 0;

            grupaDAO.pobierzStudentow((Grupa) any);
            result = new ArrayList<Student>();
            minTimes = 0;
        }};
    }

    /**
     * Test sprawdzający pozytywny przypadek przeniesienia studenta między grupami.
     * Weryfikuje:
     * - Poprawne przeniesienie studenta do nowej grupy
     * - Aktualizację liczby zajętych miejsc w obu grupach
     * - Poprawną aktualizację list członków grup
     */
    @Test
    @DisplayName("Przeniesienie studenta - przypadek pozytywny")
    void przeniesStudentaSuccess() {
        // Przygotowanie testów
        List<Grupa> aktualizowaneGrupy = new ArrayList<>();

        new MockUp<GrupaDAO>() {
            @Mock
            public void aktualizujGrupe(Grupa grupa) {
                aktualizowaneGrupy.add(grupa);
            }

            @Mock
            public boolean zapiszStudentaDoGrupy(Grupa grupa, Student st) {
                if (!grupa.getCzlonkowie().contains(st)) {
                    grupa.getCzlonkowie().add(st);
                    st.getGrupy().add(grupa);
                    grupa.setZajeteMiejsca(grupa.getZajeteMiejsca() + 1);
                    return true;
                }
                return false;
            }

            @Mock
            public List<Student> pobierzStudentow(Grupa grupa) {
                return grupa.equals(grupaZrodlowa) ? Arrays.asList(student) : new ArrayList<>();
            }
        };

        // Konfiguracja stanu początkowego
        grupaZrodlowa.dodajStudenta(student);
        student.getGrupy().add(grupaZrodlowa);
        grupaZrodlowa.setZajeteMiejsca(1);
        grupaDocelowa.setZajeteMiejsca(0);

        new Expectations() {{
            fasadaDane.pobierzStudenta(12345);
            result = student;
        }};

        // Wykonanie testu
        boolean result = fasada.przeniesStudenta(1, 2, 12345);

        // Weryfikacja rezultatów
        assertTrue(result);
        assertEquals(0, grupaZrodlowa.getZajeteMiejsca());
        assertEquals(1, grupaDocelowa.getZajeteMiejsca(), "Grupa docelowa powinna mieć dokładnie jedno zajęte miejsce");
        assertTrue(grupaDocelowa.getCzlonkowie().contains(student));
        assertTrue(student.getGrupy().contains(grupaDocelowa));
        assertFalse(grupaZrodlowa.getCzlonkowie().contains(student));
        assertFalse(student.getGrupy().contains(grupaZrodlowa));

        assertEquals(2, aktualizowaneGrupy.size(), "Powinny być zaktualizowane dwie grupy");
        assertTrue(aktualizowaneGrupy.contains(grupaZrodlowa), "Grupa źródłowa powinna być zaktualizowana");
        assertTrue(aktualizowaneGrupy.contains(grupaDocelowa), "Grupa docelowa powinna być zaktualizowana");
    }

    /**
     * Test parametryczny sprawdzający wykrywanie konfliktów czasowych
     * przy przenoszeniu studentów między grupami.
     * Testuje różne kombinacje czasów rozpoczęcia i zakończenia zajęć.
     */
    @ParameterizedTest
    @CsvSource({
            "10:00, 11:30, 12:00, 13:30, false", // Brak konfliktu
            "10:00, 12:30, 11:00, 13:30, true",  // Konflikt z istniejącą grupą
            "09:00, 11:00, 10:00, 12:00, true"   // Konflikt z istniejącą grupą
    })
    @DisplayName("Sprawdzanie konfliktów czasowych")
    void sprawdzanieKonfliktowCzasowych(
            String start1, String koniec1,
            String start2, String koniec2,
            boolean oczekiwanyKonflikt) {

        // Tworzymy dodatkową grupę, którą student już ma w planie
        Grupa istniejacaGrupa = new Grupa(
                LocalTime.parse(start1),
                LocalTime.parse(koniec1),
                3,
                grupaZrodlowa.getProwadzacy(),
                20
        );
        istniejacaGrupa.setIdGrupy(3);
        istniejacaGrupa.setRodzajGrupy(RodzajGrupy.CWICZENIA);
        istniejacaGrupa.setPrzedmiot(new Przedmiot("Inny Przedmiot", "INY001"));

        // Dodajemy studenta do grupy źródłowej
        grupaZrodlowa.dodajStudenta(student);
        student.getGrupy().add(grupaZrodlowa);

        // Dodajemy istniejącą grupę do planu studenta
        student.getGrupy().add(istniejacaGrupa);
        istniejacaGrupa.dodajStudenta(student);

        // Ustawiamy czas grupy docelowej
        grupaDocelowa.setGodzRozpoczecia(LocalTime.parse(start2));
        grupaDocelowa.setGodzZakonczenia(LocalTime.parse(koniec2));

        new Expectations() {{
            fasadaDane.pobierzStudenta(12345);
            result = student;
        }};

        if (oczekiwanyKonflikt) {
            Exception thrown = assertThrows(
                    IllegalStateException.class,
                    () -> fasada.przeniesStudenta(1, 2, 12345)
            );
            assertTrue(thrown.getMessage().contains("konflikt"));
        } else {
            assertDoesNotThrow(() -> fasada.przeniesStudenta(1, 2, 12345));
        }
    }

    /**
     * Test sprawdzający przypadek próby zapisu studenta do grupy,
     * która osiągnęła już limit miejsc.
     */
    @Test
    @DisplayName("Zapis studenta do grupy - grupa pełna")
    void zapiszStudentaDoGrupyPelna() {
        grupaDocelowa.setZajeteMiejsca(grupaDocelowa.getLimitMiejsc());

        new Expectations() {{
            fasadaDane.pobierzStudenta(12345);
            result = student;
            minTimes = 0;

            grupaDAO.zapiszStudentaDoGrupy((Grupa) any, (Student) any);
            result = false;
            minTimes = 0;
        }};

        boolean result = fasada.zapiszStudentaDoGrupy(2, 12345);
        assertFalse(result);
    }

    /**
     * Test sprawdzający zachowanie systemu przy próbie zapisu
     * studenta do grupy, która nie istnieje.
     */
    @Test
    @DisplayName("Próba zapisu do nieistniejącej grupy")
    void zapiszStudentaDoNieistniejacejGrupy() {
        Exception thrown = assertThrows(
                IllegalStateException.class,
                () -> fasada.zapiszStudentaDoGrupy(999, 12345)
        );

        assertTrue(thrown.getMessage().contains("nie istnieje"));
    }
}
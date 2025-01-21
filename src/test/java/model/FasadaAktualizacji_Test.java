package model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Klasa testowa dla FasadaAktualizacji
 * Testuje funkcjonalności związane z przenoszeniem studentów między grupami
 * oraz zapisywaniem studentów do grup
 */
@Tag("model")
@ExtendWith(TestExceptionHandler.class)
public class FasadaAktualizacji_Test {

    private FasadaDane fasadaDane;
    private FasadaAktualizacji fasada;
    private Student student;
    private Grupa grupaZrodlowa;
    private Grupa grupaDocelowa;
    private Przedmiot przedmiot;
    private Kierunek kierunek;

    /**
     * Metoda setUp wykonywana przed każdym testem
     * Inicjalizuje dane testowe: studenta, kierunek, przedmiot, grupy oraz fasady
     */
    @BeforeEach
    void setUp() {
        // Inicjalizacja studenta
        student = new Student(12345, "Jan", "Kowalski");

        // Inicjalizacja kierunku
        kierunek = new Kierunek("INF", "Informatyka");

        // Inicjalizacja przedmiotu
        przedmiot = new Przedmiot("Programowanie", "PRG001");
        przedmiot.setIdPrzedmiotu(1);

        // Inicjalizacja prowadzącego
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

        // Powiązanie przedmiotu z grupami i kierunkiem
        przedmiot.setGrupy(Arrays.asList(grupaZrodlowa, grupaDocelowa));
        kierunek.setPrzedmioty(Arrays.asList(przedmiot));

        // Implementacja fasady danych
        fasadaDane = new FasadaDane() {
            @Override
            public List<Kierunek> pobierzKierunki() {
                return Arrays.asList(kierunek);
            }

            @Override
            public Student pobierzStudenta(int nrIndeksu) {
                return student;
            }

            @Override
            public Grupa pobierzGrupe(int idGrupy) {
                if (idGrupy == grupaZrodlowa.getIdGrupy()) return grupaZrodlowa;
                if (idGrupy == grupaDocelowa.getIdGrupy()) return grupaDocelowa;
                return null;
            }
        };

        fasada = new FasadaAktualizacji(fasadaDane);
    }

    /**
     * Test sprawdzający poprawne przeniesienie studenta między grupami
     * Weryfikuje czy student został usunięty z grupy źródłowej i dodany do grupy docelowej
     */
    @Test
    @DisplayName("Przeniesienie studenta - przypadek pozytywny")
    void przeniesStudentaSuccess() {
        grupaZrodlowa.dodajStudenta(student);
        student.getGrupy().add(grupaZrodlowa);

        boolean result = fasada.przeniesStudenta(
                grupaZrodlowa.getIdGrupy(),
                grupaDocelowa.getIdGrupy(),
                student.getNrAlbumu()
        );

        assertTrue(result);
        assertEquals(0, grupaZrodlowa.getZajeteMiejsca());
        assertEquals(1, grupaDocelowa.getZajeteMiejsca());
    }

    /**
     * Test parametryzowany sprawdzający różne scenariusze konfliktów czasowych
     * między grupami przy przenoszeniu studenta
     * @param startIstniejaca  Godzina rozpoczęcia zajęć grupy już istniejącej w planie studenta
     * @param koniecIstniejaca Godzina zakończenia zajęć grupy już istniejącej w planie studenta
     * @param startDocelowa    Godzina rozpoczęcia zajęć grupy docelowej
     * @param koniecDocelowa   Godzina zakończenia zajęć grupy docelowej
     * @param oczekiwanyKonflikt true jeśli powinien wystąpić konflikt czasowy, false w przeciwnym razie
     */
    @ParameterizedTest
    @CsvSource({
            "10:00, 11:30, 12:00, 13:30, false",  // Brak konfliktu
            "10:00, 12:30, 11:00, 13:30, true",   // Konflikt
            "09:00, 11:00, 10:00, 12:00, true"    // Konflikt
    })
    @DisplayName("Sprawdzanie konfliktów czasowych z istniejącymi grupami")
    void sprawdzanieKonfliktowCzasowych(
            String startIstniejaca, String koniecIstniejaca,
            String startDocelowa, String koniecDocelowa,
            boolean oczekiwanyKonflikt) {

        Grupa grupaIstniejaca = new Grupa(
                LocalTime.parse(startIstniejaca),
                LocalTime.parse(koniecIstniejaca),
                1,
                grupaDocelowa.getProwadzacy(),
                20
        );
        grupaIstniejaca.setIdGrupy(3);
        grupaIstniejaca.setRodzajGrupy(RodzajGrupy.CWICZENIA);
        grupaIstniejaca.setPrzedmiot(przedmiot);

        grupaIstniejaca.dodajStudenta(student);
        student.getGrupy().add(grupaIstniejaca);

        grupaDocelowa.setGodzRozpoczecia(LocalTime.parse(startDocelowa));
        grupaDocelowa.setGodzZakonczenia(LocalTime.parse(koniecDocelowa));

        if (oczekiwanyKonflikt) {
            assertThrows(IllegalStateException.class, () ->
                    fasada.zapiszStudentaDoGrupy(
                            grupaDocelowa.getIdGrupy(),
                            student.getNrAlbumu()
                    )
            );
        } else {
            assertDoesNotThrow(() ->
                    fasada.zapiszStudentaDoGrupy(
                            grupaDocelowa.getIdGrupy(),
                            student.getNrAlbumu()
                    )
            );
        }
    }

    /**
     * Test sprawdzający próbę zapisania studenta do pełnej grupy
     * Weryfikuje czy system prawidłowo obsługuje sytuację, gdy grupa osiągnęła limit miejsc
     */
    @Test
    @DisplayName("Zapis studenta do grupy - grupa pełna")
    void zapiszStudentaDoGrupyPelna() {

        grupaDocelowa.setZajeteMiejsca(grupaDocelowa.getLimitMiejsc());

        assertFalse(fasada.zapiszStudentaDoGrupy(
                grupaDocelowa.getIdGrupy(),
                student.getNrAlbumu()
        ));
    }
}

/**
 * Handler do obsługi wyjątków w testach
 * Zapewnia spójne logowanie błędów podczas wykonywania testów
 */
@ExtendWith(TestExceptionHandler.class)
class TestExceptionHandler implements TestExecutionExceptionHandler {
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable)
            throws Throwable {
        System.err.println("Test failed: " + context.getDisplayName());
        System.err.println("Exception: " + throwable.getMessage());
        throw throwable;
    }
}
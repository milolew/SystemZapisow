package model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.extension.ExtendWith;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Klasa zawierająca dane testowe dla klasy Przedmiot i powiązanych klas.
 * Przechowuje stałe wartości używane w testach jednostkowych.
 */
class PrzedmiotTestData {
    // Poprawne dane testowe dla przedmiotu
    public static final String VALID_NAZWA = "Programowanie obiektowe";
    public static final String VALID_KOD = "INF101";
    public static final int VALID_ID = 1;

    // Niepoprawne dane testowe dla przedmiotu
    public static final String INVALID_NAZWA = "";
    public static final String INVALID_KOD = "";
    public static final int INVALID_ID = -1;

    // Poprawne dane testowe dla grupy
    public static final LocalTime VALID_GODZ_ROZPOCZECIA = LocalTime.of(8, 0);
    public static final LocalTime VALID_GODZ_ZAKONCZENIA = LocalTime.of(9, 30);
    public static final int VALID_DZIEN = 1;
    public static final int VALID_LIMIT = 30;
}

/**
 * Klasa testowa dla klasy Przedmiot.
 * Zawiera testy jednostkowe sprawdzające poprawność działania wszystkich metod klasy Przedmiot.
 */
@Tag("model")
@ExtendWith(PrzedmiotExceptionHandler.class)
public class Przedmiot_Test {
    private Przedmiot przedmiot;
    private Grupa grupa;
    private Prowadzacy prowadzacy;

    /**
     * Metoda inicjalizująca obiekty przed każdym testem.
     * Tworzy nowy przedmiot, prowadzącego i grupę z poprawnymi danymi.
     */
    @BeforeEach
    void setUp() {
        przedmiot = new Przedmiot(PrzedmiotTestData.VALID_NAZWA, PrzedmiotTestData.VALID_KOD);
        prowadzacy = new Prowadzacy("Jan", "Kowalski", "prof.");
        grupa = new Grupa(
                PrzedmiotTestData.VALID_GODZ_ROZPOCZECIA,
                PrzedmiotTestData.VALID_GODZ_ZAKONCZENIA,
                PrzedmiotTestData.VALID_DZIEN,
                prowadzacy,
                PrzedmiotTestData.VALID_LIMIT
        );
    }

    /**
     * Test sprawdzający poprawność tworzenia obiektu Przedmiot z prawidłowymi danymi.
     * Weryfikuje, czy wszystkie pola zostały poprawnie zainicjalizowane.
     */
    @Test
    void konstruktor_PoprawneDane_UtworzonyObiekt() {
        Przedmiot testPrzedmiot = new Przedmiot(PrzedmiotTestData.VALID_NAZWA, PrzedmiotTestData.VALID_KOD);

        Assertions.assertAll(
                () -> Assertions.assertEquals(PrzedmiotTestData.VALID_NAZWA, testPrzedmiot.getNazwa()),
                () -> Assertions.assertEquals(PrzedmiotTestData.VALID_KOD, testPrzedmiot.getKodPrzedmiotu()),
                () -> Assertions.assertTrue(testPrzedmiot.getGrupy().isEmpty()),
                () -> Assertions.assertTrue(testPrzedmiot.getRodzajeGrup().isEmpty())
        );
    }

    /**
     * Test parametryczny sprawdzający różne przypadki nieprawidłowych danych wejściowych dla konstruktora.
     * @param nazwa nazwa przedmiotu do przetestowania
     * @param kod kod przedmiotu do przetestowania
     */
    @ParameterizedTest
    @CsvSource({
            ", validKod",
            "'', validKod",
            "validNazwa,",
            "validNazwa, ''"
    })
    void konstruktor_NieprawidloweDane_WyjatekIllegalArgument(String nazwa, String kod) {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Przedmiot(nazwa, kod));
    }

    /**
     * Test sprawdzający poprawność ustawiania ID przedmiotu.
     */
    @Test
    void setIdPrzedmiotu_PoprawneId_UstawionoId() {
        przedmiot.setIdPrzedmiotu(PrzedmiotTestData.VALID_ID);

        Assertions.assertEquals(PrzedmiotTestData.VALID_ID, przedmiot.getIdPrzedmiotu());
    }

    /**
     * Test sprawdzający reakcję na próbę ustawienia nieprawidłowego ID przedmiotu.
     */
    @Test
    void setIdPrzedmiotu_NieprawidloweId_WyjatekIllegalArgument() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> przedmiot.setIdPrzedmiotu(PrzedmiotTestData.INVALID_ID));
    }

    /**
     * Test sprawdzający poprawność dodawania grupy do przedmiotu.
     */
    @Test
    void dodajGrupe_PoprawnaGrupa_GrupaDodana() {
        grupa.setRodzajGrupy(RodzajGrupy.WYKLAD);

        przedmiot.dodajGrupe(grupa);

        Assertions.assertAll(
                () -> Assertions.assertTrue(przedmiot.getGrupy().contains(grupa)),
                () -> Assertions.assertTrue(przedmiot.getRodzajeGrup().contains(RodzajGrupy.WYKLAD)),
                () -> Assertions.assertEquals(przedmiot, grupa.getPrzedmiot())
        );
    }

    /**
     * Test sprawdzający reakcję na próbę dodania grupy null.
     */
    @Test
    void dodajGrupe_GrupaNull_WyjatekIllegalArgument() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> przedmiot.dodajGrupe(null));
    }

    /**
     * Test parametryczny sprawdzający weryfikację typów grup.
     * @param rodzajGrupy rodzaj grupy do przetestowania
     */
    @ParameterizedTest
    @MethodSource("provideRodzajeGrup")
    void czyMaGrupeTypu_RozneTypyGrup_PoprawnaWeryfikacja(RodzajGrupy rodzajGrupy) {
        grupa.setRodzajGrupy(rodzajGrupy);
        przedmiot.dodajGrupe(grupa);

        Assertions.assertTrue(przedmiot.czyMaGrupeTypu(rodzajGrupy));
    }

    /**
     * Metoda dostarczająca dane testowe dla testu czyMaGrupeTypu.
     * @return strumień zawierający wszystkie możliwe rodzaje grup
     */
    private static Stream<RodzajGrupy> provideRodzajeGrup() {
        return Stream.of(RodzajGrupy.values());
    }

    /**
     * Test sprawdzający poprawność ustawiania listy grup.
     */
    @Test
    void setGrupy_ListaGrup_PoprawnieUstawionoGrupy() {
        List<Grupa> grupy = new ArrayList<>();
        grupa.setRodzajGrupy(RodzajGrupy.WYKLAD);
        grupy.add(grupa);

        przedmiot.setGrupy(grupy);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, przedmiot.getGrupy().size()),
                () -> Assertions.assertTrue(przedmiot.getRodzajeGrup().contains(RodzajGrupy.WYKLAD)),
                () -> Assertions.assertEquals(przedmiot, grupa.getPrzedmiot())
        );
    }

    /**
     * Test sprawdzający reakcję na próbę ustawienia listy grup zawierającej null.
     */
    @Test
    void setGrupy_ListaZNullem_WyjatekIllegalArgument() {
        List<Grupa> grupy = new ArrayList<>();
        grupy.add(null);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> przedmiot.setGrupy(grupy));
    }
}

/**
 * Klasa obsługująca wyjątki w testach.
 * Zapewnia dodatkowe logowanie w przypadku wystąpienia błędów podczas testów.
 */
class PrzedmiotExceptionHandler implements TestExecutionExceptionHandler {
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        System.out.println("Test failed: " + context.getTestMethod().get().getName());
        System.out.println("Exception: " + throwable.getMessage());
        throw throwable;
    }
}
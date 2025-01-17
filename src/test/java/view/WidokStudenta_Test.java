package view;

import model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TestData {
    public static List<TuraZapisow> getTuryZapisow() {
        Kierunek kierunek = new Kierunek("1", "Informatyka");
        TuraZapisow tura = new TuraZapisow(
                LocalDateTime.of(2025, 1, 1, 10, 0),
                LocalDateTime.of(2025, 1, 1, 12, 0),
                4.0f,
                kierunek
        );
        return List.of(tura);
    }

    public static List<Grupa> getGrupy() {
        Prowadzacy prowadzacy = new Prowadzacy("Adam", "Małysz", "dr inż.");
        Grupa grupa = new Grupa(
                LocalTime.of(12, 0),
                LocalTime.of(14, 0),
                3,
                prowadzacy,
                27
        );
        return List.of(grupa);
    }

    public static List<Kierunek> getKierunki() {
        Kierunek kierunek1 = new Kierunek("1", "Informatyka");
        Kierunek kierunek2 = new Kierunek("2", "Matematyka");
        return List.of(kierunek1, kierunek2);
    }

    public static List<Przedmiot> getPrzedmioty() {
        Przedmiot przedmiot1 = new Przedmiot("INF101", "Programowanie w Javie");
        Przedmiot przedmiot2 = new Przedmiot("MAT201", "Algebra liniowa");
        return List.of(przedmiot1, przedmiot2);
    }
}

/**
 * Klasa testująca funkcjonalność widoku studenta.
 * Wykorzystuje własny handler wyjątków do raportowania błędów.
 */
@Tag("view")
@ExtendWith(TestExceptionHandler.class)
public class WidokStudenta_Test {
    private WidokStudenta widokStudenta;
    private TestWyswietlanieDanych wyswietlanieDanych;

    /**
     * Przygotowanie środowiska przed każdym testem.
     * Tworzy nowe instancje obiektów potrzebnych do testów.
     */
    @BeforeEach
    void setUp() {
        wyswietlanieDanych = new TestWyswietlanieDanych();
        widokStudenta = new WidokStudenta(wyswietlanieDanych);
    }

    /**
     * Test sprawdzający wyświetlanie komunikatu o błędzie logowania.
     * Weryfikuje czy wyświetlany jest odpowiedni komunikat błędu.
     */
    @Test
    void testWyswietlBladLogowania() {
        widokStudenta.wyswietlBladLogowania();
        assertEquals("Błąd: Nie znaleziono danych studenta!",
                wyswietlanieDanych.getOstatniKomunikat());
    }

    /**
     * Test parametryzowany sprawdzający wyświetlanie tur zapisów.
     * Testuje różne scenariusze: pustą listę oraz listę z jedną turą.
     * @param tury Lista tur zapisów do przetestowania
     */
    @ParameterizedTest
    @MethodSource("provideTuryZapisow")
    void testWyswietlTuryZapisowDlaStudenta(List<TuraZapisow> tury) {
        widokStudenta.wyswietlTuryZapisowDlaStudenta(tury);
        List<String> komunikaty = wyswietlanieDanych.getKomunikaty();

        if (tury.isEmpty()) {
            assertTrue(komunikaty.contains("Brak dostępnych tur zapisów."));
        } else {
            assertTrue(komunikaty.contains("\n=== Dostępne tury zapisów ==="));
            assertTrue(komunikaty.stream()
                    .anyMatch(k -> k.contains("ID tury: " + tury.get(0).getIdTury())));
        }
    }

    static Stream<List<TuraZapisow>> provideTuryZapisow() {
        return Stream.of(
                Collections.emptyList(),
                TestData.getTuryZapisow()
        );
    }

    /**
     * Test parametryzowany sprawdzający konwersję numeru dnia na nazwę.
     * @param dzien Numer dnia tygodnia
     * @param oczekiwanaNazwa Oczekiwana nazwa dnia
     */
    @ParameterizedTest
    @CsvSource({
            "1,Poniedziałek",
            "2,Wtorek",
            "3,Środa",
            "4,Czwartek",
            "5,Piątek"
    })
    void testGetDzienTygodnia(int dzien, String oczekiwanaNazwa) {
        String actualNazwa = widokStudenta.getDzienTygodnia(dzien);
        assertEquals(oczekiwanaNazwa, actualNazwa,
                "Dzień " + dzien + " powinien być nazwany " + oczekiwanaNazwa);
    }

    @Test
    void testGetDzienTygodnia_InvalidDay() {
        assertThrows(IllegalArgumentException.class, () ->
                        widokStudenta.getDzienTygodnia(0),
                "Dzień 0 powinien rzucić wyjątek");

        assertThrows(IllegalArgumentException.class, () ->
                        widokStudenta.getDzienTygodnia(6),
                "Dzień 6 powinien rzucić wyjątek");

        assertThrows(IllegalArgumentException.class, () ->
                        widokStudenta.getDzienTygodnia(-1),
                "Dzień -1 powinien rzucić wyjątek");
    }

    @Test
    void testWyswietlDostepneGrupy_Empty() {
        widokStudenta.wyswietlDostepneGrupy(Collections.emptyList());
        assertEquals("Brak dostępnych grup.",
                wyswietlanieDanych.getOstatniKomunikat());
    }

    @Test
    void testWyswietlDostepneGrupy() {
        List<Grupa> grupy = TestData.getGrupy();
        widokStudenta.wyswietlDostepneGrupy(grupy);

        List<String> komunikaty = wyswietlanieDanych.getKomunikaty();
        assertTrue(komunikaty.contains("\nDostępne grupy:"));
    }

    @Test
    void testWyswietlDostepnePrzedmioty_Empty() {
        widokStudenta.wyswietlDostepnePrzedmioty(Collections.emptyList());
        assertEquals("Brak dostępnych przedmiotów.",
                wyswietlanieDanych.getOstatniKomunikat());
    }

    @Test
    void testWyswietlPlanZajec_PustyPlan() {
        Student student = new Student(1, "Test", "Student");
        student.setGrupy(Collections.emptyList());

        widokStudenta.wyswietlPlanZajec(student);
        assertEquals("Nie jesteś zapisany na żadne zajęcia.",
                wyswietlanieDanych.getOstatniKomunikat());
    }
}

/**
 * Klasa pomocnicza implementująca interfejs WyswietlanieDanych.
 * Służy do przechwytywania i weryfikacji wyświetlanych komunikatów w testach.
 */
class TestWyswietlanieDanych implements WyswietlanieDanych {
    private final List<String> komunikaty = new ArrayList<>();

    @Override
    public void wyswietlTuryZapisow(List<TuraZapisow> tury) {
        wyswietlKomunikat("\nDostępne tury zapisów:");
        wyswietlKomunikat("------------------------------------------");
        for (TuraZapisow tura : tury) {
            wyswietlKomunikat(String.format("ID tury: %d", tura.getIdTury()));
            wyswietlKomunikat(String.format("Kierunek: %s", tura.getKierunek().getNazwa()));
            wyswietlKomunikat(String.format("Wymagana średnia: %.2f", tura.getWymaganaSrednia()));
            wyswietlKomunikat(String.format("Rozpoczęcie: %s",
                    tura.getGodzRozpoczecia().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
            wyswietlKomunikat(String.format("Zakończenie: %s",
                    tura.getGodzZakonczenia().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
            wyswietlKomunikat("------------------------------------------");
        }
    }

    @Override
    public void wyswietlKierunki(List<Kierunek> kierunki) {
        wyswietlKomunikat("\nDostępne kierunki:");
        wyswietlKomunikat("------------------------------------------");
        for (Kierunek kierunek : kierunki) {
            wyswietlKomunikat(String.format("Kod: %s", kierunek.getKodKierunku()));
            wyswietlKomunikat(String.format("Nazwa: %s", kierunek.getNazwa()));
            wyswietlKomunikat("------------------------------------------");
        }
    }

    @Override
    public void wyswietlGrupy(List<Grupa> grupy) {
        for (Grupa grupa : grupy) {
            StringBuilder sb = new StringBuilder();
            sb.append("ID: ").append(grupa.getIdGrupy()).append(" | ");
            sb.append(grupa.getRodzajGrupy()).append(" | ");
            sb.append("Prowadzący: ").append(grupa.getProwadzacy().getStopienNaukowy())
                    .append(" ").append(grupa.getProwadzacy().getImie())
                    .append(" ").append(grupa.getProwadzacy().getNazwisko()).append(" | ");
            sb.append("Termin: ").append(getDzienTygodnia(grupa.getDzienTyg()))
                    .append(" ").append(grupa.getGodzRozpoczecia().toString())
                    .append("-").append(grupa.getGodzZakonczenia().toString()).append(" | ");
            sb.append("Miejsca: ").append(grupa.getZajeteMiejsca())
                    .append("/").append(grupa.getLimitMiejsc());
            wyswietlKomunikat(sb.toString());
        }
    }

    @Override
    public void wyswietlPrzedmioty(List<Przedmiot> przedmioty) {
        wyswietlKomunikat("\nPrzedmioty:");
        wyswietlKomunikat("------------------------------------------");
        for (Przedmiot przedmiot : przedmioty) {
            wyswietlKomunikat(String.format("Kod: %s", przedmiot.getKodPrzedmiotu()));
            wyswietlKomunikat(String.format("Nazwa: %s", przedmiot.getNazwa()));
            wyswietlKomunikat("------------------------------------------");
        }
    }

    @Override
    public void wyswietlKomunikat(String tresc) {
        komunikaty.add(tresc);
    }

    private String getDzienTygodnia(int dzien) {
        return switch (dzien) {
            case 1 -> "Poniedziałek";
            case 2 -> "Wtorek";
            case 3 -> "Środa";
            case 4 -> "Czwartek";
            case 5 -> "Piątek";
            default -> "Nieznany dzień";
        };
    }

    public String getOstatniKomunikat() {
        return komunikaty.isEmpty() ? "" : komunikaty.getLast();
    }

    public List<String> getKomunikaty() {
        return new ArrayList<>(komunikaty);
    }
}

/**
 * Handler do obsługi wyjątków w testach
 * Zapewnia spójne logowanie błędów podczas wykonywania testów
 */
class TestExceptionHandler implements TestExecutionExceptionHandler {
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable)
            throws Throwable {
        System.err.println("Test failed: " + context.getDisplayName());
        System.err.println("Exception: " + throwable.getMessage());
        throw throwable;
    }
}
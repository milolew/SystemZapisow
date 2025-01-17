package presenter;

import view.WidokStudenta;
import view.WidokPracownika;
import view.WidokUzytkownika;
import view.WyswietlanieDanych;

public class FabrykaPrezenterow {
    private final WyswietlanieDanych wyswietlanieDanych;

    public FabrykaPrezenterow(WyswietlanieDanych wyswietlanieDanych) {
        this.wyswietlanieDanych = wyswietlanieDanych;
    }

    public WidokUzytkownika utworzWidok(String rola) {
        switch (rola.toUpperCase()) {
            case "STUDENT":
                return new WidokStudenta(wyswietlanieDanych);
            case "PRACOWNIK":
                return new WidokPracownika(wyswietlanieDanych);
            default:
                throw new IllegalArgumentException("Nieznana rola: " + rola);
        }
    }
}
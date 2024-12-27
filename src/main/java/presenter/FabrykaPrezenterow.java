package main.java.presenter;

import main.java.view.WidokStudenta;
import main.java.view.WidokPracownika;
import main.java.view.WidokUzytkownika;
import main.java.view.WyswietlanieDanych;

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
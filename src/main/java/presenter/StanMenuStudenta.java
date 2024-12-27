package main.java.presenter;

import main.java.model.Student;
import main.java.model.TuraZapisow;

import java.util.List;

public class StanMenuStudenta extends StanStudenta {
    public StanMenuStudenta(PrezenterSterowanie prezenter) {
        super(prezenter);
    }

    @Override
    public void obslugaWyboru(int opcja) {
        switch (opcja) {
            case 1: // Przystąp do tury zapisów
                List<TuraZapisow> tury = prezenter.pobierzTuryDlaStudenta();
                getWidok().wyswietlTuryZapisowDlaStudenta(tury);
                prezenter.ustawStan(new StanWyboruTury(prezenter));
                break;

            case 2: // Wyświetl harmonogram zajęć
                Student student = prezenter.pobierzZalogowanegoStudenta();
                getWidok().wyswietlPlanZajec(student);
                getWidok().wyswietlMenu();
                break;

            case 0:
                prezenter.wyloguj();
                break;

            default:
                prezenter.wyswietlKomunikat("Nieprawidłowa opcja!");
                getWidok().wyswietlMenu();
                break;
        }
    }
}
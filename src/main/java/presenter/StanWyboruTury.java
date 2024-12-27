package main.java.presenter;

import main.java.model.Przedmiot;
import main.java.model.TuraZapisow;

import java.util.List;

public class StanWyboruTury extends StanStudenta {
    public StanWyboruTury(PrezenterSterowanie prezenter) {
        super(prezenter);
    }

    @Override
    public void obslugaWyboru(int idTury) {
        if (idTury == 0) {
            prezenter.ustawStan(new StanMenuStudenta(prezenter));
            getWidok().wyswietlMenu();
            return;
        }

        TuraZapisow tura = prezenter.pobierzTure(idTury);
        if (tura == null || !prezenter.sprawdzWarunkiTury(tura)) {
            prezenter.ustawStan(new StanMenuStudenta(prezenter));
            getWidok().wyswietlMenu();
            return;
        }

        List<Przedmiot> przedmioty = tura.getKierunek().getPrzedmioty();
        getWidok().wyswietlDostepnePrzedmioty(przedmioty);
        prezenter.ustawStan(new StanWyboruPrzedmiotu(prezenter, tura));
    }
}
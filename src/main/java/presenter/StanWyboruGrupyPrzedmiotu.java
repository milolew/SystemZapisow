package main.java.presenter;

import main.java.model.Przedmiot;
import main.java.model.RodzajGrupy;

import java.util.List;

public class StanWyboruGrupyPrzedmiotu extends StanStudenta {
    private final Przedmiot przedmiot;
    private final List<RodzajGrupy> pozostaleTypyGrup;

    public StanWyboruGrupyPrzedmiotu(PrezenterSterowanie prezenter, Przedmiot przedmiot, List<RodzajGrupy> typyGrup) {
        super(prezenter);
        this.przedmiot = przedmiot;
        this.pozostaleTypyGrup = typyGrup;
    }

    @Override
    public void obslugaWyboru(int idGrupy) {
        if (idGrupy == 0) {
            prezenter.ustawStan(new StanMenuStudenta(prezenter));
            getWidok().wyswietlMenu();
            return;
        }

        if (prezenter.zapiszDoGrupy(idGrupy)) {
            pozostaleTypyGrup.remove(0);

            if (pozostaleTypyGrup.isEmpty()) {
                prezenter.wyswietlKomunikat("Zapisano na wszystkie typy zajęć przedmiotu " + przedmiot.getNazwa());
                prezenter.ustawStan(new StanMenuStudenta(prezenter));
                getWidok().wyswietlMenu();
            } else {
                prezenter.wyswietlDostepneGrupy(przedmiot, pozostaleTypyGrup.get(0));
            }
        }
    }
}

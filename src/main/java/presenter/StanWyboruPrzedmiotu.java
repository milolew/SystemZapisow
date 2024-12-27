package main.java.presenter;

import main.java.model.Przedmiot;
import main.java.model.RodzajGrupy;
import main.java.model.TuraZapisow;

import java.util.ArrayList;
import java.util.List;

public class StanWyboruPrzedmiotu extends StanStudenta {
    private final TuraZapisow tura;

    public StanWyboruPrzedmiotu(PrezenterSterowanie prezenter, TuraZapisow tura) {
        super(prezenter);
        this.tura = tura;
    }

    @Override
    public void obslugaWyboru(int idPrzedmiotu) {
        if (idPrzedmiotu == 0) {
            prezenter.ustawStan(new StanMenuStudenta(prezenter));
            getWidok().wyswietlMenu();
            return;
        }

        Przedmiot przedmiot = prezenter.pobierzPrzedmiot(idPrzedmiotu);
        if (przedmiot == null || !prezenter.sprawdzDostepnoscPrzedmiotu(przedmiot)) {
            // Wracamy do menu głównego
            prezenter.ustawStan(new StanMenuStudenta(prezenter));
            getWidok().wyswietlMenu();
            return;
        }

        if (przedmiot.getRodzajeGrup().isEmpty()) {
            prezenter.wyswietlKomunikat("Przedmiot nie ma zdefiniowanych typów grup!");
            prezenter.ustawStan(new StanMenuStudenta(prezenter));
            getWidok().wyswietlMenu();
            return;
        }

        List<RodzajGrupy> typy = new ArrayList<>(przedmiot.getRodzajeGrup());
        prezenter.ustawStan(new StanWyboruGrupyPrzedmiotu(prezenter, przedmiot, typy));
        prezenter.wyswietlDostepneGrupy(przedmiot, typy.get(0));
    }
}
package main.java.presenter;

import main.java.model.Grupa;

import java.util.List;

public class StanUsuwanieGrupy extends StanPracownika {
    public StanUsuwanieGrupy(PrezenterSterowanie prezenter) {
        super(prezenter);
    }

    @Override
    public void obslugaWyboru(int idGrupy) {
        if (idGrupy == 0) {
            prezenter.ustawStan(new StanMenuPracownika(prezenter));
            getWidok().wyswietlMenu();
            return;
        }

        try {
            System.out.println(idGrupy);
            Grupa grupa = prezenter.pobierzGrupe(idGrupy);
            System.out.println(grupa.getIdGrupy());
            if (grupa == null) {
                prezenter.wyswietlKomunikat("Błąd: Grupa o podanym ID nie istnieje");
                prezenter.ustawStan(new StanMenuPracownika(prezenter));
                getWidok().wyswietlMenu();
                return;
            }

            if (grupa.getCzlonkowie().isEmpty()) {
                prezenter.usunGrupe(idGrupy);
                prezenter.wyswietlKomunikat("Grupa została usunięta.");
                prezenter.ustawStan(new StanMenuPracownika(prezenter));
                getWidok().wyswietlMenu();
                return;
            }

            List<Grupa> dostepneGrupy = prezenter.pobierzGrupyDoPrezeniesienia(idGrupy);
            if (dostepneGrupy.isEmpty()) {
                prezenter.wyswietlKomunikat("Brak dostępnych grup do przeniesienia studentów!");
                prezenter.ustawStan(new StanMenuPracownika(prezenter));
                getWidok().wyswietlMenu();
                return;
            }

            prezenter.ustawStan(new StanWyboruGrupyDocelowej(prezenter, idGrupy));
            getWidok().wyswietlGrupyDoPrezeniesienia(dostepneGrupy);

        } catch (IllegalArgumentException e) {
            prezenter.wyswietlKomunikat("Błąd: " + e.getMessage());
            prezenter.ustawStan(new StanMenuPracownika(prezenter));
            getWidok().wyswietlMenu();
        }
    }
}
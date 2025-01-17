package presenter;

import model.Kierunek;

import java.util.List;

// Zaktualizowany StanMenuPracownika
public class StanMenuPracownika extends StanPracownika {
    public StanMenuPracownika(PrezenterSterowanie prezenter) {
        super(prezenter);
    }

    @Override
    public void obslugaWyboru(int opcja) {
        switch (opcja) {
            case 1:
                List<Kierunek> kierunki = prezenter.pobierzKierunki();
                getWidok().wyswietlGrupyZajeciowe(kierunki);
                prezenter.ustawStan(new StanUsuwanieGrupy(prezenter));
                break;

            case 2:
                List<Kierunek> wszystkieKierunki = prezenter.pobierzKierunki();
                getWidok().wyswietlWszystkieGrupy(wszystkieKierunki);
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
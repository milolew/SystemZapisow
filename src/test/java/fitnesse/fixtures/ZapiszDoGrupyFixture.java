package fitnesse.fixtures;

import fit.ColumnFixture;
import presenter.PrezenterSterowanie;
import model.*;
import view.*;

/*
* Test akceptacyjny zapisu studentow do grupy, wymaga
* uzywania istniejacego w systemie studenta i istniejacych grup.
* */
public class ZapiszDoGrupyFixture extends ColumnFixture {
    public String login;
    public int idGrupy;
    private PrezenterSterowanie prezenter;

    public ZapiszDoGrupyFixture() {
        PobranieDanych pobranieDanych = new FasadaDane();
        Weryfikacja weryfikacja = new FasadaWeryfikacja(pobranieDanych);
        AktualizacjaDanych aktualizacja = new FasadaAktualizacji(pobranieDanych);
        WyswietlanieDanych wyswietlanie = new FasadaWidok();

        prezenter = new PrezenterSterowanie(
                weryfikacja,
                aktualizacja,
                pobranieDanych,
                wyswietlanie
        );
        prezenter.uruchomSystem();
    }

    public boolean zapisz() {
        prezenter.zaloguj(login, "password");
        return prezenter.zapiszDoGrupy(idGrupy);
    }
}
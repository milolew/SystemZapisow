package fitnesse.fixtures;

import fit.ColumnFixture;
import presenter.PrezenterSterowanie;
import model.*;
import view.*;

public class PrzeniesStudentowFixture extends ColumnFixture {
    public int idGrupyZrodlowej;
    public int idGrupyDocelowej;
    private static PrezenterSterowanie prezenter;
    private static boolean zalogowany = false;

    public PrzeniesStudentowFixture() {
        if (prezenter == null) {
            FasadaDane fasadaDane = new FasadaDane(); // Zawiera już zainicjalizowane dane z fabryki
            Weryfikacja weryfikacja = new FasadaWeryfikacja(fasadaDane);
            AktualizacjaDanych aktualizacja = new FasadaAktualizacji(fasadaDane);
            WyswietlanieDanych wyswietlanie = new FasadaWidok();

            prezenter = new PrezenterSterowanie(
                    weryfikacja,
                    aktualizacja,
                    fasadaDane,
                    wyswietlanie
            );
        }
    }

    public boolean przenies() {
        if (!zalogowany) {
            prezenter.uruchomSystem();
            prezenter.zaloguj("pracownik", "password");
            prezenter.wybierzOpcje(1);
            zalogowany = true;
        }

        return prezenter.przeniesStudentow(idGrupyZrodlowej, idGrupyDocelowej);
    }
}
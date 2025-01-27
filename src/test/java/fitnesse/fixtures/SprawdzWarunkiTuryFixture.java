package fitnesse.fixtures;

import fit.ColumnFixture;
import presenter.PrezenterSterowanie;
import model.*;
import view.*;
import java.time.LocalDateTime;

/*
* Test akceptacyjny sprawdzajacy poprawnosc weryfikacji mozliwosci
* przystapienia do tury zapisow. Srednia studenta oraz wymagana srednia
* jest pobierana z parametrów. Opcjonalnie można ustawić początek i koniec tury,
* domyślnie jest ustawiana jako zawsze otwarta.
* */
public class SprawdzWarunkiTuryFixture extends ColumnFixture {
    public String login;
    public String dataRozpoczecia;
    public String dataZakonczenia;
    public float srednia;
    public float wymaganaSrednia;
    public String kierunekNazwa;
    public boolean uzyjDateZParametrow;
    private static PrezenterSterowanie prezenter;

    public SprawdzWarunkiTuryFixture() {
        if (prezenter == null) {
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
        }
    }

    public boolean sprawdzWarunki() {
        prezenter.uruchomSystem();
        prezenter.zaloguj(login, "password");

        Student student = prezenter.pobierzZalogowanegoStudenta();
        student.setSrednia(srednia);

        Kierunek kierunek = new Kierunek(kierunekNazwa, kierunekNazwa.substring(0, 3).toUpperCase());

        LocalDateTime dataRozp;
        LocalDateTime dataZak;

        if (!uzyjDateZParametrow) {
            dataRozp = LocalDateTime.now().minusDays(1);
            dataZak = LocalDateTime.now().plusDays(7);
        } else {
            dataRozp = LocalDateTime.parse(dataRozpoczecia.replace(" ", "T"));
            dataZak = LocalDateTime.parse(dataZakonczenia.replace(" ", "T"));
        }
        System.out.println(dataRozp);
        System.out.println(dataZak);

        TuraZapisow tura = new TuraZapisow(dataRozp, dataZak, wymaganaSrednia, kierunek);

        return prezenter.sprawdzWarunkiTury(tura);
    }
}
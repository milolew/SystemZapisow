package model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

public class TuraZapisowDAO {
    private static int nextId = 1;
    private final Collection<TuraZapisow> turyZapisow;

    public TuraZapisowDAO(Collection<TuraZapisow> turyZapisow) {
        this.turyZapisow = turyZapisow;
    }

    /**
     * Tworzy nową turę zapisów
     * @param kierunek kierunek
     * @param min_srednia minimalna wymagana średnia
     * @param godz_rozp godzina rozpoczęcia
     * @param godz_zak godzina zakończenia
     * @return utworzona tura zapisów
     */
    public TuraZapisow utworzTureZapisow(Kierunek kierunek, float min_srednia, LocalTime godz_rozp, LocalTime godz_zak) {
        TuraZapisow tura = new TuraZapisow(
                LocalDateTime.now().with(godz_rozp),
                LocalDateTime.now().with(godz_zak),
                min_srednia,
                kierunek
        );
        tura.setIdTury(nextId++);
        turyZapisow.add(tura);
        return tura;
    }

    /**
     * Czyta kierunek przypisany do tury zapisów
     * @param tura tura zapisów
     * @return kierunek przypisany do tury
     */
    public Kierunek czytajKierunek(TuraZapisow tura) {
        return tura.getKierunek();
    }
}
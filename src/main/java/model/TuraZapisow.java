package model;

import java.time.LocalDateTime;

public class TuraZapisow {
    private int idTury;
    private Kierunek kierunek;
    private float wymaganaSrednia;
    private LocalDateTime godzRozpoczecia;
    private LocalDateTime godzZakonczenia;

    public TuraZapisow(LocalDateTime godzRozpoczecia, LocalDateTime godzZakonczenia, 
                      float wymaganaSrednia, Kierunek kierunek) {
        this.godzRozpoczecia = godzRozpoczecia;
        this.godzZakonczenia = godzZakonczenia;
        this.wymaganaSrednia = wymaganaSrednia;
        this.kierunek = kierunek;
    }

    public int getIdTury() {
        return idTury;
    }

    public void setIdTury(int idTury) {
        this.idTury = idTury;
    }

    public Kierunek getKierunek() {
        return kierunek;
    }

    public void setKierunek(Kierunek kierunek) {
        this.kierunek = kierunek;
    }

    public float getWymaganaSrednia() {
        return wymaganaSrednia;
    }

    public void setWymaganaSrednia(float wymaganaSrednia) {
        this.wymaganaSrednia = wymaganaSrednia;
    }

    public LocalDateTime getGodzRozpoczecia() {
        return godzRozpoczecia;
    }

    public void setGodzRozpoczecia(LocalDateTime godzRozpoczecia) {
        this.godzRozpoczecia = godzRozpoczecia;
    }

    public LocalDateTime getGodzZakonczenia() {
        return godzZakonczenia;
    }

    public void setGodzZakonczenia(LocalDateTime godzZakonczenia) {
        this.godzZakonczenia = godzZakonczenia;
    }
}
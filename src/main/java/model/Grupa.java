package main.java.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Grupa {
    private int idGrupy;
    private int nrGrupy;
    private LocalTime godzRozpoczecia;
    private LocalTime godzZakonczenia;
    private int dzienTyg;
    private int limitMiejsc;
    private int zajeteMiejsca;
    private RodzajGrupy rodzajGrupy;
    private List<Student> czlonkowie;
    private Prowadzacy prowadzacy;
    private Przedmiot przedmiot;

    public Grupa(LocalTime godzRozpoczecia, LocalTime godzZakonczenia, 
                int dzienTyg, Prowadzacy prowadzacy, int limitMiejsc) {
        this.godzRozpoczecia = godzRozpoczecia;
        this.godzZakonczenia = godzZakonczenia;
        this.dzienTyg = dzienTyg;
        this.prowadzacy = prowadzacy;
        this.limitMiejsc = limitMiejsc;
        this.zajeteMiejsca = 0;
        this.czlonkowie = new ArrayList<>();
    }

    // Gettery i settery
    public int getIdGrupy() {
        return idGrupy;
    }

    public void setIdGrupy(int idGrupy) {
        this.idGrupy = idGrupy;
    }

    public int getNrGrupy() {
        return nrGrupy;
    }

    public void setNrGrupy(int nrGrupy) {
        this.nrGrupy = nrGrupy;
    }

    public LocalTime getGodzRozpoczecia() {
        return godzRozpoczecia;
    }

    public void setGodzRozpoczecia(LocalTime godzRozpoczecia) {
        this.godzRozpoczecia = godzRozpoczecia;
    }

    public LocalTime getGodzZakonczenia() {
        return godzZakonczenia;
    }

    public void setGodzZakonczenia(LocalTime godzZakonczenia) {
        this.godzZakonczenia = godzZakonczenia;
    }

    public int getDzienTyg() {
        return dzienTyg;
    }

    public void setDzienTyg(int dzienTyg) {
        this.dzienTyg = dzienTyg;
    }

    public int getLimitMiejsc() {
        return limitMiejsc;
    }

    public void setLimitMiejsc(int limitMiejsc) {
        this.limitMiejsc = limitMiejsc;
    }

    public int getZajeteMiejsca() {
        return zajeteMiejsca;
    }

    public void setZajeteMiejsca(int zajeteMiejsca) {
        this.zajeteMiejsca = zajeteMiejsca;
    }

    public List<Student> getCzlonkowie() {
        return czlonkowie;
    }

    public void setCzlonkowie(List<Student> czlonkowie) {
        this.czlonkowie = czlonkowie;
    }

    public Prowadzacy getProwadzacy() {
        return prowadzacy;
    }

    public void setProwadzacy(Prowadzacy prowadzacy) {
        this.prowadzacy = prowadzacy;
    }

    public RodzajGrupy getRodzajGrupy() {
        return rodzajGrupy;
    }

    public void setRodzajGrupy(RodzajGrupy rodzajGrupy) {
        this.rodzajGrupy = rodzajGrupy;
    }

    public void setPrzedmiot(Przedmiot przedmiot) {
        this.przedmiot = przedmiot;
    }

    public Przedmiot getPrzedmiot() {
        return przedmiot;
    }

    public boolean czyMaMiejsca() {
        return zajeteMiejsca < limitMiejsc;
    }

    public boolean dodajStudenta(Student student) {
        if (!czyMaMiejsca()) {
            return false;
        }
        czlonkowie.add(student);
        zajeteMiejsca++;
        return true;
    }
}
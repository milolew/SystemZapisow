package model;

import java.util.ArrayList;
import java.util.List;

public class Student extends Uzytkownik {
    private int nrAlbumu;
    private float srednia;
    private List<Kierunek> kierunki;

    public Student(int nrAlbumu, String imie, String nazwisko) {
        super();
        this.nrAlbumu = nrAlbumu;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.kierunki = new ArrayList<>();
        this.srednia = 0.0f;
    }

    public int getNrAlbumu() {
        return nrAlbumu;
    }

    public float getSrednia() {
        return srednia;
    }

    public void setSrednia(float srednia) {
        this.srednia = srednia;
    }

    public List<Kierunek> getKierunki() {
        return kierunki;
    }

    public void setKierunki(List<Kierunek> kierunki) {
        this.kierunki = kierunki;
    }
}
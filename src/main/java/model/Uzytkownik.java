package main.java.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Uzytkownik {
    protected String imie;
    protected String nazwisko;
    private List<Grupa> grupy;

    public Uzytkownik() {
        this.grupy = new ArrayList<>();
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public List<Grupa> getGrupy() {
        return grupy;
    }

    public void setGrupy(List<Grupa> grupy) {
        this.grupy = grupy;
    }
}
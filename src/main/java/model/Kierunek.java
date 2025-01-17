package model;

import java.util.ArrayList;
import java.util.List;

public class Kierunek {
    private String kodKierunku;
    private String nazwa;
    private List<Przedmiot> przedmioty;

    public Kierunek(String kodKierunku, String nazwa) {
        this.kodKierunku = kodKierunku;
        this.nazwa = nazwa;
        this.przedmioty = new ArrayList<>();
    }

    public String getKodKierunku() {
        return kodKierunku;
    }

    public void setKodKierunku(String kodKierunku) {
        this.kodKierunku = kodKierunku;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public List<Przedmiot> getPrzedmioty() {
        return przedmioty;
    }

    public void setPrzedmioty(List<Przedmiot> przedmioty) {
        this.przedmioty = przedmioty;
    }

    public int getIdKierunku() {
        return Integer.parseInt(kodKierunku);
    }
}
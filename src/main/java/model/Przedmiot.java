package main.java.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Przedmiot {
    private String nazwa;
    private String kodPrzedmiotu;
    private int idPrzedmiotu;
    private Set<RodzajGrupy> rodzajeGrup; // Zmienione na Set<RodzajGrupy>
    private List<Grupa> grupy;

    public Przedmiot(String nazwa, String kodPrzedmiotu) {
        this.nazwa = nazwa;
        this.kodPrzedmiotu = kodPrzedmiotu;
        this.rodzajeGrup = new HashSet<>(); // Inicjalizacja jako HashSet
        this.grupy = new ArrayList<>();
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getKodPrzedmiotu() {
        return kodPrzedmiotu;
    }

    public int getIdPrzedmiotu() {
        return idPrzedmiotu;
    }

    public void setIdPrzedmiotu(int idPrzedmiotu) {
        this.idPrzedmiotu = idPrzedmiotu;
    }

    public Set<RodzajGrupy> getRodzajeGrup() {
        return rodzajeGrup;
    }

    public List<Grupa> getGrupy() {
        return grupy;
    }

    public void setGrupy(List<Grupa> grupy) {
        this.grupy = new ArrayList<>(grupy);
        // Ustaw referencję do przedmiotu w każdej grupie
        for (Grupa grupa : this.grupy) {
            grupa.setPrzedmiot(this);
        }
        aktualizujRodzajeGrup();
    }

    private void aktualizujRodzajeGrup() {
        rodzajeGrup.clear();
        for (Grupa grupa : grupy) {
            if (grupa.getRodzajGrupy() != null) {
                rodzajeGrup.add(grupa.getRodzajGrupy());
            }
        }
    }

    // Dodatkowe metody pomocnicze
    public void dodajGrupe(Grupa grupa) {
        if (!grupy.contains(grupa)) {
            grupy.add(grupa);
            rodzajeGrup.add(grupa.getRodzajGrupy());
        }
    }

    public boolean czyMaGrupeTypu(RodzajGrupy rodzaj) {
        return rodzajeGrup.contains(rodzaj);
    }
}
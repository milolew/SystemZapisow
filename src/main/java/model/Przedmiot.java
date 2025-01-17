package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Przedmiot {
    private String nazwa;
    private String kodPrzedmiotu;
    private int idPrzedmiotu;
    private Set<RodzajGrupy> rodzajeGrup;
    private List<Grupa> grupy;

    public Przedmiot(String nazwa, String kodPrzedmiotu) {
        if (nazwa == null || nazwa.trim().isEmpty()) {
            throw new IllegalArgumentException("Nazwa przedmiotu nie może być pusta");
        }
        if (kodPrzedmiotu == null || kodPrzedmiotu.trim().isEmpty()) {
            throw new IllegalArgumentException("Kod przedmiotu nie może być pusty");
        }

        this.nazwa = nazwa;
        this.kodPrzedmiotu = kodPrzedmiotu;
        this.rodzajeGrup = new HashSet<>();
        this.grupy = new ArrayList<>();
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        if (nazwa == null || nazwa.trim().isEmpty()) {
            throw new IllegalArgumentException("Nazwa przedmiotu nie może być pusta");
        }
        this.nazwa = nazwa;
    }

    public String getKodPrzedmiotu() {
        return kodPrzedmiotu;
    }

    public int getIdPrzedmiotu() {
        return idPrzedmiotu;
    }

    public void setIdPrzedmiotu(int idPrzedmiotu) {
        if (idPrzedmiotu <= 0) {
            throw new IllegalArgumentException("ID przedmiotu musi być większe od 0");
        }
        this.idPrzedmiotu = idPrzedmiotu;
    }

    public Set<RodzajGrupy> getRodzajeGrup() {
        return new HashSet<>(rodzajeGrup);
    }

    public List<Grupa> getGrupy() {
        return new ArrayList<>(grupy);
    }

    public void setGrupy(List<Grupa> grupy) {
        if (grupy == null) {
            throw new IllegalArgumentException("Lista grup nie może być null");
        }

        this.grupy = new ArrayList<>(grupy);
        for (Grupa grupa : this.grupy) {
            if (grupa == null) {
                throw new IllegalArgumentException("Grupa w liście nie może być null");
            }
            grupa.setPrzedmiot(this);
        }
        aktualizujRodzajeGrup();
    }

    private void aktualizujRodzajeGrup() {
        rodzajeGrup.clear();
        for (Grupa grupa : grupy) {
            if (grupa.getRodzajGrupy() == null) {
                throw new IllegalStateException("Rodzaj grupy nie może być null");
            }
            rodzajeGrup.add(grupa.getRodzajGrupy());
        }
    }

    public void dodajGrupe(Grupa grupa) {
        if (grupa == null) {
            throw new IllegalArgumentException("Grupa nie może być null");
        }
        if (grupa.getRodzajGrupy() == null) {
            throw new IllegalArgumentException("Rodzaj grupy nie może być null");
        }

        if (!grupy.contains(grupa)) {
            grupy.add(grupa);
            rodzajeGrup.add(grupa.getRodzajGrupy());
            grupa.setPrzedmiot(this);
        }
    }

    public boolean czyMaGrupeTypu(RodzajGrupy rodzaj) {
        if (rodzaj == null) {
            throw new IllegalArgumentException("Rodzaj grupy nie może być null");
        }
        return rodzajeGrup.contains(rodzaj);
    }
}
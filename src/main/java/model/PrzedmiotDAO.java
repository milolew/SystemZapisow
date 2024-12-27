package main.java.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PrzedmiotDAO {
    private static int nextId = 1;
    private final Collection<Kierunek> kierunki;

    public PrzedmiotDAO(Collection<Kierunek> kierunki) {
        this.kierunki = kierunki;
    }

    public Przedmiot utworzPrzedmiot(String nazwa, String kodPrzedmiotu) {
        Przedmiot przedmiot = new Przedmiot(nazwa, kodPrzedmiotu);
        przedmiot.setIdPrzedmiotu(nextId++);
        return przedmiot;
    }

    public void dodajGrupeDoPrzedmiotu(Przedmiot przedmiot, Grupa grupa) {
        if (!przedmiot.getGrupy().contains(grupa)) {
            przedmiot.dodajGrupe(grupa);
            grupa.setPrzedmiot(przedmiot);
            aktualizujPrzedmiot(przedmiot);
        }
    }

    public List<Grupa> pobierzGrupy(Przedmiot przedmiot) {
        return new ArrayList<>(przedmiot.getGrupy());
    }

    public void aktualizujPrzedmiot(Przedmiot przedmiot) {
        for (Kierunek kierunek : kierunki) {
            for (Przedmiot istniejacyPrzedmiot : kierunek.getPrzedmioty()) {
                if (istniejacyPrzedmiot.getIdPrzedmiotu() == przedmiot.getIdPrzedmiotu()) {
                    // Aktualizuj dane przedmiotu
                    istniejacyPrzedmiot.setNazwa(przedmiot.getNazwa());
                    istniejacyPrzedmiot.setGrupy(przedmiot.getGrupy());
                    return;
                }
            }
        }
    }

    public void usunPrzedmiot(Przedmiot przedmiot) {
        // Usuń referencje do przedmiotu ze wszystkich grup
        for (Grupa grupa : przedmiot.getGrupy()) {
            grupa.setPrzedmiot(null);
        }

        // Usuń przedmiot z kierunków
        for (Kierunek kierunek : kierunki) {
            kierunek.getPrzedmioty().remove(przedmiot);
        }
    }

    public Przedmiot znajdzPrzedmiotPoKodzie(String kodPrzedmiotu) {
        for (Kierunek kierunek : kierunki) {
            for (Przedmiot przedmiot : kierunek.getPrzedmioty()) {
                if (przedmiot.getKodPrzedmiotu().equals(kodPrzedmiotu)) {
                    return przedmiot;
                }
            }
        }
        return null;
    }

    public List<Przedmiot> pobierzWszystkiePrzedmioty() {
        List<Przedmiot> wszystkiePrzedmioty = new ArrayList<>();
        for (Kierunek kierunek : kierunki) {
            wszystkiePrzedmioty.addAll(kierunek.getPrzedmioty());
        }
        return wszystkiePrzedmioty;
    }
}
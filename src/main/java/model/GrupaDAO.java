package main.java.model;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

public class GrupaDAO {
    private static int nextId = 1;
    private final Collection<Kierunek> kierunki;

    public GrupaDAO(Collection<Kierunek> kierunki) {
        this.kierunki = kierunki;
    }

    public Grupa utworzGrupe(LocalTime godz_rozp, LocalTime godz_zak, int dzien_tyg, int limit) {
        Grupa grupa = new Grupa(godz_rozp, godz_zak, dzien_tyg, null, limit);
        grupa.setIdGrupy(nextId++);
        return grupa;
    }

    public List<Student> pobierzStudentow(Grupa grupa) {
        return grupa.getCzlonkowie();
    }

    public boolean zapiszStudentaDoGrupy(Grupa grupa, Student student) {
        // Sprawdzenie limitu miejsc
        if (grupa.getZajeteMiejsca() >= grupa.getLimitMiejsc()) {
            return false;
        }

        // Sprawdzenie czy student nie jest już zapisany
        if (grupa.getCzlonkowie().contains(student)) {
            return false;
        }

        // Dodaj studenta do grupy
        grupa.getCzlonkowie().add(student);
        grupa.setZajeteMiejsca(grupa.getZajeteMiejsca() + 1);

        // Dodaj grupę do studenta
        student.getGrupy().add(grupa);

        // Zaktualizuj grupę w bazie
        aktualizujGrupe(grupa);

        return true;
    }

    public void aktualizujGrupe(Grupa grupa) {
        // Znajdź grupę w odpowiednim przedmiocie
        for (Kierunek kierunek : kierunki) {
            for (Przedmiot przedmiot : kierunek.getPrzedmioty()) {
                for (Grupa istniejacaGrupa : przedmiot.getGrupy()) {
                    if (istniejacaGrupa.getIdGrupy() == grupa.getIdGrupy()) {
                        // Aktualizuj dane grupy
                        istniejacaGrupa.setCzlonkowie(grupa.getCzlonkowie());
                        istniejacaGrupa.setZajeteMiejsca(grupa.getZajeteMiejsca());
                        return; // Kończymy po znalezieniu i zaktualizowaniu grupy
                    }
                }
            }
        }
    }

    public void usunGrupe(Grupa grupa) {
        // Usuń referencje do grupy u wszystkich studentów
        for (Student student : grupa.getCzlonkowie()) {
            student.getGrupy().remove(grupa);
        }

        // Wyczyść listę członków grupy
        grupa.getCzlonkowie().clear();
        grupa.setZajeteMiejsca(0);

        // Usuń grupę z przedmiotów
        for (Kierunek kierunek : kierunki) {
            for (Przedmiot przedmiot : kierunek.getPrzedmioty()) {
                przedmiot.getGrupy().remove(grupa);
            }
        }
    }
}
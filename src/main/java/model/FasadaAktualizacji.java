package main.java.model;

import java.time.LocalTime;
import java.util.*;

public class FasadaAktualizacji implements AktualizacjaDanych {
    private final Map<Integer, Grupa> grupy;
    private final PobranieDanych pobranieDanych;
    private final KierunekDAO kierunekDAO;
    private final PrzedmiotDAO przedmiotDAO;
    private final GrupaDAO grupaDAO;
    private final TuraZapisowDAO turaZapisowDAO;

    public FasadaAktualizacji(PobranieDanych pobranieDanych) {
        this.pobranieDanych = pobranieDanych;
        this.grupy = new HashMap<>();

        // Pobranie kolekcji tur zapisów
        Collection<TuraZapisow> turyZapisow = new ArrayList<>(this.pobranieDanych.pobierzTury(null));

        // Inicjalizacja wszystkich DAO
        this.kierunekDAO = new KierunekDAO();
        this.przedmiotDAO = new PrzedmiotDAO(pobranieDanych.pobierzKierunki());
        this.grupaDAO = new GrupaDAO(pobranieDanych.pobierzKierunki());
        this.turaZapisowDAO = new TuraZapisowDAO(turyZapisow);

        inicjalizujGrupy();
    }

    private void inicjalizujGrupy() {
        for (Kierunek kierunek : pobranieDanych.pobierzKierunki()) {
            for (Przedmiot przedmiot : kierunek.getPrzedmioty()) {
                for (Grupa grupa : przedmiotDAO.pobierzGrupy(przedmiot)) {
                    grupy.put(grupa.getIdGrupy(), grupa);
                }
            }
        }
    }

    private boolean czyWystepujeKonflikt(Student student, Grupa nowaGrupa) {
        for (Grupa grupa : student.getGrupy()) {
            if (grupa.getDzienTyg() == nowaGrupa.getDzienTyg()) {
                LocalTime start1 = grupa.getGodzRozpoczecia();
                LocalTime koniec1 = grupa.getGodzZakonczenia();
                LocalTime start2 = nowaGrupa.getGodzRozpoczecia();
                LocalTime koniec2 = nowaGrupa.getGodzZakonczenia();

                if ((start2.isAfter(start1) && start2.isBefore(koniec1)) ||
                        (koniec2.isAfter(start1) && koniec2.isBefore(koniec1)) ||
                        start2.equals(start1) || koniec2.equals(koniec1)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean przeniesStudenta(int idGrupyZrodlowej, int idGrupyDocelowej, int nrIndeksu) {
        Grupa grupaZrodlowa = grupy.get(idGrupyZrodlowej);
        Grupa grupaDocelowa = grupy.get(idGrupyDocelowej);

        if (grupaZrodlowa == null || grupaDocelowa == null) {
            throw new IllegalArgumentException("Jedna z grup nie istnieje");
        }

        Student student = pobranieDanych.pobierzStudenta(nrIndeksu);
        if (student == null) {
            throw new IllegalStateException("Student nie istnieje");
        }

        // Sprawdzenie czy student jest w grupie źródłowej
        if (!grupaDAO.pobierzStudentow(grupaZrodlowa).contains(student)) {
            throw new IllegalStateException("Student nie jest członkiem grupy źródłowej");
        }

        // Sprawdzenie dostępności miejsc
        if (grupaDocelowa.getZajeteMiejsca() >= grupaDocelowa.getLimitMiejsc()) {
            return false;
        }

        // Sprawdzenie konfliktów
        if (czyWystepujeKonflikt(student, grupaDocelowa)) {
            throw new IllegalStateException("Występuje konflikt w planie zajęć");
        }

        // Usunięcie z grupy źródłowej
        grupaZrodlowa.getCzlonkowie().remove(student);
        grupaZrodlowa.setZajeteMiejsca(grupaZrodlowa.getZajeteMiejsca() - 1);
        student.getGrupy().remove(grupaZrodlowa);

        // Dodanie do grupy docelowej
        boolean dodano = grupaDAO.zapiszStudentaDoGrupy(grupaDocelowa, student);
        if (!dodano) {
            // Przywracanie stanu poprzedniego w przypadku niepowodzenia
            grupaDAO.zapiszStudentaDoGrupy(grupaZrodlowa, student);
            return false;
        }

        // Aktualizacja w DAO
        grupaDAO.aktualizujGrupe(grupaZrodlowa);
        grupaDAO.aktualizujGrupe(grupaDocelowa);

        return true;
    }

    @Override
    public void dodajStudenta(int idGrupy, int nrIndeksu) {
        Grupa grupa = grupy.get(idGrupy);
        if (grupa == null) {
            throw new IllegalArgumentException("Grupa o podanym ID nie istnieje");
        }

        Student student = pobranieDanych.pobierzStudenta(nrIndeksu);
        if (student == null) {
            throw new IllegalArgumentException("Student o podanym numerze indeksu nie istnieje");
        }

        List<Student> studenciGrupy = grupaDAO.pobierzStudentow(grupa);
        if (studenciGrupy.contains(student)) {
            throw new IllegalStateException("Student już jest w tej grupie");
        }

        if (czyWystepujeKonflikt(student, grupa)) {
            throw new IllegalStateException("Występuje konflikt w planie zajęć");
        }

        if (!grupaDAO.zapiszStudentaDoGrupy(grupa, student)) {
            throw new IllegalStateException("Nie można zapisać studenta do grupy");
        }
    }

    @Override
    public List<Grupa> pobierzDostepneGrupyDoPrezeniesienia(int idGrupy) {
        Grupa grupaZrodlowa = grupy.get(idGrupy);
        if (grupaZrodlowa == null) {
            throw new IllegalArgumentException("Grupa o podanym ID nie istnieje");
        }

        List<Grupa> dostepneGrupy = new ArrayList<>();
        Przedmiot przedmiotZrodlowy = null;

        // Znajdź przedmiot dla grupy źródłowej
        for (Przedmiot przedmiot : przedmiotDAO.pobierzWszystkiePrzedmioty()) {
            if (przedmiot.getGrupy().contains(grupaZrodlowa)) {
                przedmiotZrodlowy = przedmiot;
                break;
            }
        }

        if (przedmiotZrodlowy == null) {
            throw new IllegalStateException("Nie znaleziono przedmiotu dla podanej grupy");
        }

        // Filtruj grupy
        for (Grupa potencjalnaGrupa : przedmiotDAO.pobierzGrupy(przedmiotZrodlowy)) {
            if (potencjalnaGrupa.getIdGrupy() != idGrupy &&
                    potencjalnaGrupa.getRodzajGrupy() == grupaZrodlowa.getRodzajGrupy() &&
                    potencjalnaGrupa.getZajeteMiejsca() < potencjalnaGrupa.getLimitMiejsc()) {

                boolean moznaDolaczyc = true;
                for (Student student : grupaDAO.pobierzStudentow(grupaZrodlowa)) {
                    if (czyWystepujeKonflikt(student, potencjalnaGrupa)) {
                        moznaDolaczyc = false;
                        break;
                    }
                }

                if (moznaDolaczyc &&
                        (potencjalnaGrupa.getLimitMiejsc() - potencjalnaGrupa.getZajeteMiejsca()) >= grupaZrodlowa.getZajeteMiejsca()) {
                    dostepneGrupy.add(potencjalnaGrupa);
                }
            }
        }

        return dostepneGrupy;
    }

    @Override
    public boolean usunGrupe(int idGrupy) {
        Grupa grupa = grupy.get(idGrupy);
        if (grupa == null) {
            throw new IllegalArgumentException("Grupa o podanym ID nie istnieje");
        }

        if (grupa.getZajeteMiejsca() > 0) {
            return false;
        }

        grupaDAO.usunGrupe(grupa);
        grupy.remove(idGrupy);
        return true;
    }

    @Override
    public void usunGrupeZPrzedmiotow(int idGrupy) {
        Grupa grupa = grupy.get(idGrupy);
        if (grupa != null) {
            for (Przedmiot przedmiot : przedmiotDAO.pobierzWszystkiePrzedmioty()) {
                if (przedmiot.getGrupy().contains(grupa)) {
                    przedmiot.getGrupy().remove(grupa);
                    przedmiotDAO.aktualizujPrzedmiot(przedmiot);
                }
            }
        }
    }

    @Override
    public boolean zapiszStudentaDoGrupy(int idGrupy, int nrIndeksu) {
        Grupa grupa = grupy.get(idGrupy);
        if (grupa == null) {
            throw new IllegalStateException("Grupa o podanym ID nie istnieje");
        }

        Student student = pobranieDanych.pobierzStudenta(nrIndeksu);
        if (student == null) {
            throw new IllegalStateException("Student o podanym numerze indeksu nie istnieje");
        }

        if (czyWystepujeKonflikt(student, grupa)) {
            throw new IllegalStateException("Występuje konflikt w planie zajęć");
        }

        return grupaDAO.zapiszStudentaDoGrupy(grupa, student);
    }
}
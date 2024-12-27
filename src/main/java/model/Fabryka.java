package main.java.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class Fabryka {
    private Collection<Kierunek> kierunki;
    private Collection<TuraZapisow> turyZapisow;
    private Collection<Grupa> grupy;
    private Collection<Student> studenci;
    private Collection<Prowadzacy> prowadzacy;
    private Map<Integer, Przedmiot> przedmioty;

    public Fabryka() {
        this.kierunki = new ArrayList<>();
        this.turyZapisow = new ArrayList<>();
        this.grupy = new ArrayList<>();
        this.studenci = new ArrayList<>();
        this.prowadzacy = new ArrayList<>();
        this.przedmioty = new HashMap<>();

        inicjalizujPrzykladoweDane();
    }

    private void inicjalizujPrzykladoweDane() {
        // Inicjalizacja obiektów DAO
        KierunekDAO kierunekDAO = new KierunekDAO();
        PrzedmiotDAO przedmiotDAO = new PrzedmiotDAO(kierunki);
        GrupaDAO grupaDAO = new GrupaDAO(kierunki);
        TuraZapisowDAO turaZapisowDAO = new TuraZapisowDAO(turyZapisow);

        // Tworzenie prowadzących
        Prowadzacy p1 = new Prowadzacy("Jan", "Kowalski", "dr");
        Prowadzacy p2 = new Prowadzacy("Anna", "Nowak", "dr hab.");
        Prowadzacy p3 = new Prowadzacy("Piotr", "Wiśniewski", "prof.");
        Prowadzacy p4 = new Prowadzacy("Maria", "Kowalczyk", "dr inż.");
        Prowadzacy p5 = new Prowadzacy("Tomasz", "Lewandowski", "dr hab. inż.");
        prowadzacy.addAll(Arrays.asList(p1, p2, p3, p4, p5));

        // Tworzenie kierunków
        Kierunek inf = kierunekDAO.utworzKierunek("Informatyka", "INF");
        Kierunek is = kierunekDAO.utworzKierunek("Informatyka Stosowana", "IS");
        kierunki.addAll(Arrays.asList(inf, is));

        // Tworzenie przedmiotów dla Informatyki
        Przedmiot ioInf = przedmiotDAO.utworzPrzedmiot("Inżynieria Oprogramowania", "IO");
        Przedmiot bdInf = przedmiotDAO.utworzPrzedmiot("Bazy Danych", "BD");
        Przedmiot po = przedmiotDAO.utworzPrzedmiot("Programowanie Obiektowe", "PO");

        // Tworzenie przedmiotów dla Informatyki Stosowanej
        Przedmiot ioIs = przedmiotDAO.utworzPrzedmiot("Inżynieria Oprogramowania", "IO");
        Przedmiot bdIs = przedmiotDAO.utworzPrzedmiot("Bazy Danych", "BD");
        Przedmiot so = przedmiotDAO.utworzPrzedmiot("Systemy Operacyjne", "SO");

        // Grupy dla IO na Informatyce
        Grupa g1 = grupaDAO.utworzGrupe(LocalTime.of(8, 0), LocalTime.of(9, 30), 1, 120);
        g1.setProwadzacy(p1);
        g1.setRodzajGrupy(RodzajGrupy.WYKLAD);

        Grupa g2 = grupaDAO.utworzGrupe(LocalTime.of(10, 0), LocalTime.of(11, 30), 1, 30);
        g2.setProwadzacy(p2);
        g2.setRodzajGrupy(RodzajGrupy.CWICZENIA);

        Grupa g3 = grupaDAO.utworzGrupe(LocalTime.of(12, 0), LocalTime.of(13, 30), 2, 30);
        g3.setProwadzacy(p2);
        g3.setRodzajGrupy(RodzajGrupy.CWICZENIA);

        Grupa g4 = grupaDAO.utworzGrupe(LocalTime.of(14, 0), LocalTime.of(15, 30), 2, 15);
        g4.setProwadzacy(p3);
        g4.setRodzajGrupy(RodzajGrupy.LABORATORIUM);

        Grupa g5 = grupaDAO.utworzGrupe(LocalTime.of(8, 0), LocalTime.of(9, 30), 3, 15);
        g5.setProwadzacy(p3);
        g5.setRodzajGrupy(RodzajGrupy.LABORATORIUM);

        // Grupy dla BD na Informatyce
        Grupa g6 = grupaDAO.utworzGrupe(LocalTime.of(10, 0), LocalTime.of(11, 30), 3, 100);
        g6.setProwadzacy(p4);
        g6.setRodzajGrupy(RodzajGrupy.WYKLAD);

        Grupa g7 = grupaDAO.utworzGrupe(LocalTime.of(12, 0), LocalTime.of(13, 30), 3, 15);
        g7.setProwadzacy(p4);
        g7.setRodzajGrupy(RodzajGrupy.LABORATORIUM);

        Grupa g8 = grupaDAO.utworzGrupe(LocalTime.of(14, 0), LocalTime.of(15, 30), 4, 15);
        g8.setProwadzacy(p4);
        g8.setRodzajGrupy(RodzajGrupy.LABORATORIUM);

        Grupa g9 = grupaDAO.utworzGrupe(LocalTime.of(8, 0), LocalTime.of(9, 30), 5, 15);
        g9.setProwadzacy(p4);
        g9.setRodzajGrupy(RodzajGrupy.LABORATORIUM);

        // Grupy dla IO na Informatyce Stosowanej
        Grupa g10 = grupaDAO.utworzGrupe(LocalTime.of(10, 0), LocalTime.of(11, 30), 3, 80);
        g10.setProwadzacy(p5);
        g10.setRodzajGrupy(RodzajGrupy.WYKLAD);

        Grupa g11 = grupaDAO.utworzGrupe(LocalTime.of(12, 0), LocalTime.of(13, 30), 4, 30);
        g11.setProwadzacy(p1);
        g11.setRodzajGrupy(RodzajGrupy.CWICZENIA);

        // Kontynuacja inicjalizacji grup
        Grupa g12 = grupaDAO.utworzGrupe(LocalTime.of(14, 0), LocalTime.of(15, 30), 4, 30);
        g12.setProwadzacy(p1);
        g12.setRodzajGrupy(RodzajGrupy.CWICZENIA);

        Grupa g13 = grupaDAO.utworzGrupe(LocalTime.of(8, 0), LocalTime.of(9, 30), 5, 30);
        g13.setProwadzacy(p1);
        g13.setRodzajGrupy(RodzajGrupy.CWICZENIA);

        // Grupy dla BD na Informatyce Stosowanej
        Grupa g14 = grupaDAO.utworzGrupe(LocalTime.of(10, 0), LocalTime.of(11, 30), 4, 90);
        g14.setProwadzacy(p2);
        g14.setRodzajGrupy(RodzajGrupy.WYKLAD);

        Grupa g15 = grupaDAO.utworzGrupe(LocalTime.of(12, 0), LocalTime.of(13, 30), 5, 15);
        g15.setProwadzacy(p3);
        g15.setRodzajGrupy(RodzajGrupy.LABORATORIUM);

        Grupa g16 = grupaDAO.utworzGrupe(LocalTime.of(14, 0), LocalTime.of(15, 30), 5, 15);
        g16.setProwadzacy(p3);
        g16.setRodzajGrupy(RodzajGrupy.LABORATORIUM);

        // Grupy dla SO
        Grupa g17 = grupaDAO.utworzGrupe(LocalTime.of(8, 0), LocalTime.of(9, 30), 1, 100);
        g17.setProwadzacy(p5);
        g17.setRodzajGrupy(RodzajGrupy.WYKLAD);

        Grupa g18 = grupaDAO.utworzGrupe(LocalTime.of(10, 0), LocalTime.of(11, 30), 1, 20);
        g18.setProwadzacy(p5);
        g18.setRodzajGrupy(RodzajGrupy.PROJEKT);

        Grupa g19 = grupaDAO.utworzGrupe(LocalTime.of(12, 0), LocalTime.of(13, 30), 2, 20);
        g19.setProwadzacy(p5);
        g19.setRodzajGrupy(RodzajGrupy.PROJEKT);

        // Grupy dla PO
        Grupa g20 = grupaDAO.utworzGrupe(LocalTime.of(10, 0), LocalTime.of(11, 30), 2, 120);
        g20.setProwadzacy(p1);
        g20.setRodzajGrupy(RodzajGrupy.WYKLAD);

        Grupa g21 = grupaDAO.utworzGrupe(LocalTime.of(12, 0), LocalTime.of(13, 30), 2, 30);
        g21.setProwadzacy(p2);
        g21.setRodzajGrupy(RodzajGrupy.CWICZENIA);

        Grupa g22 = grupaDAO.utworzGrupe(LocalTime.of(14, 0), LocalTime.of(15, 30), 3, 15);
        g22.setProwadzacy(p3);
        g22.setRodzajGrupy(RodzajGrupy.LABORATORIUM);

        // Przypisanie grup do przedmiotów
        przedmiotDAO.dodajGrupeDoPrzedmiotu(ioInf, g1);
        przedmiotDAO.dodajGrupeDoPrzedmiotu(ioInf, g2);
        przedmiotDAO.dodajGrupeDoPrzedmiotu(ioInf, g3);
        przedmiotDAO.dodajGrupeDoPrzedmiotu(ioInf, g4);
        przedmiotDAO.dodajGrupeDoPrzedmiotu(ioInf, g5);

        przedmiotDAO.dodajGrupeDoPrzedmiotu(bdInf, g6);
        przedmiotDAO.dodajGrupeDoPrzedmiotu(bdInf, g7);
        przedmiotDAO.dodajGrupeDoPrzedmiotu(bdInf, g8);
        przedmiotDAO.dodajGrupeDoPrzedmiotu(bdInf, g9);

        przedmiotDAO.dodajGrupeDoPrzedmiotu(po, g20);
        przedmiotDAO.dodajGrupeDoPrzedmiotu(po, g21);
        przedmiotDAO.dodajGrupeDoPrzedmiotu(po, g22);

        przedmiotDAO.dodajGrupeDoPrzedmiotu(ioIs, g10);
        przedmiotDAO.dodajGrupeDoPrzedmiotu(ioIs, g11);
        przedmiotDAO.dodajGrupeDoPrzedmiotu(ioIs, g12);
        przedmiotDAO.dodajGrupeDoPrzedmiotu(ioIs, g13);

        przedmiotDAO.dodajGrupeDoPrzedmiotu(bdIs, g14);
        przedmiotDAO.dodajGrupeDoPrzedmiotu(bdIs, g15);
        przedmiotDAO.dodajGrupeDoPrzedmiotu(bdIs, g16);

        przedmiotDAO.dodajGrupeDoPrzedmiotu(so, g17);
        przedmiotDAO.dodajGrupeDoPrzedmiotu(so, g18);
        przedmiotDAO.dodajGrupeDoPrzedmiotu(so, g19);

        // Tworzenie i przypisywanie studentów
        Student s1 = new Student(123456, "Adam", "Nowicki");
        s1.setSrednia(4.5f);
        s1.getKierunki().add(inf);

        Student s2 = new Student(234567, "Karolina", "Wojcik");
        s2.setSrednia(4.8f);
        s2.getKierunki().add(inf);

        Student s3 = new Student(345678, "Michał", "Kowalczyk");
        s3.setSrednia(4.2f);
        s3.getKierunki().add(inf);

        Student s4 = new Student(456789, "Anna", "Lewandowska");
        s4.setSrednia(4.6f);
        s4.getKierunki().add(is);

        Student s5 = new Student(567890, "Piotr", "Zieliński");
        s5.setSrednia(4.0f);
        s5.getKierunki().add(is);

        Student s6 = new Student(678901, "Julia", "Szymańska");
        s6.setSrednia(4.7f);
        s6.getKierunki().add(is);

        Student sigma = new Student(2137, "Miłosz", "Lewandowski");
        sigma.setSrednia(5.0f);
        sigma.getKierunki().add(inf);

        studenci.addAll(Arrays.asList(s1, s2, s3, s4, s5, s6, sigma));

        // Zapisanie studentów do grup
        grupaDAO.zapiszStudentaDoGrupy(g1, s1);
        grupaDAO.zapiszStudentaDoGrupy(g2, s1);
        grupaDAO.zapiszStudentaDoGrupy(g4, s1);

        grupaDAO.zapiszStudentaDoGrupy(g1, s2);
        grupaDAO.zapiszStudentaDoGrupy(g3, s2);
        grupaDAO.zapiszStudentaDoGrupy(g5, s2);
        grupaDAO.zapiszStudentaDoGrupy(g6, s2);
        grupaDAO.zapiszStudentaDoGrupy(g7, s2);

        grupaDAO.zapiszStudentaDoGrupy(g6, s3);
        grupaDAO.zapiszStudentaDoGrupy(g8, s3);

        grupaDAO.zapiszStudentaDoGrupy(g10, s4);
        grupaDAO.zapiszStudentaDoGrupy(g11, s4);

        grupaDAO.zapiszStudentaDoGrupy(g14, s5);
        grupaDAO.zapiszStudentaDoGrupy(g15, s5);
        grupaDAO.zapiszStudentaDoGrupy(g17, s5);
        grupaDAO.zapiszStudentaDoGrupy(g18, s5);

        grupaDAO.zapiszStudentaDoGrupy(g17, s6);
        grupaDAO.zapiszStudentaDoGrupy(g19, s6);

        // Dodanie wszystkich grup do głównej kolekcji
        grupy.addAll(Arrays.asList(g1, g2, g3, g4, g5, g6, g7, g8, g9, g10,
                g11, g12, g13, g14, g15, g16, g17, g18, g19, g20, g21, g22));

        // Tworzenie tur zapisów
        TuraZapisow t1 = turaZapisowDAO.utworzTureZapisow(
                inf, 4.0f,
                LocalTime.of(0, 0), LocalTime.of(23, 59)
        );

        TuraZapisow t2 = turaZapisowDAO.utworzTureZapisow(
                inf, 0.0f,
                LocalTime.of(0, 0), LocalTime.of(23, 59)
        );

        TuraZapisow t3 = turaZapisowDAO.utworzTureZapisow(
                is, 4.0f,
                LocalTime.of(0, 0), LocalTime.of(23, 59)
        );

        TuraZapisow t4 = turaZapisowDAO.utworzTureZapisow(
                is, 0.0f,
                LocalTime.of(0, 0), LocalTime.of(23, 59)
        );

        // Przypisanie przedmiotów do kierunków
        kierunekDAO.dodajPrzedmiotyDoKierunku(inf, Arrays.asList(ioInf, bdInf, po));
        kierunekDAO.dodajPrzedmiotyDoKierunku(is, Arrays.asList(ioIs, bdIs, so));

        // Dodanie wszystkich przedmiotów do mapy
        przedmioty.put(ioInf.getIdPrzedmiotu(), ioInf);
        przedmioty.put(bdInf.getIdPrzedmiotu(), bdInf);
        przedmioty.put(po.getIdPrzedmiotu(), po);
        przedmioty.put(ioIs.getIdPrzedmiotu(), ioIs);
        przedmioty.put(bdIs.getIdPrzedmiotu(), bdIs);
        przedmioty.put(so.getIdPrzedmiotu(), so);
    }

    private void zapiszStudentaDoGrupy(Student student, Grupa grupa) {
        grupa.getCzlonkowie().add(student);
        grupa.setZajeteMiejsca(grupa.getZajeteMiejsca() + 1);
    }

    public Collection<Kierunek> getKierunki() {
        return new ArrayList<>(kierunki);
    }

    public Collection<TuraZapisow> getTuryZapisow() {
        return new ArrayList<>(turyZapisow);
    }

    public Collection<Grupa> getGrupy() {
        return new ArrayList<>(grupy);
    }

    public Collection<Student> getStudenci() {
        return new ArrayList<>(studenci);
    }

    public Student getStudent(int nrIndeksu) {
        return studenci.stream()
                .filter(s -> s.getNrAlbumu() == nrIndeksu)
                .findFirst()
                .orElse(null);
    }

    public Grupa getGrupa(int idGrupy) {
        return grupy.stream()
                .filter(g -> g.getIdGrupy() == idGrupy)
                .findFirst()
                .orElse(null);
    }
}
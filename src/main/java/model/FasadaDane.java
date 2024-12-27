package main.java.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FasadaDane implements PobranieDanych {
    private final Fabryka fabryka;
    private final KierunekDAO kierunekDAO;
    private final PrzedmiotDAO przedmiotDAO;
    private final GrupaDAO grupaDAO;
    private final TuraZapisowDAO turaZapisowDAO;

    private List<Kierunek> kierunki;
    private List<TuraZapisow> turyZapisow;
    private Map<Integer, Student> studenci;
    private Map<Integer, Grupa> grupy;
    private Map<Integer, Przedmiot> przedmioty;

    public FasadaDane() {
        this.fabryka = new Fabryka();

        // Inicjalizacja kolekcji
        this.kierunki = new ArrayList<>();
        this.turyZapisow = new ArrayList<>();
        this.studenci = new HashMap<>();
        this.grupy = new HashMap<>();
        this.przedmioty = new HashMap<>();

        // Inicjalizacja obiektów DAO
        this.kierunekDAO = new KierunekDAO();
        this.przedmiotDAO = new PrzedmiotDAO(kierunki);
        this.grupaDAO = new GrupaDAO(kierunki);
        this.turaZapisowDAO = new TuraZapisowDAO(turyZapisow);

        wczytajDaneZFabryki();
    }

    private void wczytajDaneZFabryki() {
        // Pobieranie danych podstawowych
        this.kierunki.addAll(fabryka.getKierunki());
        this.turyZapisow.addAll(fabryka.getTuryZapisow());

        // Konwersja kolekcji studentów na mapę
        for (Student student : fabryka.getStudenci()) {
            this.studenci.put(student.getNrAlbumu(), student);
        }

        // Konwersja kolekcji grup na mapę
        for (Grupa grupa : fabryka.getGrupy()) {
            this.grupy.put(grupa.getIdGrupy(), grupa);
        }

        // Pobieranie i mapowanie przedmiotów
        List<Przedmiot> wszystkiePrzedmioty = przedmiotDAO.pobierzWszystkiePrzedmioty();
        for (Przedmiot przedmiot : wszystkiePrzedmioty) {
            this.przedmioty.put(przedmiot.getIdPrzedmiotu(), przedmiot);
        }
    }

    @Override
    public List<Kierunek> pobierzKierunki() {
        return new ArrayList<>(kierunki);
    }

    @Override
    public List<TuraZapisow> pobierzTury(Kierunek kierunek) {
        List<TuraZapisow> turyKierunku = new ArrayList<>();
        for (TuraZapisow tura : turyZapisow) {
            if (turaZapisowDAO.czytajKierunek(tura).equals(kierunek)) {
                turyKierunku.add(tura);
            }
        }
        return turyKierunku;
    }

    @Override
    public Student pobierzStudenta(int nrIndeksu) {
        return studenci.get(nrIndeksu);
    }

    @Override
    public Grupa pobierzGrupe(int idGrupy) {
        return grupy.get(idGrupy);
    }

    @Override
    public TuraZapisow pobierzTure(int idTury) {
        for (TuraZapisow tura : turyZapisow) {
            if (tura.getIdTury() == idTury) {
                return tura;
            }
        }
        return null;
    }

    @Override
    public List<Grupa> pobierzGrupyDlaKierunku(String kodKierunku) {
        List<Grupa> grupyKierunku = new ArrayList<>();

        // Znajdź kierunek
        Kierunek kierunek = kierunekDAO.pobierzKierunek(kodKierunku);
        if (kierunek == null) {
            return grupyKierunku;
        }

        // Pobierz grupy dla każdego przedmiotu na kierunku
        for (Przedmiot przedmiot : kierunek.getPrzedmioty()) {
            grupyKierunku.addAll(przedmiotDAO.pobierzGrupy(przedmiot));
        }

        return grupyKierunku;
    }

    @Override
    public Przedmiot pobierzPrzedmiot(int idPrzedmiotu) {
        return przedmioty.get(idPrzedmiotu);
    }
}
package model;

import java.util.List;

public interface PobranieDanych {
    List<Kierunek> pobierzKierunki();
    List<TuraZapisow> pobierzTury(Kierunek kierunek);
    Student pobierzStudenta(int nrIndeksu);
    Grupa pobierzGrupe(int idGrupy);
    TuraZapisow pobierzTure(int idTury);
    List<Grupa> pobierzGrupyDlaKierunku(String kodKierunku);
    Przedmiot pobierzPrzedmiot(int idPrzedmiotu);
}
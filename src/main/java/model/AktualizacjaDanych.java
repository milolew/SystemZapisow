package main.java.model;

import java.util.List;

public interface AktualizacjaDanych {
    void dodajStudenta(int idGrupy, int nrIndeksu);
    boolean przeniesStudenta(int idGrupyZrodlowej, int idGrupyDocelowej, int nrIndeksu);
    boolean usunGrupe(int idGrupy);
    List<Grupa> pobierzDostepneGrupyDoPrezeniesienia(int idGrupy);
    void usunGrupeZPrzedmiotow(int idGrupy);
    boolean zapiszStudentaDoGrupy(int idGrupy, int nrIndeksu);
}
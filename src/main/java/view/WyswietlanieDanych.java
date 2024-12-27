package main.java.view;

import java.util.List;

import main.java.model.Grupa;
import main.java.model.Kierunek;
import main.java.model.Przedmiot;
import main.java.model.TuraZapisow;

public interface WyswietlanieDanych {
    /**
     * Wyświetla dostępne tury zapisów
     * @param tury lista tur zapisów do wyświetlenia
     */
    void wyswietlTuryZapisow(List<TuraZapisow> tury);

    /**
     * Wyświetla listę kierunków
     * @param kierunki lista kierunków do wyświetlenia
     */
    void wyswietlKierunki(List<Kierunek> kierunki);

    /**
     * Wyświetla grupy zajęciowe
     * @param grupy lista grup do wyświetlenia
     */
    void wyswietlGrupy(List<Grupa> grupy);

    /**
     * Wyświetla listę przedmiotów
     * @param przedmioty lista przedmiotów do wyświetlenia
     */
    void wyswietlPrzedmioty(List<Przedmiot> przedmioty);

    /**
     * Wyświetla komunikat dla użytkownika
     * @param tresc treść komunikatu do wyświetlenia
     */
    void wyswietlKomunikat(String tresc);
}
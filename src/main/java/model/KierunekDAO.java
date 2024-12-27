package main.java.model;

import java.util.Collection;

public class KierunekDAO {
    /**
     * Tworzy nowy kierunek
     * @param nazwa nazwa kierunku
     * @param kod kod kierunku
     * @return utworzony kierunek
     */
    public Kierunek utworzKierunek(String nazwa, String kod) {
        return new Kierunek(kod, nazwa);
    }

    /**
     * Pobiera kierunek o zadanym kodzie
     * @param kod kod kierunku
     * @return znaleziony kierunek lub null
     */
    public Kierunek pobierzKierunek(String kod) {
        // W rzeczywistej implementacji: pobranie z bazy danych
        return null;
    }

    /**
     * Dodaje przedmiot do kierunku
     * @param kierunek kierunek do którego dodajemy przedmiot
     * @param przedmiot przedmiot do dodania
     */
    public void dodajPrzedmiotDoKierunku(Kierunek kierunek, Przedmiot przedmiot) {
        if (!kierunek.getPrzedmioty().contains(przedmiot)) {
            kierunek.getPrzedmioty().add(przedmiot);
        }
    }

    /**
     * Dodaje kolekcję przedmiotów do kierunku
     * @param kierunek kierunek do którego dodajemy przedmioty
     * @param przedmioty kolekcja przedmiotów do dodania
     */
    public void dodajPrzedmiotyDoKierunku(Kierunek kierunek, Collection<Przedmiot> przedmioty) {
        for (Przedmiot przedmiot : przedmioty) {
            dodajPrzedmiotDoKierunku(kierunek, przedmiot);
        }
    }
}
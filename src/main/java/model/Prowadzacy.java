package main.java.model;

public class Prowadzacy extends Uzytkownik {
    private String stopienNaukowy;

    public Prowadzacy(String imie, String nazwisko, String stopienNaukowy) {
        super();
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.stopienNaukowy = stopienNaukowy;
    }

    public String getStopienNaukowy() {
        return stopienNaukowy;
    }

    public void setStopienNaukowy(String stopienNaukowy) {
        this.stopienNaukowy = stopienNaukowy;
    }

    public String getImieNazwisko() {
        return imie + " " + nazwisko;
    }
}
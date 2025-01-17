package model;

public interface Weryfikacja {
    boolean zaloguj(String login, String haslo);
    String getRola(String login);
    int getNrIndeksu(String login); 
    boolean sprawdzUprawnienia(int nrIndeksu); // dawna metoda z WeryfikacjaStudenta
}
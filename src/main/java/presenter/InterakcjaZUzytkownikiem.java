package main.java.presenter;

/**
 * Interfejs definiujący interakcje użytkownika z systemem
 */
public interface InterakcjaZUzytkownikiem {
    /**
     * Uruchamia system i wyświetla początkowy ekran
     */
    void uruchomSystem();

    /**
     * Obsługuje proces logowania użytkownika
     * @param login login użytkownika
     * @param haslo hasło użytkownika
     */
    void zaloguj(String login, String haslo);

    /**
     * Obsługuje wybór opcji z menu
     * @param opcja numer wybranej opcji
     */
    void wybierzOpcje(int opcja);

}
package presenter;

import model.*;
import view.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PrezenterSterowanie implements InterakcjaZUzytkownikiem {
    private final Weryfikacja weryfikacja;
    private final AktualizacjaDanych aktualizacjaDanych;
    private final PobranieDanych pobranieDanych;
    private final WyswietlanieDanych wyswietlanieDanych;
    private final FabrykaPrezenterow fabrykaPrezenterow;
    private WidokUzytkownika aktualnyWidok;
    private String zalogowanyUzytkownik;
    private StanPrezentera aktualnyStan;

    public PrezenterSterowanie(Weryfikacja weryfikacja,
                               AktualizacjaDanych aktualizacjaDanych,
                               PobranieDanych pobranieDanych,
                               WyswietlanieDanych wyswietlanieDanych) {
        this.weryfikacja = weryfikacja;
        this.aktualizacjaDanych = aktualizacjaDanych;
        this.pobranieDanych = pobranieDanych;
        this.wyswietlanieDanych = wyswietlanieDanych;
        this.fabrykaPrezenterow = new FabrykaPrezenterow(wyswietlanieDanych);
    }

    @Override
    public void uruchomSystem() {
        wyswietlanieDanych.wyswietlKomunikat("=== System zapisów na zajęcia ===");
        wyswietlanieDanych.wyswietlKomunikat("Podaj login: ");
    }

    @Override
    public void zaloguj(String login, String haslo) {
        if (weryfikacja.zaloguj(login, haslo)) {
            zalogowanyUzytkownik = login;
            String rola = weryfikacja.getRola(login);

            if(rola == null) {
                wyswietlanieDanych.wyswietlKomunikat("Błędny login lub hasło!");
                uruchomSystem();
            }

            try {
                aktualnyWidok = fabrykaPrezenterow.utworzWidok(rola);

                if (rola.equals("STUDENT")) {
                    int nrIndeksu = weryfikacja.getNrIndeksu(login);
                    Student student = pobranieDanych.pobierzStudenta(nrIndeksu);
                    if (student != null) {
                        aktualnyWidok.wyswietlMenu();
                        aktualnyStan = new StanMenuStudenta(this);
                    } else {
                        ((WidokStudenta) aktualnyWidok).wyswietlBladLogowania();
                        uruchomSystem();
                    }
                } else if (rola.equals("PRACOWNIK")) {
                    aktualnyWidok.wyswietlMenu();
                    aktualnyStan = new StanMenuPracownika(this);
                }
            } catch (IllegalArgumentException e) {
                wyswietlanieDanych.wyswietlKomunikat("Błąd: " + e.getMessage());
                uruchomSystem();
            }
        } else {
            wyswietlanieDanych.wyswietlKomunikat("Błędny login lub hasło!");
            uruchomSystem();
        }
    }

    @Override
    public void wybierzOpcje(int opcja) {
        if (aktualnyStan != null) {
            aktualnyStan.obslugaWyboru(opcja);
        }
    }

    public void ustawStan(StanPrezentera stan) {
        this.aktualnyStan = stan;
    }

    public StanPrezentera getStan() {
        return aktualnyStan;
    }

    public WidokUzytkownika getAktualnyWidok() {
        return aktualnyWidok;
    }

    public WyswietlanieDanych getWyswietlanieDanych() {
        return wyswietlanieDanych;
    }

    public void wyswietlKomunikat(String komunikat) {
        wyswietlanieDanych.wyswietlKomunikat(komunikat);
    }

    public List<TuraZapisow> pobierzTuryDlaStudenta() {
        List<Kierunek> kierunkiStudenta = pobranieDanych
                .pobierzStudenta(weryfikacja.getNrIndeksu(zalogowanyUzytkownik))
                .getKierunki();

        List<TuraZapisow> wszystkieTury = new ArrayList<>();
        for (Kierunek kierunek : kierunkiStudenta) {
            wszystkieTury.addAll(pobranieDanych.pobierzTury(kierunek));
        }
        return wszystkieTury;
    }

    public List<Kierunek> pobierzKierunki() {
        return pobranieDanych.pobierzKierunki();
    }

    public TuraZapisow pobierzTure(int idTury) {
        return pobranieDanych.pobierzTure(idTury);
    }

    public boolean sprawdzWarunkiTury(TuraZapisow tura) {
        Student student = pobranieDanych.pobierzStudenta(weryfikacja.getNrIndeksu(zalogowanyUzytkownik));

        if (student.getSrednia() < tura.getWymaganaSrednia()) {
            wyswietlKomunikat(String.format("Wymagana średnia: %.2f. Twoja średnia: %.2f",
                    tura.getWymaganaSrednia(), student.getSrednia()));
            return false;
        }

        LocalDateTime teraz = LocalDateTime.now();
        if (teraz.isBefore(tura.getGodzRozpoczecia()) || teraz.isAfter(tura.getGodzZakonczenia())) {
            wyswietlKomunikat(String.format("Tura jest dostępna od %s do %s",
                    tura.getGodzRozpoczecia(), tura.getGodzZakonczenia()));
            return false;
        }

        return true;
    }

    public Przedmiot pobierzPrzedmiot(int idPrzedmiotu) {
        return pobranieDanych.pobierzPrzedmiot(idPrzedmiotu);
    }

    public Grupa pobierzGrupe(int idGrupy) {
        return pobranieDanych.pobierzGrupe(idGrupy);
    }

    public boolean sprawdzDostepnoscPrzedmiotu(Przedmiot przedmiot) {
        Student student = pobranieDanych.pobierzStudenta(weryfikacja.getNrIndeksu(zalogowanyUzytkownik));
        if (student.getGrupy().stream().anyMatch(grupa -> przedmiot.getGrupy().contains(grupa))) {
            wyswietlKomunikat("Jesteś już zapisany na ten przedmiot!");
            return false;
        }
        return true;
    }

    public void wyswietlDostepneGrupy(Przedmiot przedmiot, RodzajGrupy typ) {
        List<Grupa> dostepneGrupy = przedmiot.getGrupy().stream()
                .filter(g -> g.getRodzajGrupy() == typ && g.czyMaMiejsca())
                .collect(Collectors.toList());

        wyswietlKomunikat("Wybierz grupę typu " + typ.name() + ":");
        ((WidokStudenta) aktualnyWidok).wyswietlDostepneGrupy(dostepneGrupy);
    }

    public boolean zapiszDoGrupy(int idGrupy) {
        try {
            return aktualizacjaDanych.zapiszStudentaDoGrupy(
                    idGrupy,
                    weryfikacja.getNrIndeksu(zalogowanyUzytkownik)
            );
        } catch (IllegalStateException e) {
            wyswietlKomunikat("Błąd: " + e.getMessage());
            return false;
        }
    }

    public Student pobierzZalogowanegoStudenta() {
        return pobranieDanych.pobierzStudenta(
                weryfikacja.getNrIndeksu(zalogowanyUzytkownik)
        );
    }

    public void usunGrupe(int idGrupy) {
        aktualizacjaDanych.usunGrupe(idGrupy);
    }

    public List<Grupa> pobierzGrupyDoPrezeniesienia(int idGrupy) {
        return aktualizacjaDanych.pobierzDostepneGrupyDoPrezeniesienia(idGrupy);
    }

    public boolean przeniesStudentow(int idGrupyZrodlowej, int idGrupyDocelowej) {
        Grupa grupaZrodlowa = pobranieDanych.pobierzGrupe(idGrupyZrodlowej);
        Grupa grupaDocelowa = pobranieDanych.pobierzGrupe(idGrupyDocelowej);

        // Sprawdzenie czy grupy istnieją
        if (grupaZrodlowa == null || grupaDocelowa == null) {
            wyswietlKomunikat("Jedna z grup nie istnieje!");
            return false;
        }

        // Sprawdzenie czy grupa źródłowa ma studentów
        if (grupaZrodlowa.getCzlonkowie().isEmpty()) {
            wyswietlKomunikat("Grupa źródłowa jest pusta!");
            return false;
        }

        // Znajdź przedmioty do których należą grupy
        Przedmiot przedmiotZrodlowy = znajdzPrzedmiotDlaGrupy(grupaZrodlowa);
        Przedmiot przedmiotDocelowy = znajdzPrzedmiotDlaGrupy(grupaDocelowa);

        if (przedmiotZrodlowy == null || przedmiotDocelowy == null) {
            wyswietlKomunikat("Nie można znaleźć przedmiotów dla grup!");
            return false;
        }

        // Sprawdź czy to ten sam przedmiot
        if (!przedmiotZrodlowy.equals(przedmiotDocelowy)) {
            wyswietlKomunikat("Grupy muszą należeć do tego samego przedmiotu!");
            return false;
        }

        // Sprawdź czy to ten sam kierunek
        Kierunek kierunekZrodlowy = znajdzKierunekDlaPrzedmiotu(przedmiotZrodlowy);
        Kierunek kierunekDocelowy = znajdzKierunekDlaPrzedmiotu(przedmiotDocelowy);

        if (kierunekZrodlowy == null || kierunekDocelowy == null || !kierunekZrodlowy.equals(kierunekDocelowy)) {
            wyswietlKomunikat("Grupy muszą należeć do tego samego kierunku!");
            return false;
        }

        // Sprawdzenie czy w grupie docelowej jest wystarczająco miejsca
        if (!czyJestMiejsceWGrupie(grupaDocelowa, grupaZrodlowa.getCzlonkowie().size())) {
            wyswietlKomunikat("Brak wystarczającej liczby miejsc w grupie docelowej!");
            return false;
        }

        List<Student> studenci = new ArrayList<>(grupaZrodlowa.getCzlonkowie());
        boolean wszystkoPrzeniesione = true;
        StringBuilder raport = new StringBuilder("Raport przeniesienia:\n");

        for (Student student : studenci) {
            try {
                boolean sukces = aktualizacjaDanych.przeniesStudenta(
                        idGrupyZrodlowej,
                        idGrupyDocelowej,
                        student.getNrAlbumu()
                );

                if (sukces) {
                    raport.append("Student ").append(student.getNrAlbumu())
                            .append(" - przeniesiony pomyślnie\n");
                } else {
                    wszystkoPrzeniesione = false;
                    raport.append("Student ").append(student.getNrAlbumu())
                            .append(" - nie udało się przenieść\n");
                }
            } catch (IllegalStateException e) {
                wszystkoPrzeniesione = false;
                raport.append("Student ").append(student.getNrAlbumu())
                        .append(" - ").append(e.getMessage()).append("\n");
            }
        }

        ((WidokPracownika) aktualnyWidok).wyswietlRaportPrzeniesienia(raport.toString());
        return wszystkoPrzeniesione;
    }

    private Przedmiot znajdzPrzedmiotDlaGrupy(Grupa grupa) {
        // Przejdź przez wszystkie kierunki
        for (Kierunek kierunek : pobranieDanych.pobierzKierunki()) {
            // Dla każdego przedmiotu na kierunku
            for (Przedmiot przedmiot : kierunek.getPrzedmioty()) {
                // Sprawdź czy grupa należy do tego przedmiotu
                if (przedmiot.getGrupy().contains(grupa)) {
                    return przedmiot;
                }
            }
        }
        return null;
    }

    private Kierunek znajdzKierunekDlaPrzedmiotu(Przedmiot przedmiot) {
        for (Kierunek kierunek : pobranieDanych.pobierzKierunki()) {
            if (kierunek.getPrzedmioty().contains(przedmiot)) {
                return kierunek;
            }
        }
        return null;
    }

    private boolean czyJestMiejsceWGrupie(Grupa grupa, int liczbaPrzenoszonych) {
        return (grupa.getLimitMiejsc() - grupa.getCzlonkowie().size()) >= liczbaPrzenoszonych;
    }

    public void wyloguj() {
        zalogowanyUzytkownik = null;
        aktualnyWidok = null;
        aktualnyStan = null;
        wyswietlKomunikat("Wylogowano pomyślnie!");
        uruchomSystem();
    }

    public static void main(String[] args) {
        PobranieDanych pobranieDanych = new FasadaDane();
        Weryfikacja weryfikacja = new FasadaWeryfikacja(pobranieDanych);
        AktualizacjaDanych aktualizacja = new FasadaAktualizacji(pobranieDanych);
        WyswietlanieDanych wyswietlanie = new FasadaWidok();

        PrezenterSterowanie prezenter = new PrezenterSterowanie(
                weryfikacja,
                aktualizacja,
                pobranieDanych,
                wyswietlanie
        );

        prezenter.uruchomSystem();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String input = scanner.nextLine();

                if (prezenter.zalogowanyUzytkownik == null) {
                    wyswietlanie.wyswietlKomunikat("Podaj hasło: ");
                    String haslo = scanner.nextLine();
                    prezenter.zaloguj(input, haslo);
                } else {
                    try {
                        prezenter.wybierzOpcje(Integer.parseInt(input));
                    } catch (NumberFormatException e) {
                        wyswietlanie.wyswietlKomunikat("Nieprawidłowa opcja. Podaj liczbę.");
                    }
                }
            }
        }
    }
}
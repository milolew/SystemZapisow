package presenter;

import model.*;
import view.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/*
* 2. Fasada Widok
* 3. Przedmiot (trzeba dodać throw)
* 4. FasadaAktualizacji
* */


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

    // Metody pomocnicze dla stanów
    public void ustawStan(StanPrezentera stan) {
        this.aktualnyStan = stan;
    }

    public WidokUzytkownika getAktualnyWidok() {
        return aktualnyWidok;
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
        if (grupaZrodlowa == null) {
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
                            .append(" - nie udało się przenieść (brak miejsc)\n");
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
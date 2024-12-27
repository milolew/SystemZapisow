package main.java.view;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import main.java.model.*;

public class WidokStudenta implements WidokUzytkownika {
    private final WyswietlanieDanych wyswietlanieDanych;

    public WidokStudenta(WyswietlanieDanych wyswietlanieDanych) {
        this.wyswietlanieDanych = wyswietlanieDanych;
    }

    @Override
    public void wyswietlMenu() {
        wyswietlanieDanych.wyswietlKomunikat("\nMenu studenta:");
        wyswietlanieDanych.wyswietlKomunikat("1. Przystąp do tury zapisów");
        wyswietlanieDanych.wyswietlKomunikat("2. Wyświetl plan zajęć");
        wyswietlanieDanych.wyswietlKomunikat("0. Wyloguj");
        wyswietlanieDanych.wyswietlKomunikat("\nWybierz opcję: ");
    }

    public void wyswietlTuryZapisowDlaStudenta(List<TuraZapisow> tury) {
        if (tury.isEmpty()) {
            wyswietlanieDanych.wyswietlKomunikat("Brak dostępnych tur zapisów.");
            wyswietlMenu();
            return;
        }

        wyswietlanieDanych.wyswietlKomunikat("\n=== Dostępne tury zapisów ===");
        wyswietlanieDanych.wyswietlKomunikat("------------------------------------------");
        for (TuraZapisow tura : tury) {
            wyswietlanieDanych.wyswietlKomunikat(String.format(
                    "ID tury: %d\nKierunek: %s\nWymagana średnia: %.2f\nRozpoczęcie: %s\nZakończenie: %s",
                    tura.getIdTury(),
                    tura.getKierunek().getNazwa(),
                    tura.getWymaganaSrednia(),
                    tura.getGodzRozpoczecia().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    tura.getGodzZakonczenia().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            ));
            wyswietlanieDanych.wyswietlKomunikat("------------------------------------------");
        }
        wyswietlanieDanych.wyswietlKomunikat("\nWybierz ID tury (0 aby wrócić): ");
    }

    public void wyswietlBladLogowania() {
        wyswietlanieDanych.wyswietlKomunikat("Błąd: Nie znaleziono danych studenta!");
    }

    public void wyswietlDostepneGrupy(List<Grupa> grupy) {
        if (grupy.isEmpty()) {
            wyswietlanieDanych.wyswietlKomunikat("Brak dostępnych grup.");
            return;
        }

        wyswietlanieDanych.wyswietlKomunikat("\nDostępne grupy:");
        for (Grupa grupa : grupy) {
            wyswietlanieDanych.wyswietlKomunikat(String.format(
                    "ID: %d | Grupa %d | %s-%s | Dzień: %d | Prowadzący: %s | Miejsca: %d/%d",
                    grupa.getIdGrupy(),
                    grupa.getNrGrupy(),
                    grupa.getGodzRozpoczecia(),
                    grupa.getGodzZakonczenia(),
                    grupa.getDzienTyg(),
                    grupa.getProwadzacy().getImieNazwisko(),
                    grupa.getZajeteMiejsca(),
                    grupa.getLimitMiejsc()
            ));
        }
        wyswietlanieDanych.wyswietlKomunikat("\nWybierz ID grupy (0 aby wrócić): ");
    }

    public void wyswietlDostepnePrzedmioty(List<Przedmiot> przedmioty) {
        if (przedmioty == null || przedmioty.isEmpty()) {
            wyswietlanieDanych.wyswietlKomunikat("Brak dostępnych przedmiotów.");
            return;
        }

        wyswietlanieDanych.wyswietlKomunikat("\n=== Dostępne przedmioty ===");
        wyswietlanieDanych.wyswietlKomunikat("0. Powrót do menu");

        for (Przedmiot przedmiot : przedmioty) {
            StringBuilder rodzajeGrupInfo = new StringBuilder();
            for (RodzajGrupy rodzaj : przedmiot.getRodzajeGrup()) {
                if (!rodzajeGrupInfo.isEmpty()) {
                    rodzajeGrupInfo.append(", ");
                }
                rodzajeGrupInfo.append(rodzaj.name());
            }

            wyswietlanieDanych.wyswietlKomunikat(String.format(
                    "%d. %s (%s) - Typy zajęć: %s",
                    przedmiot.getIdPrzedmiotu(),
                    przedmiot.getNazwa(),
                    przedmiot.getKodPrzedmiotu(),
                    rodzajeGrupInfo
            ));
        }

        wyswietlanieDanych.wyswietlKomunikat("\nWybierz numer przedmiotu: ");
    }

    public void wyswietlPlanZajec(Student student) {
        if (student.getGrupy().isEmpty()) {
            wyswietlanieDanych.wyswietlKomunikat("Nie jesteś zapisany na żadne zajęcia.");
            return;
        }

        wyswietlanieDanych.wyswietlKomunikat("\n=== Twój harmonogram zajęć ===");

        // Grupowanie zajęć według dni tygodnia
        Map<Integer, List<Grupa>> zajeciaPoDniach = student.getGrupy().stream()
                .collect(Collectors.groupingBy(Grupa::getDzienTyg));

        // Wyświetlanie zajęć dla każdego dnia
        for (int dzien = 1; dzien <= 5; dzien++) {
            List<Grupa> zajeciaDnia = zajeciaPoDniach.getOrDefault(dzien, new ArrayList<>());

            if (!zajeciaDnia.isEmpty()) {
                wyswietlanieDanych.wyswietlKomunikat("\n" + getDzienTygodnia(dzien) + ":");

                // Sortowanie zajęć według godziny rozpoczęcia
                zajeciaDnia.sort(Comparator.comparing(Grupa::getGodzRozpoczecia));

                for (Grupa grupa : zajeciaDnia) {
                    wyswietlanieDanych.wyswietlKomunikat(String.format(
                            "%s - %s | %s | %s | Prowadzący: %s",
                            grupa.getGodzRozpoczecia().toString(),
                            grupa.getGodzZakonczenia().toString(),
                            grupa.getRodzajGrupy(),
                            znajdzNazwePrzedmiotu(grupa),
                            grupa.getProwadzacy().getImieNazwisko()
                    ));
                }
            }
        }
    }

    private String getDzienTygodnia(int dzien) {
        return switch (dzien) {
            case 1 -> "Poniedziałek";
            case 2 -> "Wtorek";
            case 3 -> "Środa";
            case 4 -> "Czwartek";
            case 5 -> "Piątek";
            default -> "Nieznany dzień";
        };
    }

    private String znajdzNazwePrzedmiotu(Grupa grupa) {
        if (grupa.getPrzedmiot() != null) {
            return grupa.getPrzedmiot().getNazwa();
        }
        return "Nieznany przedmiot";
    }
}
package view;

import java.util.List;
import model.Grupa;
import model.Kierunek;
import model.Przedmiot;

public class WidokPracownika implements WidokUzytkownika {
    private final WyswietlanieDanych wyswietlanieDanych;

    public WidokPracownika(WyswietlanieDanych wyswietlanieDanych) {
        this.wyswietlanieDanych = wyswietlanieDanych;
    }

    @Override
    public void wyswietlMenu() {
        wyswietlanieDanych.wyswietlKomunikat("\nMenu pracownika:");
        wyswietlanieDanych.wyswietlKomunikat("1. Usuń grupę zajęciową");
        wyswietlanieDanych.wyswietlKomunikat("2. Wyświetl wszystkie grupy zajęciowe"); // New option
        wyswietlanieDanych.wyswietlKomunikat("0. Wyloguj");
        wyswietlanieDanych.wyswietlKomunikat("\nWybierz opcję: ");
    }

    public void wyswietlWszystkieGrupy(List<Kierunek> kierunki) {
        wyswietlanieDanych.wyswietlKomunikat("\nWszystkie grupy zajęciowe:");
        boolean znalezionoGrupy = false;

        for (Kierunek kierunek : kierunki) {
            wyswietlanieDanych.wyswietlKomunikat("\nKierunek: " + kierunek.getNazwa());
            for (Przedmiot przedmiot : kierunek.getPrzedmioty()) {
                List<Grupa> grupy = przedmiot.getGrupy();
                if (!grupy.isEmpty()) {
                    znalezionoGrupy = true;
                    wyswietlanieDanych.wyswietlKomunikat("\nPrzedmiot: " + przedmiot.getNazwa());
                    wyswietlanieDanych.wyswietlGrupy(grupy);
                }
            }
        }

        if (!znalezionoGrupy) {
            wyswietlanieDanych.wyswietlKomunikat("Brak dostępnych grup zajęciowych.");
        }
    }

    public void wyswietlGrupyZajeciowe(List<Kierunek> kierunki) {
        wyswietlanieDanych.wyswietlKomunikat("\nDostępne grupy zajęciowe:");
        boolean znalezionoGrupy = false;

        for (Kierunek kierunek : kierunki) {
            for (Przedmiot przedmiot : kierunek.getPrzedmioty()) {
                List<Grupa> grupy = przedmiot.getGrupy();
                if (!grupy.isEmpty()) {
                    znalezionoGrupy = true;
                    wyswietlanieDanych.wyswietlKomunikat("\nPrzedmiot: " + przedmiot.getNazwa());
                    wyswietlanieDanych.wyswietlGrupy(grupy);
                }
            }
        }

        if (!znalezionoGrupy) {
            wyswietlanieDanych.wyswietlKomunikat("Brak dostępnych grup zajęciowych.");
            return;
        }

        wyswietlanieDanych.wyswietlKomunikat("\nPodaj ID grupy do usunięcia (0 aby wrócić): ");
    }

    public void wyswietlGrupyDoPrezeniesienia(List<Grupa> dostepneGrupy) {
        if (dostepneGrupy.isEmpty()) {
            wyswietlanieDanych.wyswietlKomunikat("Brak dostępnych grup do przeniesienia studentów!");
            return;
        }

        wyswietlanieDanych.wyswietlKomunikat("\nDostępne grupy do przeniesienia studentów:");
        wyswietlanieDanych.wyswietlGrupy(dostepneGrupy);
        wyswietlanieDanych.wyswietlKomunikat("Wybierz grupę docelową dla przeniesienia studentów (0 aby anulować): ");
    }

    public void wyswietlRaportPrzeniesienia(String raport) {
        wyswietlanieDanych.wyswietlKomunikat(raport);
    }
}
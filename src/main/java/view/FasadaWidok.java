package main.java.view;

import java.time.format.DateTimeFormatter;
import java.util.List;

import main.java.model.Grupa;
import main.java.model.Kierunek;
import main.java.model.Przedmiot;
import main.java.model.TuraZapisow;

public class FasadaWidok implements WyswietlanieDanych {

    @Override
    public void wyswietlTuryZapisow(List<TuraZapisow> tury) {
        System.out.println("\nDostępne tury zapisów:");
        System.out.println("------------------------------------------");
        for (TuraZapisow tura : tury) {
            System.out.printf("ID tury: %d%n", tura.getIdTury());
            System.out.printf("Kierunek: %s%n", tura.getKierunek().getNazwa());
            System.out.printf("Wymagana średnia: %.2f%n", tura.getWymaganaSrednia());
            System.out.printf("Rozpoczęcie: %s%n", tura.getGodzRozpoczecia().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            System.out.printf("Zakończenie: %s%n", tura.getGodzZakonczenia().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            System.out.println("------------------------------------------");
        }
    }

    @Override
    public void wyswietlKierunki(List<Kierunek> kierunki) {
        System.out.println("\nDostępne kierunki:");
        System.out.println("------------------------------------------");
        for (Kierunek kierunek : kierunki) {
            System.out.printf("Kod: %s%n", kierunek.getKodKierunku());
            System.out.printf("Nazwa: %s%n", kierunek.getNazwa());
            System.out.println("------------------------------------------");
        }
    }

    public void wyswietlGrupy(List<Grupa> grupy) {
        for (Grupa grupa : grupy) {
            StringBuilder sb = new StringBuilder();
            sb.append("ID: ").append(grupa.getIdGrupy()).append(" | ");
            sb.append(grupa.getRodzajGrupy()).append(" | ");
            sb.append("Prowadzący: ").append(grupa.getProwadzacy().getStopienNaukowy())
                    .append(" ").append(grupa.getProwadzacy().getImie())
                    .append(" ").append(grupa.getProwadzacy().getNazwisko()).append(" | ");
            sb.append("Termin: ").append(zamienDzienTygodniaNaTekst(grupa.getDzienTyg()))
                    .append(" ").append(grupa.getGodzRozpoczecia().toString())
                    .append("-").append(grupa.getGodzZakonczenia().toString()).append(" | ");
            sb.append("Miejsca: ").append(grupa.getZajeteMiejsca())
                    .append("/").append(grupa.getLimitMiejsc());
            wyswietlKomunikat(sb.toString());
        }
    }

    private String zamienDzienTygodniaNaTekst(int dzien) {
        switch (dzien) {
            case 1: return "Poniedziałek";
            case 2: return "Wtorek";
            case 3: return "Środa";
            case 4: return "Czwartek";
            case 5: return "Piątek";
            default: return "Nieznany";
        }
    }

    @Override
    public void wyswietlPrzedmioty(List<Przedmiot> przedmioty) {
        System.out.println("\nPrzedmioty:");
        System.out.println("------------------------------------------");
        for (Przedmiot przedmiot : przedmioty) {
            System.out.printf("Kod: %s%n", przedmiot.getKodPrzedmiotu());
            System.out.printf("Nazwa: %s%n", przedmiot.getNazwa());
            System.out.println("------------------------------------------");
        }
    }

    @Override
    public void wyswietlKomunikat(String tresc) {
        System.out.println(tresc);
    }

    private String getDzienTygodnia(int dzien) {
        switch (dzien) {
            case 1: return "Poniedziałek";
            case 2: return "Wtorek";
            case 3: return "Środa";
            case 4: return "Czwartek";
            case 5: return "Piątek";
            default: return "Nieznany dzień";
        }
    }
}
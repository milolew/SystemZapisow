package presenter;

// Stan dla wyboru grupy docelowej przy przenoszeniu studentów
public class StanWyboruGrupyDocelowej extends StanPracownika {
    private final int idGrupyZrodlowej;

    public StanWyboruGrupyDocelowej(PrezenterSterowanie prezenter, int idGrupyZrodlowej) {
        super(prezenter);
        this.idGrupyZrodlowej = idGrupyZrodlowej;
    }

    @Override
    public void obslugaWyboru(int idGrupyDocelowej) {
        if (idGrupyDocelowej == 0) {
            prezenter.ustawStan(new StanMenuPracownika(prezenter));
            getWidok().wyswietlMenu();
            return;
        }

        try {
            boolean wszystkoPrzeniesione = prezenter.przeniesStudentow(idGrupyZrodlowej, idGrupyDocelowej);

            if (wszystkoPrzeniesione) {
                prezenter.usunGrupe(idGrupyZrodlowej);
                prezenter.wyswietlKomunikat("Grupa źródłowa została usunięta.");
            }
        } catch (Exception e) {
            prezenter.wyswietlKomunikat("Wystąpił błąd: " + e.getMessage());
        }

        prezenter.ustawStan(new StanMenuPracownika(prezenter));
        getWidok().wyswietlMenu();
    }
}

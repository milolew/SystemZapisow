package main.java.presenter;

import main.java.view.WidokPracownika;

public abstract class StanPracownika implements StanPrezentera {
    protected final PrezenterSterowanie prezenter;

    public StanPracownika(PrezenterSterowanie prezenter) {
        this.prezenter = prezenter;
    }

    protected WidokPracownika getWidok() {
        return (WidokPracownika) prezenter.getAktualnyWidok();
    }
}

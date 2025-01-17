package presenter;

import view.WidokStudenta;

public abstract class StanStudenta implements StanPrezentera {
    protected final PrezenterSterowanie prezenter;

    public StanStudenta(PrezenterSterowanie prezenter) {
        this.prezenter = prezenter;
    }

    protected WidokStudenta getWidok() {
        return (WidokStudenta) prezenter.getAktualnyWidok();
    }
}

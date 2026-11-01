package pl.iceclient.core;

/** Wspólny zegar animacji: ekran ładowania i menu główne rysują kostkę dla tego samego czasu. */
public final class IceClock {
    private static volatile long epoch = System.currentTimeMillis();

    private IceClock() {}

    public static void reset() {
        epoch = System.currentTimeMillis();
    }

    public static long sinceStart() {
        return System.currentTimeMillis() - epoch;
    }

    /** Czas animacji obrotów kostki (ujemny, dopóki kostka się składa). */
    public static long cubeTime() {
        return sinceStart() - RubikCube.LEAD_MS;
    }
}

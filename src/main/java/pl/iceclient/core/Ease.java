package pl.iceclient.core;

/** Funkcje łagodzenia animacji (czysta matematyka, bez zależności od Minecrafta). */
public final class Ease {
    private Ease() {}

    public static double clamp01(double t) {
        return t < 0 ? 0 : (t > 1 ? 1 : t);
    }

    public static double outCubic(double t) {
        double u = 1 - clamp01(t);
        return 1 - u * u * u;
    }

    public static double inOutCubic(double t) {
        t = clamp01(t);
        return t < 0.5 ? 4 * t * t * t : 1 - Math.pow(-2 * t + 2, 3) / 2;
    }

    /** Łagodzenie z lekkim przeregulowaniem (efekt „wyskoku"). */
    public static double outBack(double t) {
        double c1 = 1.70158, c3 = c1 + 1, u = clamp01(t) - 1;
        return 1 + c3 * u * u * u + c1 * u * u;
    }
}

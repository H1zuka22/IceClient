package pl.iceclient.core;

/** Położenie i rozmiar kostki na ekranie ładowania i w menu głównym (współdzielone, żeby przejście było ciągłe). */
public final class CubeLayout {
    public record Layout(float cx, float cy, float scale) {}

    private CubeLayout() {}

    public static Layout loading(int w, int h) {
        return new Layout(w / 2f, h * 0.42f, Math.max(16f, Math.min(w, h) * 0.11f));
    }

    public static boolean titleVisible(int w) {
        return w >= 360;
    }

    public static Layout title(int w, int h) {
        return new Layout(w * 0.72f, h * 0.46f, Math.max(14f, Math.min(w, h) * 0.1f));
    }

    public static Layout lerp(Layout a, Layout b, double t) {
        float k = (float) Ease.clamp01(t);
        return new Layout(a.cx() + (b.cx() - a.cx()) * k, a.cy() + (b.cy() - a.cy()) * k, a.scale() + (b.scale() - a.scale()) * k);
    }
}

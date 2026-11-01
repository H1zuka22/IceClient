package pl.iceclient.core;

/** Bezstanowe płatki śniegu: pozycje zależą tylko od czasu, więc animacja jest deterministyczna. */
public final class SnowField {
    private SnowField() {}

    private static double frac(double v) {
        return v - Math.floor(v);
    }

    public static void fill(int count, int w, int h, long timeMs, float[] xs, float[] ys, int[] sizes) {
        for (int i = 0; i < count; i++) {
            double s1 = frac(Math.sin(i * 12.9898) * 43758.5453);
            double s2 = frac(Math.sin((i + 1) * 78.233) * 12345.6789);
            double speed = 14 + s2 * 30;
            double x = (s1 * w + Math.sin(timeMs * 0.0009 + i) * 14) % w;
            if (x < 0) x += w;
            double y = (s2 * h + timeMs * 0.001 * speed) % h;
            xs[i] = (float) x;
            ys[i] = (float) y;
            sizes[i] = (i % 3 == 0) ? 2 : 1;
        }
    }
}

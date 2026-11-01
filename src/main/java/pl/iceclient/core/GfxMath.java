package pl.iceclient.core;

/** Matematyka kolorów i zaokrągleń używana przez warstwę rysowania. */
public final class GfxMath {
    private GfxMath() {}

    /** O ile pikseli przesunąć wiersz (0 = najbardziej zewnętrzny) zaokrąglonego rogu o promieniu r. */
    public static int cornerInset(int r, int row) {
        if (r <= 0) return 0;
        double dy = r - row - 0.5;
        return (int) Math.round(r - Math.sqrt(Math.max(0, (double) r * r - dy * dy)));
    }

    /** Mnoży kanał alfa koloru ARGB przez a (0..1). */
    public static int mulAlpha(int argb, float a) {
        float k = a < 0 ? 0 : (a > 1 ? 1 : a);
        int al = Math.round(((argb >>> 24) & 0xFF) * k);
        return (al << 24) | (argb & 0xFFFFFF);
    }

    public static int lerpColor(int a, int b, float t) {
        float k = t < 0 ? 0 : (t > 1 ? 1 : t);
        int out = 0;
        for (int shift = 24; shift >= 0; shift -= 8) {
            int ca = (a >>> shift) & 0xFF, cb = (b >>> shift) & 0xFF;
            out |= (Math.round(ca + (cb - ca) * k) & 0xFF) << shift;
        }
        return out;
    }
}

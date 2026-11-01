package pl.iceclient.client;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import pl.iceclient.core.GfxMath;

public final class Gfx {
    private Gfx() {}

    /** Wypełniony prostokąt z zaokrąglonymi rogami (rysowany wierszami). */
    public static void rrect(DrawContext c, int x, int y, int w, int h, int r, int color) {
        r = Math.max(0, Math.min(r, Math.min(w, h) / 2));
        for (int i = 0; i < r; i++) {
            int in = GfxMath.cornerInset(r, i);
            c.fill(x + in, y + i, x + w - in, y + i + 1, color);
            c.fill(x + in, y + h - 1 - i, x + w - in, y + h - i, color);
        }
        c.fill(x, y + r, x + w, y + h - r, color);
    }

    /** Miękka poświata: wypełnione koło rysowane liniami co 3 px (kolor z przezroczystością). */
    public static void glow(DrawContext c, float cx, float cy, float r, int color) {
        int step = 3, ir = (int) r;
        for (int dy = -ir; dy < ir; dy += step) {
            double mid = dy + step / 2.0;
            double hw = Math.sqrt(Math.max(0, (double) r * r - mid * mid));
            c.fill((int) (cx - hw), (int) (cy + dy), (int) (cx + hw), (int) (cy + dy + step), color);
        }
    }

    /** Minecraft rysuje tekst z alfą poniżej 4/255 jako w pełni widoczny, więc taki tekst pomijamy. */
    private static boolean invisible(int color) {
        return (color >>> 24) < 8;
    }

    public static void text(DrawContext c, TextRenderer tr, String s, int x, int y, int color) {
        if (!invisible(color)) c.drawTextWithShadow(tr, s, x, y, color);
    }

    public static void text(DrawContext c, TextRenderer tr, Text s, int x, int y, int color) {
        if (!invisible(color)) c.drawTextWithShadow(tr, s, x, y, color);
    }

    public static void centered(DrawContext c, TextRenderer tr, Text s, int cx, int y, int color) {
        if (!invisible(color)) c.drawCenteredTextWithShadow(tr, s, cx, y, color);
    }
}

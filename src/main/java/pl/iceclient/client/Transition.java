package pl.iceclient.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import pl.iceclient.core.Ease;
import pl.iceclient.core.GfxMath;
import pl.iceclient.core.IceConfig;

/** Płynne rozjaśnienie przy otwieraniu nowego ekranu. */
public final class Transition {
    private static final int DURATION_MS = 260;
    private static Screen last;
    private static long start;

    private Transition() {}

    public static void onScreenInit(Screen screen) {
        if (screen != last) {
            last = screen;
            start = System.currentTimeMillis();
        }
    }

    public static void draw(DrawContext ctx, int w, int h) {
        if (!IceConfig.get().transitions) return;
        double a = 1 - Ease.outCubic((System.currentTimeMillis() - start) / (double) DURATION_MS);
        if (a > 0.003) ctx.fill(0, 0, w, h, GfxMath.mulAlpha(0xFF050B14, (float) a));
    }
}

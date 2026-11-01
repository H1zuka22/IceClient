package pl.iceclient.client;

import net.minecraft.client.gui.DrawContext;
import pl.iceclient.core.GfxMath;
import pl.iceclient.core.IceConfig;
import pl.iceclient.core.SnowField;

/** Tło IceClient: gradient nocnego nieba, pasma zorzy i padający śnieg. */
public final class IceBackground {
    private static final int FLAKES = 80;
    private static final float[] XS = new float[FLAKES], YS = new float[FLAKES];
    private static final int[] SZ = new int[FLAKES];

    private IceBackground() {}

    public static void draw(DrawContext c, int w, int h, float alpha) {
        IceConfig cfg = IceConfig.get();
        long t = cfg.animations ? System.currentTimeMillis() : 0L;
        c.fillGradient(0, 0, w, h, GfxMath.mulAlpha(0xFF050D18, alpha), GfxMath.mulAlpha(0xFF123556, alpha));
        int acc = Theme.accent() & 0xFFFFFF;
        for (int i = 0; i < 3; i++) {
            float cy = h * (0.22f + 0.17f * i) + (float) Math.sin(t * 0.00035 + i * 2.1) * h * 0.05f;
            int bh = (int) (h * 0.16f), top = (int) (cy - bh), mid = (int) cy, bot = (int) (cy + bh);
            int strong = GfxMath.mulAlpha((0x30 << 24) | acc, alpha), none = acc;
            c.fillGradient(0, top, w, mid, none, strong);
            c.fillGradient(0, mid, w, bot, strong, none);
        }
        if (cfg.snow) {
            SnowField.fill(FLAKES, w, h, t, XS, YS, SZ);
            int flake = GfxMath.mulAlpha(0xB0FFFFFF, alpha);
            for (int i = 0; i < FLAKES; i++) {
                int x = (int) XS[i], y = (int) YS[i];
                c.fill(x, y, x + SZ[i], y + SZ[i], flake);
            }
        }
    }
}

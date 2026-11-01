package pl.iceclient.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import pl.iceclient.core.CubeLayout;
import pl.iceclient.core.Ease;
import pl.iceclient.core.GfxMath;
import pl.iceclient.core.IntroTimeline;

/**
 * Intro IceClient pokazywane podczas ładowania Minecrafta (własna realizacja):
 * kostka Rubika wyskakuje z poświatą, miesza się i układa, litery logo wjeżdżają kolejno,
 * a po napisie przesuwa się błysk. Czas t liczony od początku nakładki.
 */
public final class LoadingRenderer {
    private LoadingRenderer() {}

    /** handoff 0..1: po zakończeniu ładowania kostka płynnie przesuwa się na miejsce z menu głównego. */
    public static void render(DrawContext ctx, int w, int h, float alpha, long t, boolean done, double handoff) {
        MinecraftClient mc = MinecraftClient.getInstance();
        TextRenderer tr = mc.textRenderer;
        MatrixStack m = ctx.getMatrices();
        int accent = Theme.accent();
        IceBackground.draw(ctx, w, h, alpha);

        CubeLayout.Layout lay = CubeLayout.loading(w, h);
        if (CubeLayout.titleVisible(w)) lay = CubeLayout.lerp(lay, CubeLayout.title(w, h), Ease.inOutCubic(handoff));
        float scale = lay.scale(), cx = lay.cx(), cy = lay.cy();
        double assemble = IntroTimeline.assemble(t);

        // poświata za kostką rośnie razem ze składaniem
        int gr = (int) (scale * 3.2f * assemble);
        int glowA = (int) (10 + 6 * IntroTimeline.glow(t));
        for (int i = 0; i < 6; i++) {
            int rr = gr - i * (gr / 8);
            if (rr < 4) break;
            Gfx.rrect(ctx, (int) cx - rr, (int) cy - rr, rr * 2, rr * 2, rr, GfxMath.mulAlpha((glowA << 24) | (accent & 0xFFFFFF), alpha));
        }
        CubeRenderer.draw(ctx, cx, cy, scale, alpha, IntroTimeline.cubeTime(t), assemble);

        // napis: litery wjeżdżają kolejno, potem przechodzi błysk
        String s = IntroTimeline.TITLE;
        float ts = Math.min(4f, Math.max(1.5f, scale * 0.1f));
        int n = s.length();
        float[] wd = new float[n];
        float total = 0;
        for (int i = 0; i < n; i++) {
            wd[i] = (tr.getWidth(String.valueOf(s.charAt(i))) + 1) * ts;
            total += wd[i];
        }
        float x0 = w / 2f - total / 2f, ty = CubeLayout.loading(w, h).cy() + CubeLayout.loading(w, h).scale() * 3.5f;
        for (int pass = 0; pass < 2; pass++) {
            if (pass == 1) {
                if (!IntroTimeline.sweepActive(t)) break;
                float band = ts * 10f;
                float sx = x0 - band + (total + band * 2) * (float) IntroTimeline.sweep(t);
                ctx.enableScissor((int) sx, (int) ty - 4, (int) (sx + band), (int) (ty + 8 * ts + 4));
            }
            float x = x0;
            for (int i = 0; i < n; i++) {
                float la = (float) IntroTimeline.letter(i, t);
                if (la > 0.02f) {
                    int base = pass == 0 ? (i >= 4 ? accent : 0xFFE8F6FF) : 0xFFFFFFFF;
                    m.push();
                    m.translate(x, ty + (1 - la) * 8f, 0f);
                    m.scale(ts, ts, 1f);
                    Gfx.text(ctx, tr, String.valueOf(s.charAt(i)), 0, 0, GfxMath.mulAlpha(base, alpha * la));
                    m.pop();
                }
                x += wd[i];
            }
            if (pass == 1) ctx.disableScissor();
        }
        float sa = (float) Ease.outCubic((t - 1400) / 500.0);
        Gfx.centered(ctx, tr, Text.literal("Zimowy klient Minecraft"), w / 2, (int) (ty + 10 * ts + 8), GfxMath.mulAlpha(0xFF8FB0CC, alpha * sa));

        // pasek: przesuwający się błysk, a po zakończeniu ładowania pełny
        int bw = Math.max(80, (int) (w * 0.22f)), bx = w / 2 - bw / 2, by = (int) (ty + 10 * ts + 28);
        Gfx.rrect(ctx, bx, by, bw, 4, 2, GfxMath.mulAlpha(0xFF16324D, alpha));
        if (done) {
            Gfx.rrect(ctx, bx, by, bw, 4, 2, GfxMath.mulAlpha(accent, alpha));
        } else {
            int seg = bw / 3, sx = bx + (int) ((bw - seg) * (0.5 - 0.5 * Math.cos((t % 1500L) / 1500.0 * Math.PI * 2)));
            Gfx.rrect(ctx, sx, by, seg, 4, 2, GfxMath.mulAlpha(accent, alpha));
        }
    }
}

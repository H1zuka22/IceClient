package pl.iceclient.client;

import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import pl.iceclient.core.GfxMath;
import pl.iceclient.core.IceConfig;
import pl.iceclient.screen.IceMenuButton;

/** Własny wygląd przycisków: ciemny panel, poświata akcentu i płynna animacja najechania. */
public final class IceButtonRenderer {
    private static final class State {
        float t;
        long last = System.nanoTime();
    }

    private static final Map<ClickableWidget, State> STATES = new WeakHashMap<>();

    private IceButtonRenderer() {}

    public static void render(ClickableWidget w, DrawContext ctx) {
        State st = STATES.computeIfAbsent(w, k -> new State());
        long now = System.nanoTime();
        float dt = Math.min(0.1f, (now - st.last) / 1e9f);
        st.last = now;
        float target = (w.active && (w.isHovered() || w.isFocused())) ? 1f : 0f;
        st.t = IceConfig.get().animations ? st.t + (target - st.t) * Math.min(1f, dt * 14f) : target;

        float al = (w instanceof IceMenuButton b) ? b.iceAlpha : 1f;
        boolean left = (w instanceof IceMenuButton b2) && b2.left;
        int x = w.getX(), y = w.getY(), bw = w.getWidth(), bh = w.getHeight();
        int accent = Theme.accent();

        if (st.t > 0.02f) {
            int glow = GfxMath.mulAlpha((accent & 0xFFFFFF) | (0x38 << 24), al * st.t);
            Gfx.rrect(ctx, x - 2, y - 2, bw + 4, bh + 4, 5, glow);
        }
        int border = GfxMath.mulAlpha(GfxMath.lerpColor(0xFF1E3D5A, accent, st.t), al);
        int panel = GfxMath.mulAlpha(GfxMath.lerpColor(w.active ? 0xFF0C1D31 : 0xFF0A1727, 0xFF123A5C, st.t), al);
        Gfx.rrect(ctx, x, y, bw, bh, 4, border);
        Gfx.rrect(ctx, x + 1, y + 1, bw - 2, bh - 2, 3, panel);
        if (left) ctx.fill(x + 3, y + 5, x + 5, y + bh - 5, GfxMath.mulAlpha(accent, al * (0.35f + 0.65f * st.t)));

        MinecraftClient mc = MinecraftClient.getInstance();
        Text label = w.getMessage();
        int color = GfxMath.mulAlpha(w.active ? GfxMath.lerpColor(0xFFBFD4E6, 0xFFFFFFFF, st.t) : 0xFF5E7488, al);
        int ty = y + (bh - 8) / 2;
        if (left) {
            Gfx.text(ctx, mc.textRenderer, label, x + 12 + Math.round(4 * st.t), ty, color);
        } else {
            Gfx.centered(ctx, mc.textRenderer, label, x + bw / 2, ty, color);
        }
    }
}

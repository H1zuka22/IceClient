package pl.iceclient.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import pl.iceclient.client.Theme;
import pl.iceclient.core.IceConfig;

/** Ustawienia IceClient. Każda zmiana działa od razu i jest zapisywana do pliku konfiguracji. */
public class IceSettingsScreen extends Screen {
    private final Screen parent;

    public IceSettingsScreen(Screen parent) {
        super(Text.literal("Ustawienia IceClient"));
        this.parent = parent;
    }

    private static String onOff(boolean v) {
        return v ? "włączone" : "wyłączone";
    }

    private void toggle(int x, int y, int w, java.util.function.Supplier<String> label, Runnable change) {
        addDrawableChild(new IceMenuButton(x, y, w, 24, Text.literal(label.get()), false, b -> {
            change.run();
            IceConfig.get().saveCurrent();
            b.setMessage(Text.literal(label.get()));
        }));
    }

    @Override
    protected void init() {
        IceConfig cfg = IceConfig.get();
        int w = 240, x = width / 2 - w / 2, y = height / 2 - 60;
        toggle(x, y, w, () -> "Kolor akcentu: " + Theme.accentLabel(cfg.accent), () -> cfg.accent = cfg.nextAccent());
        toggle(x, y + 30, w, () -> "Animacje: " + onOff(cfg.animations), () -> cfg.animations = !cfg.animations);
        toggle(x, y + 60, w, () -> "Śnieg w tle: " + onOff(cfg.snow), () -> cfg.snow = !cfg.snow);
        toggle(x, y + 90, w, () -> "Płynne przejścia: " + onOff(cfg.transitions), () -> cfg.transitions = !cfg.transitions);
        addDrawableChild(new IceMenuButton(x, y + 130, w, 24, Text.literal("Gotowe"), false, b -> close()));
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        MatrixStack m = ctx.getMatrices();
        m.push();
        m.translate(width / 2f, height / 2f - 96, 0f);
        m.scale(2f, 2f, 1f);
        ctx.drawCenteredTextWithShadow(textRenderer, Text.literal("USTAWIENIA"), 0, 0, Theme.accent());
        m.pop();
    }

    @Override
    public void close() {
        IceConfig.get().saveCurrent();
        client.setScreen(parent);
    }
}

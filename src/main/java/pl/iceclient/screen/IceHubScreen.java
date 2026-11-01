package pl.iceclient.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import pl.iceclient.client.Theme;

/** Centrum funkcji IceClient. Funkcje, których jeszcze nie ma, są wyszarzone i opisane wprost. */
public class IceHubScreen extends Screen {
    private final Screen parent;

    public IceHubScreen(Screen parent) {
        super(Text.literal("Menu IceClient"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int w = 220, x = width / 2 - w / 2, y = height / 2 - 66;
        addDrawableChild(new IceMenuButton(x, y, w, 24, Text.literal("Ustawienia IceClient"), false,
                b -> client.setScreen(new IceSettingsScreen(this))));
        addDrawableChild(new IceMenuButton(x, y + 30, w, 24, Text.literal("Opcje Minecrafta"), false,
                b -> client.setScreen(new OptionsScreen(this, client.options))));
        String[] soon = {"HUD (wkrótce)", "Mody (wkrótce)", "Kosmetyki (wkrótce)"};
        for (int i = 0; i < soon.length; i++) {
            IceMenuButton b = new IceMenuButton(x, y + 60 + 30 * i, w, 24, Text.literal(soon[i]), false, bb -> {});
            b.active = false;
            addDrawableChild(b);
        }
        addDrawableChild(new IceMenuButton(x, y + 160, w, 24, Text.literal("Wróć"), false, b -> close()));
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        MatrixStack m = ctx.getMatrices();
        m.push();
        m.translate(width / 2f, height / 2f - 100, 0f);
        m.scale(2f, 2f, 1f);
        ctx.drawCenteredTextWithShadow(textRenderer, Text.literal("ICE CLIENT"), 0, 0, Theme.accent());
        m.pop();
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }
}

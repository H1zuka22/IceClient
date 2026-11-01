package pl.iceclient.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.MessageScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import pl.iceclient.client.Gfx;
import pl.iceclient.client.Theme;

/** Ekran pauzy IceClient. Odpowiednik GameMenuScreen z własnym wyglądem. */
public class IcePauseScreen extends Screen {
    public IcePauseScreen() {
        super(Text.literal("Pauza"));
    }

    @Override
    protected void init() {
        int w = 200, x = width / 2 - w / 2, y = height / 2 - 52;
        addDrawableChild(new IceMenuButton(x, y, w, 24, Text.literal("Wróć do gry"), false, b -> client.setScreen(null)));
        addDrawableChild(new IceMenuButton(x, y + 30, w, 24, Text.literal("Opcje Minecrafta"), false,
                b -> client.setScreen(new OptionsScreen(this, client.options))));
        addDrawableChild(new IceMenuButton(x, y + 60, w, 24, Text.literal("Menu IceClient"), false,
                b -> client.setScreen(new IceHubScreen(this))));
        addDrawableChild(new IceMenuButton(x, y + 100, w, 24, Text.literal("Zapisz i wyjdź"), false, b -> saveAndQuit()));
    }

    private void saveAndQuit() {
        MinecraftClient c = client;
        boolean local = c.isInSingleplayer();
        if (c.world != null) c.world.disconnect();
        if (local) {
            c.disconnect(new MessageScreen(Text.translatable("menu.savingLevel")));
        } else {
            c.disconnect();
        }
        TitleScreen title = new TitleScreen(); // zostanie podmieniony na IceTitleScreen
        if (local) c.setScreen(title);
        else c.setScreen(new MultiplayerScreen(title));
    }

    @Override
    public void renderBackground(DrawContext ctx, int mouseX, int mouseY, float delta) {
        ctx.fillGradient(0, 0, width, height, 0xC0050D18, 0xD0123556); // przyciemnienie świata za menu
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        int pw = 230, px = width / 2 - pw / 2, py = height / 2 - 92;
        Gfx.rrect(ctx, px, py, pw, 2, 1, Theme.accent());
        MatrixStack m = ctx.getMatrices();
        m.push();
        m.translate(width / 2f, py + 12, 0f);
        m.scale(2f, 2f, 1f);
        ctx.drawCenteredTextWithShadow(textRenderer, Text.literal("PAUZA"), 0, 0, 0xFFFFFFFF);
        m.pop();
    }
}

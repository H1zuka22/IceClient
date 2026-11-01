package pl.iceclient.screen;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import pl.iceclient.client.CubeRenderer;
import pl.iceclient.core.CubeLayout;
import pl.iceclient.core.IceClock;
import pl.iceclient.client.Theme;
import pl.iceclient.core.Ease;
import pl.iceclient.core.GfxMath;
import pl.iceclient.core.IceConfig;

/** Menu główne IceClient: kolumna przycisków po lewej, logo i animowana kostka po prawej. */
public class IceTitleScreen extends Screen {
    private final List<IceMenuButton> buttons = new ArrayList<>();
    private final List<Integer> baseX = new ArrayList<>();
    private long opened;

    public IceTitleScreen() {
        super(Text.literal("IceClient"));
    }

    private void add(int x, int y, int w, String label, net.minecraft.client.gui.widget.ButtonWidget.PressAction action) {
        IceMenuButton b = new IceMenuButton(x, y, w, 24, Text.literal(label), true, action);
        buttons.add(b);
        baseX.add(x);
        addDrawableChild(b);
    }

    @Override
    protected void init() {
        buttons.clear();
        baseX.clear();
        opened = System.currentTimeMillis();
        int w = Math.min(210, Math.max(150, width / 3));
        int x = Math.max(20, width / 14);
        int y = height / 2 - 40;
        add(x, y, w, "Jeden gracz", b -> client.setScreen(new SelectWorldScreen(this)));
        add(x, y + 30, w, "Wiele graczy", b -> client.setScreen(new MultiplayerScreen(this)));
        add(x, y + 60, w, "Menu IceClient", b -> client.setScreen(new IceHubScreen(this)));
        add(x, y + 90, w, "Opcje Minecrafta", b -> client.setScreen(new OptionsScreen(this, client.options)));
        add(x, y + 120, w, "Wyjdź z gry", b -> client.scheduleStop());
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        long now = System.currentTimeMillis();
        boolean anim = IceConfig.get().animations;
        for (int i = 0; i < buttons.size(); i++) {
            double e = anim ? Ease.outCubic((now - opened - 70L * i) / 480.0) : 1.0;
            buttons.get(i).setX(baseX.get(i) - (int) ((1 - e) * 46));
            buttons.get(i).iceAlpha = (float) e;
        }
        super.render(ctx, mouseX, mouseY, delta); // tło (mixin) + przyciski

        int x = baseX.isEmpty() ? 24 : baseX.get(0);
        int ly = Math.max(14, height / 2 - 100);
        MatrixStack m = ctx.getMatrices();
        m.push();
        m.translate(x, ly, 0f);
        m.scale(3f, 3f, 1f);
        ctx.drawTextWithShadow(textRenderer, Text.literal("ICE"), 0, 0, 0xFFFFFFFF);
        ctx.drawTextWithShadow(textRenderer, Text.literal("CLIENT"), textRenderer.getWidth("ICE ") , 0, Theme.accent());
        m.pop();
        ctx.drawTextWithShadow(textRenderer, Text.literal("Zimowy klient Minecraft"), x + 2, ly + 30, 0xFF8FB0CC);

        // kostka po prawej (to samo miejsce i ten sam zegar co na końcu ekranu ładowania)
        if (CubeLayout.titleVisible(width)) {
            CubeLayout.Layout l = CubeLayout.title(width, height);
            CubeRenderer.draw(ctx, l.cx(), l.cy(), l.scale(), 1f, IceClock.cubeTime(), 1.0);
        }
        String user = client.getSession().getUsername();
        ctx.drawTextWithShadow(textRenderer, Text.literal("IceClient 0.1.0 · Minecraft 1.21.4"), 6, height - 12, 0xFF6C88A3);
        ctx.drawTextWithShadow(textRenderer, Text.literal(user), width - textRenderer.getWidth(user) - 6, height - 12, GfxMath.mulAlpha(0xFFBFD4E6, 1f));
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}

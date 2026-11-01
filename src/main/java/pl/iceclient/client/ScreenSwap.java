package pl.iceclient.client;

import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import pl.iceclient.screen.IcePauseScreen;
import pl.iceclient.screen.IceTitleScreen;

/** Podmienia ekrany Minecrafta na ekrany IceClient (dokładne dopasowanie klasy, bez podklas). */
public final class ScreenSwap {
    private ScreenSwap() {}

    public static Screen swap(Screen s) {
        if (s == null) return null;
        Class<?> c = s.getClass();
        if (c == TitleScreen.class) return new IceTitleScreen();
        if (c == GameMenuScreen.class) return new IcePauseScreen();
        return s;
    }
}

package pl.iceclient.screen;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

/** Przycisk menu IceClient; wygląd rysuje IceButtonRenderer (przez mixin w PressableWidget). */
public class IceMenuButton extends ButtonWidget {
    public float iceAlpha = 1f;
    public final boolean left;

    public IceMenuButton(int x, int y, int w, int h, Text msg, boolean left, PressAction onPress) {
        super(x, y, w, h, msg, onPress, DEFAULT_NARRATION_SUPPLIER);
        this.left = left;
    }
}

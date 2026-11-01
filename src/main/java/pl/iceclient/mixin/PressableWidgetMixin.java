package pl.iceclient.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.iceclient.client.IceButtonRenderer;

@Mixin(PressableWidget.class)
public abstract class PressableWidgetMixin {
    /** Wszystkie zwykłe przyciski (ButtonWidget) dostają wygląd IceClient; pola wyboru i suwaki zostają bez zmian. */
    @Inject(method = "renderWidget", at = @At("HEAD"), cancellable = true)
    private void ice$renderButton(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Object self = this;
        if (self instanceof ButtonWidget) {
            IceButtonRenderer.render((ClickableWidget) self, context);
            ci.cancel();
        }
    }
}

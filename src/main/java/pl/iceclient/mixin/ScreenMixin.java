package pl.iceclient.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.iceclient.client.IceBackground;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    /** Poza grą (menu, wybór świata, serwery, opcje) tło ekranu zastępuje zimowe tło IceClient. */
    @Inject(method = "renderBackground", at = @At("HEAD"), cancellable = true)
    private void ice$background(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (MinecraftClient.getInstance().world == null) {
            Screen self = (Screen) (Object) this;
            IceBackground.draw(context, self.width, self.height, 1f);
            ci.cancel();
        }
    }
}

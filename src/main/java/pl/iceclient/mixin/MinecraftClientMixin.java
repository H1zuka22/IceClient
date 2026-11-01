package pl.iceclient.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import pl.iceclient.client.ScreenSwap;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    /** Każde otwarcie ekranu przechodzi przez podmianę na wersję IceClient (jeśli istnieje). */
    @ModifyVariable(method = "setScreen", at = @At("HEAD"), argsOnly = true)
    private Screen ice$swapScreen(Screen screen) {
        return ScreenSwap.swap(screen);
    }
}

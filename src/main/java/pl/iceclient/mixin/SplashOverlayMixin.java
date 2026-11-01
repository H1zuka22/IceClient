package pl.iceclient.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.iceclient.client.LoadingRenderer;
import pl.iceclient.core.IceClock;

@Mixin(SplashOverlay.class)
public abstract class SplashOverlayMixin {
    @Shadow
    private long reloadCompleteTime;

    /**
     * Po narysowaniu ekranu Mojang nakłada pełny ekran IceClient. Oryginał jest w pełni widoczny
     * przez sekundę po zakończeniu ładowania i dopiero potem zanika, więc nakładka robi to samo
     * i nigdy nie odsłania czerwonego ekranu Mojang. Czas pochodzi z IceClock, więc kostka
     * w menu głównym kontynuuje ruch z ekranu ładowania.
     */
    @Inject(method = "render", at = @At("TAIL"))
    private void ice$loading(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        long now = Util.getMeasuringTimeMs();
        boolean done = this.reloadCompleteTime > -1L;
        float alpha = 1f;
        double handoff = 0;
        if (done) {
            float f = (now - this.reloadCompleteTime) / 1000f;
            handoff = MathHelper.clamp(f - 1f, 0f, 1f);
            alpha = 1f - (float) handoff;
        }
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.currentScreen != null || mc.world != null) {
            alpha *= MathHelper.clamp(IceClock.sinceStart() / 350f, 0f, 1f);
        }
        LoadingRenderer.render(context, context.getScaledWindowWidth(), context.getScaledWindowHeight(), alpha, IceClock.sinceStart(), done, handoff);
    }
}

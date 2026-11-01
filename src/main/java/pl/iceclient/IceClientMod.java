package pl.iceclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.iceclient.client.Transition;
import pl.iceclient.core.IceClock;
import pl.iceclient.core.IceConfig;

public class IceClientMod implements ClientModInitializer {
    public static final Logger LOG = LoggerFactory.getLogger("IceClient");

    @Override
    public void onInitializeClient() {
        IceClock.reset();
        IceConfig.init(FabricLoader.getInstance().getConfigDir().resolve("iceclient.properties"));
        // Płynne przejście (zanikanie z czerni) przy otwarciu każdego ekranu.
        ScreenEvents.AFTER_INIT.register((client, screen, w, h) -> {
            Transition.onScreenInit(screen);
            ScreenEvents.afterRender(screen).register((s, ctx, mx, my, dt) -> Transition.draw(ctx, s.width, s.height));
        });
        LOG.info("IceClient zaladowany (konfiguracja: {})", IceConfig.get().accent);
    }
}

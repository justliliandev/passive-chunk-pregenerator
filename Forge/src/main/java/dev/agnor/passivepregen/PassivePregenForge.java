package dev.agnor.passivepregen;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.network.NetworkConstants;

@Mod(Constants.MOD_ID)
public class PassivePregenForge {
    
    public PassivePregenForge() {

        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        MinecraftForge.EVENT_BUS.addListener(this::onServerShutDown);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStart);
        MinecraftForge.EVENT_BUS.addListener(this::onServerTick);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ForgePregenConfig.SERVER_SPEC, "passivepregen.toml");
    }

    private void onServerShutDown(ServerStoppedEvent event) {
        CommonClass.onServerStop();
    }
    private void onServerStart(ServerStartedEvent event) {
        CommonClass.onServerStart(event.getServer());
    }

    private void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END)
            CommonClass.onServerTickPost();
    }
}
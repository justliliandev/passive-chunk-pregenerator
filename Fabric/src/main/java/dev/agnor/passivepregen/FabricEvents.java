package dev.agnor.passivepregen;

import lombok.Getter;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class FabricEvents implements ServerPlayConnectionEvents.Disconnect, ServerPlayConnectionEvents.Join, ServerTickEvents.EndTick {

    @Getter
    private static final FabricEvents INSTANCE = new FabricEvents();

    @Override
    public void onPlayDisconnect(ServerGamePacketListenerImpl handler, MinecraftServer server) {
        CommonClass.onPlayerLogoff(handler.player);
    }

    @Override
    public void onPlayReady(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server) {
        CommonClass.onPlayerLogin(handler.player);
    }

    @Override
    public void onEndTick(MinecraftServer server) {
        CommonClass.onServerTickPost();
    }
}

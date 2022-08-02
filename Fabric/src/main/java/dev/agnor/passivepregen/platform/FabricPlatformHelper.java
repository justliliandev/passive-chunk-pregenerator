package dev.agnor.passivepregen.platform;

import dev.agnor.passivepregen.CommonClass;
import dev.agnor.passivepregen.config.PassiveConfig;
import dev.agnor.passivepregen.platform.services.IPlatformHelper;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.PlayerList;

import java.io.File;
import java.util.concurrent.Executor;

public class FabricPlatformHelper implements IPlatformHelper, ServerLifecycleEvents.ServerStarted, ServerLifecycleEvents.ServerStopped {

    MinecraftServer server;

    @Override
    public MinecraftServer getCurrentServer() {
        return server;
    }

    @Override
    public Executor getChunkGenExecutor(ServerLevel level) {
        return level.getChunkSource().mainThreadProcessor;
    }

    @Override
    public boolean isChunkExecutorWorking(ServerLevel level) {
        return level.getChunkSource().mainThreadProcessor.getPendingTasksCount() > 0;
    }

    @Override
    public File playerDirectoryFromPlayerList(PlayerList list) {
        return list.playerIo.playerDir;
    }

    @Override
    public int getPlayerLoadDistance() {
        return PassiveConfig.player_distance;
    }

    @Override
    public int getSpawnLoadDistance() {
        return PassiveConfig.spawn_distance;
    }

    @Override
    public void onServerStarted(MinecraftServer server) {
        this.server = server;
        CommonClass.onServerStart(server);
    }

    @Override
    public void onServerStopped(MinecraftServer server) {
        this.server = null;
        CommonClass.onServerStop();
    }

}

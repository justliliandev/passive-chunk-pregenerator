package dev.agnor.passivepregen.platform;

import dev.agnor.passivepregen.ForgePregenConfig;
import dev.agnor.passivepregen.platform.services.IPlatformHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.players.PlayerList;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.io.File;
import java.util.concurrent.Executor;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public MinecraftServer getCurrentServer() {
        return ServerLifecycleHooks.getCurrentServer();
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
        return list.playerIo.getPlayerDataFolder();
    }

    @Override
    public int getPlayerLoadDistance() {
        return ForgePregenConfig.player_distance.get();
    }

    @Override
    public int getSpawnLoadDistance() {
        return ForgePregenConfig.spawn_distance.get();
    }
}

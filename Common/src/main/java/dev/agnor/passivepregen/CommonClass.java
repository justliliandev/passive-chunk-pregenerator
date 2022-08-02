package dev.agnor.passivepregen;

import dev.agnor.passivepregen.levelpos.DynamicPlayerLevelPos;
import dev.agnor.passivepregen.levelpos.ILevelPos;
import dev.agnor.passivepregen.levelpos.StaticIdentifiableLevelPos;
import dev.agnor.passivepregen.levelpos.StaticLevelPos;
import dev.agnor.passivepregen.platform.Services;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommonClass {

    @Getter
    private static List<ILevelPos> playerPos = new ArrayList<>();

    @Getter
    private static ILevelPos spawnPoint = null;

    public static void onPlayerLogin(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer))
            return;

        @Nullable
        ILevelPos offlinePlayerPos = null;
        for (ILevelPos levelPos : playerPos) {
            if (levelPos instanceof StaticIdentifiableLevelPos identifiableLevelPos) {
                if (identifiableLevelPos.getUuid().equals(player.getUUID())) {
                    offlinePlayerPos = identifiableLevelPos;
                    break;
                }
            }
        }
        if (offlinePlayerPos != null) {
            playerPos.remove(offlinePlayerPos);
        }
        playerPos.add(new DynamicPlayerLevelPos(serverPlayer));
    }

    public static void onPlayerLogoff(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer))
            return;

        @Nullable
        ILevelPos unloadablePlayerPos = null;
        for (ILevelPos levelPos : playerPos) {
            if (levelPos instanceof DynamicPlayerLevelPos dynamicPlayerLevelPos
                    && dynamicPlayerLevelPos.getPlayer() == serverPlayer) {

                unloadablePlayerPos = dynamicPlayerLevelPos;
                break;
            }
        }
        if (unloadablePlayerPos != null) {
            playerPos.remove(unloadablePlayerPos);
        }
        playerPos.add(new StaticIdentifiableLevelPos(serverPlayer));
    }

    public static void onServerStart(MinecraftServer server) {
        spawnPoint = new StaticLevelPos(Level.OVERWORLD, server.getWorldData().overworldData().getXSpawn(), server.getWorldData().overworldData().getZSpawn());
        if (server instanceof DedicatedServer dedicatedServer) {
            File playerDir = Services.PLATFORM.playerDirectoryFromPlayerList(dedicatedServer.getPlayerList());
            if (playerDir.isDirectory()) {
                for (File playerFile : playerDir.listFiles(file -> file.isFile() && file.getName().endsWith(".dat"))) {
                    try {
                        CompoundTag read = NbtIo.read(playerFile);
                        System.out.println(read);
                    } catch (IOException exception) {
                        Constants.LOG.warn("playerfile couldn't be read", exception);
                    }
                }
            } else {
                Constants.LOG.warn("playerDirectory is not a directory");
            }
        }
    }

    public static void onServerStop() {
        spawnPoint = null;
    }
    private static PassivePregenWorker worker = null;

    public static void onServerTickPost() {
        if (worker == null)
            worker = new PassivePregenWorker();
        worker.doWork();
    }
}
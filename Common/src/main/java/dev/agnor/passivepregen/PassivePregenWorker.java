package dev.agnor.passivepregen;

import dev.agnor.passivepregen.levelpos.ILevelPos;
import dev.agnor.passivepregen.levelpos.StaticLevelPos;
import dev.agnor.passivepregen.platform.Services;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class PassivePregenWorker {

    private Map<ServerLevel, List<ChunkPos>> generated = new HashMap<>();
    public AtomicInteger doneGenerating = new AtomicInteger();

    public void doWork() {
        var levelPositions = new ArrayList<>(CommonClass.getPlayerPos());
        levelPositions.add(CommonClass.getSpawnPoint());
        Collections.shuffle(levelPositions);
        for (ILevelPos levelPos: levelPositions) {
            checkPos(levelPos);
        }
    }

    private void checkPos(ILevelPos levelPos) {
        if (levelPos.isCompleted())
            return;
        ServerLevel level = levelPos.getServerLevel();
        if (Services.PLATFORM.isChunkExecutorWorking(level))
            return;
        for (int dx = 0; dx < levelPos.loadDistance(); dx++) {
            for (int dy = 0; dy < levelPos.loadDistance(); dy++) {
                for (boolean invertX : new boolean[]{true, false}) {
                    for (boolean invertY : new boolean[]{true, false}) {
                        if ((dx == 0 && !invertX) || (dy == 0 && !invertY))
                            continue;
                        ChunkPos pos = move(levelPos.getPos(), invertX ? -dx : dx, invertY ? -dy : dy);
                        if (!generated.computeIfAbsent(level, l -> new ArrayList<>()).contains(pos)) {
                            if (!level.hasChunk(pos.x, pos.z)) {
                                ChunkAccess chunk = level.getChunk(pos.x, pos.z, ChunkStatus.EMPTY, true);
                                if (!chunk.getStatus().isOrAfter(ChunkStatus.FULL)) {
                                    generated.get(level).add(pos);
                                    CompletableFuture.supplyAsync(() -> {
                                        level.getChunkSource().getChunk(pos.x, pos.z, ChunkStatus.FULL, true);
                                        doneGenerating.getAndIncrement();
                                        return null;
                                    }, Services.PLATFORM.getChunkGenExecutor(level));
                                    return;
                                }
                            }
                            generated.get(level).add(pos);
                        }
                    }
                }
            }
        }
        if (levelPos instanceof StaticLevelPos staticLevelPos) {
            staticLevelPos.setCompleted(true);
        }
    }

    private static ChunkPos move(ChunkPos pos, int x, int z) {
        return new ChunkPos(pos.x + x, pos.z + z);
    }
}

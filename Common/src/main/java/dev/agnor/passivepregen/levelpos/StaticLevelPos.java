package dev.agnor.passivepregen.levelpos;

import dev.agnor.passivepregen.platform.Services;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import static dev.agnor.passivepregen.levelpos.ILevelPos.*;

public class StaticLevelPos implements ILevelPos {

    private final ResourceKey<Level> level;

    @Getter
    private final ChunkPos pos;

    @Getter
    @Setter
    private boolean completed = false;

    public StaticLevelPos(ResourceKey<Level> level, ChunkPos pos) {
        this.level = level;
        this.pos = pos;
    }

    public StaticLevelPos(ResourceKey<Level> level, int blockX, int blockZ) {
        this.level = level;
        this.pos = new ChunkPos(chunkPosCoord(blockX), chunkPosCoord(blockZ));
    }

    public ServerLevel getServerLevel() {
        return Services.PLATFORM.getCurrentServer().getLevel(level);
    }

    public int loadDistance() {
        return Services.PLATFORM.getSpawnLoadDistance();
    }

}
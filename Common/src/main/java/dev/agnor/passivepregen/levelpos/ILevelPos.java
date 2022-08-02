package dev.agnor.passivepregen.levelpos;

import dev.agnor.passivepregen.Constants;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

public interface ILevelPos {
    ChunkPos getPos();
    ServerLevel getServerLevel();

    static int chunkPosCoord(Tag tag) {
        if (tag instanceof NumericTag numericTag) {
            return chunkPosCoord(numericTag.getAsDouble());
        }
        Constants.LOG.warn("position tag was not a double value");
        return 0;
    }
    static int chunkPosCoord(double coord) {
        return (int)coord / 16;
    }

    int loadDistance();

    default boolean isCompleted() {
        return false;
    }
}

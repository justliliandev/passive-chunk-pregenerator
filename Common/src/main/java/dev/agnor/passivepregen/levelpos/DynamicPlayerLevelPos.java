package dev.agnor.passivepregen.levelpos;

import lombok.Getter;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import static dev.agnor.passivepregen.levelpos.ILevelPos.*;

@Getter
public class DynamicPlayerLevelPos implements ILevelPos {

    private ServerPlayer player;
    public DynamicPlayerLevelPos(ServerPlayer player) {
        this.player = player;
    }

    @Override
    public ServerLevel getServerLevel() {
        return player.getLevel();
    }

    @Override
    public ChunkPos getPos() {
        return new ChunkPos(chunkPosCoord(player.getX()), chunkPosCoord(player.getZ()));
    }
}

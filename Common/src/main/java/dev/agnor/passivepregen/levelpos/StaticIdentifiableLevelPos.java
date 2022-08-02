package dev.agnor.passivepregen.levelpos;

import com.mojang.serialization.Dynamic;
import dev.agnor.passivepregen.Constants;
import dev.agnor.passivepregen.platform.Services;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import static dev.agnor.passivepregen.levelpos.ILevelPos.*;

import java.util.UUID;

@Getter
public class StaticIdentifiableLevelPos extends StaticLevelPos {
    private final UUID uuid;

    public StaticIdentifiableLevelPos(ServerPlayer player) {
        this(player.getUUID(),
                player.getLevel().dimension(),
                new ChunkPos(
                        chunkPosCoord(player.getX()),
                        chunkPosCoord(player.getZ()))
        );
    }
    public StaticIdentifiableLevelPos(UUID uuid, CompoundTag tag) {
        this(uuid,
                DimensionType.parseLegacy(new Dynamic<>(NbtOps.INSTANCE, tag.get("Dimension")))
                        .resultOrPartial(Constants.LOG::error)
                        .orElse(Level.OVERWORLD),
                new ChunkPos(
                        chunkPosCoord(tag.getList("Pos", Tag.TAG_DOUBLE).get(0)),
                        chunkPosCoord(tag.getList("Pos", Tag.TAG_DOUBLE).get(2)))
        );
    }

    public StaticIdentifiableLevelPos(UUID uuid, ResourceKey<Level> level, ChunkPos pos) {
        super(level, pos);
        this.uuid = uuid;
    }

    public int loadDistance() {
        return Services.PLATFORM.getPlayerLoadDistance();
    }
}

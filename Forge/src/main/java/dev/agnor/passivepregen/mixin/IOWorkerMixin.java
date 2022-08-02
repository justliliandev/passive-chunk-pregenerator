package dev.agnor.passivepregen.mixin;

import dev.agnor.passivepregen.Constants;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.storage.IOWorker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ConcurrentModificationException;

@Mixin(IOWorker.class)
public class IOWorkerMixin {

    @Redirect(method = "storePendingChunk",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/world/level/chunk/storage/IOWorker.runStore(Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/chunk/storage/IOWorker$PendingStore;)V"))
    private void init(IOWorker worker, ChunkPos chunkPos, IOWorker.PendingStore pendingStore) {
        try {
            worker.storage.write(chunkPos, pendingStore.data);
            pendingStore.result.complete(null);
        } catch (ConcurrentModificationException e) {
            Constants.LOG.info("Catched ChunkSaveException likely caused by PassivePregen. Chunk will be saved later");
            pendingStore.result.completeExceptionally(e);
        } catch (Exception var4) {
            worker.LOGGER.error("Failed to store chunk {}", chunkPos, var4);
            pendingStore.result.completeExceptionally(var4);
        }
    }
}
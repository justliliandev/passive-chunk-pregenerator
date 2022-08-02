package dev.agnor.passivepregen.mixin;

import dev.agnor.passivepregen.Constants;
import net.minecraft.world.level.chunk.storage.EntityStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ConcurrentModificationException;
import java.util.function.Function;

@Mixin(EntityStorage.class)
public class EntityStorageMixin {
    
    @ModifyArg(at = @At(
                value = "INVOKE",
                target = "Ljava/util/concurrent/CompletableFuture;exceptionally(Ljava/util/function/Function;)Ljava/util/concurrent/CompletableFuture;"),
            method = "storeEntities")
    private <T> Function<Throwable,T> init(Function<Throwable, ? extends T> original) {

        return throwable -> {
            if (throwable instanceof ConcurrentModificationException) {
                Constants.LOG.info("Catched ChunkSaveException likely caused by PassivePregen. Chunk will be saved later");
                return null;
            }
            return original.apply(throwable);
        };
    }
}
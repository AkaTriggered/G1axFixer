package dev.g1ax.g1axfixer.mixin;

import dev.g1ax.g1axfixer.G1axFixer;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.resource.ResourceReloader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(BakedModelManager.class)
public class ModelLoaderMixin {

    @Inject(method = "reload", at = @At("HEAD"))
    private void g1ax$onModelReload(ResourceReloader.Store store, Executor prepareExecutor, ResourceReloader.Synchronizer synchronizer, Executor applyExecutor, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        try {
            if (G1axFixer.getInstance() != null) {
                G1axFixer.LOGGER.info("[G1axFixer] Model bake/reload cycle detected — protection active.");
            }
        } catch (Exception ignored) {}
    }
}

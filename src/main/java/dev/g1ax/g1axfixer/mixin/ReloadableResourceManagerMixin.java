package dev.g1ax.g1axfixer.mixin;

import dev.g1ax.g1axfixer.G1axFixer;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.ResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mixin(ReloadableResourceManagerImpl.class)
public class ReloadableResourceManagerMixin {

    @Inject(method = "reload", at = @At("HEAD"))
    private void g1ax$onReloadStart(CallbackInfoReturnable<CompletableFuture> cir) {
        G1axFixer.LOGGER.info("[G1axFixer] Resource reload starting.");
    }

    @Inject(method = "reload", at = @At("RETURN"))
    private void g1ax$onReloadEnd(CallbackInfoReturnable<CompletableFuture> cir) {
        G1axFixer.LOGGER.info("[G1axFixer] Resource reload complete.");
        long total = G1axFixer.getInstance().getErrorHandler().getTotalFixed();
        if (total > 0) {
            G1axFixer.LOGGER.info("[G1axFixer] {} issue(s) silently fixed during this reload.", total);
        }
    }
}

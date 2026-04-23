package dev.g1ax.g1axfixer.mixin;

import dev.g1ax.g1axfixer.G1axFixer;
import dev.g1ax.g1axfixer.stats.G1axStatsTracker;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Unique private long g1ax$lastFrameTime = System.nanoTime();
    @Unique private long g1ax$frameCount = 0;
    @Unique private double g1ax$avgFrameTimeMs = 16.0;

    @Inject(method = "render", at = @At("HEAD"))
    private void g1ax$onRenderStart(CallbackInfo ci) {
        try {
            if (G1axFixer.getInstance() == null) return;

            long now = System.nanoTime();
            double frameTimeMs = (now - g1ax$lastFrameTime) / 1_000_000.0;
            g1ax$lastFrameTime = now;

            g1ax$avgFrameTimeMs = g1ax$avgFrameTimeMs * 0.95 + frameTimeMs * 0.05;
            g1ax$frameCount++;

            G1axFixer.getInstance().getResourcePackFixer().chunkRebuildThrottler.onFrameEnd();
            G1axFixer.getInstance().getResourcePackFixer().lightUpdateBatcher.flush();
        } catch (Exception ignored) {}
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void g1ax$onRenderEnd(CallbackInfo ci) {
        try {
            if (G1axFixer.getInstance() == null) return;
            if (g1ax$frameCount % 600 == 0 && G1axFixer.getInstance().getConfig().logDebug) {
                G1axFixer.LOGGER.info("[G1axFixer][RENDER] Avg frame time: {} ms, optimizations: {}",
                    String.format("%.2f", g1ax$avgFrameTimeMs),
                    G1axStatsTracker.get().getTotalOptimizations());
            }
        } catch (Exception ignored) {}
    }
}

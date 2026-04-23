package dev.g1ax.g1axfixer.mixin;

import dev.g1ax.g1axfixer.G1axFixer;
import dev.g1ax.g1axfixer.stats.G1axStatsTracker;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.render.chunk.ChunkRendererRegionBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkBuilder.class)
public class ChunkBuilderMixin {

    @Inject(method = "rebuild", at = @At("HEAD"), cancellable = true)
    private void g1ax$throttleChunkRebuild(ChunkBuilder.BuiltChunk chunk, ChunkRendererRegionBuilder regionBuilder, CallbackInfo ci) {
        try {
            if (G1axFixer.getInstance() == null) return;
            if (!G1axFixer.getInstance().getConfig().optimizeChunkRebuilds) return;

            var throttler = G1axFixer.getInstance().getResourcePackFixer().chunkRebuildThrottler;
            if (throttler.shouldThrottleRebuild(0, 0)) {
                ci.cancel();
            }
        } catch (Exception ignored) {}
    }
}

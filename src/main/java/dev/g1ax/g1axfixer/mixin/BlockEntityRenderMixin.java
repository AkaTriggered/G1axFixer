package dev.g1ax.g1axfixer.mixin;

import dev.g1ax.g1axfixer.G1axFixer;
import dev.g1ax.g1axfixer.stats.G1axStatsTracker;
import net.minecraft.client.render.block.entity.BlockEntityRenderManager;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderManager.class)
public class BlockEntityRenderMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private <S extends BlockEntityRenderState> void g1ax$cullDistantBlockEntities(S state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState camera, CallbackInfo ci) {
        try {
            if (G1axFixer.getInstance() == null) return;
            var config = G1axFixer.getInstance().getConfig();
            if (!config.optimizeBlockEntityRendering) return;
            if (state.pos == null) return;

            var client = net.minecraft.client.MinecraftClient.getInstance();
            if (client.player == null) return;

            var pos = state.pos;
            double distSq = client.player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            long maxDistSq = (long) config.blockEntityRenderDistance * config.blockEntityRenderDistance;

            if (distSq > maxDistSq) {
                G1axStatsTracker.get().entityRenderSkipped.incrementAndGet();
                ci.cancel();
            }
        } catch (Exception ignored) {}
    }
}

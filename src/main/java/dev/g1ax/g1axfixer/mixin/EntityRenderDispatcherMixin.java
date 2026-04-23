package dev.g1ax.g1axfixer.mixin;

import dev.g1ax.g1axfixer.G1axFixer;
import dev.g1ax.g1axfixer.stats.G1axStatsTracker;
import net.minecraft.client.render.entity.EntityRenderManager;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderManager.class)
public class EntityRenderDispatcherMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private <S extends EntityRenderState> void g1ax$skipDistantEntityRender(S state, CameraRenderState camera, double x, double y, double z, MatrixStack matrices, OrderedRenderCommandQueue queue, CallbackInfo ci) {
        try {
            if (G1axFixer.getInstance() == null) return;
            var config = G1axFixer.getInstance().getConfig();
            if (!config.optimizeRendering || !config.optimizeEntityRender) return;

            EntityType<?> type = state.entityType;
            if (type == EntityType.PLAYER) return;

            double distSq = state.squaredDistanceToCamera;

            if (config.skipHiddenEntityRender) {
                if (type == EntityType.ITEM && distSq > 4096) {
                    G1axStatsTracker.get().entityRenderSkipped.incrementAndGet();
                    ci.cancel();
                    return;
                }
                if (type == EntityType.ITEM_FRAME && distSq > 2048) {
                    G1axStatsTracker.get().entityRenderSkipped.incrementAndGet();
                    ci.cancel();
                    return;
                }
                if (type == EntityType.GLOW_ITEM_FRAME && distSq > 2048) {
                    G1axStatsTracker.get().entityRenderSkipped.incrementAndGet();
                    ci.cancel();
                    return;
                }
                if (type == EntityType.ARMOR_STAND && distSq > 8192) {
                    G1axStatsTracker.get().entityRenderSkipped.incrementAndGet();
                    ci.cancel();
                    return;
                }
                if (type == EntityType.EXPERIENCE_ORB && distSq > 1024) {
                    G1axStatsTracker.get().entityRenderSkipped.incrementAndGet();
                    ci.cancel();
                    return;
                }
            }

            if (distSq > config.entityRenderDistanceSq) {
                if (type != EntityType.ENDER_DRAGON && type != EntityType.WITHER) {
                    G1axStatsTracker.get().entityRenderSkipped.incrementAndGet();
                    ci.cancel();
                }
            }
        } catch (Exception ignored) {}
    }
}

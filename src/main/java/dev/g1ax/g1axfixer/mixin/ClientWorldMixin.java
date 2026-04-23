package dev.g1ax.g1axfixer.mixin;

import dev.g1ax.g1axfixer.G1axFixer;
import dev.g1ax.g1axfixer.stats.G1axStatsTracker;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {

    @Inject(method = "tickEntity", at = @At("HEAD"), cancellable = true)
    private void g1ax$optimizeEntityTick(Entity entity, CallbackInfo ci) {
        try {
            if (G1axFixer.getInstance() == null) return;
            var config = G1axFixer.getInstance().getConfig();
            if (!config.optimizeTicking || !config.optimizeEntityTick) return;

            if (entity instanceof PlayerEntity) return;
            if (entity instanceof EnderDragonEntity) return;
            if (entity instanceof WitherEntity) return;
            if (entity.hasCustomName()) return;
            if (entity instanceof ProjectileEntity) return;

            var client = net.minecraft.client.MinecraftClient.getInstance();
            if (client.player == null) return;

            double distSq = client.player.squaredDistanceTo(entity.getX(), entity.getY(), entity.getZ());
            long worldTime = ((ClientWorld) (Object) this).getTime();
            long tickRangeSq = (long) config.entityTickRange * config.entityTickRange;

            if (config.throttleItemEntityTick && entity instanceof ItemEntity) {
                if (worldTime % config.itemEntityTickRate != 0) {
                    G1axStatsTracker.get().entityTicksSkipped.incrementAndGet();
                    ci.cancel();
                    return;
                }
            }

            if (config.throttleArmorStandTick && entity instanceof ArmorStandEntity) {
                if (!entity.hasPassengers() && worldTime % 8 != 0) {
                    G1axStatsTracker.get().entityTicksSkipped.incrementAndGet();
                    ci.cancel();
                    return;
                }
            }

            if (entity instanceof ItemFrameEntity) {
                if (worldTime % 20 != 0) {
                    G1axStatsTracker.get().entityTicksSkipped.incrementAndGet();
                    ci.cancel();
                    return;
                }
            }

            if (distSq > tickRangeSq * 4) {
                if (worldTime % 8 != 0) {
                    G1axStatsTracker.get().entityTicksSkipped.incrementAndGet();
                    ci.cancel();
                    return;
                }
            } else if (distSq > tickRangeSq) {
                if (worldTime % 4 != 0) {
                    G1axStatsTracker.get().entityTicksSkipped.incrementAndGet();
                    ci.cancel();
                    return;
                }
            } else if (distSq > tickRangeSq / 4) {
                if (worldTime % 2 != 0) {
                    G1axStatsTracker.get().entityTicksSkipped.incrementAndGet();
                    ci.cancel();
                }
            }
        } catch (Exception ignored) {}
    }
}

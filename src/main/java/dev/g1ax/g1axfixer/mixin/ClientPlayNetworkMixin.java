package dev.g1ax.g1axfixer.mixin;

import dev.g1ax.g1axfixer.G1axFixer;
import dev.g1ax.g1axfixer.stats.G1axStatsTracker;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkMixin {

    @Unique private long g1ax$lastExpUpdateTime = 0;
    @Unique private long g1ax$lastHealthUpdateTime = 0;
    @Unique private int g1ax$entitySpawnBurst = 0;
    @Unique private long g1ax$lastSpawnBurstReset = System.currentTimeMillis();

    @Inject(method = "onEntitySpawn", at = @At("HEAD"), cancellable = true)
    private void g1ax$throttleEntitySpawnBurst(EntitySpawnS2CPacket packet, CallbackInfo ci) {
        try {
            if (G1axFixer.getInstance() == null) return;
            if (!G1axFixer.getInstance().getConfig().optimizeNetwork) return;

            long now = System.currentTimeMillis();
            if (now - g1ax$lastSpawnBurstReset > 1000) {
                g1ax$entitySpawnBurst = 0;
                g1ax$lastSpawnBurstReset = now;
            }

            g1ax$entitySpawnBurst++;

            if (g1ax$entitySpawnBurst > 50) {
                G1axStatsTracker.get().packetsOptimized.incrementAndGet();
                ci.cancel();
            }
        } catch (Exception ignored) {}
    }

    @Inject(method = "onParticle", at = @At("HEAD"), cancellable = true)
    private void g1ax$throttleParticlePackets(ParticleS2CPacket packet, CallbackInfo ci) {
        try {
            if (G1axFixer.getInstance() == null) return;
            var config = G1axFixer.getInstance().getConfig();
            if (!config.optimizeNetwork || !config.cullDistantParticles) return;

            var client = net.minecraft.client.MinecraftClient.getInstance();
            if (client.player == null) return;

            double distSq = client.player.squaredDistanceTo(packet.getX(), packet.getY(), packet.getZ());
            int cullDist = config.particleCullDistance;

            if (distSq > (long) cullDist * cullDist) {
                G1axStatsTracker.get().packetsOptimized.incrementAndGet();
                G1axStatsTracker.get().particlesCulled.incrementAndGet();
                ci.cancel();
            }
        } catch (Exception ignored) {}
    }

    @Inject(method = "onExplosion", at = @At("HEAD"), cancellable = true)
    private void g1ax$throttleDistantExplosions(ExplosionS2CPacket packet, CallbackInfo ci) {
        try {
            if (G1axFixer.getInstance() == null) return;
            if (!G1axFixer.getInstance().getConfig().optimizeNetwork) return;

            var client = net.minecraft.client.MinecraftClient.getInstance();
            if (client.player == null) return;

            var center = packet.center();
            double distSq = client.player.squaredDistanceTo(center.x, center.y, center.z);

            if (distSq > 65536) {
                G1axStatsTracker.get().packetsOptimized.incrementAndGet();
                ci.cancel();
            }
        } catch (Exception ignored) {}
    }

    @Inject(method = "onWorldEvent", at = @At("HEAD"), cancellable = true)
    private void g1ax$throttleDistantWorldEvents(WorldEventS2CPacket packet, CallbackInfo ci) {
        try {
            if (G1axFixer.getInstance() == null) return;
            if (!G1axFixer.getInstance().getConfig().optimizeNetwork) return;

            var client = net.minecraft.client.MinecraftClient.getInstance();
            if (client.player == null) return;

            var pos = packet.getPos();
            double distSq = client.player.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ());

            if (distSq > 32768) {
                G1axStatsTracker.get().packetsOptimized.incrementAndGet();
                ci.cancel();
            }
        } catch (Exception ignored) {}
    }

    @Inject(method = "onExperienceBarUpdate", at = @At("HEAD"), cancellable = true)
    private void g1ax$deduplicateExpUpdates(ExperienceBarUpdateS2CPacket packet, CallbackInfo ci) {
        try {
            if (G1axFixer.getInstance() == null) return;
            if (!G1axFixer.getInstance().getConfig().deduplicatePackets) return;

            long now = System.currentTimeMillis();
            if (now - g1ax$lastExpUpdateTime < 100) {
                G1axStatsTracker.get().packetsOptimized.incrementAndGet();
                ci.cancel();
                return;
            }
            g1ax$lastExpUpdateTime = now;
        } catch (Exception ignored) {}
    }

    @Inject(method = "onHealthUpdate", at = @At("HEAD"), cancellable = true)
    private void g1ax$deduplicateHealthUpdates(HealthUpdateS2CPacket packet, CallbackInfo ci) {
        try {
            if (G1axFixer.getInstance() == null) return;
            if (!G1axFixer.getInstance().getConfig().deduplicatePackets) return;

            long now = System.currentTimeMillis();
            if (now - g1ax$lastHealthUpdateTime < 50) {
                G1axStatsTracker.get().packetsOptimized.incrementAndGet();
                ci.cancel();
                return;
            }
            g1ax$lastHealthUpdateTime = now;
        } catch (Exception ignored) {}
    }
}

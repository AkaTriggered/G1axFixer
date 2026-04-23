package dev.g1ax.g1axfixer.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.g1ax.g1axfixer.G1axFixer;
import dev.g1ax.g1axfixer.stats.G1axStatsTracker;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {

    @Unique private int g1ax$particlesThisTick = 0;
    @Unique private long g1ax$lastTickReset = 0;

    @Inject(method = "addParticle(Lnet/minecraft/client/particle/Particle;)V", at = @At("HEAD"), cancellable = true)
    private void g1ax$cullDistantParticles(Particle particle, CallbackInfo ci) {
        try {
            if (G1axFixer.getInstance() == null) return;
            var config = G1axFixer.getInstance().getConfig();
            if (!config.optimizeRendering) return;

            long now = System.currentTimeMillis();
            if (now - g1ax$lastTickReset > 50) {
                g1ax$particlesThisTick = 0;
                g1ax$lastTickReset = now;
            }

            if (config.reduceParticleCount && g1ax$particlesThisTick >= config.maxParticlesPerTick) {
                G1axStatsTracker.get().particlesCulled.incrementAndGet();
                ci.cancel();
                return;
            }

            if (!config.cullDistantParticles) {
                g1ax$particlesThisTick++;
                return;
            }

            var client = net.minecraft.client.MinecraftClient.getInstance();
            if (client.player == null) {
                g1ax$particlesThisTick++;
                return;
            }

            double distSq = client.player.squaredDistanceTo(particle.x, particle.y, particle.z);
            int cullDist = config.particleCullDistance;

            boolean memPressure = G1axFixer.getInstance().getResourcePackFixer().memoryOptimizer.isUnderPressure();
            int effectiveCull = memPressure ? cullDist / 2 : cullDist;

            if (distSq > (long) effectiveCull * effectiveCull) {
                G1axStatsTracker.get().particlesCulled.incrementAndGet();
                ci.cancel();
                return;
            }

            g1ax$particlesThisTick++;
        } catch (Exception ignored) {}
    }

    @ModifyReturnValue(method = "getDebugString", at = @At("RETURN"))
    private String g1ax$addDebugInfo(String original) {
        try {
            return original + " [G1axFixer: " + G1axStatsTracker.get().particlesCulled.get() + " culled]";
        } catch (Exception e) {
            return original;
        }
    }
}

package dev.g1ax.g1axfixer.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.g1ax.g1axfixer.G1axFixer;
import dev.g1ax.g1axfixer.error.SafetyWrapper;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {

    @Inject(method = "addParticle(Lnet/minecraft/client/particle/Particle;)V", at = @At("HEAD"), cancellable = true)
    private void g1ax$cullDistantParticles(Particle particle, CallbackInfo ci) {
        SafetyWrapper.wrapSafeVoid(() -> {
            if (!G1axFixer.getInstance().getResourcePackFixer().renderOptimizer.shouldCullParticles()) {
                return;
            }

            var client = net.minecraft.client.MinecraftClient.getInstance();
            if (client.player == null) return;

            double distance = client.player.squaredDistanceTo(particle.x, particle.y, particle.z);
            int cullDist = G1axFixer.getInstance().getResourcePackFixer().renderOptimizer.getParticleCullDistance();
            
            if (distance > cullDist * cullDist) {
                ci.cancel();
            }
        }, "ParticleManager.cullDistantParticles");
    }

    @ModifyReturnValue(method = "getDebugString", at = @At("RETURN"))
    private String g1ax$addDebugInfo(String original) {
        return SafetyWrapper.wrapSafe(() -> original + " [G1axFixer: Optimized]", original, "ParticleManager.getDebugString");
    }
}

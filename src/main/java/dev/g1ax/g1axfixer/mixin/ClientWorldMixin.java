package dev.g1ax.g1axfixer.mixin;

import dev.g1ax.g1axfixer.G1axFixer;
import dev.g1ax.g1axfixer.error.SafetyWrapper;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {

    @Inject(method = "tickEntity", at = @At("HEAD"), cancellable = true)
    private void g1ax$optimizeEntityTick(Entity entity, CallbackInfo ci) {
        SafetyWrapper.wrapSafeVoid(() -> {
            if (!G1axFixer.getInstance().getResourcePackFixer().tickOptimizer.shouldOptimizeEntityTicking()) {
                return;
            }

            var client = net.minecraft.client.MinecraftClient.getInstance();
            if (client.player == null) return;

            double distance = client.player.squaredDistanceTo(entity.getX(), entity.getY(), entity.getZ());
            int tickRange = G1axFixer.getInstance().getResourcePackFixer().tickOptimizer.getEntityTickRange();
            
            if (distance > tickRange * tickRange && entity != client.player) {
                if (client.world.getTime() % 4 != 0) {
                    ci.cancel();
                }
            }
        }, "ClientWorld.tickEntity");
    }
}

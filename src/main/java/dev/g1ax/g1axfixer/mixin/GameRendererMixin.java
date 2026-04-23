package dev.g1ax.g1axfixer.mixin;

import dev.g1ax.g1axfixer.G1axFixer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Unique private long g1ax$gcCheckTimer = System.currentTimeMillis();

    @Inject(method = "render", at = @At("HEAD"))
    private void g1ax$memoryPressureCheck(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        try {
            if (G1axFixer.getInstance() == null) return;
            if (!G1axFixer.getInstance().getConfig().optimizeMemory) return;

            long now = System.currentTimeMillis();
            if (now - g1ax$gcCheckTimer > 5000) {
                g1ax$gcCheckTimer = now;
                G1axFixer.getInstance().getResourcePackFixer().memoryOptimizer.isUnderPressure();
            }
        } catch (Exception ignored) {}
    }
}

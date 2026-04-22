package dev.g1ax.g1axfixer.mixin;

import dev.g1ax.g1axfixer.G1axFixer;
import dev.g1ax.g1axfixer.error.SafetyWrapper;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void g1ax$optimizeEntityOutline(CallbackInfo ci) {
        SafetyWrapper.wrapSafeVoid(() -> {
            if (!G1axFixer.getInstance().getResourcePackFixer().renderOptimizer.shouldOptimizeEntityRendering()) {
                return;
            }
        }, "WorldRenderer.render");
    }
}

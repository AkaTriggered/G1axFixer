package dev.g1ax.g1axfixer.mixin;

import dev.g1ax.g1axfixer.G1axFixer;
import dev.g1ax.g1axfixer.error.ErrorCategory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpriteAtlasTexture.class)
public class SpriteAtlasMixin {

    @Inject(method = "upload", at = @At("HEAD"))
    private void g1ax$beforeUpload(CallbackInfo ci) {
        try {
            if (G1axFixer.getInstance() != null && G1axFixer.getInstance().getConfig().logDebug) {
                G1axFixer.getInstance().getErrorHandler().debug(
                    ErrorCategory.ATLAS_OVERFLOW, this.toString(), "Atlas upload started"
                );
            }
        } catch (Exception ignored) {}
    }

    @Inject(method = "upload", at = @At("RETURN"))
    private void g1ax$afterUpload(CallbackInfo ci) {
        try {
            if (G1axFixer.getInstance() != null && G1axFixer.getInstance().getConfig().logDebug) {
                G1axFixer.getInstance().getErrorHandler().debug(
                    ErrorCategory.ATLAS_OVERFLOW, this.toString(), "Atlas upload complete"
                );
            }
        } catch (Exception ignored) {}
    }
}

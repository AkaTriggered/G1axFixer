package dev.g1ax.g1axfixer.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.g1ax.g1axfixer.G1axFixer;
import dev.g1ax.g1axfixer.error.ErrorCategory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.Sprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpriteAtlasTexture.class)
public class SpriteAtlasMixin {

    @Inject(method = "upload", at = @At("HEAD"))
    private void g1ax$beforeUpload(CallbackInfo ci) {
        G1axFixer.getInstance().getErrorHandler().debug(ErrorCategory.ATLAS_OVERFLOW, this.toString(), "Atlas upload started");
    }

    @Inject(method = "upload", at = @At("RETURN"))
    private void g1ax$afterUpload(CallbackInfo ci) {
        G1axFixer.getInstance().getErrorHandler().debug(ErrorCategory.ATLAS_OVERFLOW, this.toString(), "Atlas upload complete");
    }

    @WrapOperation(method = "upload", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/SpriteAtlasTexture;getSprite(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/texture/Sprite;"))
    private Sprite g1ax$checkSpriteSize(SpriteAtlasTexture instance, Object id, Operation<Sprite> original) {
        try {
            Sprite sprite = original.call(instance, id);
            if (sprite != null && G1axFixer.getInstance().getResourcePackFixer().atlasStitchFixer.shouldSkipOversizedSprite(id.toString(), sprite.getContents().getWidth(), sprite.getContents().getHeight())) {
                return null;
            }
            return sprite;
        } catch (Exception e) {
            G1axFixer.getInstance().getResourcePackFixer().atlasStitchFixer.handleStitchException(this.toString(), e);
            return null;
        }
    }
}

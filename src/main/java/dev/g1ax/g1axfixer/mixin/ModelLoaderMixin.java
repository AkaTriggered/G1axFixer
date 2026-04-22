package dev.g1ax.g1axfixer.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.g1ax.g1axfixer.G1axFixer;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BakedModelManager.class)
public class ModelLoaderMixin {

    @WrapOperation(method = "reload", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/UnbakedModel;bake(Lnet/minecraft/client/render/model/Baker;Ljava/util/function/Function;Lnet/minecraft/client/render/model/ModelBakeSettings;)Lnet/minecraft/client/render/model/BakedModel;"))
    private Object g1ax$handleBrokenModel(UnbakedModel instance, Object baker, Object textureGetter, Object settings, Operation<Object> original) {
        try {
            return original.call(instance, baker, textureGetter, settings);
        } catch (Exception e) {
            G1axFixer.getInstance().getResourcePackFixer().modelLoadFixer.handleBrokenModel(Identifier.of("minecraft", "unknown"), e);
            return null;
        }
    }
}

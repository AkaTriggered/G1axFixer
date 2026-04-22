package dev.g1ax.g1axfixer.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.g1ax.g1axfixer.G1axFixer;
import net.minecraft.resource.ResourcePackProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ResourcePackProfile.class)
public class ResourcePackProfileMixin {

    @WrapOperation(method = "loadMetadata", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourcePack;parseMetadata(Lnet/minecraft/resource/metadata/ResourceMetadataReader;)Ljava/lang/Object;"))
    private Object g1ax$fixMetadata(Object pack, Object reader, Operation<Object> original) {
        try {
            return original.call(pack, reader);
        } catch (Exception e) {
            G1axFixer.getInstance().getResourcePackFixer().metadataSanitizer.getFallbackMeta(pack.toString(), e);
            return null;
        }
    }
}

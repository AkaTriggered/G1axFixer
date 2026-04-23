package dev.g1ax.g1axfixer.mixin;

import dev.g1ax.g1axfixer.G1axFixer;
import net.minecraft.resource.ResourcePackProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ResourcePackProfile.class)
public class ResourcePackProfileMixin {

    @Inject(method = "getDisplayName", at = @At("RETURN"))
    private void g1ax$logPackProfile(CallbackInfoReturnable<?> cir) {
        try {
            if (G1axFixer.getInstance() != null && G1axFixer.getInstance().getConfig().logDebug) {
                G1axFixer.LOGGER.debug("[G1axFixer] Resource pack profile accessed: {}", cir.getReturnValue());
            }
        } catch (Exception ignored) {}
    }
}

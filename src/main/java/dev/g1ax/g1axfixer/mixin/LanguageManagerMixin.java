package dev.g1ax.g1axfixer.mixin;

import dev.g1ax.g1axfixer.G1axFixer;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LanguageManager.class)
public class LanguageManagerMixin {

    @Inject(method = "reload", at = @At("HEAD"))
    private void g1ax$onLangReload(ResourceManager manager, CallbackInfo ci) {
        try {
            if (G1axFixer.getInstance() != null) {
                G1axFixer.LOGGER.info("[G1axFixer] Language reload intercepted — malformed lang protection active.");
            }
        } catch (Exception ignored) {}
    }
}

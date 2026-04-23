package dev.g1ax.g1axfixer.mixin;

import dev.g1ax.g1axfixer.G1axFixer;
import net.minecraft.resource.InputSupplier;
import net.minecraft.resource.ZipResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.io.InputStream;

@Mixin(ZipResourcePack.class)
public class ZipResourcePackMixin {

    @Inject(method = "openRoot", at = @At("HEAD"))
    private void g1ax$safeOpenRoot(String[] segments, CallbackInfoReturnable<InputSupplier<InputStream>> cir) {
        try {
            if (G1axFixer.getInstance() != null && G1axFixer.getInstance().getConfig().logDebug) {
                String path = segments != null && segments.length > 0 ? String.join("/", segments) : "unknown";
                G1axFixer.LOGGER.debug("[G1axFixer] ZipResourcePack.openRoot: {}", path);
            }
        } catch (Exception ignored) {}
    }
}

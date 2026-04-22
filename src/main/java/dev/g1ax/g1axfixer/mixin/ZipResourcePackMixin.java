package dev.g1ax.g1axfixer.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.g1ax.g1axfixer.G1axFixer;
import dev.g1ax.g1axfixer.error.SafetyWrapper;
import net.minecraft.resource.ZipResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import java.io.InputStream;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

@Mixin(ZipResourcePack.class)
public class ZipResourcePackMixin {

    @WrapOperation(method = "openRoot", at = @At(value = "INVOKE", target = "Ljava/util/zip/ZipFile;getInputStream(Ljava/util/zip/ZipEntry;)Ljava/io/InputStream;"))
    private InputStream g1ax$safeOpenRoot(ZipFile zipFile, Object entry, Operation<InputStream> original) {
        return SafetyWrapper.wrapSafe(() -> {
            try {
                return original.call(zipFile, entry);
            } catch (Exception e) {
                return G1axFixer.getInstance().getResourcePackFixer().zipGuard.safeOpen(zipFile.getName(), entry.toString(), new ZipException(e.getMessage()));
            }
        }, null, "ZipResourcePack.openRoot");
    }
}


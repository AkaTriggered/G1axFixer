package dev.g1ax.g1axfixer.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.g1ax.g1axfixer.G1axFixer;
import net.minecraft.client.resource.language.LanguageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import java.util.Map;

@Mixin(LanguageManager.class)
public class LanguageManagerMixin {

    @WrapOperation(method = "apply(Ljava/util/Map;Lnet/minecraft/client/resource/language/LanguageManager$LanguageDefinition;)V", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    private Object g1ax$validateLangEntry(Map<String, String> instance, Object key, Object value, Operation<Object> original) {
        if (!G1axFixer.getInstance().getResourcePackFixer().languageFileFixer.isValidEntry((String) key, value)) {
            return null;
        }
        try {
            return original.call(instance, key, value);
        } catch (Exception e) {
            G1axFixer.getInstance().getResourcePackFixer().languageFileFixer.handleMalformedLang("unknown", key.toString(), e);
            return null;
        }
    }
}

package dev.g1ax.g1axfixer.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.g1ax.g1axfixer.G1axFixer;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SoundManager.class)
public class SoundManagerMixin {

    @WrapOperation(method = "play(Lnet/minecraft/client/sound/SoundInstance;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundInstance;getSound()Lnet/minecraft/sound/SoundEvent;"))
    private SoundEvent g1ax$handleMissingSound(SoundInstance instance, Operation<SoundEvent> original) {
        try {
            return original.call(instance);
        } catch (Exception e) {
            Identifier id = Identifier.of("minecraft", "missing");
            G1axFixer.getInstance().getResourcePackFixer().soundEventFixer.handleMissingSound(id, e);
            return null;
        }
    }
}

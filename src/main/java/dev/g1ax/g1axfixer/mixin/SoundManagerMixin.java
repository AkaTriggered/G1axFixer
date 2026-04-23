package dev.g1ax.g1axfixer.mixin;

import dev.g1ax.g1axfixer.G1axFixer;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundManager.class)
public class SoundManagerMixin {

    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;I)V", at = @At("HEAD"), cancellable = true)
    private void g1ax$handleMissingSound(SoundInstance sound, int delay, CallbackInfo ci) {
        try {
            if (G1axFixer.getInstance() == null) return;
            if (!G1axFixer.getInstance().getConfig().fixMissingSound) return;

            if (sound == null || sound.getId() == null) {
                G1axFixer.getInstance().getResourcePackFixer().soundEventFixer
                    .handleMissingSound(net.minecraft.util.Identifier.of("minecraft", "unknown"), null);
                ci.cancel();
            }
        } catch (Exception ignored) {}
    }
}

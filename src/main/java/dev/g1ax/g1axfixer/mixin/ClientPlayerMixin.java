package dev.g1ax.g1axfixer.mixin;

import dev.g1ax.g1axfixer.G1axFixer;
import dev.g1ax.g1axfixer.stats.G1axStatsTracker;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerMixin {

    @Unique private long g1ax$lastPositionPacketTime = 0;
    @Unique private double g1ax$lastSentX = Double.NaN;
    @Unique private double g1ax$lastSentY = Double.NaN;
    @Unique private double g1ax$lastSentZ = Double.NaN;
    @Unique private float g1ax$lastSentYaw = Float.NaN;
    @Unique private float g1ax$lastSentPitch = Float.NaN;

    @Inject(method = "sendMovementPackets", at = @At("HEAD"), cancellable = true)
    private void g1ax$throttlePositionPackets(CallbackInfo ci) {
        try {
            if (G1axFixer.getInstance() == null) return;
            var config = G1axFixer.getInstance().getConfig();
            if (!config.optimizeNetwork || !config.throttlePositionPackets) return;

            ClientPlayerEntity self = (ClientPlayerEntity) (Object) this;
            long now = System.currentTimeMillis();

            double dx = self.getX() - g1ax$lastSentX;
            double dy = self.getY() - g1ax$lastSentY;
            double dz = self.getZ() - g1ax$lastSentZ;
            float dYaw = Math.abs(self.getYaw() - g1ax$lastSentYaw);
            float dPitch = Math.abs(self.getPitch() - g1ax$lastSentPitch);
            double moveSq = dx * dx + dy * dy + dz * dz;

            boolean significantMove = moveSq > 0.0001 || dYaw > 0.5f || dPitch > 0.5f;
            boolean timeElapsed = (now - g1ax$lastPositionPacketTime) >= config.positionPacketMinIntervalMs;

            if (!significantMove && !timeElapsed && (now - g1ax$lastPositionPacketTime) < 1000) {
                G1axStatsTracker.get().packetsOptimized.incrementAndGet();
                ci.cancel();
                return;
            }

            if (significantMove || timeElapsed) {
                g1ax$lastSentX = self.getX();
                g1ax$lastSentY = self.getY();
                g1ax$lastSentZ = self.getZ();
                g1ax$lastSentYaw = self.getYaw();
                g1ax$lastSentPitch = self.getPitch();
                g1ax$lastPositionPacketTime = now;
            }
        } catch (Exception ignored) {}
    }
}

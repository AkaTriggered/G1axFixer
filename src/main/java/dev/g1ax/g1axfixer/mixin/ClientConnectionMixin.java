package dev.g1ax.g1axfixer.mixin;

import dev.g1ax.g1axfixer.G1axFixer;
import dev.g1ax.g1axfixer.stats.G1axStatsTracker;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import io.netty.channel.ChannelHandlerContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Unique private final Map<String, Long> g1ax$lastPacketTime = new ConcurrentHashMap<>();
    @Unique private long g1ax$totalPackets = 0;
    @Unique private long g1ax$droppedPackets = 0;
    @Unique private long g1ax$lastStatLog = System.currentTimeMillis();

    @Inject(method = "exceptionCaught", at = @At("HEAD"))
    private void g1ax$handleNetworkException(ChannelHandlerContext ctx, Throwable cause, CallbackInfo ci) {
        try {
            if (G1axFixer.getInstance() != null) {
                G1axFixer.LOGGER.debug("[G1axFixer] Network exception intercepted: {}", cause.getMessage());
                G1axStatsTracker.get().packetsOptimized.incrementAndGet();
            }
        } catch (Exception ignored) {}
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void g1ax$optimizeIncomingPackets(ChannelHandlerContext ctx, Packet<?> packet, CallbackInfo ci) {
        try {
            if (G1axFixer.getInstance() == null) return;
            var config = G1axFixer.getInstance().getConfig();
            if (!config.optimizeNetwork) return;

            g1ax$totalPackets++;
            String packetType = packet.getClass().getSimpleName();

            if (config.deduplicatePackets) {
                long now = System.currentTimeMillis();
                Long lastTime = g1ax$lastPacketTime.get(packetType);

                boolean isDuplicatable = packetType.contains("ParticleS2CPacket") ||
                                         packetType.contains("WorldEventS2CPacket") ||
                                         packetType.contains("PlaySoundS2CPacket");

                if (isDuplicatable && lastTime != null && (now - lastTime) < config.packetDedupeWindowMs) {
                    g1ax$droppedPackets++;
                    G1axStatsTracker.get().packetsOptimized.incrementAndGet();
                    ci.cancel();
                    return;
                }

                g1ax$lastPacketTime.put(packetType, now);
            }

            long now = System.currentTimeMillis();
            if (now - g1ax$lastStatLog > 60000) {
                g1ax$lastStatLog = now;
                if (config.logDebug && g1ax$totalPackets > 0) {
                    double dropRate = (double) g1ax$droppedPackets / g1ax$totalPackets * 100;
                    G1axFixer.LOGGER.info("[G1axFixer][NET] Packets: {} total, {} deduped ({} %)",
                        g1ax$totalPackets, g1ax$droppedPackets, String.format("%.1f", dropRate));
                }
                g1ax$totalPackets = 0;
                g1ax$droppedPackets = 0;
                g1ax$lastPacketTime.clear();
            }
        } catch (Exception ignored) {}
    }
}

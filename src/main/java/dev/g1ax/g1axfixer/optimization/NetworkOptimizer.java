package dev.g1ax.g1axfixer.optimization;

import dev.g1ax.g1axfixer.config.G1axConfig;
import dev.g1ax.g1axfixer.G1axFixer;

public class NetworkOptimizer {

    private final G1axConfig config;

    public NetworkOptimizer(G1axConfig config) {
        this.config = config;
    }

    public void register() {
        G1axFixer.LOGGER.info("[G1axFixer]   NetworkOptimizer  → {}", config.optimizeNetwork ? "ON" : "OFF");
        if (config.optimizeNetwork) {
            G1axFixer.LOGGER.info("[G1axFixer]     PacketBatching   → {}", config.batchNetworkPackets ? "ON" : "OFF");
            G1axFixer.LOGGER.info("[G1axFixer]     PacketDedup      → {}", config.deduplicatePackets ? "ON" : "OFF");
            G1axFixer.LOGGER.info("[G1axFixer]     PosThrottle      → {}", config.throttlePositionPackets ? "ON" : "OFF");
            G1axFixer.LOGGER.info("[G1axFixer]     PacketPriority   → {}", config.prioritizePackets ? "ON" : "OFF");
        }
    }

    public boolean shouldBatchPackets() { return config.optimizeNetwork && config.batchNetworkPackets; }
    public int getPacketBatchSize() { return config.packetBatchSize; }
    public boolean shouldCompressPackets() { return config.optimizeNetwork && config.compressLargePackets; }
    public int getCompressionThreshold() { return config.compressionThreshold; }
    public boolean shouldDeduplicatePackets() { return config.optimizeNetwork && config.deduplicatePackets; }
    public boolean shouldThrottlePositionPackets() { return config.optimizeNetwork && config.throttlePositionPackets; }
}

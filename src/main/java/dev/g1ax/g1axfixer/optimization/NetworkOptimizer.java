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
    }

    public boolean shouldBatchPackets() {
        return config.optimizeNetwork && config.batchNetworkPackets;
    }

    public int getPacketBatchSize() {
        return config.packetBatchSize;
    }

    public boolean shouldCompressPackets() {
        return config.optimizeNetwork && config.compressLargePackets;
    }

    public int getCompressionThreshold() {
        return config.compressionThreshold;
    }
}

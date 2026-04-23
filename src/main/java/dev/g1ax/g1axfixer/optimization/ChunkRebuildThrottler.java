package dev.g1ax.g1axfixer.optimization;

import dev.g1ax.g1axfixer.config.G1axConfig;
import dev.g1ax.g1axfixer.stats.G1axStatsTracker;
import dev.g1ax.g1axfixer.G1axFixer;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ChunkRebuildThrottler {

    private final G1axConfig config;
    private final AtomicInteger rebuildsThisFrame = new AtomicInteger();
    private final Set<Long> queuedChunks = ConcurrentHashMap.newKeySet();

    public ChunkRebuildThrottler(G1axConfig config) {
        this.config = config;
    }

    public void register() {
        G1axFixer.LOGGER.info("[G1axFixer]   ChunkThrottler   → {}", config.optimizeChunkRebuilds ? "ON" : "OFF");
    }

    public boolean shouldThrottleRebuild(int chunkX, int chunkZ) {
        if (!config.optimizeChunkRebuilds) return false;

        long key = ((long) chunkX << 32) | (chunkZ & 0xFFFFFFFFL);

        if (queuedChunks.contains(key)) {
            G1axStatsTracker.get().chunkRebuildsThrottled.incrementAndGet();
            return true;
        }

        if (rebuildsThisFrame.get() >= config.maxChunkRebuildsPerFrame) {
            G1axStatsTracker.get().chunkRebuildsThrottled.incrementAndGet();
            return true;
        }

        rebuildsThisFrame.incrementAndGet();
        queuedChunks.add(key);
        return false;
    }

    public void onFrameEnd() {
        rebuildsThisFrame.set(0);
        queuedChunks.clear();
    }
}

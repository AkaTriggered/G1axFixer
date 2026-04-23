package dev.g1ax.g1axfixer.optimization;

import dev.g1ax.g1axfixer.config.G1axConfig;
import dev.g1ax.g1axfixer.stats.G1axStatsTracker;
import dev.g1ax.g1axfixer.G1axFixer;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class LightUpdateBatcher {

    private final G1axConfig config;
    private final Set<Long> pendingUpdates = ConcurrentHashMap.newKeySet();

    public LightUpdateBatcher(G1axConfig config) {
        this.config = config;
    }

    public void register() {
        G1axFixer.LOGGER.info("[G1axFixer]   LightBatcher     → {}", config.batchLightUpdates ? "ON" : "OFF");
    }

    public boolean shouldDeduplicate(int x, int y, int z) {
        if (!config.batchLightUpdates) return false;

        long key = ((long) x & 0xFFFFF) | (((long) y & 0xFFF) << 20) | (((long) z & 0xFFFFF) << 32);
        if (!pendingUpdates.add(key)) {
            G1axStatsTracker.get().lightUpdatesDeduped.incrementAndGet();
            return true;
        }
        return false;
    }

    public void flush() {
        pendingUpdates.clear();
    }
}

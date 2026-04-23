package dev.g1ax.g1axfixer.optimization;

import dev.g1ax.g1axfixer.config.G1axConfig;
import dev.g1ax.g1axfixer.stats.G1axStatsTracker;
import dev.g1ax.g1axfixer.G1axFixer;

public class MemoryOptimizer {

    private final G1axConfig config;
    private long lastCheckTime = 0;
    private boolean underPressure = false;

    public MemoryOptimizer(G1axConfig config) {
        this.config = config;
    }

    public void register() {
        G1axFixer.LOGGER.info("[G1axFixer]   MemoryOptimizer  → {}", config.optimizeMemory ? "ON" : "OFF");
    }

    public boolean isUnderPressure() {
        if (!config.optimizeMemory) return false;

        long now = System.currentTimeMillis();
        if (now - lastCheckTime < 1000) return underPressure;
        lastCheckTime = now;

        Runtime rt = Runtime.getRuntime();
        long free = rt.freeMemory();
        long max = rt.maxMemory();
        double freePercent = (double) free / max * 100.0;

        boolean wasPressure = underPressure;
        underPressure = freePercent < config.memoryPressureThreshold;

        if (underPressure && !wasPressure) {
            G1axStatsTracker.get().memoryPressureEvents.incrementAndGet();
            G1axFixer.LOGGER.warn("[G1axFixer] Memory pressure detected ({} % free). Increasing culling aggression.", String.format("%.1f", freePercent));
        }

        return underPressure;
    }

    public int getAggressiveCullMultiplier() {
        return underPressure ? 2 : 1;
    }
}

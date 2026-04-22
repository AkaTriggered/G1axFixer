package dev.g1ax.g1axfixer.optimization;

import dev.g1ax.g1axfixer.config.G1axConfig;
import dev.g1ax.g1axfixer.G1axFixer;

public class TickOptimizer {

    private final G1axConfig config;

    public TickOptimizer(G1axConfig config) {
        this.config = config;
    }

    public void register() {
        G1axFixer.LOGGER.info("[G1axFixer]   TickOptimizer     → {}", config.optimizeTicking ? "ON" : "OFF");
    }

    public boolean shouldOptimizeEntityTicking() {
        return config.optimizeTicking && config.optimizeEntityTick;
    }

    public boolean shouldOptimizeBlockEntityTicking() {
        return config.optimizeTicking && config.optimizeBlockEntityTick;
    }

    public int getEntityTickRange() {
        return config.entityTickRange;
    }
}

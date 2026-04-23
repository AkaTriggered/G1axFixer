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
        if (config.optimizeTicking) {
            G1axFixer.LOGGER.info("[G1axFixer]     EntityTick       → {} (range: {})", config.optimizeEntityTick ? "ON" : "OFF", config.entityTickRange);
            G1axFixer.LOGGER.info("[G1axFixer]     BlockEntityTick  → {}", config.optimizeBlockEntityTick ? "ON" : "OFF");
            G1axFixer.LOGGER.info("[G1axFixer]     ItemEntityThrot  → {} (rate: 1/{})", config.throttleItemEntityTick ? "ON" : "OFF", config.itemEntityTickRate);
            G1axFixer.LOGGER.info("[G1axFixer]     ArmorStandThrot  → {}", config.throttleArmorStandTick ? "ON" : "OFF");
            G1axFixer.LOGGER.info("[G1axFixer]     PathfindThrottle → {} (dist: {})", config.throttlePathfinding ? "ON" : "OFF", config.pathfindingThrottleDistance);
        }
    }

    public boolean shouldOptimizeEntityTicking() { return config.optimizeTicking && config.optimizeEntityTick; }
    public boolean shouldOptimizeBlockEntityTicking() { return config.optimizeTicking && config.optimizeBlockEntityTick; }
    public int getEntityTickRange() { return config.entityTickRange; }
}

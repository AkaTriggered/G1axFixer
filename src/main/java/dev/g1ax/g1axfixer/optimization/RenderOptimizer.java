package dev.g1ax.g1axfixer.optimization;

import dev.g1ax.g1axfixer.config.G1axConfig;
import dev.g1ax.g1axfixer.G1axFixer;

public class RenderOptimizer {

    private final G1axConfig config;

    public RenderOptimizer(G1axConfig config) {
        this.config = config;
    }

    public void register() {
        G1axFixer.LOGGER.info("[G1axFixer]   RenderOptimizer   → {}", config.optimizeRendering ? "ON" : "OFF");
        if (config.optimizeRendering) {
            G1axFixer.LOGGER.info("[G1axFixer]     ParticleCull     → {} (dist: {})", config.cullDistantParticles ? "ON" : "OFF", config.particleCullDistance);
            G1axFixer.LOGGER.info("[G1axFixer]     ParticleCap      → {} (max: {})", config.reduceParticleCount ? "ON" : "OFF", config.maxParticlesPerTick);
            G1axFixer.LOGGER.info("[G1axFixer]     EntityRender     → {} (distSq: {})", config.optimizeEntityRender ? "ON" : "OFF", config.entityRenderDistanceSq);
            G1axFixer.LOGGER.info("[G1axFixer]     HiddenEntitySkip → {}", config.skipHiddenEntityRender ? "ON" : "OFF");
            G1axFixer.LOGGER.info("[G1axFixer]     EntityLOD        → {}", config.lodEntityRendering ? "ON" : "OFF");
            G1axFixer.LOGGER.info("[G1axFixer]     BlockEntityCull  → {} (dist: {})", config.optimizeBlockEntityRendering ? "ON" : "OFF", config.blockEntityRenderDistance);
        }
    }

    public boolean shouldCullParticles() { return config.optimizeRendering && config.cullDistantParticles; }
    public int getParticleCullDistance() { return config.particleCullDistance; }
    public boolean shouldOptimizeEntityRendering() { return config.optimizeRendering && config.optimizeEntityRender; }
    public boolean shouldReduceParticles() { return config.optimizeRendering && config.reduceParticleCount; }
}

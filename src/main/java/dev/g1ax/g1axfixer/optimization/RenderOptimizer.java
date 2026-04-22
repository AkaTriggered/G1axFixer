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
    }

    public boolean shouldCullParticles() {
        return config.optimizeRendering && config.cullDistantParticles;
    }

    public int getParticleCullDistance() {
        return config.particleCullDistance;
    }

    public boolean shouldOptimizeEntityRendering() {
        return config.optimizeRendering && config.optimizeEntityRender;
    }

    public boolean shouldReduceParticles() {
        return config.optimizeRendering && config.reduceParticleCount;
    }
}

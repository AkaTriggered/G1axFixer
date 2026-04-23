package dev.g1ax.g1axfixer.optimization;

import dev.g1ax.g1axfixer.config.G1axConfig;
import dev.g1ax.g1axfixer.G1axFixer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ItemRenderCache {

    private final G1axConfig config;
    private final Map<Integer, CachedDecision> cache = new ConcurrentHashMap<>();
    private long lastClearTime = System.currentTimeMillis();

    public record CachedDecision(boolean shouldRender, int lodLevel, long timestamp) {}

    public ItemRenderCache(G1axConfig config) {
        this.config = config;
    }

    public void register() {
        G1axFixer.LOGGER.info("[G1axFixer]   ItemRenderCache  → {}", config.optimizeItemRendering ? "ON" : "OFF");
    }

    public CachedDecision getCached(int entityId, double distanceSq) {
        if (!config.optimizeItemRendering) return null;

        long now = System.currentTimeMillis();
        if (now - lastClearTime > 5000) {
            cache.entrySet().removeIf(e -> now - e.getValue().timestamp > 2000);
            lastClearTime = now;
        }

        CachedDecision cached = cache.get(entityId);
        if (cached != null && now - cached.timestamp < 500) {
            return cached;
        }

        int lod = distanceSq > 4096 ? 2 : distanceSq > 1024 ? 1 : 0;
        boolean render = distanceSq < config.particleCullDistance * config.particleCullDistance;
        CachedDecision decision = new CachedDecision(render, lod, now);
        cache.put(entityId, decision);
        return decision;
    }

    public void invalidate(int entityId) {
        cache.remove(entityId);
    }
}

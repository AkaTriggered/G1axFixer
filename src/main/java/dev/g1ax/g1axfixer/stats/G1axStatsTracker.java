package dev.g1ax.g1axfixer.stats;

import java.util.concurrent.atomic.AtomicLong;

public class G1axStatsTracker {

    private static final G1axStatsTracker INSTANCE = new G1axStatsTracker();
    public static G1axStatsTracker get() { return INSTANCE; }

    public final AtomicLong particlesCulled = new AtomicLong();
    public final AtomicLong entityTicksSkipped = new AtomicLong();
    public final AtomicLong chunkRebuildsThrottled = new AtomicLong();
    public final AtomicLong memoryPressureEvents = new AtomicLong();
    public final AtomicLong resourceIssuesFixed = new AtomicLong();
    public final AtomicLong entityRenderSkipped = new AtomicLong();
    public final AtomicLong lightUpdatesDeduped = new AtomicLong();
    public final AtomicLong packetsOptimized = new AtomicLong();

    private long sessionStartTime = System.currentTimeMillis();

    public long getSessionSeconds() {
        return (System.currentTimeMillis() - sessionStartTime) / 1000;
    }

    public long getTotalOptimizations() {
        return particlesCulled.get() + entityTicksSkipped.get() +
               chunkRebuildsThrottled.get() + entityRenderSkipped.get() +
               lightUpdatesDeduped.get() + packetsOptimized.get();
    }

    public long getEstimatedMemorySavedKB() {
        return (particlesCulled.get() * 2) + (entityTicksSkipped.get()) +
               (chunkRebuildsThrottled.get() * 64) + (lightUpdatesDeduped.get() * 4);
    }

    public void reset() {
        particlesCulled.set(0);
        entityTicksSkipped.set(0);
        chunkRebuildsThrottled.set(0);
        memoryPressureEvents.set(0);
        resourceIssuesFixed.set(0);
        entityRenderSkipped.set(0);
        lightUpdatesDeduped.set(0);
        packetsOptimized.set(0);
        sessionStartTime = System.currentTimeMillis();
    }
}

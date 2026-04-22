package dev.g1ax.g1axfixer.error;

import dev.g1ax.g1axfixer.G1axFixer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class G1axErrorHandler {

    public record FixEvent(ErrorCategory category, String resource, String fixApplied, long timestamp) {}

    private final List<FixEvent> eventLog = Collections.synchronizedList(new ArrayList<>());
    private final Map<ErrorCategory, AtomicLong> counters = new ConcurrentHashMap<>();
    private final Map<String, Long> seenKeys = new ConcurrentHashMap<>();
    private static final int MAX_LOG = 512;

    public G1axErrorHandler() {
        for (ErrorCategory cat : ErrorCategory.values()) {
            counters.put(cat, new AtomicLong(0));
        }
    }

    public void report(ErrorCategory category, String resource, String fixApplied) {
        String dedupKey = category.name() + ":" + resource;

        if (seenKeys.putIfAbsent(dedupKey, System.currentTimeMillis()) != null) {
            counters.get(category).incrementAndGet();
            return;
        }

        counters.get(category).incrementAndGet();

        FixEvent event = new FixEvent(category, resource, fixApplied, System.currentTimeMillis());

        if (eventLog.size() < MAX_LOG) {
            eventLog.add(event);
        }

        if (G1axFixer.getInstance().getConfig().logFixedIssues) {
            G1axFixer.LOGGER.warn("[G1axFixer][{}] {} → Fix: {}", category.name(), resource, fixApplied);
        }
    }

    public void debug(ErrorCategory category, String resource, String detail) {
        if (G1axFixer.getInstance().getConfig().logDebug) {
            G1axFixer.LOGGER.info("[G1axFixer][DEBUG][{}] {} — {}", category.name(), resource, detail);
        }
    }

    public void critical(ErrorCategory category, String resource, Throwable cause) {
        G1axFixer.LOGGER.error("[G1axFixer][CRITICAL][{}] Unrecoverable issue in '{}': {}", category.name(), resource, cause.getMessage(), cause);
        counters.get(category).incrementAndGet();
    }

    public long getCount(ErrorCategory category) {
        return counters.get(category).get();
    }

    public long getTotalFixed() {
        return counters.values().stream().mapToLong(AtomicLong::get).sum();
    }

    public List<FixEvent> getEventLog() {
        return Collections.unmodifiableList(eventLog);
    }

    public void printSummary() {
        G1axFixer.LOGGER.info("[G1axFixer] ── Fix Summary ──────────────────────");
        for (ErrorCategory cat : ErrorCategory.values()) {
            long count = counters.get(cat).get();
            if (count > 0) {
                G1axFixer.LOGGER.info("[G1axFixer]   {:30s} : {} fixed", cat.description, count);
            }
        }
        G1axFixer.LOGGER.info("[G1axFixer]   TOTAL fixed this session         : {}", getTotalFixed());
        G1axFixer.LOGGER.info("[G1axFixer] ─────────────────────────────────────");
    }
}

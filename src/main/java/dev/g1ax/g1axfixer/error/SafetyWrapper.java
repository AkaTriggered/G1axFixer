package dev.g1ax.g1axfixer.error;

public class SafetyWrapper {

    public static <T> T wrapSafe(SafeOperation<T> operation, T fallback, String context) {
        try {
            return operation.execute();
        } catch (Throwable t) {
            try {
                dev.g1ax.g1axfixer.G1axFixer.LOGGER.error("[G1axFixer] Caught error in {}: {}", context, t.getMessage());
                if (dev.g1ax.g1axfixer.G1axFixer.getInstance() != null && dev.g1ax.g1axfixer.G1axFixer.getInstance().getErrorHandler() != null) {
                    dev.g1ax.g1axfixer.G1axFixer.getInstance().getErrorHandler().critical(ErrorCategory.UNKNOWN, context, t);
                }
            } catch (Exception ignored) {}
            return fallback;
        }
    }

    public static void wrapSafeVoid(SafeVoidOperation operation, String context) {
        try {
            operation.execute();
        } catch (Throwable t) {
            try {
                dev.g1ax.g1axfixer.G1axFixer.LOGGER.error("[G1axFixer] Caught error in {}: {}", context, t.getMessage());
                if (dev.g1ax.g1axfixer.G1axFixer.getInstance() != null && dev.g1ax.g1axfixer.G1axFixer.getInstance().getErrorHandler() != null) {
                    dev.g1ax.g1axfixer.G1axFixer.getInstance().getErrorHandler().critical(ErrorCategory.UNKNOWN, context, t);
                }
            } catch (Exception ignored) {}
        }
    }

    @FunctionalInterface
    public interface SafeOperation<T> {
        T execute() throws Throwable;
    }

    @FunctionalInterface
    public interface SafeVoidOperation {
        void execute() throws Throwable;
    }
}

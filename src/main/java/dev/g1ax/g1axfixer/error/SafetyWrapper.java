package dev.g1ax.g1axfixer.error;

import dev.g1ax.g1axfixer.G1axFixer;
import org.spongepowered.asm.mixin.MixinEnvironment;

public class SafetyWrapper {

    public static <T> T wrapSafe(SafeOperation<T> operation, T fallback, String context) {
        try {
            return operation.execute();
        } catch (Throwable t) {
            G1axFixer.LOGGER.error("[G1axFixer] Caught error in {}: {}", context, t.getMessage());
            if (G1axFixer.getInstance() != null && G1axFixer.getInstance().getErrorHandler() != null) {
                G1axFixer.getInstance().getErrorHandler().critical(ErrorCategory.UNKNOWN, context, t);
            }
            return fallback;
        }
    }

    public static void wrapSafeVoid(SafeVoidOperation operation, String context) {
        try {
            operation.execute();
        } catch (Throwable t) {
            G1axFixer.LOGGER.error("[G1axFixer] Caught error in {}: {}", context, t.getMessage());
            if (G1axFixer.getInstance() != null && G1axFixer.getInstance().getErrorHandler() != null) {
                G1axFixer.getInstance().getErrorHandler().critical(ErrorCategory.UNKNOWN, context, t);
            }
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

package dev.g1ax.g1axfixer.fix;

import dev.g1ax.g1axfixer.config.G1axConfig;
import dev.g1ax.g1axfixer.error.ErrorCategory;
import dev.g1ax.g1axfixer.error.G1axErrorHandler;
import net.minecraft.util.Identifier;

public class ModelLoadFixer {

    public static final Identifier MISSING_MODEL = Identifier.of("minecraft", "builtin/missing");

    private final G1axConfig config;
    private final G1axErrorHandler errors;

    public ModelLoadFixer(G1axConfig config, G1axErrorHandler errors) {
        this.config = config;
        this.errors = errors;
    }

    public Identifier handleBrokenModel(Identifier original, Throwable cause) {
        if (!config.fixBrokenModel) return original;

        errors.report(ErrorCategory.BROKEN_MODEL, original.toString(), "Substituted missing_model. Cause: " + (cause != null ? cause.getMessage() : "null/unresolvable"));
        return MISSING_MODEL;
    }

    public boolean isMalformedModelJson(String json) {
        if (json == null || json.isBlank()) return true;
        String trimmed = json.stripLeading();
        return !trimmed.startsWith("{");
    }
}

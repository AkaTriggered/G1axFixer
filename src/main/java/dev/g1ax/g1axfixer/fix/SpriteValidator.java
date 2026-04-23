package dev.g1ax.g1axfixer.fix;

import dev.g1ax.g1axfixer.config.G1axConfig;
import dev.g1ax.g1axfixer.error.ErrorCategory;
import dev.g1ax.g1axfixer.error.G1axErrorHandler;
import net.minecraft.util.Identifier;

public class SpriteValidator {

    public static final Identifier MISSING_SPRITE = Identifier.of("minecraft", "missingno");

    private final G1axConfig config;
    private final G1axErrorHandler errors;

    public SpriteValidator(G1axConfig config, G1axErrorHandler errors) {
        this.config = config;
        this.errors = errors;
    }

    public Identifier handleMissingSprite(Identifier original, Throwable cause) {
        if (!config.fixMissingSprite) return original;

        errors.report(ErrorCategory.MISSING_SPRITE, original.toString(), "Substituted with missingno fallback. Cause: " + (cause != null ? cause.getMessage() : "null sprite"));

        return MISSING_SPRITE;
    }

    public boolean isMalformed(String path) {
        if (path == null || path.isBlank()) return true;
        if (!path.endsWith(".png") && path.contains("textures/")) return true;
        if (path.contains("..")) return true;
        return false;
    }
}

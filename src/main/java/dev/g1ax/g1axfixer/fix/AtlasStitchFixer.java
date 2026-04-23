package dev.g1ax.g1axfixer.fix;

import dev.g1ax.g1axfixer.config.G1axConfig;
import dev.g1ax.g1axfixer.error.ErrorCategory;
import dev.g1ax.g1axfixer.error.G1axErrorHandler;

public class AtlasStitchFixer {

    public static final int MAX_SAFE_ATLAS_SIZE = 8192;

    private final G1axConfig config;
    private final G1axErrorHandler errors;

    public AtlasStitchFixer(G1axConfig config, G1axErrorHandler errors) {
        this.config = config;
        this.errors = errors;
    }

    public boolean shouldSkipOversizedSprite(String spriteId, int width, int height) {
        if (!config.fixAtlasStitch) return false;

        if (width > MAX_SAFE_ATLAS_SIZE || height > MAX_SAFE_ATLAS_SIZE) {
            errors.report(ErrorCategory.ATLAS_OVERFLOW, spriteId, String.format("Sprite %dx%d exceeds safe atlas limit (%d). Skipped.", width, height, MAX_SAFE_ATLAS_SIZE));
            return true;
        }
        return false;
    }

    public boolean handleStitchException(String atlasId, Throwable cause) {
        if (!config.fixAtlasStitch) return false;

        errors.report(ErrorCategory.ATLAS_OVERFLOW, atlasId, "Atlas stitch exception suppressed: " + cause.getMessage());
        return true;
    }
}

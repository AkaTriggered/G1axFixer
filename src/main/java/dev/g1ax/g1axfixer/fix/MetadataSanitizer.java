package dev.g1ax.g1axfixer.fix;

import dev.g1ax.g1axfixer.config.G1axConfig;
import dev.g1ax.g1axfixer.error.ErrorCategory;
import dev.g1ax.g1axfixer.error.G1axErrorHandler;

public class MetadataSanitizer {

    public static final String FALLBACK_MCMETA = """
        {
          "pack": {
            "pack_format": 34,
            "description": "Resource pack (repaired by G1axFixer)"
          }
        }
        """;

    private final G1axConfig config;
    private final G1axErrorHandler errors;

    public MetadataSanitizer(G1axConfig config, G1axErrorHandler errors) {
        this.config = config;
        this.errors = errors;
    }

    public String getFallbackMeta(String packName, Throwable cause) {
        if (!config.fixPackMetadata) return null;

        errors.report(ErrorCategory.PACK_METADATA, packName + "/pack.mcmeta", "Injected fallback pack.mcmeta. Cause: " + (cause != null ? cause.getMessage() : "file not found"));
        return FALLBACK_MCMETA;
    }

    public boolean isPackFormatSupported(int format) {
        return format >= 32 && format <= 40;
    }
}

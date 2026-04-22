package dev.g1ax.g1axfixer.fix;

import dev.g1ax.g1axfixer.config.G1axConfig;
import dev.g1ax.g1axfixer.error.ErrorCategory;
import dev.g1ax.g1axfixer.error.G1axErrorHandler;

public class LanguageFileFixer {

    private final G1axConfig config;
    private final G1axErrorHandler errors;

    public LanguageFileFixer(G1axConfig config, G1axErrorHandler errors) {
        this.config = config;
        this.errors = errors;
    }

    public boolean handleMalformedLang(String packName, String langPath, Throwable cause) {
        if (!config.fixLanguageJson) return false;

        errors.report(ErrorCategory.LANGUAGE_JSON, packName + "/" + langPath, "Skipped malformed lang file: " + cause.getMessage());
        return true;
    }

    public boolean isValidEntry(String key, Object value) {
        if (key == null || key.isBlank()) return false;
        if (value == null) return false;
        if (key.contains(" ")) return false;
        return true;
    }
}

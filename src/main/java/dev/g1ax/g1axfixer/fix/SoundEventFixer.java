package dev.g1ax.g1axfixer.fix;

import dev.g1ax.g1axfixer.config.G1axConfig;
import dev.g1ax.g1axfixer.error.ErrorCategory;
import dev.g1ax.g1axfixer.error.G1axErrorHandler;
import net.minecraft.util.Identifier;

public class SoundEventFixer {

    private final G1axConfig config;
    private final G1axErrorHandler errors;

    public SoundEventFixer(G1axConfig config, G1axErrorHandler errors) {
        this.config = config;
        this.errors = errors;
    }

    public boolean handleMissingSound(Identifier soundId, Throwable cause) {
        if (!config.fixMissingSound) return false;

        errors.report(ErrorCategory.MISSING_SOUND, soundId.toString(), "Sound event skipped — not found in any loaded resource pack. " + (cause != null ? cause.getMessage() : "null entry"));
        return true;
    }

    public boolean handleCorruptOgg(String path, Throwable cause) {
        if (!config.fixMissingSound) return false;

        errors.report(ErrorCategory.MISSING_SOUND, path, "Corrupt OGG skipped: " + cause.getMessage());
        return true;
    }
}

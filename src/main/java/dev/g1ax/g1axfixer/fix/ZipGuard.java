package dev.g1ax.g1axfixer.fix;

import dev.g1ax.g1axfixer.config.G1axConfig;
import dev.g1ax.g1axfixer.error.ErrorCategory;
import dev.g1ax.g1axfixer.error.G1axErrorHandler;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.zip.ZipException;

public class ZipGuard {

    private static final byte[] EMPTY_BYTES = new byte[0];

    private final G1axConfig config;
    private final G1axErrorHandler errors;

    public ZipGuard(G1axConfig config, G1axErrorHandler errors) {
        this.config = config;
        this.errors = errors;
    }

    public InputStream safeOpen(String packName, String entryPath, ZipException cause) {
        if (!config.fixCorruptZip) throw new RuntimeException(cause);

        errors.report(ErrorCategory.CORRUPT_ZIP, packName + "!" + entryPath, "Returned empty stream instead of crashing on ZipException: " + cause.getMessage());

        return new ByteArrayInputStream(EMPTY_BYTES);
    }

    public boolean shouldSkipPack(String packName, Exception cause) {
        if (!config.fixCorruptZip) return false;

        errors.report(ErrorCategory.CORRUPT_ZIP, packName, "Pack skipped — archive is entirely unreadable: " + cause.getMessage());
        return true;
    }
}

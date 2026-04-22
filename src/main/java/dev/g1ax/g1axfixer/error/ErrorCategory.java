package dev.g1ax.g1axfixer.error;

public enum ErrorCategory {
    CORRUPT_ZIP("Corrupt ZIP / resource pack archive"),
    ATLAS_OVERFLOW("Sprite atlas overflow / too-large texture"),
    MISSING_SPRITE("Missing or unresolvable sprite / texture"),
    MISSING_SOUND("Missing sound event or OGG file"),
    BROKEN_MODEL("Malformed or unresolvable block/item model"),
    LANGUAGE_JSON("Malformed language translation JSON"),
    PACK_METADATA("Invalid or missing pack.mcmeta"),
    RELOAD_TIMEOUT("Resource reload exceeded timeout threshold"),
    UNKNOWN("Uncategorized resource pack error");

    public final String description;
    ErrorCategory(String desc) { this.description = desc; }
}

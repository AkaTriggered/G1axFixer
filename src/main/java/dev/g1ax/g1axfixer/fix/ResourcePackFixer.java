package dev.g1ax.g1axfixer.fix;

import dev.g1ax.g1axfixer.config.G1axConfig;
import dev.g1ax.g1axfixer.error.G1axCrashReport;
import dev.g1ax.g1axfixer.error.G1axErrorHandler;
import dev.g1ax.g1axfixer.optimization.*;
import dev.g1ax.g1axfixer.G1axFixer;

public class ResourcePackFixer {

    private final G1axConfig config;
    private final G1axErrorHandler errors;

    public final ZipGuard zipGuard;
    public final SpriteValidator spriteValidator;
    public final AtlasStitchFixer atlasStitchFixer;
    public final SoundEventFixer soundEventFixer;
    public final ModelLoadFixer modelLoadFixer;
    public final LanguageFileFixer languageFileFixer;
    public final MetadataSanitizer metadataSanitizer;
    public final NetworkOptimizer networkOptimizer;
    public final RenderOptimizer renderOptimizer;
    public final TickOptimizer tickOptimizer;
    public final ChunkRebuildThrottler chunkRebuildThrottler;
    public final MemoryOptimizer memoryOptimizer;
    public final ItemRenderCache itemRenderCache;
    public final LightUpdateBatcher lightUpdateBatcher;

    public ResourcePackFixer(G1axConfig config, G1axErrorHandler errors) {
        this.config = config;
        this.errors = errors;

        zipGuard = new ZipGuard(config, errors);
        spriteValidator = new SpriteValidator(config, errors);
        atlasStitchFixer = new AtlasStitchFixer(config, errors);
        soundEventFixer = new SoundEventFixer(config, errors);
        modelLoadFixer = new ModelLoadFixer(config, errors);
        languageFileFixer = new LanguageFileFixer(config, errors);
        metadataSanitizer = new MetadataSanitizer(config, errors);
        networkOptimizer = new NetworkOptimizer(config);
        renderOptimizer = new RenderOptimizer(config);
        tickOptimizer = new TickOptimizer(config);
        chunkRebuildThrottler = new ChunkRebuildThrottler(config);
        memoryOptimizer = new MemoryOptimizer(config);
        itemRenderCache = new ItemRenderCache(config);
        lightUpdateBatcher = new LightUpdateBatcher(config);
    }

    public void register() {
        G1axCrashReport.register();

        G1axFixer.LOGGER.info("[G1axFixer] Fix modules registered:");
        G1axFixer.LOGGER.info("[G1axFixer]   ZipGuard          → {}", config.fixCorruptZip ? "ON" : "OFF");
        G1axFixer.LOGGER.info("[G1axFixer]   SpriteValidator   → {}", config.fixMissingSprite ? "ON" : "OFF");
        G1axFixer.LOGGER.info("[G1axFixer]   AtlasStitchFixer  → {}", config.fixAtlasStitch ? "ON" : "OFF");
        G1axFixer.LOGGER.info("[G1axFixer]   SoundEventFixer   → {}", config.fixMissingSound ? "ON" : "OFF");
        G1axFixer.LOGGER.info("[G1axFixer]   ModelLoadFixer    → {}", config.fixBrokenModel ? "ON" : "OFF");
        G1axFixer.LOGGER.info("[G1axFixer]   LanguageFileFixer → {}", config.fixLanguageJson ? "ON" : "OFF");
        G1axFixer.LOGGER.info("[G1axFixer]   MetadataSanitizer → {}", config.fixPackMetadata ? "ON" : "OFF");

        networkOptimizer.register();
        renderOptimizer.register();
        tickOptimizer.register();
        chunkRebuildThrottler.register();
        memoryOptimizer.register();
        itemRenderCache.register();
        lightUpdateBatcher.register();
    }
}

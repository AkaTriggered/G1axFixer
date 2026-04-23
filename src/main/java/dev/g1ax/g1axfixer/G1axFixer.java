package dev.g1ax.g1axfixer;

import dev.g1ax.g1axfixer.config.G1axConfig;
import dev.g1ax.g1axfixer.error.G1axErrorHandler;
import dev.g1ax.g1axfixer.fix.ResourcePackFixer;
import dev.g1ax.g1axfixer.stats.G1axStatsTracker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class G1axFixer implements ClientModInitializer {

    public static final String MOD_ID = "g1axfixer";
    public static final String MOD_NAME = "G1axFixer";
    public static final String VERSION = "1.1.0";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    private static G1axFixer INSTANCE;
    public static G1axFixer getInstance() { return INSTANCE; }

    private G1axConfig config;
    private G1axErrorHandler errorHandler;
    private ResourcePackFixer resourcePackFixer;

    @Override
    public void onInitializeClient() {
        INSTANCE = this;

        LOGGER.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        LOGGER.info("  {} v{} initializing...", MOD_NAME, VERSION);
        LOGGER.info("  Authors : AkaTriggered, G1ax");
        LOGGER.info("  Discord : https://discord.gg/XPYnrMd39C");
        LOGGER.info("  GitHub  : https://github.com/AkaTriggered");
        LOGGER.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        try {
            config = G1axConfig.load();
            LOGGER.info("[{}] Config loaded — {} fixes/optimizations enabled.", MOD_NAME, config.countEnabledFixes());

            errorHandler = new G1axErrorHandler();

            resourcePackFixer = new ResourcePackFixer(config, errorHandler);
            resourcePackFixer.register();

            G1axStatsTracker.get().reset();

            LOGGER.info("[{}] All systems active. {} modules loaded.", MOD_NAME, config.countEnabledFixes());
        } catch (Throwable t) {
            LOGGER.error("[{}] FATAL: Initialization failed!", MOD_NAME, t);
            throw new RuntimeException("G1axFixer failed to initialize", t);
        }
    }

    public G1axConfig getConfig() { return config; }
    public G1axErrorHandler getErrorHandler() { return errorHandler; }
    public ResourcePackFixer getResourcePackFixer() { return resourcePackFixer; }
}

package dev.g1ax.g1axfixer.config;

import com.google.gson.*;
import dev.g1ax.g1axfixer.G1axFixer;
import net.fabricmc.loader.api.FabricLoader;
import java.io.*;
import java.nio.file.*;

public class G1axConfig {

    public boolean fixCorruptZip = true;
    public boolean fixAtlasStitch = true;
    public boolean fixMissingSprite = true;
    public boolean fixMissingSound = true;
    public boolean fixBrokenModel = true;
    public boolean fixLanguageJson = true;
    public boolean fixPackMetadata = true;
    public boolean fixReloadHang = true;
    public boolean logFixedIssues = true;
    public boolean logDebug = false;
    public int reloadTimeoutSeconds = 30;

    public boolean optimizeNetwork = true;
    public boolean batchNetworkPackets = true;
    public int packetBatchSize = 10;
    public boolean compressLargePackets = true;
    public int compressionThreshold = 256;

    public boolean optimizeRendering = true;
    public boolean cullDistantParticles = true;
    public int particleCullDistance = 64;
    public boolean optimizeEntityRender = true;
    public boolean reduceParticleCount = true;

    public boolean optimizeTicking = true;
    public boolean optimizeEntityTick = true;
    public boolean optimizeBlockEntityTick = true;
    public int entityTickRange = 128;

    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("g1axfixer.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static G1axConfig load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader r = Files.newBufferedReader(CONFIG_PATH)) {
                G1axConfig cfg = GSON.fromJson(r, G1axConfig.class);
                G1axFixer.LOGGER.info("[G1axFixer] Config loaded from disk.");
                return cfg != null ? cfg : defaults();
            } catch (Exception e) {
                G1axFixer.LOGGER.warn("[G1axFixer] Config read failed ({}), using defaults.", e.getMessage());
            }
        }
        G1axConfig cfg = defaults();
        cfg.save();
        return cfg;
    }

    public void save() {
        try (Writer w = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(this, w);
        } catch (Exception e) {
            G1axFixer.LOGGER.warn("[G1axFixer] Config save failed: {}", e.getMessage());
        }
    }

    public int countEnabledFixes() {
        int count = 0;
        if (fixCorruptZip) count++;
        if (fixAtlasStitch) count++;
        if (fixMissingSprite) count++;
        if (fixMissingSound) count++;
        if (fixBrokenModel) count++;
        if (fixLanguageJson) count++;
        if (fixPackMetadata) count++;
        if (fixReloadHang) count++;
        if (optimizeNetwork) count++;
        if (optimizeRendering) count++;
        if (optimizeTicking) count++;
        return count;
    }

    private static G1axConfig defaults() { return new G1axConfig(); }
}

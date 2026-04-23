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
    public boolean deduplicatePackets = true;
    public int packetDedupeWindowMs = 50;
    public boolean throttlePositionPackets = true;
    public int positionPacketMinIntervalMs = 25;
    public boolean optimizeChunkPackets = true;
    public boolean skipEmptyChunkSections = true;
    public boolean prioritizePackets = true;

    public boolean optimizeRendering = true;
    public boolean cullDistantParticles = true;
    public int particleCullDistance = 64;
    public boolean optimizeEntityRender = true;
    public boolean reduceParticleCount = true;
    public int maxParticlesPerTick = 1000;
    public int entityRenderDistanceSq = 16384;
    public boolean skipHiddenEntityRender = true;
    public boolean lodEntityRendering = true;
    public int lodNearDistance = 32;
    public int lodFarDistance = 96;

    public boolean optimizeTicking = true;
    public boolean optimizeEntityTick = true;
    public boolean optimizeBlockEntityTick = true;
    public int entityTickRange = 128;
    public boolean throttleItemEntityTick = true;
    public int itemEntityTickRate = 4;
    public boolean throttleArmorStandTick = true;
    public boolean throttlePathfinding = true;
    public int pathfindingThrottleDistance = 64;
    public boolean optimizeWeatherTick = true;
    public boolean reduceRedstoneUpdates = true;

    public boolean optimizeChunkRebuilds = true;
    public int maxChunkRebuildsPerFrame = 4;

    public boolean optimizeMemory = true;
    public double memoryPressureThreshold = 15.0;

    public boolean optimizeItemRendering = true;
    public boolean batchLightUpdates = true;

    public boolean optimizeBlockEntityRendering = true;
    public int blockEntityRenderDistance = 64;

    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("g1axfixer.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static G1axConfig load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader r = Files.newBufferedReader(CONFIG_PATH)) {
                G1axConfig cfg = GSON.fromJson(r, G1axConfig.class);
                G1axFixer.LOGGER.info("[G1axFixer] Config loaded from disk.");
                if (cfg != null) {
                    cfg.save();
                    return cfg;
                }
                return defaults();
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
        int c = 0;
        if (fixCorruptZip) c++;
        if (fixAtlasStitch) c++;
        if (fixMissingSprite) c++;
        if (fixMissingSound) c++;
        if (fixBrokenModel) c++;
        if (fixLanguageJson) c++;
        if (fixPackMetadata) c++;
        if (fixReloadHang) c++;
        if (optimizeNetwork) c++;
        if (optimizeRendering) c++;
        if (optimizeTicking) c++;
        if (optimizeChunkRebuilds) c++;
        if (optimizeMemory) c++;
        if (optimizeItemRendering) c++;
        if (batchLightUpdates) c++;
        if (deduplicatePackets) c++;
        if (throttlePositionPackets) c++;
        if (optimizeBlockEntityRendering) c++;
        return c;
    }

    private static G1axConfig defaults() { return new G1axConfig(); }
}

package dev.g1ax.g1axfixer.compat;

import dev.g1ax.g1axfixer.G1axFixer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import java.util.*;

public class CompatibilityChecker {

    private static final Map<String, String> INCOMPATIBLE_MODS = Map.of(
        "optifabric", "OptiFabric conflicts with G1axFixer resource pack fixes",
        "oldoptifine", "OptiFine is incompatible with G1axFixer"
    );

    private static final Map<String, String> REQUIRED_MODS = Map.of(
        "fabric-api", "Fabric API is required for G1axFixer to function"
    );

    public static void checkCompatibility() {
        List<String> errors = new ArrayList<>();

        for (Map.Entry<String, String> entry : REQUIRED_MODS.entrySet()) {
            if (!FabricLoader.getInstance().isModLoaded(entry.getKey())) {
                errors.add("MISSING: " + entry.getValue());
            }
        }

        for (Map.Entry<String, String> entry : INCOMPATIBLE_MODS.entrySet()) {
            if (FabricLoader.getInstance().isModLoaded(entry.getKey())) {
                errors.add("INCOMPATIBLE: " + entry.getValue());
            }
        }

        if (!errors.isEmpty()) {
            G1axFixer.LOGGER.error("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            G1axFixer.LOGGER.error("  G1axFixer Compatibility Issues Detected!");
            errors.forEach(e -> G1axFixer.LOGGER.error("  • {}", e));
            G1axFixer.LOGGER.error("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            throw new RuntimeException("G1axFixer: Incompatible mods detected. See log for details.");
        }

        G1axFixer.LOGGER.info("[G1axFixer] Compatibility check passed.");
    }

    public static String getModVersion(String modId) {
        return FabricLoader.getInstance()
            .getModContainer(modId)
            .map(ModContainer::getMetadata)
            .map(m -> m.getVersion().getFriendlyString())
            .orElse("unknown");
    }
}

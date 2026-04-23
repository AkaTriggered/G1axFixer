package dev.g1ax.g1axfixer.compat;

import dev.g1ax.g1axfixer.G1axFixer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class EarlyCompatCheck implements PreLaunchEntrypoint {

    @Override
    public void onPreLaunch() {
        try {
            CompatibilityChecker.checkCompatibility();
        } catch (Exception e) {
            G1axFixer.LOGGER.error("[G1axFixer] Pre-launch compatibility check failed!", e);
            throw e;
        }
    }
}

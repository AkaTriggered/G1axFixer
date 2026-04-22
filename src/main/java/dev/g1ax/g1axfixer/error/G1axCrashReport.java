package dev.g1ax.g1axfixer.error;

import dev.g1ax.g1axfixer.G1axFixer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public class G1axCrashReport {

    public static void register() {
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            G1axFixer.getInstance().getErrorHandler().printSummary();
        });
    }
}

package dev.g1ax.g1axfixer.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.g1ax.g1axfixer.G1axFixer;
import dev.g1ax.g1axfixer.error.SafetyWrapper;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import java.util.ArrayList;
import java.util.List;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    private final List<Packet<?>> packetBatch = new ArrayList<>();
    private long lastFlush = System.currentTimeMillis();

    @WrapOperation(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;sendImmediately(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;Z)V"))
    private void g1ax$batchPackets(ClientConnection instance, Packet<?> packet, Object callbacks, boolean flush, Operation<Void> original) {
        SafetyWrapper.wrapSafeVoid(() -> {
            if (!G1axFixer.getInstance().getResourcePackFixer().networkOptimizer.shouldBatchPackets()) {
                original.call(instance, packet, callbacks, flush);
                return;
            }

            packetBatch.add(packet);
            
            if (packetBatch.size() >= G1axFixer.getInstance().getResourcePackFixer().networkOptimizer.getPacketBatchSize() || 
                System.currentTimeMillis() - lastFlush > 50) {
                for (Packet<?> p : packetBatch) {
                    original.call(instance, p, callbacks, flush);
                }
                packetBatch.clear();
                lastFlush = System.currentTimeMillis();
            }
        }, "ClientConnection.batchPackets");
    }
}

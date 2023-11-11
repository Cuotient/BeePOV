package com.cuotient.pobee;

import com.cuotient.pobee.packet.CameraBeeIdS2CPacket;
import com.cuotient.pobee.packet.ToggleCameraBeeC2SPacket;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.listener.ClientPlayPacketListener;

public class ClientBeeManager {
    public static ClientBeeManager INSTANCE = new ClientBeeManager();

    private CameraBeeEntity clientBee;
    private int clientBeeID = -1;

    public void onBeeBindingPress () {
        ClientSidePacketRegistry.INSTANCE.sendToServer(new ToggleCameraBeeC2SPacket(this.clientBee == null));
    }

    public void onSpawnCameraBee (CameraBeeIdS2CPacket packet, ClientPlayPacketListener listener) {
        this.clientBeeID = packet.getCameraBeeEntityID();

        if (this.clientBeeID == -1) {
            this.clientBee = null;
            return;
        }
    }

    // This is pretty janky. Ideally we would just wait for the packet that spawns the entity on the client, but I can't figure out which one that is.
    public void onTick () {
        ClientWorld cWorld = MinecraftClient.getInstance().world;

        if (cWorld != null) {
            if (this.clientBeeID != -1) {
                this.clientBee = (CameraBeeEntity) cWorld.getEntityById(this.clientBeeID);
            }
        }
    }
}

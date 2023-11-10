package com.cuotient.pobee;

import com.cuotient.pobee.packet.CameraBeeIdS2CPacket;
import com.cuotient.pobee.packet.ToggleCameraBeeC2SPacket;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.listener.ClientPlayPacketListener;

public class ClientBeeManager {
    public static ClientBeeManager INSTANCE = new ClientBeeManager();

    private CameraBeeEntity clientBee;

    public void onBeeBindingPress () {
        ClientSidePacketRegistry.INSTANCE.sendToServer(new ToggleCameraBeeC2SPacket(this.clientBee == null));
    }

    public void onSpawnCameraBee (CameraBeeIdS2CPacket packet, ClientPlayPacketListener listener) {
        // TODO: We might get the CameraBeeId packet before we get the EntitySpawn packet, if so we have to wait to set clientBee until we get the EntitySpawn packet
        POBee.LOGGER.info("\n\n\nWE GOT THA BEE");
        POBee.LOGGER.info(packet.getCameraBeeEntityID() + "\n\n\n");

        if (packet.getCameraBeeEntityID() != -1) {
            POBee.LOGGER.info(MinecraftClient.getInstance().world.getEntityById(packet.getCameraBeeEntityID()));
        }
    }
}

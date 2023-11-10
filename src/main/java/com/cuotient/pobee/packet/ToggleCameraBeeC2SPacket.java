package com.cuotient.pobee.packet;

import com.cuotient.pobee.ServerBeeManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ServerPlayPacketListener;

import java.io.IOException;

public class ToggleCameraBeeC2SPacket implements Packet<ServerPlayPacketListener> {
    private boolean enablePOBee;

    public ToggleCameraBeeC2SPacket(boolean enablePOBee) {
        this.enablePOBee = enablePOBee;
    }

    @Override
    public void read(PacketByteBuf buf) throws IOException {
        this.enablePOBee = buf.readBoolean();
    }

    @Override
    public void write(PacketByteBuf buf) throws IOException {
        buf.writeBoolean(this.enablePOBee);
    }

    @Override
    public void apply(ServerPlayPacketListener listener) {
        ServerBeeManager.INSTANCE.onToggleCameraBee(this, listener);
    }

    public boolean isEnablePOBee() {
        return enablePOBee;
    }

    // Might make code slightly easier to read.
    public boolean isDisablePOBee () {
        return !enablePOBee;
    }
}

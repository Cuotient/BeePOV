package com.cuotient.pobee.packet;

import com.cuotient.pobee.ClientBeeManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;

import java.io.IOException;

public class CameraBeeIdS2CPacket implements Packet<ClientPlayPacketListener> {
    private int cameraBeeEntityID;

    public CameraBeeIdS2CPacket(int cameraBeeEntityID) {
        this.cameraBeeEntityID = cameraBeeEntityID;
    }

    @Override
    public void read(PacketByteBuf buf) throws IOException {
        this.cameraBeeEntityID = buf.readInt();
    }

    @Override
    public void write(PacketByteBuf buf) throws IOException {
        buf.writeInt(cameraBeeEntityID);
    }

    @Override
    public void apply(ClientPlayPacketListener listener) {
        ClientBeeManager.INSTANCE.onSpawnCameraBee(this, listener);
    }

    public int getCameraBeeEntityID() {
        return cameraBeeEntityID;
    }
}

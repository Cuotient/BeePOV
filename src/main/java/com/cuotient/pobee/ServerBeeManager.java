package com.cuotient.pobee;

import com.cuotient.pobee.mixin.ServerPlayNetworkHandlerAccessor;
import com.cuotient.pobee.packet.CameraBeeIdS2CPacket;
import com.cuotient.pobee.packet.ToggleCameraBeeC2SPacket;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ServerBeeManager {
    public static ServerBeeManager INSTANCE = new ServerBeeManager();

    // TODO: I just realized this is NO GOOD on multiplayer servers
    //     Replace this with a map from players to bees. When a given player leaves, kill their bee.
    private CameraBeeEntity serverBee;

    public void onToggleCameraBee (ToggleCameraBeeC2SPacket packet, ServerPlayPacketListener listener) {
        ServerPlayNetworkHandlerAccessor handler = (ServerPlayNetworkHandlerAccessor) listener;

        // Don't do anything if the user sent multiple packets before the server did its thing
        if (packet.isEnablePOBee() && this.serverBee != null) {
            ServerSidePacketRegistry.INSTANCE.sendToPlayer(handler.getPlayer(), new CameraBeeIdS2CPacket(this.serverBee.getEntityId()));
            return;
        }

        // Always disable the bee if the client requests it
        if (packet.isDisablePOBee()) {
            ServerSidePacketRegistry.INSTANCE.sendToPlayer(handler.getPlayer(), new CameraBeeIdS2CPacket(-1));
            this.killBee();
            return;
        }

        ServerPlayerEntity player = handler.getPlayer();
        World world = player.world;

        System.out.println("Server: " + player);
        this.serverBee = new CameraBeeEntity(CameraBeeEntity.CAMERA_BEE, world, player);

        int id = world.spawnEntity(this.serverBee) ? this.serverBee.getEntityId() : -1;

        if (id != -1) {
            world.playSound(null, new BlockPos(player.getPos()), SoundEvents.BLOCK_BEEHIVE_EXIT, SoundCategory.BLOCKS, 1.0F, 1.0F);

            this.serverBee.attachLeash(player, true);
        }

        ServerSidePacketRegistry.INSTANCE.sendToPlayer(handler.getPlayer(), new CameraBeeIdS2CPacket(id));
    }

    private void killBee () {
        if (this.serverBee != null) {
            this.serverBee.kill();
        }

        this.serverBee = null;
    }

    public void onServerTick(MinecraftServer server, CallbackInfo ci) {
        if (server.isRunning()) {
            this.killBee();
        }
    }
}

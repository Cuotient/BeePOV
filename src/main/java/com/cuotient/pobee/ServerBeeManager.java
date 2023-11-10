package com.cuotient.pobee;

import com.cuotient.pobee.mixin.ServerPlayNetworkHandlerAccessor;
import com.cuotient.pobee.packet.CameraBeeIdS2CPacket;
import com.cuotient.pobee.packet.ToggleCameraBeeC2SPacket;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.minecraft.entity.EntityType.BEE;

// TODO: Handle when player disconnects
// TODO: Handle when world closes (reset both managers if world closes)
public class ServerBeeManager {
    public static ServerBeeManager INSTANCE = new ServerBeeManager();

    private CameraBeeEntity serverBee;

    public void onToggleCameraBee (ToggleCameraBeeC2SPacket packet, ServerPlayPacketListener listener) {
        // Don't do anything if the user sent multiple packets before the server did its thing
//        if (packet.isEnablePOBee() == (serverBee != null)) {
//            return;
//        }

        ServerPlayNetworkHandlerAccessor handler = (ServerPlayNetworkHandlerAccessor) listener;
        ServerPlayerEntity player = handler.getPlayer();
        MinecraftServer server = handler.getServer();
        World world = player.world;

        // TODO: Temp
        if (serverBee != null) {
            serverBee.remove();
            serverBee = null;
        }

        serverBee = new CameraBeeEntity(BEE, world, player);

        int id = world.spawnEntity(serverBee) ? serverBee.getEntityId() : -1;

        if (id != -1) {
            world.playSound(null, new BlockPos(player.getPos()), SoundEvents.BLOCK_BEEHIVE_EXIT, SoundCategory.BLOCKS, 1.0F, 1.0F);
            CompoundTag compoundTag = serverBee.toTag(new CompoundTag());
            POBee.LOGGER.info("\n" + compoundTag.toText().getString());
        }

        ServerSidePacketRegistry.INSTANCE.sendToPlayer(handler.getPlayer(), new CameraBeeIdS2CPacket(id));
    }
}

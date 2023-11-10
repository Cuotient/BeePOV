package com.cuotient.pobee;

import com.cuotient.pobee.mixin.ServerPlayNetworkHandlerAccessor;
import com.cuotient.pobee.packet.CameraBeeIdS2CPacket;
import com.cuotient.pobee.packet.ToggleCameraBeeC2SPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

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

/*
{
    NoGravity: 1b,
    Brain: {
        memories: {}
    },
    HurtByTimestamp: 0,
    FlowerPos: {X: -40, Y: 64, Z: -86},
    HasStung: 0b,
    Attributes: [{Base: 0.30000001192092896d, Name: "minecraft:generic.movement_speed"}, {Base: 48.0d, Modifiers: [{Amount: 0.02187015365926644d, Operation: 1, UUID: [I; 454557446, 924601554, -2132076394, 164482903], Name: "Random spawn bonus"}], Name: "minecraft:generic.follow_range"}],
    Invulnerable: 0b,
    FallFlying: 0b,
    ForcedAge: 0,
    PortalCooldown: 0,
    AbsorptionAmount: 0.0f,
    FallDistance: 0.0f,
    InLove: 0,
    DeathTime: 0s,
    HandDropChances: [0.085f, 0.085f],
    CannotEnterHiveTicks: 0,
    PersistenceRequired: 0b,
    Age: 0,
    TicksSincePollination: 55,
    AngerTime: 0,
    Motion: [-0.002600345995534812d, 4.120076963644869E-4d, 0.00269581580829757d],
    Health: 10.0f,
    HasNectar: 0b,
    LeftHanded: 0b,
    Air: 300s,
    OnGround: 0b,
    Rotation: [43.96808f, 348.6516f],
    HandItems: [{}, {}],
    ArmorDropChances: [0.085f, 0.085f, 0.085f, 0.085f],
    Fire: -1s,
    ArmorItems: [{}, {}, {}, {}],
    CropsGrownSincePollination: 0,
    CanPickUpLoot: 0b,
    HurtTime: 0s
}

{
    Brain: {
        memories: {}
    },
    HurtByTimestamp: 0,
    HasStung: 0b,
    Attributes: [],
    Invulnerable: 0b,
    FallFlying: 0b,
    ForcedAge: 0,
    PortalCooldown: 0,
    AbsorptionAmount: 0.0f,
    FallDistance: 0.0f,
    InLove: 0,
    DeathTime: 0s,
    HandDropChances: [0.085f, 0.085f],
    CannotEnterHiveTicks: 0,
    PersistenceRequired: 0b,
    UUID: [I; -1206771072, 52579000, -1333001541, 219876594],
    Age: 0,
    TicksSincePollination: 0,
    AngerTime: 0,
    Motion: [0.0d, 0.0d, 0.0d],
    Health: 10.0f,
    HasNectar: 0b,
    LeftHanded: 0b,
    Air: 300s,
    OnGround: 0b,
    Rotation: [4.7397656f, 0.0f],
    HandItems: [{}, {}],
    ArmorDropChances: [0.085f, 0.085f, 0.085f, 0.085f],
    Pos: [-39.285510088334355d, 65.62000000476837d, -85.67897585363588d],
    Fire: -1s,
    ArmorItems: [{}, {}, {}, {}],
    CropsGrownSincePollination: 0,
    CanPickUpLoot: 0b,
    HurtTime: 0s
}
 */

        ServerPlayNetworkHandlerAccessor handler = (ServerPlayNetworkHandlerAccessor) listener;
        ServerPlayerEntity player = handler.getPlayer();
        MinecraftServer server = handler.getServer();
        World world = player.world;

        // TODO: Temp
        if (serverBee != null) {
            serverBee.remove();
            serverBee = null;
        }

        serverBee = new CameraBeeEntity(EntityType.BEE, world, player);
        serverBee.setPos(player.getX(), player.getY() + player.getEyeHeight(player.getPose()), player.getZ());

        int id = world.spawnEntity(serverBee) ? serverBee.getEntityId() : -1;

        if (id != -1) {
            world.playSound(null, new BlockPos(player.getPos()), SoundEvents.BLOCK_BEEHIVE_EXIT, SoundCategory.BLOCKS, 1.0F, 1.0F);
            CompoundTag compoundTag = serverBee.toTag(new CompoundTag());
            POBee.LOGGER.info("\n" + compoundTag.toText().getString());
        }

        ServerSidePacketRegistry.INSTANCE.sendToPlayer(handler.getPlayer(), new CameraBeeIdS2CPacket(id));
    }
}

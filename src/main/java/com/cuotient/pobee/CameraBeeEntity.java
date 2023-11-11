package com.cuotient.pobee;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

// TODO: Let /kill kill them
// TODO: Have the bee leashed to the player, if the lead breaks, tp the bee to the player
// TODO: Make the lead invisible (maybe)
// TODO: Kill on world close
// TODO: Make the bee act like the player is holding a flower (probably will have to make sure the bee doesn't get too close to the player)
// TODO: Spawn heart particles around the player when in bee cam (or just at the start of bee cam) (bee vision, the bee loves the player)
public class CameraBeeEntity extends BeeEntity {
    public static final EntityType<CameraBeeEntity> CAMERA_BEE;

    private final PlayerEntity trackingPlayer;

    // Constructor used by the server
    public CameraBeeEntity(EntityType<? extends CameraBeeEntity> entityType, World world, PlayerEntity trackingPlayer) {
        super(entityType, world);

        this.trackingPlayer = trackingPlayer;

        if (this.trackingPlayer != null) {
            this.setPos(this.trackingPlayer.getX(), this.trackingPlayer.getY() + this.trackingPlayer.getEyeHeight(this.trackingPlayer.getPose()), this.trackingPlayer.getZ());

            // I don't know why but if we don't do this the bee gets tp'd to 0 0 0
            Box boundingBox = this.getBoundingBox();
            this.setBoundingBox(
                    new Box(
                            boundingBox.maxX + this.trackingPlayer.getX(),
                            boundingBox.maxY + this.trackingPlayer.getY(),
                            boundingBox.maxZ + this.trackingPlayer.getZ(),
                            boundingBox.minX + this.trackingPlayer.getX(),
                            boundingBox.minY + this.trackingPlayer.getY(),
                            boundingBox.minZ + this.trackingPlayer.getZ()
                    )
            );
        }
    }

    // Constructor used by the client
    public CameraBeeEntity(EntityType<? extends CameraBeeEntity> entityType, World world) {
        super(entityType, world);

        if (MinecraftClient.getInstance().player == null) {
            // This shouldn't ever happen
            POBee.LOGGER.error("Error instantiating CameraBee on the client, player is null");
            this.trackingPlayer = null;
            this.kill(); // This means that the bee is somehow not removed from the world on exit
            return;
        }

        this.trackingPlayer = MinecraftClient.getInstance().player;

        // Client network handler handles positioning
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
//        return damageSource != DamageSource.OUT_OF_WORLD;
        return false;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
//        if (source == DamageSource.OUT_OF_WORLD) {
            return super.damage(source, amount);
//        }

//        return false;
    }

    // TODO: Handle tracking stuff

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return (EntityType)Registry.register(Registry.ENTITY_TYPE, (String)id, type.build(id));
    }

    static {
        CAMERA_BEE = register("camera_bee", EntityType.Builder.<CameraBeeEntity>create(CameraBeeEntity::new, SpawnGroup.MISC).setDimensions(0.7F, 0.6F).maxTrackingRange(8));
    }
}

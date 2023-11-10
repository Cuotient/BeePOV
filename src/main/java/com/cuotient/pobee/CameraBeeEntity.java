package com.cuotient.pobee;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

// TODO: Make immune to damage
public class CameraBeeEntity extends BeeEntity {
    private final PlayerEntity trackingPlayer;

    public CameraBeeEntity(EntityType<? extends BeeEntity> entityType, World world, PlayerEntity trackingPlayer) {
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

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return true;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

//        POBee.LOGGER.info("GOOD");

        if (this.getPos().x == 0) {
            POBee.LOGGER.info("NO GOOD");
        }
    }

    // TODO: Handle tracking stuff
}

package com.cuotient.pobee;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

// TODO: Make immune to damage
public class CameraBeeEntity extends BeeEntity {
    private final ServerPlayerEntity trackingPlayer;

    public CameraBeeEntity(EntityType<? extends BeeEntity> entityType, World world, ServerPlayerEntity trackingPlayer) {
        super(entityType, world);
        this.trackingPlayer = trackingPlayer;

        this.setPose(trackingPlayer.getPose());
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return true;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    // TODO: Handle tracking stuff
}

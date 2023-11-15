package com.cuotient.pobee;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class CameraBeeEntity extends BeeEntity {
    public static final EntityType<CameraBeeEntity> CAMERA_BEE;

    private final PlayerEntity trackingPlayer;

    // Constructor used by the server
    public CameraBeeEntity(EntityType<? extends CameraBeeEntity> entityType, World world, PlayerEntity trackingPlayer) {
        super(entityType, world);

        this.lookControl = new CameraBeeLookControl(this);

        this.trackingPlayer = trackingPlayer;

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
        return damageSource != DamageSource.OUT_OF_WORLD;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source == DamageSource.OUT_OF_WORLD) {
            return super.damage(source, amount);
        }

        return false;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new FollowPlayerGoal(this, 2));
        super.initGoals();
    }

//    @Override
//    protected EntityNavigation createNavigation(World world) {
//        BirdNavigation birdNavigation = new BirdNavigation(this, world) {
//            public boolean isValidPosition(BlockPos pos) {
//                return !this.world.getBlockState(pos.down()).isAir();
//            }
//
//            public void tick() {
//                super.tick();
//            }
//        };
//        birdNavigation.setCanPathThroughDoors(false);
//        birdNavigation.setCanSwim(true); // TODO
//        birdNavigation.setCanEnterOpenDoors(true);
//        return birdNavigation;
//    }


    public PlayerEntity getTrackingPlayer() {
        return trackingPlayer;
    }

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return (EntityType)Registry.register(Registry.ENTITY_TYPE, (String)id, type.build(id));
    }

    static {
        CAMERA_BEE = register("camera_bee", EntityType.Builder.<CameraBeeEntity>create(CameraBeeEntity::new, SpawnGroup.MISC).setDimensions(0.7F, 0.6F).maxTrackingRange(8));
    }

    public class FollowPlayerGoal extends Goal {
        private final CameraBeeEntity bee;
        private final double speed;

        private static final double SPEED = 2;

        public FollowPlayerGoal(CameraBeeEntity animal, double speed) {
            this.bee = animal;
            this.speed = speed;
        }

        public boolean canStart() {
            return this.bee.getTrackingPlayer() != null;
        }

        public boolean shouldContinue() {
            return true;
        }

        public void start() {
        }

        public void stop() {
        }

        public void tick() {
            this.bee.getLookControl().lookAt(this.bee.getTrackingPlayer(), (float)(this.bee.getBodyYawSpeed() + 20), (float)this.bee.getLookPitchSpeed());

            if (this.bee.squaredDistanceTo(this.bee.getTrackingPlayer()) < 10) {
                this.bee.getNavigation().stop();
            } else {
                this.bee.getNavigation().startMovingTo(this.bee.getTrackingPlayer(), SPEED);
            }
        }
    }

    class CameraBeeLookControl extends LookControl {
        CameraBeeLookControl(MobEntity entity) {
            super(entity);
        }

        public void tick() {
            if (this.active) {
                this.active = false;
                this.entity.headYaw = this.changeAngle(this.entity.headYaw, this.getTargetYaw(), 1000000);
                this.entity.pitch = this.changeAngle(this.entity.pitch, this.getTargetPitch(), 1000000);
            }
        }

        protected float changeAngle(float from, float to) {
            float f = MathHelper.subtractAngles(from, to);
            return from + f;
        }

        protected boolean shouldStayHorizontal() {
            return true; // TODO
        }
    }
}

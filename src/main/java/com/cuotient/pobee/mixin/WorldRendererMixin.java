package com.cuotient.pobee.mixin;

import com.cuotient.pobee.ClientBeeManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
 * SHAMELESSLY stolen from https://github.com/hashalite/Freecam/blob/main/src/main/java/net/xolt/freecam/mixins/WorldRendererMixin.java
 */

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow
    @Final
    private BufferBuilderStorage bufferBuilders;

    // Make sure the player is actually rendered
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;checkEmpty(Lnet/minecraft/client/util/math/MatrixStack;)V", ordinal = 0))
    private void onRender(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci) {
        if (ClientBeeManager.INSTANCE.isEnabled()) {
            Vec3d cameraPos = camera.getPos();
            renderEntity(MinecraftClient.getInstance().player, cameraPos.x, cameraPos.y, cameraPos.z, tickDelta, matrices, bufferBuilders.getEntityVertexConsumers());
        }
    }

    @Shadow
    private void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {}
}

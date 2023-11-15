package com.cuotient.pobee.mixin;

import com.cuotient.pobee.ClientBeeManager;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Inject(method = "getCameraPlayer", at = @At("HEAD"), cancellable = true)
    private void getCameraPlayer(CallbackInfoReturnable<PlayerEntity> cir) {
        PlayerEntity player = ClientBeeManager.INSTANCE.onGetCameraPlayer();
        if (player != null) cir.setReturnValue(player);
    }

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void renderCrosshair(MatrixStack matrixStack, CallbackInfo ci) {
        ClientBeeManager.INSTANCE.onRenderCrosshair(ci);
    }
}

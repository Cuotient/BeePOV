package com.cuotient.pobee.mixin;

import com.cuotient.pobee.ClientBeeManager;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Inject(method = "render", at = @At("HEAD"))
    private void renderPRE(boolean tick, CallbackInfo ci) {
        ClientBeeManager.INSTANCE.onClientRenderPRE(tick);
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void renderPOST(boolean tick, CallbackInfo ci) {
        ClientBeeManager.INSTANCE.onClientRenderPOST(tick);
    }
}

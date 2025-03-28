package com.avaris.towncrier.client.mixin;

import com.avaris.towncrier.client.api.v1.impl.ClientPlayerEvents;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class MissedAttackMixin {
    @Inject(method = "doAttack",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;resetLastAttackedTicks()V"))
    void onMissedDoAttack(CallbackInfoReturnable<Boolean> cir, @Local ItemStack stack){
        ClientPlayerEvents.PLAYER_DO_ATTACK_MISS_EVENT.invoker().onPlayerDoAttackMiss(((MinecraftClient) (Object)this).player,stack);
    }
}

package com.avaris.towncrier.mixin;

import com.avaris.towncrier.api.v1.impl.PlayerEvents;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "onDeath",at = @At("RETURN"))
    void onDeath(DamageSource damageSource, CallbackInfo ci){
        PlayerEvents.DEATH_EVENT.invoker().onDeath((ServerPlayerEntity)(Object)this,damageSource);
    }

    @Inject(method = "enterCombat", at = @At("RETURN"))
    void onEnterCombat(CallbackInfo ci){
        PlayerEvents.ENTER_COMBAT_EVENT.invoker().onEnterCombat((ServerPlayerEntity)(Object)this);
    }

    @Inject(method = "endCombat", at = @At("RETURN"))
    void onEndCombat(CallbackInfo ci){
        PlayerEvents.END_COMBAT_EVENT.invoker().onEndCombat((ServerPlayerEntity)(Object)this);
    }
}

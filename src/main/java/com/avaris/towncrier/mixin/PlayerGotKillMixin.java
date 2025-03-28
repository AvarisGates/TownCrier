package com.avaris.towncrier.mixin;


import com.avaris.towncrier.api.v1.impl.PlayerEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class PlayerGotKillMixin {
    @Inject(method = "onDeath", at = @At("RETURN"))
    void onDeath(DamageSource damageSource, CallbackInfo ci){
        if(damageSource.getSource() instanceof ServerPlayerEntity player){
            PlayerEvents.PLAYER_GOT_KILL_EVENT.invoker().onPlayerGotKill(player,(LivingEntity) (Object)(this));
        }
    }
}

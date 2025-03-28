package com.avaris.towncrier.client.api.v1.impl;

import com.avaris.towncrier.TownCrier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public class ClientPlayerEvents {
    /**
     * Triggered when a player swings their hand (weapon) and doesn't hit anything or simply misses.
     */
    public static final Event<PlayerDoAttackMiss> PLAYER_DO_ATTACK_MISS_EVENT = EventFactory.createArrayBacked(PlayerDoAttackMiss.class,(callbacks) -> (player, stack) ->{
        TownCrier.logEventCall(ClientPlayerEntity.class,"PLAYER_DO_ATTACK_MISS_EVENT");
        for(var callback : callbacks){
            callback.onPlayerDoAttackMiss(player,stack);
        }
    });

    @FunctionalInterface
    public interface PlayerDoAttackMiss{
        void onPlayerDoAttackMiss(ClientPlayerEntity player, ItemStack stack);
    }

}

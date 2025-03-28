package com.avaris.towncrier.api.v1.impl;

import com.avaris.towncrier.TownCrier;
import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.SocketAddress;

/**
 * Handles events related to the {@link ServerPlayerEntity}<br>
 *
 * Events referencing {@link net.minecraft.entity.player.PlayerEntity} (As opposed to {@link ServerPlayerEntity}) will be called on the common side, meaning both the server and the client.<br>
 * To check which logical side the event is fired on: access the player's world - {@link net.minecraft.entity.player.PlayerEntity#world} and use {@link net.minecraft.world.World#isClient()}
 */
public class PlayerEvents {
    /**
     * Called after a player joins the server.
     * Specifically at the end of {@link net.minecraft.server.PlayerManager#onPlayerConnect(ClientConnection, ServerPlayerEntity, ConnectedClientData)}, before {@link PlayerEvents#PLAYER_JOIN_EVENT}
     */
    public static final Event<PlayerJoin> PLAYER_JOIN_EVENT = EventFactory.createArrayBacked(PlayerJoin.class,(callbacks) -> (player) -> {
        TownCrier.logEventCall(PlayerEvents.class,"PLAYER_JOIN_EVENT");
        for(var callback : callbacks){
            callback.onPlayerJoin(player);
        }
    });

    /**
     * Extended player join event, calls {@link PlayerEvents#PLAYER_JOIN_EVENT}
     * Specifically at the end of {@link net.minecraft.server.PlayerManager#onPlayerConnect(ClientConnection, ServerPlayerEntity, ConnectedClientData)}, after {@link PlayerEvents#PLAYER_JOIN_EX_EVENT}
     */
    public static final Event<PlayerJoinEx> PLAYER_JOIN_EX_EVENT = EventFactory.createArrayBacked(PlayerJoinEx.class,(callbacks) -> (connection,player,clientData) -> {
        TownCrier.logEventCall(PlayerEvents.class,"PLAYER_JOIN_EX_EVENT");
        for(var callback : callbacks){
            callback.onPlayerJoin(connection,player,clientData);
        }
        PlayerEvents.PLAYER_JOIN_EVENT.invoker().onPlayerJoin(player);
    });

    /**
     * Called after a {@link ServerPlayerEntity} leaves the server.
     * Specifically at the end of {@link net.minecraft.server.PlayerManager#remove(ServerPlayerEntity)} 
     */
    public static final Event<PlayerLeave> PLAYER_LEAVE_EVENT = EventFactory.createArrayBacked(PlayerLeave.class,(callbacks) -> (player) ->{
        TownCrier.logEventCall(PlayerEvents.class,"PLAYER_LEAVE_EVENT");
        for(var callback : callbacks){
            callback.onPlayerLeave(player);
        }
    });

    /**
     * Called when the server authenticates a player.
     * Return {@code null} to allow the player to join. Or a {@link Text} reason for the player not to be allowed to join the server.
     */
    public static final Event<CanJoin> CAN_JOIN_EVENT = EventFactory.createArrayBacked(CanJoin.class,(callbacks) -> (address,profile) ->{
        TownCrier.logEventCall(PlayerEvents.class,"CAN_JOIN_EVENT");
        for(var callback : callbacks){
            Text ret = callback.onCanJoin(address,profile);
            if(ret != null){
                return ret;
            }
        }
        return null;
    });

    /**
     * Called when the {@link ServerCommonNetworkHandler} (server) sends a chat message packet to a client.
     */
    public static final Event<ServerSendChatMessage> SERVER_SEND_CHAT_MESSAGE_EVENT = EventFactory.createArrayBacked(ServerSendChatMessage.class,(callbacks) -> (server,connection, packet) -> {
        TownCrier.logEventCall(PlayerEvents.class,"SERVER_SEND_CHAT_MESSAGE_EVENT");
        for(var callback : callbacks){
            boolean ret = callback.onServerSendChatMessage(server,connection,packet);
            if(!ret){
                return false;
            }
        }
        return true;
    });

    /**
     * Called when a {@link ServerPlayerEntity} kills a {@link LivingEntity}<br>
     * Specifically at the end of the {@link LivingEntity#onDeath(DamageSource)} call.<br>
     * @see com.avaris.towncrier.mixin.PlayerGotKillMixin#onDeath(DamageSource, CallbackInfo)
     */
    public static final Event<PlayerGotKill> PLAYER_GOT_KILL_EVENT = EventFactory.createArrayBacked(PlayerGotKill.class,(callbacks) -> (player,entity) ->{
        TownCrier.logEventCall(PlayerEvents.class,"PLAYER_GOT_KILL_EVENT");
        for(var callback : callbacks){
            callback.onPlayerGotKill(player,entity);
        }
    });

    /**
     * Called after a {@link ServerPlayerEntity} is respawned.<br>
     * Specifically at the end of the {@link net.minecraft.server.PlayerManager#respawnPlayer(ServerPlayerEntity, boolean, Entity.RemovalReason)} call.<br>
     * The {@code player} is fully initialized.
     * @see com.avaris.towncrier.mixin.PlayerManagerMixin#onPlayerRespawn(ServerPlayerEntity, boolean, Entity.RemovalReason, CallbackInfoReturnable)
     */
    public static final Event<PlayerRespawned> PLAYER_RESPAWNED_EVENT = EventFactory.createArrayBacked(PlayerRespawned.class,(callbacks) -> (player, alive, removalReason) ->{
        TownCrier.logEventCall(PlayerEvents.class,"PLAYER_RESPAWNED_EVENT");
        for(var callback : callbacks){
            callback.onPlayerRespawned(player,alive,removalReason);
        }
    });

    /**
     * Called after a {@link ServerPlayerEntity} dies. <br>
     * Specifically at the end of the {@link ServerPlayerEntity#onDeath(DamageSource)} call.
     */
    public static final Event<Death> DEATH_EVENT = EventFactory.createArrayBacked(Death.class,(callbacks) -> (player, damageSource) -> {
        TownCrier.logEventCall(PlayerEvents.class,"DEATH_EVENT");
        for(var callback : callbacks){
            callback.onDeath(player, damageSource);
        }
    });

    /**
     * Called after a {@link ServerPlayerEntity} enters combat. <br>
     * Specifically at the end of the {@link ServerPlayerEntity#enterCombat()} call.
     */
    public static final Event<EnterCombat> ENTER_COMBAT_EVENT = EventFactory.createArrayBacked(EnterCombat.class,(callbacks) -> (player) -> {
        TownCrier.logEventCall(PlayerEvents.class,"ENTER_COMBAT_EVENT");
        for(var callback : callbacks){
            callback.onEnterCombat(player);
        }
    });

    /**
     * Called after a {@link ServerPlayerEntity} ends combat. <br>
     * Specifically at the end of the {@link ServerPlayerEntity#endCombat()} call.
     */
    public static final Event<EndCombat> END_COMBAT_EVENT = EventFactory.createArrayBacked(EndCombat.class,(callbacks) -> (player) -> {
        TownCrier.logEventCall(PlayerEvents.class,"END_COMBAT_EVENT");
        for(var callback : callbacks){
            callback.onEndCombat(player);
        }
    });

    @FunctionalInterface
    public interface PlayerJoin{
        void onPlayerJoin(@NotNull ServerPlayerEntity player);
    }

    @FunctionalInterface
    public interface PlayerJoinEx{
        void onPlayerJoin(@NotNull ClientConnection connection,@NotNull ServerPlayerEntity player, ConnectedClientData clientData);
    }

    @FunctionalInterface
    public interface PlayerLeave{
        void onPlayerLeave(@NotNull ServerPlayerEntity player);
    }

    @FunctionalInterface
    public interface CanJoin{
        Text onCanJoin(SocketAddress address, GameProfile profile);
    }

    @FunctionalInterface
    public interface ServerSendChatMessage {
       boolean onServerSendChatMessage(@NotNull MinecraftServer server,@NotNull ClientConnection connection, ChatMessageS2CPacket packet);
    }

    @FunctionalInterface
    public interface PlayerGotKill{
        void onPlayerGotKill(@NotNull ServerPlayerEntity player, LivingEntity entity);
    }

    @FunctionalInterface
    public interface PlayerRespawned{
        void onPlayerRespawned(@NotNull ServerPlayerEntity player, boolean alive, Entity.RemovalReason removalReason);
    }

    @FunctionalInterface
    public interface Death {
        void onDeath(@NotNull ServerPlayerEntity player, DamageSource damageSource);
    }

    @FunctionalInterface
    public interface EnterCombat {
        void onEnterCombat(@NotNull ServerPlayerEntity player);
    }

    @FunctionalInterface
    public interface EndCombat {
        void onEndCombat(@NotNull ServerPlayerEntity player);
    }
}

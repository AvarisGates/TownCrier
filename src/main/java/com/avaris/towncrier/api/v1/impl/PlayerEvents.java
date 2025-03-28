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
    public static final Event<PlayerJoin> PLAYER_JOIN_EVENT = TownCrier.createNewEvent(PlayerEvents.class,"PLAYER_JOIN_EVENT", PlayerJoin.class,(callbacks) -> (player) -> {
        for(var callback : callbacks){
            callback.onPlayerJoin(player);
        }
    });

    /**
     * Extended player join event, calls {@link PlayerEvents#PLAYER_JOIN_EVENT}
     * Specifically at the end of {@link net.minecraft.server.PlayerManager#onPlayerConnect(ClientConnection, ServerPlayerEntity, ConnectedClientData)}, after {@link PlayerEvents#PLAYER_JOIN_EVENT}
     */
    public static final Event<PlayerJoinEx> PLAYER_JOIN_EX_EVENT = TownCrier.createNewEvent(PlayerEvents.class,"PLAYER_JOIN_EX_EVENT",PlayerJoinEx.class,(callbacks) -> (connection,player,clientData) -> {
        for(var callback : callbacks){
            callback.onPlayerJoin(connection,player,clientData);
        }
        PlayerEvents.PLAYER_JOIN_EVENT.invoker().onPlayerJoin(player);
    });

    /**
     * Called after a {@link ServerPlayerEntity} leaves the server.
     * Specifically at the end of {@link net.minecraft.server.PlayerManager#remove(ServerPlayerEntity)} 
     */
    public static final Event<PlayerLeave> PLAYER_LEAVE_EVENT = TownCrier.createNewEvent(PlayerEvents.class,"PLAYER_LEAVE_EVENT",PlayerLeave.class,(callbacks) -> (player) ->{
        for(var callback : callbacks){
            callback.onPlayerLeave(player);
        }
    });

    /**
     * Called when the server authenticates a player.
     * Return {@code null} to allow the player to join. Or a {@link Text} reason for the player not to be allowed to join the server.
     */
    public static final Event<CanJoin> CAN_JOIN_EVENT = TownCrier.createNewEvent(PlayerEvents.class,"CAN_JOIN_EVENT",CanJoin.class,(callbacks) -> (address,profile) ->{
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
    public static final Event<ServerSendChatMessage> SERVER_SEND_CHAT_MESSAGE_EVENT = TownCrier.createNewEvent(PlayerEvents.class,"SERVER_SEND_CHAT_MESSAGE_EVENT",ServerSendChatMessage.class,(callbacks) -> (server,connection, packet) -> {
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
    public static final Event<PlayerGotKill> PLAYER_GOT_KILL_EVENT = TownCrier.createNewEvent(PlayerEvents.class,"PLAYER_GOT_KILL_EVENT",PlayerGotKill.class,(callbacks) -> (player,entity) ->{
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
    public static final Event<PlayerRespawned> PLAYER_RESPAWNED_EVENT = TownCrier.createNewEvent(PlayerEvents.class,"PLAYER_RESPAWNED_EVENT",PlayerRespawned.class,(callbacks) -> (player, alive, removalReason) ->{
        for(var callback : callbacks){
            callback.onPlayerRespawned(player,alive,removalReason);
        }
    });

    /**
     * Called after a {@link ServerPlayerEntity} dies. <br>
     * Specifically at the end of the {@link ServerPlayerEntity#onDeath(DamageSource)} call.
     */
    public static final Event<OnDeath> ON_DEATH_EVENT = TownCrier.createNewEvent(PlayerEvents.class,"ON_DEATH_EVENT",OnDeath.class,(callbacks) -> (damageSource) -> {
        for(var callback : callbacks){
            callback.onDeath(damageSource);
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
    public interface OnDeath{
        void onDeath(DamageSource damageSource);
    }
}

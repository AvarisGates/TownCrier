package com.avaris.towncrier.api.v1.impl;

import com.avaris.towncrier.TownCrier;
import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.net.SocketAddress;

public class PlayerEvents {
    /**
     * Called after a player joins the server.
     * (At the ond of {@link net.minecraft.server.PlayerManager#onPlayerConnect(ClientConnection, ServerPlayerEntity, ConnectedClientData)} )
     */
    public static final Event<PlayerJoin> PLAYER_JOIN_EVENT = EventFactory.createArrayBacked(PlayerJoin.class,(callbacks) -> (player) -> {
        TownCrier.logEventCall(PlayerEvents.class,"PLAYER_JOIN_EVENT");
        for(var callback : callbacks){
            callback.onPlayerJoin(player);
        }
    });

    /**
     * Extended player join event, calls {@link PlayerEvents#PLAYER_JOIN_EVENT}
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
     * Called when a {@link ServerPlayerEntity} kills a {@link LivingEntity}
     */
    public static final Event<PlayerGotKill> PLAYER_GOT_KILL_EVENT = EventFactory.createArrayBacked(PlayerGotKill.class,(callbacks) -> (player,entity) ->{
        TownCrier.logEventCall(PlayerEvents.class,"PLAYER_GOT_KILL_EVENT");
        for(var callback : callbacks){
            callback.onPlayerGotKill(player,entity);
        }
    });

    /**
     * Called after a {@link ServerPlayerEntity} is respawned.
     */
    public static final Event<PlayerRespawned> PLAYER_RESPAWNED_EVENT = EventFactory.createArrayBacked(PlayerRespawned.class,(callbacks) -> (player, alive, removalReason) ->{
        TownCrier.logEventCall(PlayerEvents.class,"PLAYER_RESPAWNED_EVENT");
        for(var callback : callbacks){
            callback.onPlayerRespawned(player,alive,removalReason);
        }
    });

    @FunctionalInterface
    public interface PlayerJoin{
        void onPlayerJoin(ServerPlayerEntity player);
    }

    @FunctionalInterface
    public interface PlayerJoinEx{
        void onPlayerJoin(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData);
    }

    @FunctionalInterface
    public interface PlayerLeave{
        void onPlayerLeave(ServerPlayerEntity player);
    }

    @FunctionalInterface
    public interface CanJoin{
        Text onCanJoin(SocketAddress address, GameProfile profile);
    }

    @FunctionalInterface
    public interface ServerSendChatMessage {
       boolean onServerSendChatMessage(MinecraftServer server, ClientConnection connection, ChatMessageS2CPacket packet);
    }

    @FunctionalInterface
    public interface PlayerGotKill{
        void onPlayerGotKill(ServerPlayerEntity player, LivingEntity entity);
    }

    @FunctionalInterface
    public interface PlayerRespawned{
        void onPlayerRespawned(ServerPlayerEntity player, boolean alive, Entity.RemovalReason removalReason);
    }
}

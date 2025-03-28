package com.avaris.towncrier.mixin;

import com.avaris.towncrier.api.v1.impl.PlayerEvents;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommonNetworkHandler.class)
public class ServerSendChatPacketMixin {
    @Shadow @Final protected ClientConnection connection;

    @Shadow @Final protected MinecraftServer server;

    @Inject(method = "send",at = @At("HEAD"),cancellable = true)
    void onSend(Packet<?> packet, @Nullable PacketCallbacks callbacks, CallbackInfo ci){
        if(packet instanceof ChatMessageS2CPacket chatMessageS2CPacket){
            if(!PlayerEvents.SERVER_SEND_CHAT_MESSAGE_EVENT.invoker().onServerSendChatMessage(this.server,this.connection,chatMessageS2CPacket)){
                ci.cancel();
            }
        }
    }
}

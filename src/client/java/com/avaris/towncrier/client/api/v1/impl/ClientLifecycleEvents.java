package com.avaris.towncrier.client.api.v1.impl;


import com.avaris.towncrier.TownCrier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.ClientPlayerEntity;

@Environment(EnvType.CLIENT)
public class ClientLifecycleEvents {
    public static final Event<Initialize> INITIALIZE_EVENT = TownCrier.createNewEvent(ClientLifecycleEvents.class,"INITIALIZE_EVENT",Initialize.class,(callbacks) -> () -> {
        for(var callback : callbacks){
            callback.onInitialize();
        }
    });

    public static final Event<Initialized> INITIALIZED_EVENT = TownCrier.createNewEvent(ClientLifecycleEvents.class,"INITIALIZED_EVENT",Initialized.class,(callbacks) -> () -> {
        for(var callback : callbacks){
            callback.onInitialized();
        }
    });

    @FunctionalInterface
    public interface Initialize {
        void onInitialize();
    }

    @FunctionalInterface
    public interface Initialized{
        void onInitialized();
    }
}

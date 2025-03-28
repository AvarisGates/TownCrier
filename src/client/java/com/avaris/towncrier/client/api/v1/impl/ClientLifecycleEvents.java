package com.avaris.towncrier.client.api.v1.impl;


import com.avaris.towncrier.TownCrier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@Environment(EnvType.CLIENT)
public class ClientLifecycleEvents {
    public static final Event<Initialize> INITIALIZE_EVENT = EventFactory.createArrayBacked(Initialize.class,(callbacks) -> () -> {
        TownCrier.logEventCall(ClientLifecycleEvents.class,"INITIALIZE_EVENT");
        for(var callback : callbacks){
            callback.onInitialize();
        }
    });

    public static final Event<Initialized> INITIALIZED_EVENT = EventFactory.createArrayBacked(Initialized.class,(callbacks) -> () -> {
        TownCrier.logEventCall(ClientLifecycleEvents.class,"INITIALIZED_EVENT");
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

package com.avaris.towncrier.api.v1.impl;


import com.avaris.towncrier.TownCrier;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * TownCrier lifecycle events ie. initialization.
 */
public class ModLifecycleEvents {
    public static final Event<Initialize> INITIALIZE_EVENT = TownCrier.createNewEvent(ModLifecycleEvents.class,"INITIALIZE_EVENT",Initialize.class,(callbacks) -> () -> {
        for(var callback : callbacks){
            callback.onInitialize();
        }
    });

    public static final Event<Initialized> INITIALIZED_EVENT = TownCrier.createNewEvent(ModLifecycleEvents.class,"INITIALIZED_EVENT",Initialized.class,(callbacks) -> () -> {
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

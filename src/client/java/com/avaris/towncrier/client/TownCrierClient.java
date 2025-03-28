package com.avaris.towncrier.client;

import com.avaris.towncrier.client.api.v1.impl.ClientLifecycleEvents;
import net.fabricmc.api.ClientModInitializer;

public class TownCrierClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientLifecycleEvents.INITIALIZE_EVENT.invoker().onInitialize();


        ClientLifecycleEvents.INITIALIZED_EVENT.invoker().onInitialized();
    }
}

package com.avaris.towncrier;

import com.avaris.towncrier.api.v1.impl.ModLifecycleEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * The common entrypoint for TownCrier
 * @see ModLifecycleEvents
 */
public class TownCrier implements ModInitializer {

    public static String MOD_ID = "towncrier";

    public static Logger LOGGER = LoggerFactory.getLogger(TownCrier.class);

    public static boolean shouldDisplayDebugInfo(){
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    public static void logEventCall(Class<?> clazz,String eventName){
        if(shouldDisplayDebugInfo()){
            String className = clazz.getName().substring(clazz.getName().lastIndexOf('.')+1);
            LOGGER.info("{}.{} called",className,eventName);
        }
    }

    @Override
    public void onInitialize() {
        ModLifecycleEvents.INITIALIZE_EVENT.invoker().onInitialize();

        ModLifecycleEvents.INITIALIZED_EVENT.invoker().onInitialized();
    }
}

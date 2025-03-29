package com.avaris.towncrier;

import com.avaris.towncrier.api.v1.impl.ModLifecycleEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The common entrypoint for TownCrier
 * @see com.avaris.towncrier.api.v1.impl.TownCrierApi
 */
public class TownCrier implements ModInitializer {

    public static String MOD_ID = "towncrier";

    public static Logger LOGGER = LoggerFactory.getLogger(TownCrier.class);

    private static boolean debugLogsEnabled = false;

    public static void enableDebugLogs(){
        debugLogsEnabled = true;
    }

    public static boolean shouldDisplayDebugInfo(){
        return FabricLoader.getInstance().isDevelopmentEnvironment() && debugLogsEnabled;
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

        // On the default debug environment there are 55 mods.
        // If there are 55 mods loaded we assume we are in the mod's debug environment.
        //debugLogsEnabled = FabricLoader.getInstance().getAllMods().size() == 55;

        ModLifecycleEvents.INITIALIZED_EVENT.invoker().onInitialized();
    }
}

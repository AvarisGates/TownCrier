package com.avaris.towncrier.api.v1.impl;

import com.avaris.towncrier.TownCrier;

/**
 * API interface for TownCrier.
 * @see PlayerEvents
 * @see ModLifecycleEvents
 */
public class TownCrierApi {
    /**
     * Enables debug logs, it's recommended to use only when debugging the mods itself.
     */
   public static void enableDebugLogs(){
       TownCrier.enableDebugLogs();
   }
}

package com.antonius.harmonics;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client-side initialization for Harmonics mod.
 * 
 * This class handles client-only setup like rendering initialization.
 * Armor rendering is handled automatically by Minecraft's EquipmentAssets
 * system when equipment JSON files and textures are properly configured.
 */
public class HarmonicsClient implements ClientModInitializer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger("harmonics");
    
    @Override
    public void onInitializeClient() {
        LOGGER.info("[Harmonics] Client-side initialization complete!");
    }
}

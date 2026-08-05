package com.antonius.harmonics;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client-side initialization for Harmonics (NeoForge).
 *
 * Armor rendering is handled automatically by Minecraft's EquipmentAssets
 * system when equipment JSON files and textures are properly configured.
 */
@Mod(value = Harmonics.MOD_ID, dist = Dist.CLIENT)
public class HarmonicsClient {

    private static final Logger LOGGER = LoggerFactory.getLogger("harmonics");

    public HarmonicsClient() {
        LOGGER.info("[Harmonics] Client-side initialization complete!");
    }
}

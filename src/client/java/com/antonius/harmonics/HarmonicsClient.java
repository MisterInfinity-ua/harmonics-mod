package com.antonius.harmonics;

import com.antonius.harmonics.item.InstrumentMaterial;
import com.antonius.harmonics.item.InstrumentType;
import com.antonius.harmonics.item.ModItems;
import com.antonius.harmonics.item.ModWeapons;
import com.antonius.harmonics.item.WeaponMaterial;
import com.antonius.harmonics.item.WeaponType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.world.item.CreativeModeTabs;

public class HarmonicsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Add every instrument and weapon into the vanilla "Tools & Utilities" creative tab.
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
            for (InstrumentMaterial material : InstrumentMaterial.values()) {
                for (InstrumentType type : InstrumentType.values()) {
                    entries.accept(ModItems.get(material, type));
                }
            }
            for (WeaponMaterial material : WeaponMaterial.values()) {
                for (WeaponType type : WeaponType.values()) {
                    entries.accept(ModWeapons.get(material, type));
                }
            }
        });
    }
}

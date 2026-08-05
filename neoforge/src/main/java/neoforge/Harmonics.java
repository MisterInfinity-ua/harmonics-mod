package com.antonius.harmonics;

import com.antonius.harmonics.armor.ModArmorItems;
import com.antonius.harmonics.block.ModBlocks;
import com.antonius.harmonics.item.ModItems;
import com.antonius.harmonics.item.ModWeapons;
import com.antonius.harmonics.item.WeaponMaterial;
import com.antonius.harmonics.item.WeaponType;
import com.antonius.harmonics.recipe.ModRecipes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Harmonics - Unified Mod (Weapons + Instruments + Armor) for NeoForge.
 *
 * Total: 130 items (36 weapons + 50 instruments + 44 armor)
 *
 * The item/recipe/armor registration classes are shared with the Fabric build and
 * register through vanilla Registry.register, so they are invoked here during the
 * NeoForge RegisterEvent phase (when the target registries are unfrozen).
 */
@Mod(Harmonics.MOD_ID)
public class Harmonics {
    public static final String MOD_ID = "harmonics";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final ResourceKey<CreativeModeTab> HARMONICS_TAB_KEY = ResourceKey.create(
        Registries.CREATIVE_MODE_TAB,
        Identifier.fromNamespaceAndPath(MOD_ID, "harmonics_tab")
    );

    public Harmonics(IEventBus modBus) {
        LOGGER.info("====================================");
        LOGGER.info("[Harmonics] Loading Unified Mod (NeoForge)...");
        LOGGER.info("====================================");

        modBus.addListener(this::registerContents);
    }

    private void registerContents(RegisterEvent event) {
        ResourceKey<?> registryKey = event.getRegistryKey();

        if (registryKey == Registries.DATA_COMPONENT_TYPE) {
            HarmonicsComponents.init();
        } else if (registryKey == Registries.RECIPE_SERIALIZER) {
            ModRecipes.init();
        } else if (registryKey == Registries.BLOCK) {
            ModBlocks.init();
        } else if (registryKey == Registries.ITEM) {
            ModItems.init();
            ModWeapons.init();
            LOGGER.info("[Harmonics] Loading Armor content...");
            ModArmorItems.registerArmorItems();
        } else if (registryKey == Registries.CREATIVE_MODE_TAB) {
            registerCreativeTab();
        }
    }

    private void registerCreativeTab() {
        CreativeModeTab tab = CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + MOD_ID))
            .icon(() -> new ItemStack(ModWeapons.get(WeaponMaterial.OBSIDIAN, WeaponType.SWORD)))
            .displayItems((parameters, output) -> {
                // Add weapons
                for (WeaponMaterial material : WeaponMaterial.values()) {
                    for (WeaponType type : WeaponType.values()) {
                        output.accept(ModWeapons.get(material, type));
                    }
                }

                // Add instruments
                ModItems.getAllItems().forEach(output::accept);

                // Add armor (24 total - Wood, Stone, Obsidian, Bone, Amethyst, Emerald)
                output.accept(ModArmorItems.WOODEN_HELMET);
                output.accept(ModArmorItems.WOODEN_CHESTPLATE);
                output.accept(ModArmorItems.WOODEN_LEGGINGS);
                output.accept(ModArmorItems.WOODEN_BOOTS);

                output.accept(ModArmorItems.STONE_HELMET);
                output.accept(ModArmorItems.STONE_CHESTPLATE);
                output.accept(ModArmorItems.STONE_LEGGINGS);
                output.accept(ModArmorItems.STONE_BOOTS);

                output.accept(ModArmorItems.OBSIDIAN_HELMET);
                output.accept(ModArmorItems.OBSIDIAN_CHESTPLATE);
                output.accept(ModArmorItems.OBSIDIAN_LEGGINGS);
                output.accept(ModArmorItems.OBSIDIAN_BOOTS);

                output.accept(ModArmorItems.BONE_HELMET);
                output.accept(ModArmorItems.BONE_CHESTPLATE);
                output.accept(ModArmorItems.BONE_LEGGINGS);
                output.accept(ModArmorItems.BONE_BOOTS);

                output.accept(ModArmorItems.AMETHYST_HELMET);
                output.accept(ModArmorItems.AMETHYST_CHESTPLATE);
                output.accept(ModArmorItems.AMETHYST_LEGGINGS);
                output.accept(ModArmorItems.AMETHYST_BOOTS);

                output.accept(ModArmorItems.EMERALD_HELMET);
                output.accept(ModArmorItems.EMERALD_CHESTPLATE);
                output.accept(ModArmorItems.EMERALD_LEGGINGS);
                output.accept(ModArmorItems.EMERALD_BOOTS);

                // Crystal Armor (v1.5)
                output.accept(ModArmorItems.CRYSTAL_HELMET);
                output.accept(ModArmorItems.CRYSTAL_CHESTPLATE);
                output.accept(ModArmorItems.CRYSTAL_LEGGINGS);
                output.accept(ModArmorItems.CRYSTAL_BOOTS);

                // Shadow Armor (v1.5)
                output.accept(ModArmorItems.SHADOW_HELMET);
                output.accept(ModArmorItems.SHADOW_CHESTPLATE);
                output.accept(ModArmorItems.SHADOW_LEGGINGS);
                output.accept(ModArmorItems.SHADOW_BOOTS);

                // Astral Armor (v1.5)
                output.accept(ModArmorItems.ASTRAL_HELMET);
                output.accept(ModArmorItems.ASTRAL_CHESTPLATE);
                output.accept(ModArmorItems.ASTRAL_LEGGINGS);
                output.accept(ModArmorItems.ASTRAL_BOOTS);

                // Ender Armor (v1.6)
                output.accept(ModArmorItems.ENDER_HELMET);
                output.accept(ModArmorItems.ENDER_CHESTPLATE);
                output.accept(ModArmorItems.ENDER_LEGGINGS);
                output.accept(ModArmorItems.ENDER_BOOTS);

                // Frost Armor (v1.6)
                output.accept(ModArmorItems.FROST_HELMET);
                output.accept(ModArmorItems.FROST_CHESTPLATE);
                output.accept(ModArmorItems.FROST_LEGGINGS);
                output.accept(ModArmorItems.FROST_BOOTS);

                // Ruby gem (v1.6)
                output.accept(ModItems.RUBY);

                // Ruby Armor (v1.6)
                output.accept(ModArmorItems.RUBY_HELMET);
                output.accept(ModArmorItems.RUBY_CHESTPLATE);
                output.accept(ModArmorItems.RUBY_LEGGINGS);
                output.accept(ModArmorItems.RUBY_BOOTS);
            })
            .build();

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, HARMONICS_TAB_KEY, tab);
    }
}

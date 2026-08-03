package com.antonius.harmonics.armor;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;

import java.util.Map;

/**
 * Registration class for all armor items in the unified Harmonics mod.
 * 44 armor items: 11 materials x 4 pieces each.
 */
public class ModArmorItems {

    private static final String MOD_ID = "harmonics";

    // === Custom Armor Materials ===
    public static final ArmorMaterial WOODEN_ARMOR_MATERIAL = createArmorMaterial(
        "wooden", 15, ArmorMaterials.makeDefense(1, 3, 2, 1, 3), 0, 0.0f, 0.0f, ItemTags.PLANKS
    );
    public static final ArmorMaterial STONE_ARMOR_MATERIAL = createArmorMaterial(
        "stone", 20, ArmorMaterials.makeDefense(2, 5, 4, 2, 5), 5, 0.0f, 0.0f, ItemTags.PLANKS
    );
    public static final ArmorMaterial OBSIDIAN_ARMOR_MATERIAL = createArmorMaterial(
        "obsidian", 40, ArmorMaterials.makeDefense(3, 8, 6, 3, 8), 8, 2.0f, 0.0f, ItemTags.PLANKS
    );
    public static final ArmorMaterial BONE_ARMOR_MATERIAL = createArmorMaterial(
        "bone", 20, ArmorMaterials.makeDefense(2, 4, 4, 2, 4), 15, 0.0f, 0.0f, ItemTags.PLANKS
    );
    public static final ArmorMaterial AMETHYST_ARMOR_MATERIAL = createArmorMaterial(
        "amethyst", 30, ArmorMaterials.makeDefense(3, 6, 5, 3, 6), 10, 1.0f, 0.0f, ItemTags.PLANKS
    );
    public static final ArmorMaterial EMERALD_ARMOR_MATERIAL = createArmorMaterial(
        "emerald", 45, ArmorMaterials.makeDefense(4, 8, 7, 4, 8), 8, 2.0f, 0.0f, ItemTags.PLANKS
    );
    // === NEW ARMOR SETS v1.5 ===
    public static final ArmorMaterial CRYSTAL_ARMOR_MATERIAL = createArmorMaterial(
        "crystal", 50, ArmorMaterials.makeDefense(3, 8, 7, 3, 8), 12, 2.0f, 0.1f, ItemTags.PLANKS
    );
    public static final ArmorMaterial SHADOW_ARMOR_MATERIAL = createArmorMaterial(
        "shadow", 55, ArmorMaterials.makeDefense(4, 9, 8, 4, 9), 10, 2.5f, 0.15f, ItemTags.PLANKS
    );
    public static final ArmorMaterial ASTRAL_ARMOR_MATERIAL = createArmorMaterial(
        "astral", 45, ArmorMaterials.makeDefense(3, 7, 6, 3, 7), 14, 1.5f, 0.0f, ItemTags.PLANKS
    );
    // === NEW ARMOR SETS v1.6 ===
    public static final ArmorMaterial ENDER_ARMOR_MATERIAL = createArmorMaterial(
        "ender", 50, ArmorMaterials.makeDefense(4, 9, 7, 4, 9), 18, 2.0f, 0.1f, ItemTags.PLANKS
    );
    public static final ArmorMaterial FROST_ARMOR_MATERIAL = createArmorMaterial(
        "frost", 35, ArmorMaterials.makeDefense(3, 7, 6, 3, 7), 12, 1.5f, 0.0f, ItemTags.PLANKS
    );

    // === WOODEN ARMOR SET ===
    public static final Item WOODEN_HELMET = createArmorItem("wooden_helmet", WOODEN_ARMOR_MATERIAL, ArmorType.HELMET, false);
    public static final Item WOODEN_CHESTPLATE = createArmorItem("wooden_chestplate", WOODEN_ARMOR_MATERIAL, ArmorType.CHESTPLATE, false);
    public static final Item WOODEN_LEGGINGS = createArmorItem("wooden_leggings", WOODEN_ARMOR_MATERIAL, ArmorType.LEGGINGS, false);
    public static final Item WOODEN_BOOTS = createArmorItem("wooden_boots", WOODEN_ARMOR_MATERIAL, ArmorType.BOOTS, false);

    // === STONE ARMOR SET ===
    public static final Item STONE_HELMET = createArmorItem("stone_helmet", STONE_ARMOR_MATERIAL, ArmorType.HELMET, false);
    public static final Item STONE_CHESTPLATE = createArmorItem("stone_chestplate", STONE_ARMOR_MATERIAL, ArmorType.CHESTPLATE, false);
    public static final Item STONE_LEGGINGS = createArmorItem("stone_leggings", STONE_ARMOR_MATERIAL, ArmorType.LEGGINGS, false);
    public static final Item STONE_BOOTS = createArmorItem("stone_boots", STONE_ARMOR_MATERIAL, ArmorType.BOOTS, false);

    // === OBSIDIAN ARMOR SET (Fire Resistant) ===
    public static final Item OBSIDIAN_HELMET = createArmorItem("obsidian_helmet", OBSIDIAN_ARMOR_MATERIAL, ArmorType.HELMET, true);
    public static final Item OBSIDIAN_CHESTPLATE = createArmorItem("obsidian_chestplate", OBSIDIAN_ARMOR_MATERIAL, ArmorType.CHESTPLATE, true);
    public static final Item OBSIDIAN_LEGGINGS = createArmorItem("obsidian_leggings", OBSIDIAN_ARMOR_MATERIAL, ArmorType.LEGGINGS, true);
    public static final Item OBSIDIAN_BOOTS = createArmorItem("obsidian_boots", OBSIDIAN_ARMOR_MATERIAL, ArmorType.BOOTS, true);

    // === BONE ARMOR SET ===
    public static final Item BONE_HELMET = createArmorItem("bone_helmet", BONE_ARMOR_MATERIAL, ArmorType.HELMET, false);
    public static final Item BONE_CHESTPLATE = createArmorItem("bone_chestplate", BONE_ARMOR_MATERIAL, ArmorType.CHESTPLATE, false);
    public static final Item BONE_LEGGINGS = createArmorItem("bone_leggings", BONE_ARMOR_MATERIAL, ArmorType.LEGGINGS, false);
    public static final Item BONE_BOOTS = createArmorItem("bone_boots", BONE_ARMOR_MATERIAL, ArmorType.BOOTS, false);

    // === AMETHYST ARMOR SET ===
    public static final Item AMETHYST_HELMET = createArmorItem("amethyst_helmet", AMETHYST_ARMOR_MATERIAL, ArmorType.HELMET, false);
    public static final Item AMETHYST_CHESTPLATE = createArmorItem("amethyst_chestplate", AMETHYST_ARMOR_MATERIAL, ArmorType.CHESTPLATE, false);
    public static final Item AMETHYST_LEGGINGS = createArmorItem("amethyst_leggings", AMETHYST_ARMOR_MATERIAL, ArmorType.LEGGINGS, false);
    public static final Item AMETHYST_BOOTS = createArmorItem("amethyst_boots", AMETHYST_ARMOR_MATERIAL, ArmorType.BOOTS, false);

    // === EMERALD ARMOR SET (Fire Resistant) ===
    public static final Item EMERALD_HELMET = createArmorItem("emerald_helmet", EMERALD_ARMOR_MATERIAL, ArmorType.HELMET, true);
    public static final Item EMERALD_CHESTPLATE = createArmorItem("emerald_chestplate", EMERALD_ARMOR_MATERIAL, ArmorType.CHESTPLATE, true);
    public static final Item EMERALD_LEGGINGS = createArmorItem("emerald_leggings", EMERALD_ARMOR_MATERIAL, ArmorType.LEGGINGS, true);
    public static final Item EMERALD_BOOTS = createArmorItem("emerald_boots", EMERALD_ARMOR_MATERIAL, ArmorType.BOOTS, true);

    // === CRYSTAL ARMOR SET (Prism Refraction, Magic Protection) ===
    public static final Item CRYSTAL_HELMET = createArmorItem("crystal_helmet", CRYSTAL_ARMOR_MATERIAL, ArmorType.HELMET, true);
    public static final Item CRYSTAL_CHESTPLATE = createArmorItem("crystal_chestplate", CRYSTAL_ARMOR_MATERIAL, ArmorType.CHESTPLATE, true);
    public static final Item CRYSTAL_LEGGINGS = createArmorItem("crystal_leggings", CRYSTAL_ARMOR_MATERIAL, ArmorType.LEGGINGS, true);
    public static final Item CRYSTAL_BOOTS = createArmorItem("crystal_boots", CRYSTAL_ARMOR_MATERIAL, ArmorType.BOOTS, true);

    // === SHADOW ARMOR SET (Stealth, Evasion) ===
    public static final Item SHADOW_HELMET = createArmorItem("shadow_helmet", SHADOW_ARMOR_MATERIAL, ArmorType.HELMET, false);
    public static final Item SHADOW_CHESTPLATE = createArmorItem("shadow_chestplate", SHADOW_ARMOR_MATERIAL, ArmorType.CHESTPLATE, false);
    public static final Item SHADOW_LEGGINGS = createArmorItem("shadow_leggings", SHADOW_ARMOR_MATERIAL, ArmorType.LEGGINGS, false);
    public static final Item SHADOW_BOOTS = createArmorItem("shadow_boots", SHADOW_ARMOR_MATERIAL, ArmorType.BOOTS, false);

    // === ASTRAL ARMOR SET (Cosmic Protection, Flight) ===
    public static final Item ASTRAL_HELMET = createArmorItem("astral_helmet", ASTRAL_ARMOR_MATERIAL, ArmorType.HELMET, true);
    public static final Item ASTRAL_CHESTPLATE = createArmorItem("astral_chestplate", ASTRAL_ARMOR_MATERIAL, ArmorType.CHESTPLATE, true);
    public static final Item ASTRAL_LEGGINGS = createArmorItem("astral_leggings", ASTRAL_ARMOR_MATERIAL, ArmorType.LEGGINGS, true);
    public static final Item ASTRAL_BOOTS = createArmorItem("astral_boots", ASTRAL_ARMOR_MATERIAL, ArmorType.BOOTS, true);

    // === ENDER ARMOR SET (Blink, Dimensional Shift) ===
    public static final Item ENDER_HELMET = createArmorItem("ender_helmet", ENDER_ARMOR_MATERIAL, ArmorType.HELMET, false);
    public static final Item ENDER_CHESTPLATE = createArmorItem("ender_chestplate", ENDER_ARMOR_MATERIAL, ArmorType.CHESTPLATE, false);
    public static final Item ENDER_LEGGINGS = createArmorItem("ender_leggings", ENDER_ARMOR_MATERIAL, ArmorType.LEGGINGS, false);
    public static final Item ENDER_BOOTS = createArmorItem("ender_boots", ENDER_ARMOR_MATERIAL, ArmorType.BOOTS, false);

    // === FROST ARMOR SET (Freeze Aura, Ice Protection) ===
    public static final Item FROST_HELMET = createArmorItem("frost_helmet", FROST_ARMOR_MATERIAL, ArmorType.HELMET, false);
    public static final Item FROST_CHESTPLATE = createArmorItem("frost_chestplate", FROST_ARMOR_MATERIAL, ArmorType.CHESTPLATE, false);
    public static final Item FROST_LEGGINGS = createArmorItem("frost_leggings", FROST_ARMOR_MATERIAL, ArmorType.LEGGINGS, false);
    public static final Item FROST_BOOTS = createArmorItem("frost_boots", FROST_ARMOR_MATERIAL, ArmorType.BOOTS, false);

    private static ArmorMaterial createArmorMaterial(String name, int durability, Map<ArmorType, Integer> defense,
            int enchantmentValue, float toughness, float knockbackResistance, net.minecraft.tags.TagKey<Item> repairIngredient) {
        
        // Correctly creates the equipment asset key under "harmonics:<name>"
        ResourceKey<EquipmentAsset> assetId = ResourceKey.create(
            EquipmentAssets.ROOT_ID, 
            Identifier.fromNamespaceAndPath(MOD_ID, name)
        );

        return new ArmorMaterial(
            durability,
            defense,
            enchantmentValue,
            SoundEvents.ARMOR_EQUIP_GENERIC,
            toughness,
            knockbackResistance,
            repairIngredient,
            assetId
        );
    }

    private static Item createArmorItem(String name, ArmorMaterial material, ArmorType type, boolean fireResistant) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, name));

        Equippable equippable = Equippable.builder(type.getSlot())
                .setEquipSound(material.equipSound())
                .setAsset(material.assetId())
                .setDamageOnHurt(true)
                .setSwappable(true)
                .build();

        ItemAttributeModifiers attributes = material.createAttributes(type);

        Item.Properties props = new Item.Properties()
                .setId(key)
                .durability(type.getDurability(material.durability()))
                .component(DataComponents.EQUIPPABLE, equippable)
                .component(DataComponents.ATTRIBUTE_MODIFIERS, attributes);

        if (fireResistant) {
            props = props.fireResistant();
        }

        return new Item(props);
    }

    public static void registerArmorItems() {
        registerItem("wooden_helmet", WOODEN_HELMET);
        registerItem("wooden_chestplate", WOODEN_CHESTPLATE);
        registerItem("wooden_leggings", WOODEN_LEGGINGS);
        registerItem("wooden_boots", WOODEN_BOOTS);

        registerItem("stone_helmet", STONE_HELMET);
        registerItem("stone_chestplate", STONE_CHESTPLATE);
        registerItem("stone_leggings", STONE_LEGGINGS);
        registerItem("stone_boots", STONE_BOOTS);

        registerItem("obsidian_helmet", OBSIDIAN_HELMET);
        registerItem("obsidian_chestplate", OBSIDIAN_CHESTPLATE);
        registerItem("obsidian_leggings", OBSIDIAN_LEGGINGS);
        registerItem("obsidian_boots", OBSIDIAN_BOOTS);

        registerItem("bone_helmet", BONE_HELMET);
        registerItem("bone_chestplate", BONE_CHESTPLATE);
        registerItem("bone_leggings", BONE_LEGGINGS);
        registerItem("bone_boots", BONE_BOOTS);

        registerItem("amethyst_helmet", AMETHYST_HELMET);
        registerItem("amethyst_chestplate", AMETHYST_CHESTPLATE);
        registerItem("amethyst_leggings", AMETHYST_LEGGINGS);
        registerItem("amethyst_boots", AMETHYST_BOOTS);

        registerItem("emerald_helmet", EMERALD_HELMET);
        registerItem("emerald_chestplate", EMERALD_CHESTPLATE);
        registerItem("emerald_leggings", EMERALD_LEGGINGS);
        registerItem("emerald_boots", EMERALD_BOOTS);

        // === NEW ARMOR SETS v1.5 ===
        registerItem("crystal_helmet", CRYSTAL_HELMET);
        registerItem("crystal_chestplate", CRYSTAL_CHESTPLATE);
        registerItem("crystal_leggings", CRYSTAL_LEGGINGS);
        registerItem("crystal_boots", CRYSTAL_BOOTS);

        registerItem("shadow_helmet", SHADOW_HELMET);
        registerItem("shadow_chestplate", SHADOW_CHESTPLATE);
        registerItem("shadow_leggings", SHADOW_LEGGINGS);
        registerItem("shadow_boots", SHADOW_BOOTS);

        registerItem("astral_helmet", ASTRAL_HELMET);
        registerItem("astral_chestplate", ASTRAL_CHESTPLATE);
        registerItem("astral_leggings", ASTRAL_LEGGINGS);
        registerItem("astral_boots", ASTRAL_BOOTS);

        // === NEW ARMOR SETS v1.6 ===
        registerItem("ender_helmet", ENDER_HELMET);
        registerItem("ender_chestplate", ENDER_CHESTPLATE);
        registerItem("ender_leggings", ENDER_LEGGINGS);
        registerItem("ender_boots", ENDER_BOOTS);

        registerItem("frost_helmet", FROST_HELMET);
        registerItem("frost_chestplate", FROST_CHESTPLATE);
        registerItem("frost_leggings", FROST_LEGGINGS);
        registerItem("frost_boots", FROST_BOOTS);
    }

    private static void registerItem(String name, Item item) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, name));
        Registry.register(BuiltInRegistries.ITEM, key, item);
    }
}
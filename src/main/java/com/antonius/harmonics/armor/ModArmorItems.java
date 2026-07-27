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
 * 28 armor items: 7 materials x 4 pieces each.
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
    public static final ArmorMaterial COPPER_ARMOR_MATERIAL = createArmorMaterial(
        "copper", 24, ArmorMaterials.makeDefense(2, 6, 5, 2, 6), 12, 0.0f, 0.0f, ItemTags.PLANKS
    );
    public static final ArmorMaterial AMETHYST_ARMOR_MATERIAL = createArmorMaterial(
        "amethyst", 30, ArmorMaterials.makeDefense(3, 6, 5, 3, 6), 10, 1.0f, 0.0f, ItemTags.PLANKS
    );
    public static final ArmorMaterial EMERALD_ARMOR_MATERIAL = createArmorMaterial(
        "emerald", 45, ArmorMaterials.makeDefense(4, 8, 7, 4, 8), 8, 2.0f, 0.0f, ItemTags.PLANKS
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

    // === COPPER ARMOR SET ===
    public static final Item COPPER_HELMET = createArmorItem("copper_helmet", COPPER_ARMOR_MATERIAL, ArmorType.HELMET, false);
    public static final Item COPPER_CHESTPLATE = createArmorItem("copper_chestplate", COPPER_ARMOR_MATERIAL, ArmorType.CHESTPLATE, false);
    public static final Item COPPER_LEGGINGS = createArmorItem("copper_leggings", COPPER_ARMOR_MATERIAL, ArmorType.LEGGINGS, false);
    public static final Item COPPER_BOOTS = createArmorItem("copper_boots", COPPER_ARMOR_MATERIAL, ArmorType.BOOTS, false);

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

    private static ArmorMaterial createArmorMaterial(String name, int durability, Map<ArmorType, Integer> defense,
            int enchantmentValue, float toughness, float knockbackResistance, net.minecraft.tags.TagKey<Item> repairIngredient) {
        ResourceKey<EquipmentAsset> assetId = EquipmentAssets.createId(name);
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

        // Reverted: .setAsset() omitted so no 3D skin layer renders on player model
        Equippable equippable = Equippable.builder(type.getSlot())
                .setEquipSound(material.equipSound())
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

        registerItem("copper_helmet", COPPER_HELMET);
        registerItem("copper_chestplate", COPPER_CHESTPLATE);
        registerItem("copper_leggings", COPPER_LEGGINGS);
        registerItem("copper_boots", COPPER_BOOTS);

        registerItem("amethyst_helmet", AMETHYST_HELMET);
        registerItem("amethyst_chestplate", AMETHYST_CHESTPLATE);
        registerItem("amethyst_leggings", AMETHYST_LEGGINGS);
        registerItem("amethyst_boots", AMETHYST_BOOTS);

        registerItem("emerald_helmet", EMERALD_HELMET);
        registerItem("emerald_chestplate", EMERALD_CHESTPLATE);
        registerItem("emerald_leggings", EMERALD_LEGGINGS);
        registerItem("emerald_boots", EMERALD_BOOTS);
    }

    private static void registerItem(String name, Item item) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, name));
        Registry.register(BuiltInRegistries.ITEM, key, item);
    }
}
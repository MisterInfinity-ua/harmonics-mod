package com.antonius.harmonics.item;

import com.antonius.harmonics.Harmonics;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.EnumMap;
import java.util.Map;

public final class ModWeapons {

    public static final Map<WeaponMaterial, Map<WeaponType, ModWeaponItem>> WEAPONS =
            new EnumMap<>(WeaponMaterial.class);

    private ModWeapons() {
    }

    public static void init() {
        for (WeaponMaterial material : WeaponMaterial.values()) {
            Map<WeaponType, ModWeaponItem> byType = new EnumMap<>(WeaponType.class);
            for (WeaponType type : WeaponType.values()) {
                byType.put(type, register(material, type));
            }
            WEAPONS.put(material, byType);
        }
        Harmonics.LOGGER.info("[Harmonics] Registered {} weapon types x {} materials = {} total weapons",
                WeaponType.values().length, WeaponMaterial.values().length,
                WeaponType.values().length * WeaponMaterial.values().length);
    }

    private static ModWeaponItem register(WeaponMaterial material, WeaponType type) {
        String id = material.id() + "_" + type.id();

        // FIX: the ResourceKey must be built and set on Properties via .setId(key)
        // BEFORE the Item constructor runs -- this was missing before, which is why
        // construction crashed. Register with this same key afterward, not a
        // separately-built Identifier.
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Harmonics.MOD_ID, id));

        Item.Properties props = new Item.Properties()
                .stacksTo(1)
                .durability(material.getDurability())
                .enchantable(material.getEnchantability())
                .setId(key);

        if (material == WeaponMaterial.LAVA || material == WeaponMaterial.NETHERITE_PLUS) {
            props.fireResistant();
        }

        ModWeaponItem weapon = new ModWeaponItem(material, type, props);
        return Registry.register(BuiltInRegistries.ITEM, key, weapon);
    }

    public static ModWeaponItem get(WeaponMaterial material, WeaponType type) {
        return WEAPONS.get(material).get(type);
    }

    public static boolean isModWeapon(Item item) {
        return item instanceof ModWeaponItem;
    }
}

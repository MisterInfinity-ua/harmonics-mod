package com.antonius.harmonics.item;

import com.antonius.harmonics.Harmonics;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;

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

        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Harmonics.MOD_ID, id));

        // Build attack_damage and attack_speed attributes BEFORE construction
        // so the vanilla combat system handles weapon damage properly.
        float effectiveDamage = type.getEffectiveDamage(material);
        float effectiveSpeed = type.getEffectiveAttackSpeed(material);
        // damageBonus: effectiveDamage is total damage, attribute = total - playerBase(1.0)
        float damageBonus = effectiveDamage - 1.0f;
        // speedBonus: effectiveSpeed IS the attribute modifier (player base is 4.0)
        // Clamp so total attack speed (4.0 + speedBonus) is never below 1.0
        float speedBonus = Math.max(effectiveSpeed, -3.0f);

        ItemAttributeModifiers weaponAttrs = ItemAttributeModifiers.builder()
            .add(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(
                    Identifier.fromNamespaceAndPath(Harmonics.MOD_ID, id + "_attack_damage"),
                    damageBonus,
                    AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
            )
            .add(
                Attributes.ATTACK_SPEED,
                new AttributeModifier(
                    Identifier.fromNamespaceAndPath(Harmonics.MOD_ID, id + "_attack_speed"),
                    speedBonus,
                    AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
            )
            .build();

        Item.Properties props = new Item.Properties()
                .stacksTo(1)
                .durability(material.getDurability())
                .enchantable(material.getEnchantability())
                .component(DataComponents.ATTRIBUTE_MODIFIERS, weaponAttrs)
                .setId(key);

        if (material == WeaponMaterial.LAVA || material == WeaponMaterial.NETHERITE_PLUS
                || material == WeaponMaterial.WITHER) {
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

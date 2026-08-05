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

/**
 * Registers one InstrumentItem for every (material, type) pair --
 * 9 materials x 5 types = 45 base instruments. Masterwork versions are not
 * separate items; they're the same item with the MASTERWORK component applied
 * by the fusion recipe (see FusionRecipe).
 *
 * Registration pattern follows the current (26.x, Mojang-mappings) Fabric docs:
 * build a ResourceKey<Item> first, pass it into Item.Properties via setId(),
 * then Registry.register against BuiltInRegistries.ITEM (the actual registry
 * instance -- Registries.ITEM is just the ResourceKey<Registry<Item>>).
 */
public final class ModItems {

    public static final Map<InstrumentMaterial, Map<InstrumentType, InstrumentItem>> INSTRUMENTS =
            new EnumMap<>(InstrumentMaterial.class);

    // Standalone Ruby gem item (crafting ingredient for the Ruby weapon + armor sets).
    public static Item RUBY;

    private ModItems() {
    }

    private static ResourceKey<Item> id(String name) {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Harmonics.MOD_ID, name));
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, id(name), item);
    }

    public static void init() {
        for (InstrumentMaterial material : InstrumentMaterial.values()) {
            Map<InstrumentType, InstrumentItem> byType = new EnumMap<>(InstrumentType.class);
            for (InstrumentType type : InstrumentType.values()) {
                byType.put(type, register(material, type));
            }
            INSTRUMENTS.put(material, byType);
        }
        RUBY = registerItem("ruby", new Item(new Item.Properties().setId(id("ruby")).stacksTo(64)));
    }

    private static InstrumentItem register(InstrumentMaterial material, InstrumentType type) {
        String id = material.id() + "_" + type.id();
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Harmonics.MOD_ID, id));

        InstrumentItem item = new InstrumentItem(material, type,
                new Item.Properties()
                        .stacksTo(1)
                        .setId(key));

        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    public static InstrumentItem get(InstrumentMaterial material, InstrumentType type) {
        return INSTRUMENTS.get(material).get(type);
    }

    /**
     * Get all instrument items for creative tab display.
     */
    public static Iterable<Item> getAllItems() {
        return () -> INSTRUMENTS.values().stream()
            .flatMap(map -> map.values().stream())
            .map(item -> (Item) item)
            .iterator();
    }
}

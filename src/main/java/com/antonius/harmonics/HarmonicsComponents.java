package com.antonius.harmonics;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;

/**
 * Data components are the modern (post-1.20.5) replacement for NBT-driven item behavior.
 * We use a single presence-flag "masterwork" component (present = fused/upgraded, absent = base tier).
 * Item models switch texture based on whether this component is present -- see the item
 * definition json's "minecraft:condition" / "minecraft:has_component" predicate.
 */
public final class HarmonicsComponents {

    public static final DataComponentType<Unit> MASTERWORK = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(Harmonics.MOD_ID, "masterwork"),
            DataComponentType.<Unit>builder().persistent(Unit.CODEC).build()
    );

    private HarmonicsComponents() {
    }

    public static void init() {
        // Registration above runs on class load; this just forces that to happen early.
    }
}

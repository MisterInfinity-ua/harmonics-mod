package com.antonius.harmonics.recipe;

import com.antonius.harmonics.Harmonics;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeSerializer;

public final class ModRecipes {

    public static final RecipeSerializer<FusionRecipe> FUSION_SERIALIZER = Registry.register(
            BuiltInRegistries.RECIPE_SERIALIZER,
            Identifier.fromNamespaceAndPath(Harmonics.MOD_ID, "crafting_special_fusion"),
            new RecipeSerializer<>(FusionRecipe.MAP_CODEC, FusionRecipe.STREAM_CODEC)
    );

    private ModRecipes() {
    }

    public static void init() {
        // Triggers static registration above.
    }
}

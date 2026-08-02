package com.antonius.harmonics.recipe;

import com.antonius.harmonics.HarmonicsComponents;
import com.antonius.harmonics.item.InstrumentItem;
import com.antonius.harmonics.item.ModWeaponItem;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Special (code-driven, not JSON-ingredient-driven) recipe: put exactly two of the
 * SAME base-tier instrument OR weapon anywhere in the crafting grid -> get one
 * masterwork (fused) item back. Works for every material/type combo without needing
 * dozens of separate JSON recipes.
 *
 * As of 26.x, CustomRecipe takes no constructor arguments (the old
 * CraftingBookCategory param and the CustomRecipe.Serializer inner class are both
 * gone), and recipe serializers are just a MapCodec + StreamCodec pair passed
 * directly into RecipeSerializer's constructor -- no wrapper class needed.
 * Since this recipe has no persisted data, both codecs are "unit" codecs.
 *
 * Rules:
 *  - Exactly 2 items in the grid, all other slots empty.
 *  - Both stacks must be the same item, and either both InstrumentItems or both
 *    ModWeaponItems (never a mix).
 *  - Neither input may already be masterwork (no double-fusing in one craft).
 */
public class FusionRecipe extends CustomRecipe {

    public static final MapCodec<FusionRecipe> MAP_CODEC = MapCodec.unit(FusionRecipe::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, FusionRecipe> STREAM_CODEC =
            StreamCodec.unit(new FusionRecipe());

    public FusionRecipe() {
        super();
    }

    private static boolean isFusable(ItemStack stack) {
        return stack.getItem() instanceof InstrumentItem || stack.getItem() instanceof ModWeaponItem;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        ItemStack first = ItemStack.EMPTY;
        int count = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (!isFusable(stack)) return false;
            if (stack.has(HarmonicsComponents.MASTERWORK)) return false;

            if (first.isEmpty()) {
                first = stack;
            } else if (!ItemStack.isSameItem(first, stack)) {
                return false;
            }
            count++;
        }

        return count == 2;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) {
                ItemStack result = stack.copyWithCount(1);
                result.set(HarmonicsComponents.MASTERWORK, Unit.INSTANCE);
                return result;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return ModRecipes.FUSION_SERIALIZER;
    }
}

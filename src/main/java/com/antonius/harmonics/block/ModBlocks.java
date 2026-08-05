package com.antonius.harmonics.block;

import com.antonius.harmonics.Harmonics;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * Ruby ore blocks. Two variants (stone + deepslate) that drop ruby when mined,
 * matching emerald ore behaviour. Registered into the shared BLOCK and ITEM
 * registries so the same class works for both the Fabric and NeoForge builds.
 */
public final class ModBlocks {

    public static Block RUBY_ORE;
    public static Block DEEPSLATE_RUBY_ORE;

    private ModBlocks() {
    }

    private static ResourceKey<Block> blockId(String name) {
        return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Harmonics.MOD_ID, name));
    }

    private static ResourceKey<Item> itemId(String name) {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Harmonics.MOD_ID, name));
    }

    public static void init() {
        RUBY_ORE = registerOre("ruby_ore");
        DEEPSLATE_RUBY_ORE = registerOre("deepslate_ruby_ore");
    }

    private static Block registerOre(String name) {
        Block block = new Block(BlockBehaviour.Properties.of()
                .setId(blockId(name))
                .strength(3.0f, 3.0f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.STONE));
        Registry.register(BuiltInRegistries.BLOCK, blockId(name), block);
        Registry.register(BuiltInRegistries.ITEM, itemId(name),
                new BlockItem(block, new Item.Properties().setId(itemId(name))));
        return block;
    }
}

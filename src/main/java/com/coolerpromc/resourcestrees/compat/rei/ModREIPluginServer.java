package com.coolerpromc.resourcestrees.compat.rei;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.item.ModItems;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.plugins.REICommonPlugin;
import me.shedaniel.rei.forge.REIPluginCommon;

@REIPluginCommon
public class ModREIPluginServer implements REICommonPlugin {
    @Override
    public void registerItemComparators(ItemComparatorRegistry registry) {
        registry.registerComponents(ModBlocks.RESOURCES_OAK_SAPLING.asItem());
        registry.registerComponents(ModBlocks.RESOURCES_SPRUCE_SAPLING.asItem());
        registry.registerComponents(ModBlocks.RESOURCES_BIRCH_SAPLING.asItem());
        registry.registerComponents(ModBlocks.RESOURCES_JUNGLE_SAPLING.asItem());
        registry.registerComponents(ModBlocks.RESOURCES_ACACIA_SAPLING.asItem());
        registry.registerComponents(ModBlocks.RESOURCES_DARK_OAK_SAPLING.asItem());
        registry.registerComponents(ModBlocks.RESOURCES_CHERRY_SAPLING.asItem());
        registry.registerComponents(ModBlocks.RESOURCES_PALE_OAK_SAPLING.asItem());

        registry.registerComponents(ModBlocks.RESOURCES_OAK_LEAVES.asItem());
        registry.registerComponents(ModBlocks.RESOURCES_SPRUCE_LEAVES.asItem());
        registry.registerComponents(ModBlocks.RESOURCES_BIRCH_LEAVES.asItem());
        registry.registerComponents(ModBlocks.RESOURCES_JUNGLE_LEAVES.asItem());
        registry.registerComponents(ModBlocks.RESOURCES_ACACIA_LEAVES.asItem());
        registry.registerComponents(ModBlocks.RESOURCES_DARK_OAK_LEAVES.asItem());
        registry.registerComponents(ModBlocks.RESOURCES_CHERRY_LEAVES.asItem());
        registry.registerComponents(ModBlocks.RESOURCES_PALE_OAK_LEAVES.asItem());

        registry.registerComponents(ModItems.LEAF_FRAGMENT.get());
    }
}

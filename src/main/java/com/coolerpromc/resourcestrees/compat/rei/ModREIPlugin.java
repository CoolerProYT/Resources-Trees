package com.coolerpromc.resourcestrees.compat.rei;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.compat.rei.category.TreeSimulatorCategory;
import com.coolerpromc.resourcestrees.compat.rei.display.TreeSimulatorDisplay;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.recipe.custom.TreeSimulatorRecipe;
import com.coolerpromc.resourcestrees.screen.custom.TreeSimulatorScreen;
import com.coolerpromc.resourcestrees.util.RecipeViewerFiller;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

@REIPluginClient
public class ModREIPlugin implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new TreeSimulatorCategory(), config -> config.addWorkstations(EntryStacks.of(ModBlocks.TREE_SIMULATOR.get())));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        List<TreeSimulatorRecipe> treeSimulatorRecipe = new ArrayList<>(Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(ModRecipes.TREE_SIMULATOR_TYPE.get()));
        List<ResourceLocation> keys = treeSimulatorRecipe.stream().map(TreeSimulatorRecipe::id).toList();

        treeSimulatorRecipe.addAll(RecipeViewerFiller.addUndefinedRecipes(BasicDisplay.registryAccess(), keys));
        treeSimulatorRecipe.stream().map(TreeSimulatorDisplay::new).forEach(registry::add);
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerClickArea(screen -> new Rectangle(((screen.width - 176) / 2) + 59, ((screen.height - 166) / 2) + 27, 22, 15), TreeSimulatorScreen.class, TreeSimulatorDisplay.CATEGORY_IDENTIFIER);
    }

    @Override
    public void registerItemComparators(ItemComparatorRegistry registry) {
        registry.registerNbt(ModBlocks.RESOURCES_OAK_SAPLING.get().asItem());
        registry.registerNbt(ModBlocks.RESOURCES_SPRUCE_SAPLING.get().asItem());
        registry.registerNbt(ModBlocks.RESOURCES_BIRCH_SAPLING.get().asItem());
        registry.registerNbt(ModBlocks.RESOURCES_JUNGLE_SAPLING.get().asItem());
        registry.registerNbt(ModBlocks.RESOURCES_ACACIA_SAPLING.get().asItem());
        registry.registerNbt(ModBlocks.RESOURCES_DARK_OAK_SAPLING.get().asItem());
        registry.registerNbt(ModBlocks.RESOURCES_CHERRY_SAPLING.get().asItem());

        registry.registerNbt(ModBlocks.RESOURCES_OAK_LEAVES.get().asItem());
        registry.registerNbt(ModBlocks.RESOURCES_SPRUCE_LEAVES.get().asItem());
        registry.registerNbt(ModBlocks.RESOURCES_BIRCH_LEAVES.get().asItem());
        registry.registerNbt(ModBlocks.RESOURCES_JUNGLE_LEAVES.get().asItem());
        registry.registerNbt(ModBlocks.RESOURCES_ACACIA_LEAVES.get().asItem());
        registry.registerNbt(ModBlocks.RESOURCES_DARK_OAK_LEAVES.get().asItem());
        registry.registerNbt(ModBlocks.RESOURCES_CHERRY_LEAVES.get().asItem());

        registry.registerNbt(ModItems.LEAF_FRAGMENT.get());
    }
}

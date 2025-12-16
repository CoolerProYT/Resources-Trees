package com.coolerpromc.resourcestrees.compat.jei;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.ResourcesTreesClient;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.compat.jei.category.TreeSimulatorCategory;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.recipe.custom.TreeSimulatorRecipe;
import com.coolerpromc.resourcestrees.screen.custom.TreeSimulatorScreen;
import com.coolerpromc.resourcestrees.util.RecipeViewerFiller;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class ModJEIPlugin implements IModPlugin {
    @Override
    public Identifier getPluginUid() {
        return Identifier.of(ResourcesTrees.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        registration.addRecipeCategories(new TreeSimulatorCategory(guiHelper));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(TreeSimulatorCategory.TREE_SIMULATOR_TYPE, ModBlocks.TREE_SIMULATOR);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<RecipeEntry<TreeSimulatorRecipe>> treeSimulatorRecipe = new ArrayList<>(ResourcesTreesClient.recipeMap.getAll(ModRecipes.TREE_SIMULATOR_TYPE));
        List<RegistryKey<Recipe<?>>> keys = treeSimulatorRecipe.stream().map(RecipeEntry::id).toList();

        treeSimulatorRecipe.addAll(RecipeViewerFiller.addUndefinedRecipes(MinecraftClient.getInstance().world.getRegistryManager(), keys));
        registration.addRecipes(TreeSimulatorCategory.TREE_SIMULATOR_TYPE, treeSimulatorRecipe.stream().toList());
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_OAK_SAPLING.asItem(), ModDataComponents.TYPE);
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_SPRUCE_SAPLING.asItem(), ModDataComponents.TYPE);
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_BIRCH_SAPLING.asItem(), ModDataComponents.TYPE);
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_JUNGLE_SAPLING.asItem(), ModDataComponents.TYPE);
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_ACACIA_SAPLING.asItem(), ModDataComponents.TYPE);
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_DARK_OAK_SAPLING.asItem(), ModDataComponents.TYPE);
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_CHERRY_SAPLING.asItem(), ModDataComponents.TYPE);
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_PALE_OAK_SAPLING.asItem(), ModDataComponents.TYPE);

        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_OAK_LEAVES.asItem(), ModDataComponents.TYPE);
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_SPRUCE_LEAVES.asItem(), ModDataComponents.TYPE);
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_BIRCH_LEAVES.asItem(), ModDataComponents.TYPE);
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_JUNGLE_LEAVES.asItem(), ModDataComponents.TYPE);
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_ACACIA_LEAVES.asItem(), ModDataComponents.TYPE);
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_DARK_OAK_LEAVES.asItem(), ModDataComponents.TYPE);
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_CHERRY_LEAVES.asItem(), ModDataComponents.TYPE);
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_PALE_OAK_LEAVES.asItem(), ModDataComponents.TYPE);

        registration.registerFromDataComponentTypes(ModItems.LEAF_FRAGMENT, ModDataComponents.TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(TreeSimulatorScreen.class, 59, 35, 22, 16, TreeSimulatorCategory.TREE_SIMULATOR_TYPE);
    }
}

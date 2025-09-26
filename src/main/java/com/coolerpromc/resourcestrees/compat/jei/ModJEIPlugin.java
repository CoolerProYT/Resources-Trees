/*
package com.coolerpromc.resourcestrees.compat.jei;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.compat.jei.category.TreeSimulatorCategory;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.event.ModRecipeReceived;
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
import mezz.jei.common.util.RegistryUtil;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class ModJEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(ResourcesTrees.MODID, "jei_plugin");
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
        List<RecipeHolder<TreeSimulatorRecipe>> treeSimulatorRecipe = new ArrayList<>(ModRecipeReceived.recipeMap.byType(ModRecipes.TREE_SIMULATOR_TYPE.get()));
        List<ResourceKey<Recipe<?>>> keys = treeSimulatorRecipe.stream().map(RecipeHolder::id).toList();

        treeSimulatorRecipe.addAll(RecipeViewerFiller.addUndefinedRecipes(RegistryUtil.getRegistryAccess(), keys));
        registration.addRecipes(TreeSimulatorCategory.TREE_SIMULATOR_TYPE, treeSimulatorRecipe.stream().toList());
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_OAK_SAPLING.asItem(), ModDataComponents.TYPE.get());
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_SPRUCE_SAPLING.asItem(), ModDataComponents.TYPE.get());
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_BIRCH_SAPLING.asItem(), ModDataComponents.TYPE.get());
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_JUNGLE_SAPLING.asItem(), ModDataComponents.TYPE.get());
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_ACACIA_SAPLING.asItem(), ModDataComponents.TYPE.get());
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_DARK_OAK_SAPLING.asItem(), ModDataComponents.TYPE.get());
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_CHERRY_SAPLING.asItem(), ModDataComponents.TYPE.get());
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_PALE_OAK_SAPLING.asItem(), ModDataComponents.TYPE.get());

        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_OAK_LEAVES.asItem(), ModDataComponents.TYPE.get());
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_SPRUCE_LEAVES.asItem(), ModDataComponents.TYPE.get());
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_BIRCH_LEAVES.asItem(), ModDataComponents.TYPE.get());
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_JUNGLE_LEAVES.asItem(), ModDataComponents.TYPE.get());
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_ACACIA_LEAVES.asItem(), ModDataComponents.TYPE.get());
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_DARK_OAK_LEAVES.asItem(), ModDataComponents.TYPE.get());
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_CHERRY_LEAVES.asItem(), ModDataComponents.TYPE.get());
        registration.registerFromDataComponentTypes(ModBlocks.RESOURCES_PALE_OAK_LEAVES.asItem(), ModDataComponents.TYPE.get());

        registration.registerFromDataComponentTypes(ModItems.LEAF_FRAGMENT.get(), ModDataComponents.TYPE.get());
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(TreeSimulatorScreen.class, 59, 35, 22, 16, TreeSimulatorCategory.TREE_SIMULATOR_TYPE);
    }
}*/

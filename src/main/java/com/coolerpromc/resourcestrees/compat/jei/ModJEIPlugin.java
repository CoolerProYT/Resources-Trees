package com.coolerpromc.resourcestrees.compat.jei;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.compat.jei.category.TreeSimulatorCategory;
import com.coolerpromc.resourcestrees.compat.jei.subtype.ResourcesTypeSubtypeInterpreter;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.recipe.custom.TreeSimulatorRecipe;
import com.coolerpromc.resourcestrees.screen.custom.TreeSimulatorScreen;
import com.coolerpromc.resourcestrees.util.RecipeViewerFiller;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import mezz.jei.common.util.RegistryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class ModJEIPlugin implements IModPlugin {
    public static final RecipeType<TreeSimulatorRecipe> TREE_SIMULATOR_TYPE = RecipeType.create(ResourcesTrees.MODID, "tree_simulator", TreeSimulatorRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ResourcesTrees.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        registration.addRecipeCategories(new TreeSimulatorCategory(guiHelper));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(ModBlocks.TREE_SIMULATOR.get(), TREE_SIMULATOR_TYPE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<TreeSimulatorRecipe> treeSimulatorRecipe = new ArrayList<>(Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(ModRecipes.TREE_SIMULATOR_TYPE.get()));
        List<ResourceLocation> keys = treeSimulatorRecipe.stream().map(TreeSimulatorRecipe::id).toList();

        treeSimulatorRecipe.addAll(RecipeViewerFiller.addUndefinedRecipes(RegistryUtil.getRegistryAccess(), keys));
        registration.addRecipes(TREE_SIMULATOR_TYPE, treeSimulatorRecipe);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(ModBlocks.RESOURCES_OAK_SAPLING.get().asItem(), ResourcesTypeSubtypeInterpreter.INSTANCE);
        registration.registerSubtypeInterpreter(ModBlocks.RESOURCES_SPRUCE_SAPLING.get().asItem(), ResourcesTypeSubtypeInterpreter.INSTANCE);
        registration.registerSubtypeInterpreter(ModBlocks.RESOURCES_BIRCH_SAPLING.get().asItem(), ResourcesTypeSubtypeInterpreter.INSTANCE);
        registration.registerSubtypeInterpreter(ModBlocks.RESOURCES_JUNGLE_SAPLING.get().asItem(), ResourcesTypeSubtypeInterpreter.INSTANCE);
        registration.registerSubtypeInterpreter(ModBlocks.RESOURCES_ACACIA_SAPLING.get().asItem(), ResourcesTypeSubtypeInterpreter.INSTANCE);
        registration.registerSubtypeInterpreter(ModBlocks.RESOURCES_DARK_OAK_SAPLING.get().asItem(), ResourcesTypeSubtypeInterpreter.INSTANCE);
        registration.registerSubtypeInterpreter(ModBlocks.RESOURCES_CHERRY_SAPLING.get().asItem(), ResourcesTypeSubtypeInterpreter.INSTANCE);

        registration.registerSubtypeInterpreter(ModBlocks.RESOURCES_OAK_LEAVES.get().asItem(), ResourcesTypeSubtypeInterpreter.INSTANCE);
        registration.registerSubtypeInterpreter(ModBlocks.RESOURCES_SPRUCE_LEAVES.get().asItem(), ResourcesTypeSubtypeInterpreter.INSTANCE);
        registration.registerSubtypeInterpreter(ModBlocks.RESOURCES_BIRCH_LEAVES.get().asItem(), ResourcesTypeSubtypeInterpreter.INSTANCE);
        registration.registerSubtypeInterpreter(ModBlocks.RESOURCES_JUNGLE_LEAVES.get().asItem(), ResourcesTypeSubtypeInterpreter.INSTANCE);
        registration.registerSubtypeInterpreter(ModBlocks.RESOURCES_ACACIA_LEAVES.get().asItem(), ResourcesTypeSubtypeInterpreter.INSTANCE);
        registration.registerSubtypeInterpreter(ModBlocks.RESOURCES_DARK_OAK_LEAVES.get().asItem(), ResourcesTypeSubtypeInterpreter.INSTANCE);
        registration.registerSubtypeInterpreter(ModBlocks.RESOURCES_CHERRY_LEAVES.get().asItem(), ResourcesTypeSubtypeInterpreter.INSTANCE);

        registration.registerSubtypeInterpreter(ModItems.LEAF_FRAGMENT.get(), ResourcesTypeSubtypeInterpreter.INSTANCE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(TreeSimulatorScreen.class, 59, 35, 22, 16, TREE_SIMULATOR_TYPE);
    }
}
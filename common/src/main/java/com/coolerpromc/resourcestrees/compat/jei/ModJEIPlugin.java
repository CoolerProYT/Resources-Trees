package com.coolerpromc.resourcestrees.compat.jei;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.compat.jei.category.TreeSimulatorCategory;
import com.coolerpromc.resourcestrees.event.ModRecipeReceived;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.recipe.custom.StrictShapedRecipe;
import com.coolerpromc.resourcestrees.recipe.custom.TreeSimulatorRecipe;
import com.coolerpromc.resourcestrees.screen.custom.TreeSimulatorScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import mezz.jei.api.registration.*;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class ModJEIPlugin implements IModPlugin {
    @Override
    public Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(Constants.MODID, "jei_plugin");
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

        registration.addRecipes(TreeSimulatorCategory.TREE_SIMULATOR_TYPE, treeSimulatorRecipe.stream().toList());
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(TreeSimulatorScreen.class, 59, 35, 22, 16, TreeSimulatorCategory.TREE_SIMULATOR_TYPE);
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getCraftingCategory().addExtension(StrictShapedRecipe.class, new ICraftingCategoryExtension<>() {
            @Override
            public int getWidth(RecipeHolder<StrictShapedRecipe> recipeHolder) {
                return recipeHolder.value().getWidth();
            }

            @Override
            public int getHeight(RecipeHolder<StrictShapedRecipe> recipeHolder) {
                return recipeHolder.value().getHeight();
            }

            @Override
            public List<SlotDisplay> getIngredients(RecipeHolder<StrictShapedRecipe> recipeHolder) {
                List<RecipeDisplay> displays = recipeHolder.value().display();
                if (!displays.isEmpty() && displays.getFirst() instanceof ShapedCraftingRecipeDisplay shaped) {
                    return shaped.ingredients();
                }
                return List.of();
            }
        });
    }
}

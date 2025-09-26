package com.coolerpromc.resourcestrees.compat.rei;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.ResourcesTreesClient;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.compat.rei.category.TreeSimulatorCategory;
import com.coolerpromc.resourcestrees.compat.rei.display.TreeSimulatorDisplay;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.item.custom.LeafFragmentItem;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.recipe.custom.TreeSimulatorRecipe;
import com.coolerpromc.resourcestrees.screen.custom.TreeSimulatorScreen;
import com.coolerpromc.resourcestrees.util.RecipeViewerFiller;
import dev.architectury.event.EventResult;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModREIPlugin implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new TreeSimulatorCategory(), config -> config.addWorkstations(EntryStacks.of(ModBlocks.TREE_SIMULATOR)));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        List<RecipeEntry<TreeSimulatorRecipe>> treeSimulatorRecipe = new ArrayList<>(ResourcesTreesClient.recipeMap.getAll(ModRecipes.TREE_SIMULATOR_TYPE));
        List<RegistryKey<Recipe<?>>> keys = treeSimulatorRecipe.stream().map(RecipeEntry::id).toList();

        treeSimulatorRecipe.addAll(RecipeViewerFiller.addUndefinedRecipes(BasicDisplay.registryAccess(), keys));
        treeSimulatorRecipe.stream().map(RecipeEntry::value).map(TreeSimulatorDisplay::new).forEach(registry::add);

        registry.registerVisibilityPredicate((displayCategory, display) -> {
            if (display.getDisplayLocation().isPresent() && displayCategory.getCategoryIdentifier() == BuiltinPlugin.CRAFTING) {
                for (EntryIngredient ing : display.getInputEntries()){
                    if (!ing.isEmpty() && ing.getFirst().getValue() instanceof ItemStack stack) {
                        if (stack.getItem() instanceof LeafFragmentItem){
                            if (!stack.contains(ModDataComponents.TYPE)){
                                return EventResult.interrupt(false);
                            }
                            if (Objects.equals(stack.get(ModDataComponents.TYPE), ResourcesTrees.id("empty"))){
                                return EventResult.interrupt(false);
                            }
                        }
                    }
                }
            }
            return EventResult.interrupt(true);
        });
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerClickArea(screen -> new Rectangle(((screen.width - 176) / 2) + 59, ((screen.height - 166) / 2) + 27, 22, 15), TreeSimulatorScreen.class, TreeSimulatorDisplay.CATEGORY_IDENTIFIER);
    }
}

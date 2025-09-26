package com.coolerpromc.resourcestrees.compat.rei;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.compat.rei.category.TreeSimulatorCategory;
import com.coolerpromc.resourcestrees.compat.rei.display.TreeSimulatorDisplay;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.item.ModItems;
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
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomShapedDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@REIPluginClient
public class ModREIPlugin implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new TreeSimulatorCategory(), config -> config.addWorkstations(EntryStacks.of(ModBlocks.TREE_SIMULATOR.get())));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        List<RecipeHolder<TreeSimulatorRecipe>> treeSimulatorRecipe = new ArrayList<>(Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(ModRecipes.TREE_SIMULATOR_TYPE.get()));
        List<ResourceLocation> keys = treeSimulatorRecipe.stream().map(RecipeHolder::id).toList();

        treeSimulatorRecipe.addAll(RecipeViewerFiller.addUndefinedRecipes(BasicDisplay.registryAccess(), keys));
        treeSimulatorRecipe.stream().map(RecipeHolder::value).map(TreeSimulatorDisplay::new).forEach(registry::add);

        registry.registerVisibilityPredicate((displayCategory, display) -> {
            if (display.getDisplayLocation().isPresent() && displayCategory.getCategoryIdentifier() == BuiltinPlugin.CRAFTING) {
                for (EntryIngredient ing : display.getInputEntries()){
                    if (!ing.isEmpty() && ing.getFirst().getValue() instanceof ItemStack stack) {
                        if (stack.getItem() instanceof LeafFragmentItem){
                            if (!stack.has(ModDataComponents.TYPE)){
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

        registry.registerRecipeFiller(ShapedRecipe.class, RecipeType.CRAFTING, recipeEntry -> {
            ShapedRecipe recipe = recipeEntry.value();

            boolean usesComponents = recipe.getIngredients().stream()
                    .anyMatch(ing -> ing.getCustomIngredient() instanceof DataComponentIngredient);
            if (!usesComponents) return null;

            int width = recipe.getWidth();
            int height = recipe.getHeight();

            List<EntryIngredient> inputs = new ArrayList<>(width * height);

            for (Ingredient ing : recipe.getIngredients()) {
                if (ing.isEmpty()) {
                    inputs.add(EntryIngredient.empty());
                    continue;
                }

                if (ing.getCustomIngredient() instanceof DataComponentIngredient components) {
                    List<ItemStack> stacks = components.getItems().toList();
                    inputs.add(EntryIngredients.ofItemStacks(stacks));
                } else {
                    inputs.add(EntryIngredients.ofIngredient(ing));
                }
            }
            EntryIngredient output = EntryIngredients.of(recipe.result);
            return new DefaultCustomShapedDisplay(recipeEntry, inputs, List.of(output), width, height);
        });
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerClickArea(screen -> new Rectangle(((screen.width - 176) / 2) + 59, ((screen.height - 166) / 2) + 27, 22, 15), TreeSimulatorScreen.class, TreeSimulatorDisplay.CATEGORY_IDENTIFIER);
    }

    @Override
    public void registerItemComparators(ItemComparatorRegistry registry) {
        registry.registerComponents(ModBlocks.RESOURCES_OAK_SAPLING.asItem());
        registry.registerComponents(ModBlocks.RESOURCES_SPRUCE_SAPLING.asItem());
        registry.registerComponents(ModBlocks.RESOURCES_BIRCH_SAPLING.asItem());
        registry.registerComponents(ModBlocks.RESOURCES_JUNGLE_SAPLING.asItem());
        registry.registerComponents(ModBlocks.RESOURCES_ACACIA_SAPLING.asItem());
        registry.registerComponents(ModBlocks.RESOURCES_DARK_OAK_SAPLING.asItem());
        registry.registerComponents(ModBlocks.RESOURCES_CHERRY_SAPLING.asItem());

        registry.registerComponents(ModBlocks.RESOURCES_OAK_LEAVES.asItem());
        registry.registerComponents(ModBlocks.RESOURCES_SPRUCE_LEAVES.asItem());
        registry.registerComponents(ModBlocks.RESOURCES_BIRCH_LEAVES.asItem());
        registry.registerComponents(ModBlocks.RESOURCES_JUNGLE_LEAVES.asItem());
        registry.registerComponents(ModBlocks.RESOURCES_ACACIA_LEAVES.asItem());
        registry.registerComponents(ModBlocks.RESOURCES_DARK_OAK_LEAVES.asItem());
        registry.registerComponents(ModBlocks.RESOURCES_CHERRY_LEAVES.asItem());

        registry.registerComponents(ModItems.LEAF_FRAGMENT.get());
    }
}

/*
package com.coolerpromc.resourcestrees.compat.rei;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.item.ModItems;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.plugins.REICommonPlugin;
import me.shedaniel.rei.api.common.registry.display.ServerDisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.forge.REIPluginCommon;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomShapedDisplay;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Override
    public void registerDisplays(ServerDisplayRegistry registry) {
        registry.beginRecipeFiller(ShapedRecipe.class).fill(recipeEntry -> {
            ShapedRecipe recipe = recipeEntry.value();

            // 1) Only override REI for recipes that actually use fabric:components
            boolean usesComponents = recipe.getIngredients().stream()
                    .anyMatch(ing -> ing.isPresent() && ing.get().getCustomIngredient() instanceof DataComponentIngredient);
            if (!usesComponents) return null; // IMPORTANT: let REI handle normal recipes

            int width = recipe.getWidth();
            int height = recipe.getHeight();

            List<EntryIngredient> inputs = new ArrayList<>(width * height);

            for (Optional<Ingredient> ing : recipe.getIngredients()) {
                // preserve empty slots
                if (ing.isEmpty()) {
                    inputs.add(EntryIngredient.empty());
                    continue;
                }

                // if it's a Fabric CustomIngredient (ComponentsIngredient) -> use its toDisplay()
                if (ing.get().getCustomIngredient() instanceof DataComponentIngredient components) {
                    SlotDisplay display = components.display();

                    if (display instanceof SlotDisplay.ItemStackSlotDisplay(ItemStack stack)) {
                        inputs.add(EntryIngredients.of(stack));
                    } else if (display instanceof SlotDisplay.Composite(List<SlotDisplay> contents)) {
                        List<ItemStack> stacks = contents.stream()
                                .filter(d -> d instanceof SlotDisplay.ItemStackSlotDisplay)
                                .map(d -> ((SlotDisplay.ItemStackSlotDisplay) d).stack())
                                .toList();
                        inputs.add(EntryIngredients.ofItemStacks(stacks)); // keep your method name (ofItemStacks) if that matches your REI version
                    } else {
                        inputs.add(EntryIngredient.empty());
                    }
                } else {
                    // fallback to vanilla ingredient conversion
                    inputs.add(EntryIngredients.ofIngredient(ing.get()));
                }
            }

            // Build output entry (use method you had previously; adjust if your REI version differs)
            EntryIngredient output = EntryIngredients.of(recipe.result);
            Optional<Identifier> id = Optional.of(recipeEntry.id().location());

            // Return a shaped display only for component-using recipes
            return new DefaultCustomShapedDisplay(inputs, List.of(output), id, width, height);
        });
    }
}
*/

/*
package com.coolerpromc.resourcestrees.compat.rei.display;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.recipe.custom.TreeSimulatorRecipe;
import com.coolerpromc.resourcestrees.recipe.output.TreeSimulatorOutput;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record TreeSimulatorDisplay(TreeSimulatorRecipe treeSimulatorRecipe) implements Display {
    public static CategoryIdentifier<TreeSimulatorDisplay> CATEGORY_IDENTIFIER = CategoryIdentifier.of(ResourcesTrees.MODID, "tree_simulator");

    @Override
    public List<EntryIngredient> getInputEntries() {
        return List.of(EntryIngredients.of(treeSimulatorRecipe.tree()));
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return treeSimulatorRecipe.drops().stream().map(TreeSimulatorOutput::output).map(EntryIngredients::of).toList();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return CATEGORY_IDENTIFIER;
    }

    @Override
    public Optional<Identifier> getDisplayLocation() {
        return Optional.empty();
    }

    @Override
    public @Nullable DisplaySerializer<? extends Display> getSerializer() {
        return null;
    }
}
*/

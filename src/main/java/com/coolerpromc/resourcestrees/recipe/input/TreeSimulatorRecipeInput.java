package com.coolerpromc.resourcestrees.recipe.input;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

public record TreeSimulatorRecipeInput(ItemStack tree) implements RecipeInput {
    @Override
    public ItemStack getStackInSlot(int slot) {
        return tree;
    }

    @Override
    public int size() {
        return 1;
    }
}

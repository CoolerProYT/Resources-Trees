package com.coolerpromc.resourcestrees.recipe.input;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record TreeSimulatorRecipeInput(ItemStack tree) implements RecipeInput {
    @Override
    public ItemStack getItem(int i) {
        return tree;
    }

    @Override
    public int size() {
        return 1;
    }
}

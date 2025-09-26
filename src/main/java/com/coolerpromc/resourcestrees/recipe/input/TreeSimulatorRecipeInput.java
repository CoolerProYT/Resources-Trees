package com.coolerpromc.resourcestrees.recipe.input;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record TreeSimulatorRecipeInput(ItemStack tree) implements Container {

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return tree().isEmpty();
    }

    @Override
    public ItemStack getItem(int i) {
        return tree;
    }

    @Override
    public ItemStack removeItem(int i, int i1) {
        tree.shrink(i1);
        return tree;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return tree;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {

    }

    @Override
    public void setChanged() {

    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {

    }
}

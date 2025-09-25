package com.coolerpromc.resourcestrees.screen.container;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.function.Predicate;

public class OutputSlot extends SlotItemHandler {
    private final Predicate<ItemStack> canPlace;

    public OutputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, Predicate<ItemStack> canPlace) {
        super(itemHandler, index, xPosition, yPosition);
        this.canPlace = canPlace;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return canPlace.test(stack);
    }
}

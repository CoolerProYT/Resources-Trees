package com.coolerpromc.resourcestrees.screen.container;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

import java.util.function.Predicate;

public class OutputSlot extends Slot {
    private final Predicate<ItemStack> canPlace;

    public OutputSlot(Inventory itemHandler, int index, int xPosition, int yPosition, Predicate<ItemStack> canPlace) {
        super(itemHandler, index, xPosition, yPosition);
        this.canPlace = canPlace;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return canPlace.test(stack);
    }
}

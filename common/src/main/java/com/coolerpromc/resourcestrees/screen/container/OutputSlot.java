package com.coolerpromc.resourcestrees.screen.container;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

import java.util.function.Predicate;

public class OutputSlot extends ResourceHandlerSlot {
    private final Predicate<ItemStack> canPlace;

    public OutputSlot(ItemStacksResourceHandler itemHandler, int index, int xPosition, int yPosition, Predicate<ItemStack> canPlace) {
        super(itemHandler, itemHandler::set, index, xPosition, yPosition);
        this.canPlace = canPlace;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return canPlace.test(stack);
    }
}

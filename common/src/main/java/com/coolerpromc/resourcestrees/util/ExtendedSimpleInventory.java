package com.coolerpromc.resourcestrees.util;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class ExtendedSimpleInventory extends SimpleContainer {
    public ExtendedSimpleInventory(int size){
        super(size);
    }

    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else if (!this.canPlaceItem(slot, stack)) {
            return stack;
        } else {
            this.validateSlotIndex(slot);
            ItemStack existing = this.getItems().get(slot);
            int limit = existing.getMaxStackSize();
            if (!existing.isEmpty()) {
                if (!ItemStack.isSameItemSameComponents(stack, existing)) {
                    return stack;
                }

                limit -= existing.getCount();
            }

            if (limit <= 0) {
                return stack;
            } else {
                boolean reachedLimit = stack.getCount() > limit;
                if (!simulate) {
                    if (existing.isEmpty()) {
                        this.getItems().set(slot, reachedLimit ? stack.copyWithCount(limit) : stack);
                    } else {
                        existing.grow(reachedLimit ? limit : stack.getCount());
                    }
                }

                return reachedLimit ? stack.copyWithCount(stack.getCount() - limit) : ItemStack.EMPTY;
            }
        }
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= this.getItems().size()) {
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + this.getItems().size() + ")");
        }
    }

    public int getSlots() {
        return this.getItems().size();
    }
}

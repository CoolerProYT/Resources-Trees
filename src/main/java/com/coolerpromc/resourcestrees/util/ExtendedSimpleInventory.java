package com.coolerpromc.resourcestrees.util;

import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class ExtendedSimpleInventory extends SimpleInventory implements SidedInventory {
    public ExtendedSimpleInventory(int size){
        super(size);
    }

    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else if (!this.isValid(slot, stack)) {
            return stack;
        } else {
            this.validateSlotIndex(slot);
            ItemStack existing = this.heldStacks.get(slot);
            int limit = existing.getMaxCount();
            if (!existing.isEmpty()) {
                if (!ItemStack.areItemsAndComponentsEqual(stack, existing)) {
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
                        this.heldStacks.set(slot, reachedLimit ? stack.copyWithCount(limit) : stack);
                    } else {
                        existing.increment(reachedLimit ? limit : stack.getCount());
                    }
                }

                return reachedLimit ? stack.copyWithCount(stack.getCount() - limit) : ItemStack.EMPTY;
            }
        }
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= this.heldStacks.size()) {
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + this.heldStacks.size() + ")");
        }
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        int[] result = new int[size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }

        return result;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return true;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    public int getSlots() {
        return this.heldStacks.size();
    }
}

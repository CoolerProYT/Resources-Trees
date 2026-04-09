package com.coolerpromc.resourcestrees.platform.util;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public interface RegistryHandler<T> extends Supplier<T> {
    Identifier id();
    Holder<T> holder();

    default ItemStack toStack() {
        T value = get();
        if (value instanceof ItemLike itemLike) {
            return itemLike.asItem().getDefaultInstance();
        }
        throw new UnsupportedOperationException("Cannot create ItemStack from " + value);
    }
}
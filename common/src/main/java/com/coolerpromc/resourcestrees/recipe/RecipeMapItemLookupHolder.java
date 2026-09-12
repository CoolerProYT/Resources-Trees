package com.coolerpromc.resourcestrees.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;

public final class RecipeMapItemLookupHolder {
    public static final ThreadLocal<HolderLookup.RegistryLookup<Item>> CURRENT = new ThreadLocal<>();

    private RecipeMapItemLookupHolder() {
    }
}

package com.coolerpromc.resourcestrees.platform.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

public interface BlockRegistryHandler<T extends Block> extends RegistryHandler<T>, ItemLike {
    default ItemStack toStack(){
        return asItem().getDefaultInstance();
    }
    default ResourceKey<Block> key(){
        return (ResourceKey<Block>) holder().unwrapKey().orElse(null);
    }
}

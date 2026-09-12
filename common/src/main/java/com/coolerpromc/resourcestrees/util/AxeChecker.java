package com.coolerpromc.resourcestrees.util;

import net.minecraft.core.Holder;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.BlockTransformers;

public class AxeChecker {
    public static boolean isAxe(Item item){
        try{
            Holder<BlockTransformer> holder = item.components().get(DataComponents.BLOCK_TRANSFORMER);
            return holder != null && holder.is(BlockTransformers.AXE);
        }
        catch (Exception e){
            return item.builtInRegistryHolder().key().identifier().getPath().contains("axe");
        }
    }
}

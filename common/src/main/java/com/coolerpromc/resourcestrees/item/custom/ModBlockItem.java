package com.coolerpromc.resourcestrees.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class ModBlockItem extends BlockItem {
    public ModBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public Component getName(ItemStack itemStack) {
        return super.getName(itemStack).getString().equals(descriptionId) ? getBlock().getName() : super.getName(itemStack);
    }
}

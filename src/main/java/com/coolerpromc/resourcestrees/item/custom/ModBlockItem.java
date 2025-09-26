package com.coolerpromc.resourcestrees.item.custom;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModBlockItem extends BlockItem {
    public ModBlockItem(Block block, Settings properties) {
        super(block, properties);
    }

    @Override
    public Text getName(ItemStack stack) {
        Identifier type = stack.getOrDefault(ModDataComponents.TYPE, ResourcesTrees.id("empty"));
        ResourcesTypes resourcesTypes = ResourcesTypes.byId(type, null);
        if (Block.getBlockFromItem(stack.getItem()) instanceof ResourcesSaplingBlock || Block.getBlockFromItem(stack.getItem()) instanceof ResourcesLeavesBlock){
            return Text.translatable(resourcesTypes.translationKey()).append(" ").append(super.getName(stack));
        }
        return super.getName(stack);
    }
}

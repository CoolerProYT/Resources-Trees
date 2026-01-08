package com.coolerpromc.resourcestrees.item.custom;

import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;

public class ModBlockItem extends BlockItem {
    public ModBlockItem(Block block, Settings properties) {
        super(block, properties);
    }

    @Override
    public Text getName(ItemStack stack) {
        RegistryEntry<ResourcesTypes> type = stack.get(ModDataComponents.TYPE);
        if (type != null && (Block.getBlockFromItem(stack.getItem()) instanceof ResourcesSaplingBlock || Block.getBlockFromItem(stack.getItem()) instanceof ResourcesLeavesBlock)){
            return Text.translatable("type.resourcestrees." + type.getKey().get().getValue().getPath()).append(" ").append(super.getName(stack));
        }
        return super.getName(stack);
    }
}

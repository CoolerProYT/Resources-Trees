package com.coolerpromc.resourcestrees.item.custom;

import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class ModBlockItem extends BlockItem {
    public ModBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        Holder<ResourcesTypes> type = stack.get(ModDataComponents.TYPE.get());
        if (type != null && (Block.byItem(stack.getItem()) instanceof ResourcesSaplingBlock || Block.byItem(stack.getItem()) instanceof ResourcesLeavesBlock)){
            return Component.translatable("type.resourcestrees." + type.unwrapKey().get().identifier().getPath()).append(" ").append(super.getName(stack));
        }
        return super.getName(stack);
    }
}

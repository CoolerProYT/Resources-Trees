package com.coolerpromc.resourcestrees.item.custom;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class ModBlockItem extends BlockItem {
    public ModBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        ResourceLocation type = stack.getOrDefault(ModDataComponents.TYPE, ResourcesTrees.id("empty"));
        ResourcesTypes resourcesTypes = ResourcesTypes.byId(type, null);
        if (Block.byItem(stack.getItem()) instanceof ResourcesSaplingBlock || Block.byItem(stack.getItem()) instanceof ResourcesLeavesBlock){
            return Component.translatable(resourcesTypes.translationKey()).append(" ").append(super.getName(stack));
        }
        return super.getName(stack);
    }
}

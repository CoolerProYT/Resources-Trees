package com.coolerpromc.resourcestrees.item.custom;

import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModBlockItem extends BlockItem {
    public ModBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag p_40575_) {
        super.appendHoverText(stack, level, components, p_40575_);
        ResourceLocation type = new ResourceLocation(stack.getOrCreateTag().getString("type"));
        Holder<ResourcesTypes> resourcesTypes = ResourcesTypes.asHolder(level, type);
        if (resourcesTypes != null && (Block.byItem(stack.getItem()) instanceof ResourcesSaplingBlock || Block.byItem(stack.getItem()) instanceof ResourcesLeavesBlock)){
            components.set(0, Component.translatable(resourcesTypes.value().translationKey()).append(" ").append(super.getName(stack)));
        }
    }
}

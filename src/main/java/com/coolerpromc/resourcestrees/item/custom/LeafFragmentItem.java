package com.coolerpromc.resourcestrees.item.custom;

import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class LeafFragmentItem extends Item {
    public LeafFragmentItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        Holder<ResourcesTypes> resourcesTypes = stack.get(ModDataComponents.TYPE);
        if (resourcesTypes != null){
            return Component.translatable("type.resourcestrees." + resourcesTypes.getKey().identifier().getPath()).append(" ").append(super.getName(stack));
        }
        return super.getName(stack);
    }
}

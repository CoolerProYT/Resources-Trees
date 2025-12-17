package com.coolerpromc.resourcestrees.item.custom;

import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
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
        Holder<ResourcesTypes> resourcesTypes = stack.get(ModDataComponents.TYPE.get());
        if (resourcesTypes != null){
            return Component.translatable(resourcesTypes.value().translationKey()).append(" ").append(super.getName(stack));
        }
        return super.getName(stack);
    }
}

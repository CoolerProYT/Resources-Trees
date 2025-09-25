package com.coolerpromc.resourcestrees.item.custom;

import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class LeafFragmentItem extends Item {
    public LeafFragmentItem(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        ResourceLocation type = stack.get(ModDataComponents.TYPE);
        ResourcesTypes resourcesTypes = ResourcesTypes.byId(type, null);
        if (resourcesTypes != null){
            return Component.translatable(resourcesTypes.translationKey()).append(" ").append(super.getName(stack));
        }
        return super.getName(stack);
    }
}

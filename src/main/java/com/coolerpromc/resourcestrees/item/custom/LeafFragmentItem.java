package com.coolerpromc.resourcestrees.item.custom;

import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LeafFragmentItem extends Item {
    public LeafFragmentItem(Settings properties) {
        super(properties);
    }

    @Override
    public Text getName(ItemStack stack) {
        Identifier type = stack.get(ModDataComponents.TYPE);
        ResourcesTypes resourcesTypes = ResourcesTypes.byId(type, null);
        if (resourcesTypes != null){
            return Text.translatable(resourcesTypes.translationKey()).append(" ").append(super.getName(stack));
        }
        return super.getName(stack);
    }
}

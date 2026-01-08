package com.coolerpromc.resourcestrees.item.custom;

import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LeafFragmentItem extends Item {
    public LeafFragmentItem(Settings properties) {
        super(properties);
    }

    @Override
    public Text getName(ItemStack stack) {
        RegistryEntry<ResourcesTypes> resourcesTypes = stack.get(ModDataComponents.TYPE);
        if (resourcesTypes != null){
            return Text.translatable("type.resourcestrees." + resourcesTypes.getKey().get().getValue().getPath()).append(" ").append(super.getName(stack));
        }
        return super.getName(stack);
    }
}

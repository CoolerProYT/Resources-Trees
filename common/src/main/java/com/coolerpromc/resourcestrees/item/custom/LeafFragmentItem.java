package com.coolerpromc.resourcestrees.item.custom;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.api.resources.ResourcesType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class LeafFragmentItem extends Item {
    private final ResourcesType resourcesType;

    public LeafFragmentItem(Properties properties, ResourcesType resourcesType) {
        super(properties);
        this.resourcesType = resourcesType;
    }

    @Override
    public Component getName(ItemStack itemStack) {
        if (!Objects.equals(super.getName(itemStack).getString(), getDescriptionId())){
            return super.getName(itemStack);
        }
        return Component.translatable("item.resourcestrees.leaf_fragments",
                Constants.getOrFallback("resources_type.resourcestrees." + resourcesType.name(), resourcesType.name()),
                Constants.getOrFallback("item.resourcestrees.leaf_fragment", "Leaf Fragment"));
    }

    public ResourcesType getResourcesType() {
        return resourcesType;
    }
}

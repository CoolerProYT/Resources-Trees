package com.coolerpromc.resourcestrees.item.custom;

import com.coolerpromc.resourcestrees.api.resources.ResourcesType;
import net.minecraft.world.item.Item;

public class LeafFragmentItem extends Item {
    private final ResourcesType resourcesType;

    public LeafFragmentItem(Properties properties, ResourcesType resourcesType) {
        super(properties);
        this.resourcesType = resourcesType;
    }

    // TODO: See if this method still needed
    /*@Override
    public Component getName(ItemStack stack) {
        Holder<ResourcesType> resourcesTypes = stack.get(ModDataComponents.TYPE.get());
        if (resourcesTypes != null){
            return Component.translatable("type.resourcestrees." + resourcesTypes.unwrapKey().orElseThrow().identifier().getPath()).append(" ").append(super.getName(stack));
        }
        return super.getName(stack);
    }*/

    public ResourcesType getResourcesType() {
        return resourcesType;
    }
}

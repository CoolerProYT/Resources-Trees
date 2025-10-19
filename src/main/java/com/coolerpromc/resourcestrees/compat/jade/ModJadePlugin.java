package com.coolerpromc.resourcestrees.compat.jade;

import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.ui.IElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@WailaPlugin
public class ModJadePlugin implements IWailaPlugin {
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.addTooltipCollectedCallback((iTooltip, accessor) -> {
            if (accessor instanceof BlockAccessor blockAccessor && blockAccessor.getBlockEntity() instanceof ResourcesTypesBlockEntity blockEntity){
                if (blockEntity.getResourcesType() != null){
                    Holder<ResourcesTypes> resourcesTypes = ResourcesTypes.asHolder(blockAccessor.getLevel(), blockEntity.getResourcesType());
                    if (resourcesTypes != null){
                        List<Component> list = new ArrayList<>();
                        for (int i = 0;i < iTooltip.size(); i++){
                            if(i == 0){
                                list.add(Component.translatable(resourcesTypes.value().translationKey()).append(" " + iTooltip.get(i, IElement.Align.LEFT).get(0).getMessage()).withStyle(ChatFormatting.WHITE));
                            }
                            else{
                                list.add(Component.literal(Objects.requireNonNull(iTooltip.get(i, IElement.Align.LEFT).get(0).getMessage())));
                            }
                        }
                        iTooltip.clear();
                        iTooltip.addAll(list);
                    }
                }
            }
        });

        registration.addRayTraceCallback((hitResult, accessor, originalAccessor) -> {
            if (accessor instanceof BlockAccessor blockAccessor) {
                if (blockAccessor.getBlockEntity() instanceof ResourcesTypesBlockEntity blockEntity) {
                    ItemStack stack = blockAccessor.getBlock().asItem().getDefaultInstance();
                    stack.getOrCreateTag().putString("type", blockEntity.getResourcesType().toString());
                    return registration.blockAccessor().from(blockAccessor).fakeBlock(stack).build();
                }
            }
            return accessor;
        });
    }
}

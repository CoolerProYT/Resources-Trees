package com.coolerpromc.resourcestrees.compat.jade;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

@WailaPlugin
public class ModJadePlugin implements IWailaPlugin {
    @Override
    public void registerClient(IWailaClientRegistration registration) {

        registration.registerBlockIcon(new IBlockComponentProvider() {
            @Override
            public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
                if (blockAccessor.getBlockEntity() instanceof ResourcesTypesBlockEntity blockEntity){
                    if (blockEntity.getResourcesType() != null){
                        ResourcesTypes resourcesTypes = ResourcesTypes.getAllResourcesTypes(blockAccessor.getLevel()).getOrDefault(blockEntity.getResourcesType(), ResourcesTypes.EMPTY);
                        ItemStack stack = blockAccessor.getBlock().asItem().getDefaultInstance();
                        stack.getOrCreateTag().putString("type", blockEntity.getResourcesType().toString());
                        iTooltip.add(Component.translatable(resourcesTypes.translationKey()));
                    }
                }
            }

            @Override
            public ResourceLocation getUid() {
                return ResourcesTrees.id("resources_leaves");
            }
        }, ResourcesLeavesBlock.class);

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

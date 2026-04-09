package com.coolerpromc.resourcestrees.compat.jade;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
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
                        Holder<ResourcesTypes> resourcesTypes = blockEntity.getResourcesType();
                        if (resourcesTypes != null){
                            ItemStack stack = blockAccessor.getBlock().asItem().getDefaultInstance();
                            stack.set(ModDataComponents.TYPE.get(), blockEntity.getResourcesType());
                            iTooltip.add(Component.translatable("type.resourcestrees." + resourcesTypes.unwrapKey().orElseThrow().identifier().getPath()));
                        }
                    }
                }
            }

            @Override
            public Identifier getUid() {
                return Constants.id("resources_leaves");
            }
        }, ResourcesLeavesBlock.class);

        registration.addRayTraceCallback((hitResult, accessor, originalAccessor) -> {
            if (accessor instanceof BlockAccessor blockAccessor) {
                if (blockAccessor.getBlockEntity() instanceof ResourcesTypesBlockEntity blockEntity) {
                    ItemStack stack = blockAccessor.getBlock().asItem().getDefaultInstance();
                    if (blockEntity.getResourcesType() == null) return accessor;
                    stack.set(ModDataComponents.TYPE.get(), blockEntity.getResourcesType());
                    return registration.blockAccessor().from(blockAccessor).serversideRep(stack).build();
                }
            }
            return accessor;
        });
    }
}
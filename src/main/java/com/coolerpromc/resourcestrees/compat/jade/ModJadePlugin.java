package com.coolerpromc.resourcestrees.compat.jade;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
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
                        RegistryEntry<ResourcesTypes> resourcesTypes = blockEntity.getResourcesType();
                        if (resourcesTypes != null){
                            ItemStack stack = blockAccessor.getBlock().asItem().getDefaultStack();
                            stack.set(ModDataComponents.TYPE, blockEntity.getResourcesType());
                            iTooltip.add(Text.translatable(resourcesTypes.value().translationKey()));
                        }
                    }
                }
            }

            @Override
            public Identifier getUid() {
                return ResourcesTrees.id("resources_leaves");
            }
        }, ResourcesLeavesBlock.class);

        registration.addRayTraceCallback((hitResult, accessor, originalAccessor) -> {
            if (accessor instanceof BlockAccessor blockAccessor) {
                if (blockAccessor.getBlockEntity() instanceof ResourcesTypesBlockEntity blockEntity) {
                    ItemStack stack = blockAccessor.getBlock().asItem().getDefaultStack();
                    if (blockEntity.getResourcesType() == null) return accessor;
                    stack.set(ModDataComponents.TYPE, blockEntity.getResourcesType());
                    return registration.blockAccessor().from(blockAccessor).serversideRep(stack).build();
                }
            }
            return accessor;
        });
    }
}

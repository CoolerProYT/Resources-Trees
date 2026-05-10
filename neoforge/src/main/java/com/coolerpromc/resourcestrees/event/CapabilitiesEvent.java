package com.coolerpromc.resourcestrees.event;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import net.minecraft.world.Container;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;

@EventBusSubscriber(modid = Constants.MODID)
public class CapabilitiesEvent {
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.Item.BLOCK, ModBlockEntities.TREE_SIMULATOR_BE.get(),
                (be, direction) -> {
                    Container container = be.getHandlerForSide(direction);
                    return container != null ? VanillaContainerWrapper.of(container) : null;
                });
    }
}
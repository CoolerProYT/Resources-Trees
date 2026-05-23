package com.coolerpromc.resourcestrees.event;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.block.entity.custom.TreeSimulatorBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class CapabilitiesEvent {
    @SubscribeEvent
    public static void onRegisterCapabilities(AttachCapabilitiesEvent.BlockEntities event) {
        if (event.getObject() instanceof TreeSimulatorBlockEntity be){
            event.addCapability(Constants.id("tree_simulator"), new ICapabilityProvider() {
                @Override
                public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction direction) {
                    if (cap == ForgeCapabilities.ITEM_HANDLER){
                        Container container = be.getHandlerForSide(direction);
                        return container != null ? LazyOptional.of(() -> new InvWrapper(container)).cast() : LazyOptional.empty();
                    }
                    return LazyOptional.empty();
                }
            });
        }
    }
}
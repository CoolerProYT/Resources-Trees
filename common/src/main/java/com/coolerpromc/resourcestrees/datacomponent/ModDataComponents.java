package com.coolerpromc.resourcestrees.datacomponent;

import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.platform.Services;
import com.coolerpromc.resourcestrees.platform.util.RegistryHandler;
import com.coolerpromc.resourcestrees.registry.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.RegistryFileCodec;

public class ModDataComponents {
    public static final RegistryHandler<DataComponentType<Holder<ResourcesTypes>>> TYPE = Services.REGISTRY.registerDataComponent("resources_type", builder ->
            builder.persistent(RegistryFileCodec.create(ModRegistries.RESOURCES_TYPES_KEY, ResourcesTypes.CODEC)).networkSynchronized(ByteBufCodecs.holder(ModRegistries.RESOURCES_TYPES_KEY, ResourcesTypes.STREAM_CODEC)));

    public static void init() {
        // Force class loading to trigger static initializers
    }
}

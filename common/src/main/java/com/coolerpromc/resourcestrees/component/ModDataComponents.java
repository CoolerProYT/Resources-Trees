package com.coolerpromc.resourcestrees.component;

import com.coolerpromc.resourcestrees.api.resources.ResourcesType;
import com.coolerpromc.resourcestrees.platform.Services;
import com.coolerpromc.resourcestrees.platform.util.RegistryHandler;
import com.coolerpromc.resourcestrees.registry.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.RegistryFileCodec;

@Deprecated(forRemoval = true)
public class ModDataComponents {
    public static final RegistryHandler<DataComponentType<Holder<ResourcesType>>> LEGACY_TYPE = Services.REGISTRY.registerDataComponent("resources_type", builder ->
            builder.persistent(RegistryFileCodec.create(ModRegistries.RESOURCES_TYPES_KEY, ResourcesType.LEGACY_CODEC)).networkSynchronized(ByteBufCodecs.holder(ModRegistries.RESOURCES_TYPES_KEY, ResourcesType.LEGACY_STREAM_CODEC)));

    public static void init() {
        // Force class loading to trigger static initializers
    }
}
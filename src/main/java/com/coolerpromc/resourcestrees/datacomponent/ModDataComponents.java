package com.coolerpromc.resourcestrees.datacomponent;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.registry.ModRegistries;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.function.UnaryOperator;

public class ModDataComponents {
    public static final ComponentType<RegistryEntry<ResourcesTypes>> TYPE = register("resources_type", builder ->
            builder.codec(RegistryElementCodec.of(ModRegistries.RESOURCES_TYPES_KEY, ResourcesTypes.CODEC)).packetCodec(PacketCodecs.registryEntry(ModRegistries.RESOURCES_TYPES_KEY, ResourcesTypes.STREAM_CODEC)));

    private static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderUnaryOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, ResourcesTrees.id(name), builderUnaryOperator.apply(ComponentType.builder()).build());
    }

    public static void register() {
        ResourcesTrees.LOGGER.info("Registering data components.");
    }
}

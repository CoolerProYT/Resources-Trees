package com.coolerpromc.resourcestrees.datacomponent;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

public class ModDataComponents {
    public static final ComponentType<Identifier> TYPE = register("resources_type", builder -> builder.codec(Identifier.CODEC).packetCodec(Identifier.PACKET_CODEC));

    private static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderUnaryOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, ResourcesTrees.id(name), builderUnaryOperator.apply(ComponentType.builder()).build());
    }

    public static void register() {
        ResourcesTrees.LOGGER.info("Registering data components.");
    }
}

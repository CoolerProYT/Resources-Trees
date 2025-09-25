package com.coolerpromc.resourcestrees.datacomponent;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> REGISTRY = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, ResourcesTrees.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResourceLocation>> TYPE = register("resources_type", builder -> builder.persistent(ResourceLocation.CODEC).networkSynchronized(ResourceLocation.STREAM_CODEC));

    private static <T>DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderUnaryOperator) {
        return REGISTRY.register(name, () -> builderUnaryOperator.apply(DataComponentType.builder()).build());
    }
    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}

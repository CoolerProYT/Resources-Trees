package com.coolerpromc.resourcestrees.datacomponent;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.registry.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.UnaryOperator;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> REGISTRY = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, ResourcesTrees.MODID);

    public static final RegistryObject<DataComponentType<Holder<ResourcesTypes>>> TYPE = register("resources_type", builder ->
            builder.persistent(RegistryFileCodec.create(ModRegistries.RESOURCES_TYPES_KEY, ResourcesTypes.CODEC)).networkSynchronized(ByteBufCodecs.holder(ModRegistries.RESOURCES_TYPES_KEY, ResourcesTypes.STREAM_CODEC)));

    private static <T> RegistryObject<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderUnaryOperator) {
        return REGISTRY.register(name, () -> builderUnaryOperator.apply(DataComponentType.builder()).build());
    }
    public static void register(BusGroup eventBus) {
        REGISTRY.register(eventBus);
    }
}

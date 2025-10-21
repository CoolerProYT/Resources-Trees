package com.coolerpromc.resourcestrees.datagen.model;

import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.Nullable;

public record ResourcesTypeTintSource(int defaultColor) implements TintSource {
    public static final MapCodec<ResourcesTypeTintSource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codecs.ARGB.fieldOf("default").forGetter(ResourcesTypeTintSource::defaultColor)
    ).apply(instance, ResourcesTypeTintSource::new));

    @Override
    public int getTint(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity user) {
        RegistryEntry<ResourcesTypes> types = stack.get(ModDataComponents.TYPE);
        if (types != null){
            return types.value().color();
        }
        return defaultColor;
    }

    @Override
    public MapCodec<? extends TintSource> getCodec() {
        return MAP_CODEC;
    }
}
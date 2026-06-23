package com.coolerpromc.resourcestrees.client.tint;

import com.coolerpromc.resourcestrees.block.custom.AbstractResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.item.custom.LeafFragmentItem;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public record ResourcesTypeTintSource(int defaultColor) implements ItemTintSource {
    public static final MapCodec<ResourcesTypeTintSource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.ARGB_COLOR_CODEC.fieldOf("default").forGetter(ResourcesTypeTintSource::defaultColor)
    ).apply(instance, ResourcesTypeTintSource::new));

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        if (itemStack.getItem() instanceof LeafFragmentItem item){
            return item.getResourcesType().color();
        }
        if (Block.byItem(itemStack.getItem()) instanceof AbstractResourcesLeavesBlock leavesBlock){
            return leavesBlock.getResourcesType().color();
        }
        if (Block.byItem(itemStack.getItem()) instanceof ResourcesSaplingBlock saplingBlock){
            return saplingBlock.getResourcesType().color();
        }
        return defaultColor;
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}


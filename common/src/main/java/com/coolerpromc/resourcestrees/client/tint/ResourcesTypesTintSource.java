package com.coolerpromc.resourcestrees.client.tint;

import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ResourcesTypesTintSource implements BlockTintSource {
    @Override
    public int color(BlockState state) {
        return -1;
    }

    @Override
    public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ResourcesTypesBlockEntity be){
            return be.getColor();
        }
        return 0xFFFFFFFF;
    }
}
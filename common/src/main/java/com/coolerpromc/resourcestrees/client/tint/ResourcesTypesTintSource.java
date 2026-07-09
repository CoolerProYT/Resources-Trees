package com.coolerpromc.resourcestrees.client.tint;

import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ResourcesTypesTintSource implements BlockTintSource {
    @Override
    public int color(BlockState state) {
        if (state.getBlock() instanceof ResourcesSaplingBlock block){
            return block.getResourcesType().color();
        }
        if (state.getBlock() instanceof ResourcesLeavesBlock block){
            return block.getResourcesType().color();
        }
        return 0xFFFFFFFF;
    }

    @Override
    public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
        return color(state);
    }
}
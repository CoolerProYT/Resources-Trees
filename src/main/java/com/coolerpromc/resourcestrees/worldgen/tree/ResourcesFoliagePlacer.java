package com.coolerpromc.resourcestrees.worldgen.tree;

import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

import java.util.ArrayList;
import java.util.List;

public class ResourcesFoliagePlacer extends FoliagePlacer {
    private final FoliagePlacer vanilla;
    private final ResourcesTypes resourceType;

    public ResourcesFoliagePlacer(FoliagePlacer vanilla, ResourcesTypes type) {
        super(vanilla.radius, vanilla.offset);
        this.vanilla = vanilla;
        this.resourceType = type;
    }

    @Override
    public FoliagePlacerType<?> type() {
        return vanilla.type();
    }

    @Override
    public void createFoliage(LevelSimulatedReader reader, FoliageSetter setter, RandomSource random, TreeConfiguration config, int trunkHeight, FoliageAttachment attachment, int foliageHeight, int radius, int offset) {
        List<BlockPos> entityBlockPositions = new ArrayList<>();

        vanilla.createFoliage(reader, new FoliageSetter() {
            @Override
            public void set(BlockPos pos, BlockState state) {
                if (state.getBlock() instanceof EntityBlock) {
                    entityBlockPositions.add(pos.immutable());
                }

                setter.set(pos, state);
            }

            @Override
            public boolean isSet(BlockPos blockPos) {
                return setter.isSet(blockPos);
            }
        }, random, config, trunkHeight, attachment, foliageHeight, radius);


        if (reader instanceof ServerLevel serverLevel) {
            for (BlockPos pos : entityBlockPositions) {
                BlockEntity blockEntity = serverLevel.getBlockEntity(pos);
                if (blockEntity instanceof ResourcesTypesBlockEntity typeBe) {
                    typeBe.setResourcesType(resourceType);
                    typeBe.setChanged();

                    serverLevel.sendBlockUpdated(pos, blockEntity.getBlockState(), blockEntity.getBlockState(), Block.UPDATE_CLIENTS);
                }
            }
        }
    }

    @Override
    public int foliageHeight(RandomSource random, int trunkHeight, TreeConfiguration config) {
        return vanilla.foliageHeight(random, trunkHeight, config);
    }

    @Override
    public boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int radius, boolean large) {
        return vanilla.shouldSkipLocation(random, dx, y, dz, radius, large);
    }
}

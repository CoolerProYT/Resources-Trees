package com.coolerpromc.resourcestrees.worldgen.tree;

import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

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
    public FoliagePlacerType<?> getType() {
        return vanilla.getType();
    }

    protected void generate(TestableWorld world, BlockPlacer placer, Random random, TreeFeatureConfig config, int trunkHeight, TreeNode treeNode, int foliageHeight, int radius, int offset) {
        List<BlockPos> entityBlockPositions = new ArrayList<>();

        vanilla.generate(world, new BlockPlacer() {
            @Override
            public void placeBlock(BlockPos pos, BlockState state) {
                if (state.getBlock() instanceof BlockEntityProvider) {
                    entityBlockPositions.add(pos.toImmutable());
                }

                placer.placeBlock(pos, state);
            }

            @Override
            public boolean hasPlacedBlock(BlockPos pos) {
                return placer.hasPlacedBlock(pos);
            }
        }, random, config, trunkHeight, treeNode, foliageHeight, radius);


        if (world instanceof ServerWorld serverLevel) {
            for (BlockPos pos : entityBlockPositions) {
                BlockEntity blockEntity = serverLevel.getBlockEntity(pos);
                if (blockEntity instanceof ResourcesTypesBlockEntity typeBe) {
                    typeBe.setResourcesType(resourceType);
                    typeBe.markDirty();

                    serverLevel.updateListeners(pos, blockEntity.getCachedState(), blockEntity.getCachedState(), Block.NOTIFY_LISTENERS);
                }
            }
        }
    }

    public int getRandomHeight(Random random, int trunkHeight, TreeFeatureConfig config) {
        return vanilla.getRandomHeight(random, trunkHeight, config);
    }

    public boolean isInvalidForLeaves(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        return vanilla.isInvalidForLeaves(random, dx, y, dz, radius, giantTrunk);
    }
}

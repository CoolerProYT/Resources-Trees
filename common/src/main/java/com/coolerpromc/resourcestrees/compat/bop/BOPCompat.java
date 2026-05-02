package com.coolerpromc.resourcestrees.compat.bop;

import biomesoplenty.worldgen.feature.configurations.*;
import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class BOPCompat {
    public static boolean isBOPTreeConfig(TreeConfiguration configuration) {
        return configuration instanceof BOPTreeConfiguration;
    }

    public static TreeConfiguration getNewTreeConfiguration(TreeConfiguration configuration, Block leavesBlock) {
        BOPTreeConfiguration old = (BOPTreeConfiguration) configuration;
        BlockStateProvider foliage = BlockStateProvider.simple(leavesBlock);

        switch (old) {
            case BasicTreeConfiguration o -> {
                return copyBase(new BasicTreeConfiguration.Builder()
                        .leafLayers(o.leafLayers)
                        .leavesOffset(o.leavesOffset)
                        .maxLeavesRadius(o.maxLeavesRadius)
                        .leavesLayerHeight(o.leavesLayerHeight)
                        .hangingChance(o.hangingChance), o, foliage).build();
            }
            case BayouTreeConfiguration o -> {
                return copyBase(new BayouTreeConfiguration.Builder()
                        .trunkWidth(o.trunkWidth), o, foliage).build();
            }
            case BigTreeConfiguration o -> {
                return copyBase(new BigTreeConfiguration.Builder()
                        .trunkWidth(o.trunkWidth)
                        .foliageHeight(o.foliageHeight)
                        .foliageDensity((int) o.foliageDensity), o, foliage).build();
            }
            case CypressTreeConfiguration o -> {
                return copyBase(new CypressTreeConfiguration.Builder()
                        .leavesAtBottom(o.leavesAtBottom), o, foliage).build();
            }
            case EmpyrealTreeConfiguration o -> {
                return copyBase(new EmpyrealTreeConfiguration.Builder(), o, foliage).build();
            }
            case MagicTreeConfiguration o -> {
                return copyBase(new MagicTreeConfiguration.Builder(), o, foliage).build();
            }
            case MahoganyTreeConfiguration o -> {
                return copyBase(new MahoganyTreeConfiguration.Builder(), o, foliage).build();
            }
            case PalmTreeConfiguration o -> {
                return copyBase(new PalmTreeConfiguration.Builder(), o, foliage).build();
            }
            case PineTreeConfiguration o -> {
                return copyBase(new PineTreeConfiguration.Builder(), o, foliage).build();
            }
            case TaigaTreeConfiguration o -> {
                return copyBase(new TaigaTreeConfiguration.Builder()
                        .trunkWidth(o.trunkWidth), o, foliage).build();
            }
            case TwigletTreeConfiguration o -> {
                return copyBase(new TwigletTreeConfiguration.Builder()
                        .leafChance(o.leafChanceEven, o.leafChanceOdd), o, foliage).build();
            }
            default -> {
                BOPTreeConfiguration.Builder<?> b = new BOPTreeConfiguration.Builder<>();
                b.trunk(old.trunkProvider);
                b.foliage(foliage);
                b.vine(old.vineProvider);
                b.hanging(old.hangingProvider);
                b.trunkFruit(old.trunkFruitProvider);
                b.altFoliage(old.altFoliageProvider);
                b.minHeight(old.minHeight);
                b.maxHeight(old.maxHeight);
                old.decorators.forEach(b::decorator);
                return b.build();
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static boolean doPlace(ConfiguredFeature<?, ?> configuredFeature, TreeConfiguration config, WorldGenLevel level, ChunkGenerator generator, RandomSource random, BlockPos pos, Holder<ResourcesTypes> resourcesType) {
        Feature rawFeature = configuredFeature.feature();
        boolean result = rawFeature.place(config, level, generator, random, pos);

        if (result && level instanceof ServerLevel serverLevel && config instanceof BOPTreeConfiguration bopConfig) {
            int radius = 12;
            int height = bopConfig.maxHeight + 15;
            for (BlockPos scanPos : BlockPos.betweenClosed(pos.offset(-radius, 0, -radius), pos.offset(radius, height, radius))) {
                BlockEntity be = serverLevel.getBlockEntity(scanPos);
                if (be instanceof ResourcesTypesBlockEntity typeBe && typeBe.getResourcesType() == null) {
                    typeBe.setResourcesType(resourcesType);
                    typeBe.setChanged();
                    serverLevel.sendBlockUpdated(scanPos, be.getBlockState(), be.getBlockState(), Block.UPDATE_CLIENTS);
                }
            }
        }

        return result;
    }

    protected static <T extends BOPTreeConfiguration.Builder<T>> T copyBase(T builder, BOPTreeConfiguration old, BlockStateProvider foliage) {
        builder.trunk(old.trunkProvider)
                .foliage(foliage)
                .vine(old.vineProvider)
                .hanging(old.hangingProvider)
                .trunkFruit(old.trunkFruitProvider)
                .altFoliage(old.altFoliageProvider)
                .minHeight(old.minHeight)
                .maxHeight(old.maxHeight);
        old.decorators.forEach(builder::decorator);
        return builder;
    }
}

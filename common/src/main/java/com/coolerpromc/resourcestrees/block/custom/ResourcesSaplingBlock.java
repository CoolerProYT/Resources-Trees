package com.coolerpromc.resourcestrees.block.custom;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.api.resources.ResourcesType;
import com.coolerpromc.resourcestrees.api.tree.TreeType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.List;
import java.util.Objects;

public class ResourcesSaplingBlock extends SaplingBlock {
    private final ResourcesType resourcesType;
    private final TreeType treeType;

    public ResourcesSaplingBlock(Properties properties, ResourcesType resourcesType, TreeType treeType) {
        super(TreeGrower.GROWERS.get(treeType.treeGrowerName()), properties);
        this.resourcesType = resourcesType;
        this.treeType = treeType;
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> drops = super.getDrops(state, params);
        if (drops.isEmpty()){
            drops.add(this.asItem().getDefaultInstance());
        }
        return drops;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        if (state.getBlock() instanceof ResourcesSaplingBlock) {
            ResourceKey<Feature> resourcekey = treeGrower.getConfiguredMegaFeature(random);

            if(resourcekey != null){
                Holder<Feature> holder = level.registryAccess().lookupOrThrow(Registries.FEATURE).get(resourcekey).orElse(null);

                if (holder != null) {
                    Feature feature = holder.value();
                    for(int i = 0; i >= -1; --i) {
                        for(int j = 0; j >= -1; --j) {
                            if (isTwoByTwoSapling(state, level, pos, i, j)) {
                                BlockState blockstate = Blocks.AIR.defaultBlockState();
                                level.setBlock(pos.offset(i, 0, j), blockstate, 260);
                                level.setBlock(pos.offset(i + 1, 0, j), blockstate, 260);
                                level.setBlock(pos.offset(i, 0, j + 1), blockstate, 260);
                                level.setBlock(pos.offset(i + 1, 0, j + 1), blockstate, 260);
                                if (feature instanceof TreeFeature oldConfig){
                                    TreeFeature config = createNewTree(oldConfig);
                                    boolean success = config.place(level, level.getChunkSource().getGenerator(), random, pos.offset(i, 0, j));

                                    if (success) {
                                        return;
                                    }
                                }

                                level.setBlock(pos.offset(i, 0, j), state, 260);
                                level.setBlock(pos.offset(i + 1, 0, j), state, 260);
                                level.setBlock(pos.offset(i, 0, j + 1), state, 260);
                                level.setBlock(pos.offset(i + 1, 0, j + 1), state, 260);
                                return;
                            }
                        }
                    }
                }
            }

            ResourceKey<Feature> resourcekey1 = treeGrower.getConfiguredFeature(random, treeGrower.hasFlowers(level, pos));

            if (resourcekey1 != null){
                Holder<Feature> holder = level.registryAccess().lookupOrThrow(Registries.FEATURE).get(resourcekey1).orElse(null);

                if (holder != null){
                    Feature feature = holder.value();

                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 4);
                    if (feature instanceof TreeFeature oldConfig){
                        TreeFeature config = createNewTree(oldConfig);
                        boolean success = config.place(level, level.getChunkSource().getGenerator(), random, pos);

                        if (!success) {
                            level.setBlock(pos, state, 3);
                        }
                    }
                }

                return;
            }
        }

        super.advanceTree(level, pos, state, random);
    }

    public TreeFeature createNewTree(TreeFeature oldConfig){
        return new TreeFeature.Builder(
                oldConfig.trunkProvider(),
                oldConfig.trunkPlacer(),
                BlockStateProvider.simple(resourcesType.leavesBlock(treeType.name()).get()),
                oldConfig.foliagePlacer(),
                oldConfig.rootPlacer(),
                oldConfig.minimumSize(),
                oldConfig.belowTrunkProvider()
        ).build();
    }

    private static boolean isTwoByTwoSapling(BlockState state, BlockGetter level, BlockPos pos, int ox, int oz) {
        Block block = state.getBlock();
        return level.getBlockState(pos.offset(ox, 0, oz)).is(block) && level.getBlockState(pos.offset(ox + 1, 0, oz)).is(block) && level.getBlockState(pos.offset(ox, 0, oz + 1)).is(block) && level.getBlockState(pos.offset(ox + 1, 0, oz + 1)).is(block);
    }

    public ResourcesType getResourcesType() {
        return resourcesType;
    }

    public TreeType getTreeType() {
        return treeType;
    }

    @Override
    public MutableComponent getName() {
        if (!Objects.equals(super.getName().getString(), getDescriptionId())){
            return super.getName();
        }
        return Component.translatable("item.resourcestrees.trees",
                Constants.getOrFallback("resources_type.resourcestrees." + resourcesType.name(), resourcesType.name()),
                Constants.getOrFallback("tree_type.resourcestrees." + treeType.name(), treeType.name()),
                Constants.getOrFallback("item.resourcestrees.sapling", "Sapling"));
    }
}

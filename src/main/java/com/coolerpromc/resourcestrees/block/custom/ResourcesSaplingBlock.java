package com.coolerpromc.resourcestrees.block.custom;

import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.worldgen.tree.ResourcesFoliagePlacer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ResourcesSaplingBlock extends SaplingBlock implements EntityBlock {
    private final ResourceLocation leaves;

    public ResourcesSaplingBlock(AbstractTreeGrower treeGrower, Properties properties, ResourceLocation leaves) {
        super(treeGrower, properties);
        this.leaves = leaves;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> drops = super.getDrops(state, params);
        BlockEntity blockEntity = params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);

        if (blockEntity instanceof ResourcesTypesBlockEntity be){
            if (!drops.isEmpty() && be.getResourcesType() != null){
                drops.get(0).getOrCreateTag().putString("type", be.getResourcesType().toString());
            }
        }
        return drops;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ResourcesTypesBlockEntity be && be.getResourcesType() != null){
            ItemStack stack = super.getCloneItemStack(state, target, level, pos, player);
            stack.getOrCreateTag().putString("type", be.getResourcesType().toString());
            return stack;
        }
        return super.getCloneItemStack(state, target, level, pos, player);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ResourcesTypesBlockEntity be && stack.hasTag() && stack.getTag().contains("type")){
            be.setResourcesType(new ResourceLocation(stack.getTag().getString("type")));
        }
    }

    @Override
    public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (state.getBlock() instanceof ResourcesSaplingBlock && blockEntity instanceof ResourcesTypesBlockEntity be && be.getResourcesType() != null) {
            ResourceLocation type = be.getResourcesType();
            Holder<ResourcesTypes> typesHolder = ResourcesTypes.asHolder(level, type);
            if (typesHolder == null){
                super.advanceTree(level, pos, state, random);
                return;
            }
            ResourcesTypes resourcesTypes = typesHolder.value();
            ResourceKey<ConfiguredFeature<?, ?>> resourcekey = null;

            if (treeGrower instanceof AbstractMegaTreeGrower megaTreeGrower){
                resourcekey = megaTreeGrower.getConfiguredMegaFeature(random);
            }

            if(resourcekey != null){
                Holder<ConfiguredFeature<?, ?>> holder = level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(resourcekey).orElse(null);

                if (holder != null) {
                    ConfiguredFeature<?, ?> feature = holder.value();
                    for(int i = 0; i >= -1; --i) {
                        for(int j = 0; j >= -1; --j) {
                            if (isTwoByTwoSapling(state, level, pos, i, j)) {
                                BlockState blockstate = Blocks.AIR.defaultBlockState();
                                level.setBlock(pos.offset(i, 0, j), blockstate, 260);
                                level.setBlock(pos.offset(i + 1, 0, j), blockstate, 260);
                                level.setBlock(pos.offset(i, 0, j + 1), blockstate, 260);
                                level.setBlock(pos.offset(i + 1, 0, j + 1), blockstate, 260);
                                if (feature.config() instanceof TreeConfiguration oldConfig){
                                    TreeConfiguration config = createNewTree(type, oldConfig, random, pos, resourcesTypes.weight(), leaves);
                                    if (Feature.TREE.place(config, level, level.getChunkSource().getGenerator(), random, pos.offset(i, 0, j))) {
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

            ResourceKey<ConfiguredFeature<?, ?>> resourcekey1 = treeGrower.getConfiguredFeature(random, treeGrower.hasFlowers(level, pos));

            if (resourcekey1 != null){
                Holder<ConfiguredFeature<?, ?>> holder = level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(resourcekey1).orElse(null);

                if (holder != null){
                    ConfiguredFeature<?, ?> feature = holder.value();

                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 4);
                    if (feature.config() instanceof TreeConfiguration oldConfig){
                        TreeConfiguration config = createNewTree(type, oldConfig, random, pos, resourcesTypes.weight(), leaves);
                        boolean success = Feature.TREE.place(config, level, level.getChunkSource().getGenerator(), random, pos);

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

    public static TreeConfiguration createNewTree(ResourceLocation type, TreeConfiguration oldConfig, RandomSource randomSource, BlockPos pos, int weight, ResourceLocation leaves){
        Block block = BuiltInRegistries.BLOCK.get(leaves);

        return new TreeConfiguration.TreeConfigurationBuilder(
                oldConfig.trunkProvider,
                oldConfig.trunkPlacer,
                new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                        .add(block.defaultBlockState(), Math.max(weight, 1))
                        .build()),
                new ResourcesFoliagePlacer(oldConfig.foliagePlacer, type),
                oldConfig.minimumSize
        ).build();
    }

    public static boolean isTwoByTwoSapling(BlockState state, BlockGetter level, BlockPos pos, int xOffset, int yOffset) {
        Block block = state.getBlock();
        BlockEntity blockEntity = level.getBlockEntity(pos);
        BlockEntity blockEntity1 = level.getBlockEntity(pos.offset(xOffset, 0, yOffset));
        BlockEntity blockEntity2 = level.getBlockEntity(pos.offset(xOffset + 1, 0, yOffset));
        BlockEntity blockEntity3 = level.getBlockEntity(pos.offset(xOffset, 0, yOffset + 1));
        BlockEntity blockEntity4 = level.getBlockEntity(pos.offset(xOffset + 1, 0, yOffset + 1));

        if (blockEntity instanceof ResourcesTypesBlockEntity be && blockEntity1 instanceof ResourcesTypesBlockEntity be1 && blockEntity2 instanceof ResourcesTypesBlockEntity be2 && blockEntity3 instanceof ResourcesTypesBlockEntity be3 && blockEntity4 instanceof ResourcesTypesBlockEntity be4){
            ResourceLocation type = be.getResourcesType();
            ResourceLocation type1 = be1.getResourcesType();
            ResourceLocation type2 = be2.getResourcesType();
            ResourceLocation type3 = be3.getResourcesType();
            ResourceLocation type4 = be4.getResourcesType();

            BlockState state1 = level.getBlockState(pos.offset(xOffset, 0, yOffset));
            boolean cond1 = state1.is(block) && type1.equals(type);
            BlockState state2 = level.getBlockState(pos.offset(xOffset + 1, 0, yOffset));
            boolean cond2 = state2.is(block) && type2.equals(type);
            BlockState state3 = level.getBlockState(pos.offset(xOffset, 0, yOffset + 1));
            boolean cond3 = state3.is(block) && type3.equals(type);
            BlockState state4 = level.getBlockState(pos.offset(xOffset + 1, 0, yOffset + 1));
            boolean cond4 = state4.is(block) && type4.equals(type);
            return cond1 && cond2 && cond3 && cond4;
        }
        return false;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ResourcesTypesBlockEntity(blockPos, blockState);
    }

    public ItemStack getLeaves(ResourceLocation type) {
        ItemStack stack = BuiltInRegistries.ITEM.get(leaves).getDefaultInstance();
        stack.getOrCreateTag().putString("type", type.toString());
        return stack;
    }

    public ResourceLocation getLeaves() {
        return leaves;
    }

    public AbstractTreeGrower getTreeGrower(){
        return treeGrower;
    }
}

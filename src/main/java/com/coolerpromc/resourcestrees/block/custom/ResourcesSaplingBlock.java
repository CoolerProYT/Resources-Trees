package com.coolerpromc.resourcestrees.block.custom;

import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.worldgen.tree.ResourcesFoliagePlacer;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class ResourcesSaplingBlock extends SaplingBlock implements BlockEntityProvider {
    public static final MapCodec<ResourcesSaplingBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            SaplingGenerator.CODEC.fieldOf("tree").forGetter((p_304391_) -> p_304391_.generator),
            createSettingsCodec(),
            Identifier.CODEC.fieldOf("leaves").forGetter(block -> block.leaves)
    ).apply(instance, ResourcesSaplingBlock::new));

    private final Identifier leaves;

    public ResourcesSaplingBlock(SaplingGenerator treeGrower, Settings properties, Identifier leaves) {
        super(treeGrower, properties);
        this.leaves = leaves;
    }

    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, LootWorldContext.Builder builder) {
        List<ItemStack> drops = super.getDroppedStacks(state, builder);
        BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);

        if (blockEntity instanceof ResourcesTypesBlockEntity be){
            if (!drops.isEmpty() && be.getResourcesType() != null){
                drops.getFirst().set(ModDataComponents.TYPE, be.getResourcesType());
            }
        }
        return drops;
    }

    @Override
    protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ResourcesTypesBlockEntity be && be.getResourcesType() != null){
            ItemStack stack = super.getPickStack(world, pos, state, includeData);
            stack.set(ModDataComponents.TYPE, be.getResourcesType());
            return stack;
        }
        return super.getPickStack(world, pos, state, includeData);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ResourcesTypesBlockEntity be && itemStack.contains(ModDataComponents.TYPE)){
            be.setResourcesType(itemStack.get(ModDataComponents.TYPE));
        }
    }

    @Override
    public void generate(ServerWorld world, BlockPos pos, BlockState state, Random random) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (state.getBlock() instanceof ResourcesSaplingBlock && blockEntity instanceof ResourcesTypesBlockEntity be && be.getResourcesType() != null) {
            RegistryEntry<ResourcesTypes> resourcesTypes = be.getResourcesType();

            RegistryKey<ConfiguredFeature<?, ?>> resourcekey = generator.getMegaTreeFeature(random);

            if(resourcekey != null){
                RegistryEntry<ConfiguredFeature<?, ?>> holder = world.getRegistryManager().getOrThrow(RegistryKeys.CONFIGURED_FEATURE).getOptional(resourcekey).orElse(null);

                if (holder != null) {
                    ConfiguredFeature<?, ?> feature = holder.value();
                    for(int i = 0; i >= -1; --i) {
                        for(int j = 0; j >= -1; --j) {
                            if (isTwoByTwoSapling(state, world, pos, i, j)) {
                                BlockState blockstate = Blocks.AIR.getDefaultState();
                                world.setBlockState(pos.add(i, 0, j), blockstate, 260);
                                world.setBlockState(pos.add(i + 1, 0, j), blockstate, 260);
                                world.setBlockState(pos.add(i, 0, j + 1), blockstate, 260);
                                world.setBlockState(pos.add(i + 1, 0, j + 1), blockstate, 260);
                                if (feature.config() instanceof TreeFeatureConfig oldConfig){
                                    TreeFeatureConfig config = createNewTree(resourcesTypes, oldConfig, random, pos, resourcesTypes.value().weight(), leaves);
                                    if (Feature.TREE.generateIfValid(config, world, world.getChunkManager().getChunkGenerator(), random, pos.add(i, 0, j))) {
                                        return;
                                    }
                                }

                                world.setBlockState(pos.add(i, 0, j), state, 260);
                                world.setBlockState(pos.add(i + 1, 0, j), state, 260);
                                world.setBlockState(pos.add(i, 0, j + 1), state, 260);
                                world.setBlockState(pos.add(i + 1, 0, j + 1), state, 260);
                                return;
                            }
                        }
                    }
                }
            }

            RegistryKey<ConfiguredFeature<?, ?>> resourcekey1 = generator.getSmallTreeFeature(random, generator.areFlowersNearby(world, pos));

            if (resourcekey1 != null){
                RegistryEntry<ConfiguredFeature<?, ?>> holder = world.getRegistryManager().getOrThrow(RegistryKeys.CONFIGURED_FEATURE).getOptional(resourcekey1).orElse(null);

                if (holder != null){
                    ConfiguredFeature<?, ?> feature = holder.value();

                    world.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
                    if (feature.config() instanceof TreeFeatureConfig oldConfig){
                        TreeFeatureConfig config = createNewTree(resourcesTypes, oldConfig, random, pos, resourcesTypes.value().weight(), leaves);
                        boolean success = Feature.TREE.generateIfValid(config, world, world.getChunkManager().getChunkGenerator(), random, pos);

                        if (!success) {
                            world.setBlockState(pos, state, 3);
                        }
                    }
                }

                return;
            }
        }

        super.generate(world, pos, state, random);
    }

    public static TreeFeatureConfig createNewTree(RegistryEntry<ResourcesTypes> type, TreeFeatureConfig oldConfig, Random randomSource, BlockPos pos, int weight, Identifier leaves){
        Block block = Registries.BLOCK.get(leaves);

        return new TreeFeatureConfig.Builder(
                oldConfig.trunkProvider,
                oldConfig.trunkPlacer,
                new WeightedBlockStateProvider(Pool.<BlockState>builder()
                        .add(block.getDefaultState(), Math.max(weight, 1))
                        .build()),
                new ResourcesFoliagePlacer(oldConfig.foliagePlacer, type),
                oldConfig.minimumSize
        ).build();
    }

    public static boolean isTwoByTwoSapling(BlockState state, ServerWorld level, BlockPos pos, int xOffset, int yOffset) {
        Block block = state.getBlock();
        BlockEntity blockEntity = level.getBlockEntity(pos);
        BlockEntity blockEntity1 = level.getBlockEntity(pos.add(xOffset, 0, yOffset));
        BlockEntity blockEntity2 = level.getBlockEntity(pos.add(xOffset + 1, 0, yOffset));
        BlockEntity blockEntity3 = level.getBlockEntity(pos.add(xOffset, 0, yOffset + 1));
        BlockEntity blockEntity4 = level.getBlockEntity(pos.add(xOffset + 1, 0, yOffset + 1));

        if (blockEntity instanceof ResourcesTypesBlockEntity be && blockEntity1 instanceof ResourcesTypesBlockEntity be1 && blockEntity2 instanceof ResourcesTypesBlockEntity be2 && blockEntity3 instanceof ResourcesTypesBlockEntity be3 && blockEntity4 instanceof ResourcesTypesBlockEntity be4){
            RegistryEntry<ResourcesTypes> type = be.getResourcesType();
            RegistryEntry<ResourcesTypes> type1 = be1.getResourcesType();
            RegistryEntry<ResourcesTypes> type2 = be2.getResourcesType();
            RegistryEntry<ResourcesTypes> type3 = be3.getResourcesType();
            RegistryEntry<ResourcesTypes> type4 = be4.getResourcesType();

            BlockState state1 = level.getBlockState(pos.add(xOffset, 0, yOffset));
            boolean cond1 = state1.isOf(block) && Objects.equals(type1, type);
            BlockState state2 = level.getBlockState(pos.add(xOffset + 1, 0, yOffset));
            boolean cond2 = state2.isOf(block) && Objects.equals(type2, type);
            BlockState state3 = level.getBlockState(pos.add(xOffset, 0, yOffset + 1));
            boolean cond3 = state3.isOf(block) && Objects.equals(type3, type);
            BlockState state4 = level.getBlockState(pos.add(xOffset + 1, 0, yOffset + 1));
            boolean cond4 = state4.isOf(block) && Objects.equals(type4, type);
            return cond1 && cond2 && cond3 && cond4;
        }
        return false;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ResourcesTypesBlockEntity(pos, state);
    }

    public Identifier getLeaves() {
        return leaves;
    }
}

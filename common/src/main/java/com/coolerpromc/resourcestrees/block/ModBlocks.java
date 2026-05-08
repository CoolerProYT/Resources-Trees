package com.coolerpromc.resourcestrees.block;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.api.resources.ResourcesTypes;
import com.coolerpromc.resourcestrees.api.tree.TreeTypes;
import com.coolerpromc.resourcestrees.block.custom.*;
import com.coolerpromc.resourcestrees.platform.Services;
import com.coolerpromc.resourcestrees.platform.util.BlockRegistryHandler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ModBlocks {
    public static final List<BlockRegistryHandler<ResourcesSaplingBlock>> SAPLINGS = new ArrayList<>();
    public static final List<BlockRegistryHandler<ResourcesLeavesBlock>> LEAVES = new ArrayList<>();

    public static final BlockRegistryHandler<TreeSimulatorBlock> TREE_SIMULATOR = registerBlock("tree_simulator", TreeSimulatorBlock::new, BlockBehaviour.Properties.of().strength(3.0f).requiresCorrectToolForDrops());

    @Deprecated(forRemoval = true)
    public static final BlockRegistryHandler<LegacyResourcesSaplingBlock> RESOURCES_OAK_SAPLING =
            registerSaplingBlock("resources_oak_sapling", LegacyResourcesSaplingBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING));
    @Deprecated(forRemoval = true)
    public static final BlockRegistryHandler<LegacyResourcesLeavesBlock> RESOURCES_OAK_LEAVES =
            registerLeavesBlock("resources_oak_leaves", LegacyResourcesLeavesBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES));

    @Deprecated(forRemoval = true)
    public static final BlockRegistryHandler<LegacyResourcesSaplingBlock> RESOURCES_SPRUCE_SAPLING =
            registerSaplingBlock("resources_spruce_sapling", LegacyResourcesSaplingBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SAPLING));
    @Deprecated(forRemoval = true)
    public static final BlockRegistryHandler<LegacyResourcesLeavesBlock> RESOURCES_SPRUCE_LEAVES =
            registerLeavesBlock("resources_spruce_leaves", LegacyResourcesLeavesBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_LEAVES));

    @Deprecated(forRemoval = true)
    public static final BlockRegistryHandler<LegacyResourcesSaplingBlock> RESOURCES_BIRCH_SAPLING =
            registerSaplingBlock("resources_birch_sapling", LegacyResourcesSaplingBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_SAPLING));
    @Deprecated(forRemoval = true)
    public static final BlockRegistryHandler<LegacyResourcesLeavesBlock> RESOURCES_BIRCH_LEAVES =
            registerLeavesBlock("resources_birch_leaves", LegacyResourcesLeavesBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_LEAVES));

    @Deprecated(forRemoval = true)
    public static final BlockRegistryHandler<LegacyResourcesSaplingBlock> RESOURCES_JUNGLE_SAPLING =
            registerSaplingBlock("resources_jungle_sapling", LegacyResourcesSaplingBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_SAPLING));
    @Deprecated(forRemoval = true)
    public static final BlockRegistryHandler<LegacyResourcesLeavesBlock> RESOURCES_JUNGLE_LEAVES =
            registerLeavesBlock("resources_jungle_leaves", LegacyResourcesLeavesBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_LEAVES));

    @Deprecated(forRemoval = true)
    public static final BlockRegistryHandler<LegacyResourcesSaplingBlock> RESOURCES_ACACIA_SAPLING =
            registerSaplingBlock("resources_acacia_sapling", LegacyResourcesSaplingBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_SAPLING));
    @Deprecated(forRemoval = true)
    public static final BlockRegistryHandler<LegacyResourcesLeavesBlock> RESOURCES_ACACIA_LEAVES =
            registerLeavesBlock("resources_acacia_leaves", LegacyResourcesLeavesBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_LEAVES));

    @Deprecated(forRemoval = true)
    public static final BlockRegistryHandler<LegacyResourcesSaplingBlock> RESOURCES_DARK_OAK_SAPLING =
            registerSaplingBlock("resources_dark_oak_sapling", LegacyResourcesSaplingBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_SAPLING));
    @Deprecated(forRemoval = true)
    public static final BlockRegistryHandler<LegacyResourcesLeavesBlock> RESOURCES_DARK_OAK_LEAVES =
            registerLeavesBlock("resources_dark_oak_leaves", LegacyResourcesLeavesBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_LEAVES));

    @Deprecated(forRemoval = true)
    public static final BlockRegistryHandler<LegacyResourcesSaplingBlock> RESOURCES_CHERRY_SAPLING =
            registerSaplingBlock("resources_cherry_sapling", LegacyResourcesSaplingBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_SAPLING));
    @Deprecated(forRemoval = true)
    public static final BlockRegistryHandler<LegacyResourcesLeavesBlock> RESOURCES_CHERRY_LEAVES =
            registerLeavesBlock("resources_cherry_leaves", LegacyResourcesLeavesBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_LEAVES));

    @Deprecated(forRemoval = true)
    public static final BlockRegistryHandler<LegacyResourcesSaplingBlock> RESOURCES_PALE_OAK_SAPLING =
            registerSaplingBlock("resources_pale_oak_sapling", LegacyResourcesSaplingBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.PALE_OAK_SAPLING));
    @Deprecated(forRemoval = true)
    public static final BlockRegistryHandler<LegacyResourcesLeavesBlock> RESOURCES_PALE_OAK_LEAVES =
            registerLeavesBlock("resources_pale_oak_leaves", LegacyResourcesLeavesBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.PALE_OAK_LEAVES));


    private static <T extends Block> BlockRegistryHandler<T> registerLeavesBlock(String name, Function<BlockBehaviour.Properties, T> func, BlockBehaviour.Properties properties){
        return registerBlock(name, func, properties);
    }

    private static <T extends Block> BlockRegistryHandler<T> registerSaplingBlock(String name, Function<BlockBehaviour.Properties, T> func, BlockBehaviour.Properties properties){
        return registerBlock(name, func, properties);
    }

    private static <T extends Block> BlockRegistryHandler<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> func, BlockBehaviour.Properties properties){
        return Services.REGISTRY.registerBlock(name, func, properties);
    }

    public static void init() {
        TreeTypes.getTypes().forEach(treeType -> ResourcesTypes.getTypes().forEach(resourcesType -> {
            Block sapling = BuiltInRegistries.BLOCK.getValue(Identifier.parse(treeType.originalSapling()));
            Block leaves = BuiltInRegistries.BLOCK.getValue(Identifier.parse(treeType.originalLeaves()));
            BlockRegistryHandler<ResourcesSaplingBlock> saplingBlock = registerSaplingBlock(resourcesType.name() + "_" + treeType.name() + "_sapling", properties -> new ResourcesSaplingBlock(properties, resourcesType, treeType), BlockBehaviour.Properties.ofFullCopy(sapling));
            BlockRegistryHandler<ResourcesLeavesBlock> leavesBlock = registerLeavesBlock(resourcesType.name() + "_" + treeType.name() + "_leaves", properties -> new ResourcesLeavesBlock(properties.noOcclusion(), resourcesType, treeType), BlockBehaviour.Properties.ofFullCopy(leaves));
            LEAVES.add(leavesBlock);
            SAPLINGS.add(saplingBlock);
            resourcesType.setSaplingBlock(treeType.name(), saplingBlock);
            resourcesType.setLeavesBlock(treeType.name(), leavesBlock);
        }));
    }
}
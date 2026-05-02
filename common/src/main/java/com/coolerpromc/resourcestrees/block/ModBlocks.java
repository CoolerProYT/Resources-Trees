package com.coolerpromc.resourcestrees.block;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.block.custom.TreeSimulatorBlock;
import com.coolerpromc.resourcestrees.platform.Services;
import com.coolerpromc.resourcestrees.platform.util.BlockRegistryHandler;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ModBlocks {
    public static final List<BlockRegistryHandler<? extends Block>> SAPLINGS = new ArrayList<>();
    public static final List<BlockRegistryHandler<? extends Block>> LEAVES = new ArrayList<>();

    public static final BlockRegistryHandler<TreeSimulatorBlock> TREE_SIMULATOR = registerBlock("tree_simulator", TreeSimulatorBlock::new, BlockBehaviour.Properties.of());

    public static final BlockRegistryHandler<ResourcesSaplingBlock> RESOURCES_OAK_SAPLING =
            registerSaplingBlock("resources_oak_sapling", properties -> new ResourcesSaplingBlock(TreeGrower.OAK, properties, Constants.id("resources_oak_leaves")), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING));
    public static final BlockRegistryHandler<ResourcesLeavesBlock> RESOURCES_OAK_LEAVES =
            registerLeavesBlock("resources_oak_leaves", properties -> new ResourcesLeavesBlock(0.01f, properties, RESOURCES_OAK_SAPLING), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES));

    public static final BlockRegistryHandler<ResourcesSaplingBlock> RESOURCES_SPRUCE_SAPLING =
            registerSaplingBlock("resources_spruce_sapling", properties -> new ResourcesSaplingBlock(TreeGrower.SPRUCE, properties, Constants.id("resources_spruce_leaves")), BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SAPLING));
    public static final BlockRegistryHandler<ResourcesLeavesBlock> RESOURCES_SPRUCE_LEAVES =
            registerLeavesBlock("resources_spruce_leaves", properties -> new ResourcesLeavesBlock(0.01f, properties, RESOURCES_SPRUCE_SAPLING), BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_LEAVES));

    public static final BlockRegistryHandler<ResourcesSaplingBlock> RESOURCES_BIRCH_SAPLING =
            registerSaplingBlock("resources_birch_sapling", properties -> new ResourcesSaplingBlock(TreeGrower.BIRCH, properties, Constants.id("resources_birch_leaves")), BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_SAPLING));
    public static final BlockRegistryHandler<ResourcesLeavesBlock> RESOURCES_BIRCH_LEAVES =
            registerLeavesBlock("resources_birch_leaves", properties -> new ResourcesLeavesBlock(0.01f, properties, RESOURCES_BIRCH_SAPLING), BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_LEAVES));

    public static final BlockRegistryHandler<ResourcesSaplingBlock> RESOURCES_JUNGLE_SAPLING =
            registerSaplingBlock("resources_jungle_sapling", properties -> new ResourcesSaplingBlock(TreeGrower.JUNGLE, properties, Constants.id("resources_jungle_leaves")), BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_SAPLING));
    public static final BlockRegistryHandler<ResourcesLeavesBlock> RESOURCES_JUNGLE_LEAVES =
            registerLeavesBlock("resources_jungle_leaves", properties -> new ResourcesLeavesBlock(0.01f, properties, RESOURCES_JUNGLE_SAPLING), BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_LEAVES));

    public static final BlockRegistryHandler<ResourcesSaplingBlock> RESOURCES_ACACIA_SAPLING =
            registerSaplingBlock("resources_acacia_sapling", properties -> new ResourcesSaplingBlock(TreeGrower.ACACIA, properties, Constants.id("resources_acacia_leaves")), BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_SAPLING));
    public static final BlockRegistryHandler<ResourcesLeavesBlock> RESOURCES_ACACIA_LEAVES =
            registerLeavesBlock("resources_acacia_leaves", properties -> new ResourcesLeavesBlock(0.01f, properties, RESOURCES_ACACIA_SAPLING), BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_LEAVES));

    public static final BlockRegistryHandler<ResourcesSaplingBlock> RESOURCES_DARK_OAK_SAPLING =
            registerSaplingBlock("resources_dark_oak_sapling", properties -> new ResourcesSaplingBlock(TreeGrower.DARK_OAK, properties, Constants.id("resources_dark_oak_leaves")), BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_SAPLING));
    public static final BlockRegistryHandler<ResourcesLeavesBlock> RESOURCES_DARK_OAK_LEAVES =
            registerLeavesBlock("resources_dark_oak_leaves", properties -> new ResourcesLeavesBlock(0.01f, properties, RESOURCES_DARK_OAK_SAPLING), BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_LEAVES));

    public static final BlockRegistryHandler<ResourcesSaplingBlock> RESOURCES_CHERRY_SAPLING =
            registerSaplingBlock("resources_cherry_sapling", properties -> new ResourcesSaplingBlock(TreeGrower.CHERRY, properties, Constants.id("resources_cherry_leaves")), BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_SAPLING));
    public static final BlockRegistryHandler<ResourcesLeavesBlock> RESOURCES_CHERRY_LEAVES =
            registerLeavesBlock("resources_cherry_leaves", properties -> new ResourcesLeavesBlock(0.01f, properties, RESOURCES_CHERRY_SAPLING), BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_LEAVES));

    public static final BlockRegistryHandler<ResourcesSaplingBlock> RESOURCES_PALE_OAK_SAPLING =
            registerSaplingBlock("resources_pale_oak_sapling", properties -> new ResourcesSaplingBlock(TreeGrower.PALE_OAK, properties, Constants.id("resources_pale_oak_leaves")), BlockBehaviour.Properties.ofFullCopy(Blocks.PALE_OAK_SAPLING));
    public static final BlockRegistryHandler<ResourcesLeavesBlock> RESOURCES_PALE_OAK_LEAVES =
            registerLeavesBlock("resources_pale_oak_leaves", properties -> new ResourcesLeavesBlock(0.01f, properties, RESOURCES_PALE_OAK_SAPLING), BlockBehaviour.Properties.ofFullCopy(Blocks.PALE_OAK_LEAVES));

    private static <T extends Block> BlockRegistryHandler<T> registerLeavesBlock(String name, Function<BlockBehaviour.Properties, T> func, BlockBehaviour.Properties properties){
        BlockRegistryHandler<T> block = registerBlock(name, func, properties);
        LEAVES.add(block);
        return block;
    }

    private static <T extends Block> BlockRegistryHandler<T> registerSaplingBlock(String name, Function<BlockBehaviour.Properties, T> func, BlockBehaviour.Properties properties){
        BlockRegistryHandler<T> block = registerBlock(name, func, properties);
        SAPLINGS.add(block);
        return block;
    }

    private static <T extends Block> BlockRegistryHandler<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> func, BlockBehaviour.Properties properties){
        return Services.REGISTRY.registerBlock(name, func, properties);
    }

    public static void init() {

    }
}
package com.coolerpromc.resourcestrees.block;

import com.coolerpromc.resourcestrees.api.resources.ResourcesTypes;
import com.coolerpromc.resourcestrees.api.tree.TreeTypes;
import com.coolerpromc.resourcestrees.block.custom.*;
import com.coolerpromc.resourcestrees.platform.Services;
import com.coolerpromc.resourcestrees.platform.util.BlockRegistryHandler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ModBlocks {
    public static final List<BlockRegistryHandler<ResourcesSaplingBlock>> SAPLINGS = new ArrayList<>();
    public static final List<BlockRegistryHandler<AbstractResourcesLeavesBlock>> LEAVES = new ArrayList<>();

    public static final BlockRegistryHandler<TreeSimulatorBlock> TREE_SIMULATOR = registerBlock("tree_simulator", TreeSimulatorBlock::new, BlockBehaviour.Properties.of().strength(3.0f).requiresCorrectToolForDrops());

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
            BlockRegistryHandler<AbstractResourcesLeavesBlock> leavesBlock = registerLeavesBlock(resourcesType.name() + "_" + treeType.name() + "_leaves", properties -> treeType.particle() > 0f ? new ResourcesTintedParticlesLeavesBlock(properties.noOcclusion(), resourcesType, treeType) : new ResourcesLeavesBlock(properties.noOcclusion(), resourcesType, treeType), BlockBehaviour.Properties.ofFullCopy(leaves));
            LEAVES.add(leavesBlock);
            SAPLINGS.add(saplingBlock);
            resourcesType.setSaplingBlock(treeType.name(), saplingBlock);
            resourcesType.setLeavesBlock(treeType.name(), leavesBlock);
        }));
    }
}
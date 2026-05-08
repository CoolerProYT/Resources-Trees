package com.coolerpromc.resourcestrees.block.entity;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.entity.custom.LegacyResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.block.entity.custom.TreeSimulatorBlockEntity;
import com.coolerpromc.resourcestrees.platform.Services;
import com.coolerpromc.resourcestrees.platform.util.RegistryHandler;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    @SuppressWarnings("unchecked")
    public static final RegistryHandler<BlockEntityType<TreeSimulatorBlockEntity>> TREE_SIMULATOR_BE = Services.REGISTRY.registerBlockEntity("tree_simulator_be", TreeSimulatorBlockEntity::new, ModBlocks.TREE_SIMULATOR);

    @SuppressWarnings("unchecked")
    @Deprecated(forRemoval = true)
    public static final RegistryHandler<BlockEntityType<LegacyResourcesTypesBlockEntity>> RESOURCES_TYPE_BE =
            Services.REGISTRY.registerBlockEntity("resources_type_be", LegacyResourcesTypesBlockEntity::new,
                    ModBlocks.RESOURCES_OAK_SAPLING, ModBlocks.RESOURCES_SPRUCE_SAPLING, ModBlocks.RESOURCES_BIRCH_SAPLING,
                    ModBlocks.RESOURCES_JUNGLE_SAPLING, ModBlocks.RESOURCES_ACACIA_SAPLING, ModBlocks.RESOURCES_DARK_OAK_SAPLING,
                    ModBlocks.RESOURCES_CHERRY_SAPLING, ModBlocks.RESOURCES_PALE_OAK_SAPLING,
                    ModBlocks.RESOURCES_OAK_LEAVES, ModBlocks.RESOURCES_SPRUCE_LEAVES, ModBlocks.RESOURCES_BIRCH_LEAVES,
                    ModBlocks.RESOURCES_JUNGLE_LEAVES, ModBlocks.RESOURCES_ACACIA_LEAVES, ModBlocks.RESOURCES_DARK_OAK_LEAVES,
                    ModBlocks.RESOURCES_CHERRY_LEAVES, ModBlocks.RESOURCES_PALE_OAK_LEAVES);


    public static void init() {
        // Force class loading to trigger static initializers
    }
}

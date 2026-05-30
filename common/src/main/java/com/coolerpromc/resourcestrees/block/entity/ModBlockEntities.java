package com.coolerpromc.resourcestrees.block.entity;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.entity.custom.TreeSimulatorBlockEntity;
import com.coolerpromc.resourcestrees.platform.Services;
import com.coolerpromc.resourcestrees.platform.util.RegistryHandler;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    @SuppressWarnings("unchecked")
    public static final RegistryHandler<BlockEntityType<TreeSimulatorBlockEntity>> TREE_SIMULATOR_BE = Services.REGISTRY.registerBlockEntity("tree_simulator_be", TreeSimulatorBlockEntity::new, ModBlocks.TREE_SIMULATOR);

    public static void init() {
        // Force class loading to trigger static initializers
    }
}

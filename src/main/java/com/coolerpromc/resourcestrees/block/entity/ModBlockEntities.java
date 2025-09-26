package com.coolerpromc.resourcestrees.block.entity;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.block.entity.custom.TreeSimulatorBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlockEntities {
    public static final BlockEntityType<ResourcesTypesBlockEntity> RESOURCES_TYPE_BE = register("resources_type_be",
            FabricBlockEntityTypeBuilder.create(ResourcesTypesBlockEntity::new,
                    ModBlocks.RESOURCES_OAK_SAPLING,
                    ModBlocks.RESOURCES_SPRUCE_SAPLING,
                    ModBlocks.RESOURCES_BIRCH_SAPLING,
                    ModBlocks.RESOURCES_JUNGLE_SAPLING,
                    ModBlocks.RESOURCES_ACACIA_SAPLING,
                    ModBlocks.RESOURCES_DARK_OAK_SAPLING,
                    ModBlocks.RESOURCES_CHERRY_SAPLING,
                    ModBlocks.RESOURCES_PALE_OAK_SAPLING,
                    ModBlocks.RESOURCES_OAK_LEAVES,
                    ModBlocks.RESOURCES_SPRUCE_LEAVES,
                    ModBlocks.RESOURCES_BIRCH_LEAVES,
                    ModBlocks.RESOURCES_JUNGLE_LEAVES,
                    ModBlocks.RESOURCES_ACACIA_LEAVES,
                    ModBlocks.RESOURCES_DARK_OAK_LEAVES,
                    ModBlocks.RESOURCES_CHERRY_LEAVES,
                    ModBlocks.RESOURCES_PALE_OAK_LEAVES).build());

    public static final BlockEntityType<TreeSimulatorBlockEntity> TREE_SIMULATOR_BE = register("tree_simulator_be", FabricBlockEntityTypeBuilder.create(TreeSimulatorBlockEntity::new, ModBlocks.TREE_SIMULATOR).build());

    public static <T extends BlockEntityType<?>> T register(String name, T blockEntity){
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, ResourcesTrees.id(name), blockEntity);
    }

    public static void register() {
        ResourcesTrees.LOGGER.info("Registering block entities.");
    }
}

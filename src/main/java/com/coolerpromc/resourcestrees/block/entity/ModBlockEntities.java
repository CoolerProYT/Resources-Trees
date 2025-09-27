package com.coolerpromc.resourcestrees.block.entity;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.block.entity.custom.TreeSimulatorBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;

import java.util.Set;
import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ResourcesTrees.MODID);

    public static final Supplier<BlockEntityType<ResourcesTypesBlockEntity>> RESOURCES_TYPE_BE = BLOCK_ENTITIES.register("resources_type_be",
            () -> new BlockEntityType<>(ResourcesTypesBlockEntity::new,
                    Set.of(
                            ModBlocks.RESOURCES_OAK_SAPLING.get(),
                            ModBlocks.RESOURCES_SPRUCE_SAPLING.get(),
                            ModBlocks.RESOURCES_BIRCH_SAPLING.get(),
                            ModBlocks.RESOURCES_JUNGLE_SAPLING.get(),
                            ModBlocks.RESOURCES_ACACIA_SAPLING.get(),
                            ModBlocks.RESOURCES_DARK_OAK_SAPLING.get(),
                            ModBlocks.RESOURCES_CHERRY_SAPLING.get(),
                            ModBlocks.RESOURCES_PALE_OAK_SAPLING.get(),
                            ModBlocks.RESOURCES_OAK_LEAVES.get(),
                            ModBlocks.RESOURCES_SPRUCE_LEAVES.get(),
                            ModBlocks.RESOURCES_BIRCH_LEAVES.get(),
                            ModBlocks.RESOURCES_JUNGLE_LEAVES.get(),
                            ModBlocks.RESOURCES_ACACIA_LEAVES.get(),
                            ModBlocks.RESOURCES_DARK_OAK_LEAVES.get(),
                            ModBlocks.RESOURCES_CHERRY_LEAVES.get(),
                            ModBlocks.RESOURCES_PALE_OAK_LEAVES.get()
                    )
            )
    );

    public static final Supplier<BlockEntityType<TreeSimulatorBlockEntity>> TREE_SIMULATOR_BE = BLOCK_ENTITIES.register("tree_simulator_be", () -> new BlockEntityType<>(TreeSimulatorBlockEntity::new, Set.of(ModBlocks.TREE_SIMULATOR.get())));

    public static void register(BusGroup eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}

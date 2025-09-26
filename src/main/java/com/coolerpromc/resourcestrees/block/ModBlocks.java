package com.coolerpromc.resourcestrees.block;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.block.custom.TreeSimulatorBlock;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.item.custom.ModBlockItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, ResourcesTrees.MODID);

    public static final RegistryObject<TreeSimulatorBlock> TREE_SIMULATOR = registerBlock("tree_simulator", TreeSimulatorBlock::new, BlockBehaviour.Properties.of());

    public static final RegistryObject<ResourcesSaplingBlock> RESOURCES_OAK_SAPLING =
            registerBlock("resources_oak_sapling", properties -> new ResourcesSaplingBlock(new OakTreeGrower(), properties, ResourcesTrees.id("resources_oak_leaves")), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING));
    public static final RegistryObject<ResourcesLeavesBlock> RESOURCES_OAK_LEAVES =
            registerBlock("resources_oak_leaves", properties -> new ResourcesLeavesBlock(properties, RESOURCES_OAK_SAPLING), BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES));

    public static final RegistryObject<ResourcesSaplingBlock> RESOURCES_SPRUCE_SAPLING =
            registerBlock("resources_spruce_sapling", properties -> new ResourcesSaplingBlock(new SpruceTreeGrower(), properties, ResourcesTrees.id("resources_spruce_leaves")), BlockBehaviour.Properties.copy(Blocks.SPRUCE_SAPLING));
    public static final RegistryObject<ResourcesLeavesBlock> RESOURCES_SPRUCE_LEAVES =
            registerBlock("resources_spruce_leaves", properties -> new ResourcesLeavesBlock(properties, RESOURCES_SPRUCE_SAPLING), BlockBehaviour.Properties.copy(Blocks.SPRUCE_LEAVES));

    public static final RegistryObject<ResourcesSaplingBlock> RESOURCES_BIRCH_SAPLING =
            registerBlock("resources_birch_sapling", properties -> new ResourcesSaplingBlock(new BirchTreeGrower(), properties, ResourcesTrees.id("resources_birch_leaves")), BlockBehaviour.Properties.copy(Blocks.BIRCH_SAPLING));
    public static final RegistryObject<ResourcesLeavesBlock> RESOURCES_BIRCH_LEAVES =
            registerBlock("resources_birch_leaves", properties -> new ResourcesLeavesBlock(properties, RESOURCES_BIRCH_SAPLING), BlockBehaviour.Properties.copy(Blocks.BIRCH_LEAVES));

    public static final RegistryObject<ResourcesSaplingBlock> RESOURCES_JUNGLE_SAPLING =
            registerBlock("resources_jungle_sapling", properties -> new ResourcesSaplingBlock(new JungleTreeGrower(), properties, ResourcesTrees.id("resources_jungle_leaves")), BlockBehaviour.Properties.copy(Blocks.JUNGLE_SAPLING));
    public static final RegistryObject<ResourcesLeavesBlock> RESOURCES_JUNGLE_LEAVES =
            registerBlock("resources_jungle_leaves", properties -> new ResourcesLeavesBlock(properties, RESOURCES_JUNGLE_SAPLING), BlockBehaviour.Properties.copy(Blocks.JUNGLE_LEAVES));

    public static final RegistryObject<ResourcesSaplingBlock> RESOURCES_ACACIA_SAPLING =
            registerBlock("resources_acacia_sapling", properties -> new ResourcesSaplingBlock(new AcaciaTreeGrower(), properties, ResourcesTrees.id("resources_acacia_leaves")), BlockBehaviour.Properties.copy(Blocks.ACACIA_SAPLING));
    public static final RegistryObject<ResourcesLeavesBlock> RESOURCES_ACACIA_LEAVES =
            registerBlock("resources_acacia_leaves", properties -> new ResourcesLeavesBlock(properties, RESOURCES_ACACIA_SAPLING), BlockBehaviour.Properties.copy(Blocks.ACACIA_LEAVES));

    public static final RegistryObject<ResourcesSaplingBlock> RESOURCES_DARK_OAK_SAPLING =
            registerBlock("resources_dark_oak_sapling", properties -> new ResourcesSaplingBlock(new DarkOakTreeGrower(), properties, ResourcesTrees.id("resources_dark_oak_leaves")), BlockBehaviour.Properties.copy(Blocks.DARK_OAK_SAPLING));
    public static final RegistryObject<ResourcesLeavesBlock> RESOURCES_DARK_OAK_LEAVES =
            registerBlock("resources_dark_oak_leaves", properties -> new ResourcesLeavesBlock(properties, RESOURCES_DARK_OAK_SAPLING), BlockBehaviour.Properties.copy(Blocks.DARK_OAK_LEAVES));

    public static final RegistryObject<ResourcesSaplingBlock> RESOURCES_CHERRY_SAPLING =
            registerBlock("resources_cherry_sapling", properties -> new ResourcesSaplingBlock(new CherryTreeGrower(), properties, ResourcesTrees.id("resources_cherry_leaves")), BlockBehaviour.Properties.copy(Blocks.CHERRY_SAPLING));
    public static final RegistryObject<ResourcesLeavesBlock> RESOURCES_CHERRY_LEAVES =
            registerBlock("resources_cherry_leaves", properties -> new ResourcesLeavesBlock(properties, RESOURCES_CHERRY_SAPLING), BlockBehaviour.Properties.copy(Blocks.CHERRY_LEAVES));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends T> func, BlockBehaviour.Properties properties){
        RegistryObject<T> block = BLOCKS.register(name, () -> func.apply(properties));
        registerBlockItem(name, block);
        return block;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block){
        ModItems.ITEMS.register(name, () -> new ModBlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
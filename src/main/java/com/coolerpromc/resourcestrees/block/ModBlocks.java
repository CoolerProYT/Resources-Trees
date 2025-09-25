package com.coolerpromc.resourcestrees.block;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.block.custom.TreeSimulatorBlock;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.item.custom.ModBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ResourcesTrees.MODID);

    public static final DeferredBlock<TreeSimulatorBlock> TREE_SIMULATOR = registerBlock("tree_simulator", TreeSimulatorBlock::new, BlockBehaviour.Properties.of());

    public static final DeferredBlock<ResourcesSaplingBlock> RESOURCES_OAK_SAPLING =
            registerBlock("resources_oak_sapling", properties -> new ResourcesSaplingBlock(TreeGrower.OAK, properties, ResourcesTrees.id("resources_oak_leaves")), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING));
    public static final DeferredBlock<ResourcesLeavesBlock> RESOURCES_OAK_LEAVES =
            registerBlock("resources_oak_leaves", properties -> new ResourcesLeavesBlock(properties, RESOURCES_OAK_SAPLING), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES));

    public static final DeferredBlock<ResourcesSaplingBlock> RESOURCES_SPRUCE_SAPLING =
            registerBlock("resources_spruce_sapling", properties -> new ResourcesSaplingBlock(TreeGrower.SPRUCE, properties, ResourcesTrees.id("resources_spruce_leaves")), BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SAPLING));
    public static final DeferredBlock<ResourcesLeavesBlock> RESOURCES_SPRUCE_LEAVES =
            registerBlock("resources_spruce_leaves", properties -> new ResourcesLeavesBlock(properties, RESOURCES_SPRUCE_SAPLING), BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_LEAVES));

    public static final DeferredBlock<ResourcesSaplingBlock> RESOURCES_BIRCH_SAPLING =
            registerBlock("resources_birch_sapling", properties -> new ResourcesSaplingBlock(TreeGrower.BIRCH, properties, ResourcesTrees.id("resources_birch_leaves")), BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_SAPLING));
    public static final DeferredBlock<ResourcesLeavesBlock> RESOURCES_BIRCH_LEAVES =
            registerBlock("resources_birch_leaves", properties -> new ResourcesLeavesBlock(properties, RESOURCES_BIRCH_SAPLING), BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_LEAVES));

    public static final DeferredBlock<ResourcesSaplingBlock> RESOURCES_JUNGLE_SAPLING =
            registerBlock("resources_jungle_sapling", properties -> new ResourcesSaplingBlock(TreeGrower.JUNGLE, properties, ResourcesTrees.id("resources_jungle_leaves")), BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_SAPLING));
    public static final DeferredBlock<ResourcesLeavesBlock> RESOURCES_JUNGLE_LEAVES =
            registerBlock("resources_jungle_leaves", properties -> new ResourcesLeavesBlock(properties, RESOURCES_JUNGLE_SAPLING), BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_LEAVES));

    public static final DeferredBlock<ResourcesSaplingBlock> RESOURCES_ACACIA_SAPLING =
            registerBlock("resources_acacia_sapling", properties -> new ResourcesSaplingBlock(TreeGrower.ACACIA, properties, ResourcesTrees.id("resources_acacia_leaves")), BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_SAPLING));
    public static final DeferredBlock<ResourcesLeavesBlock> RESOURCES_ACACIA_LEAVES =
            registerBlock("resources_acacia_leaves", properties -> new ResourcesLeavesBlock(properties, RESOURCES_ACACIA_SAPLING), BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_LEAVES));

    public static final DeferredBlock<ResourcesSaplingBlock> RESOURCES_DARK_OAK_SAPLING =
            registerBlock("resources_dark_oak_sapling", properties -> new ResourcesSaplingBlock(TreeGrower.DARK_OAK, properties, ResourcesTrees.id("resources_dark_oak_leaves")), BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_SAPLING));
    public static final DeferredBlock<ResourcesLeavesBlock> RESOURCES_DARK_OAK_LEAVES =
            registerBlock("resources_dark_oak_leaves", properties -> new ResourcesLeavesBlock(properties, RESOURCES_DARK_OAK_SAPLING), BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_LEAVES));

    public static final DeferredBlock<ResourcesSaplingBlock> RESOURCES_CHERRY_SAPLING =
            registerBlock("resources_cherry_sapling", properties -> new ResourcesSaplingBlock(TreeGrower.CHERRY, properties, ResourcesTrees.id("resources_cherry_leaves")), BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_SAPLING));
    public static final DeferredBlock<ResourcesLeavesBlock> RESOURCES_CHERRY_LEAVES =
            registerBlock("resources_cherry_leaves", properties -> new ResourcesLeavesBlock(properties, RESOURCES_CHERRY_SAPLING), BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_LEAVES));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends T> func, BlockBehaviour.Properties properties){
        DeferredBlock<T> block = BLOCKS.registerBlock(name, func, properties);
        registerBlockItem(name, block);
        return block;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block){
        ModItems.ITEMS.registerItem(name, properties -> new ModBlockItem(block.get(), properties));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
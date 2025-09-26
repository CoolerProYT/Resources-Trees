package com.coolerpromc.resourcestrees.block;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.block.custom.TreeSimulatorBlock;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.item.custom.ModBlockItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingGenerator;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Function;

public class ModBlocks {
    public static final TreeSimulatorBlock TREE_SIMULATOR = registerBlock("tree_simulator", TreeSimulatorBlock::new, AbstractBlock.Settings.create());

    public static final ResourcesSaplingBlock RESOURCES_OAK_SAPLING =
            registerBlock("resources_oak_sapling", properties -> new ResourcesSaplingBlock(SaplingGenerator.OAK, properties, ResourcesTrees.id("resources_oak_leaves")), AbstractBlock.Settings.copy(Blocks.OAK_SAPLING));
    public static final ResourcesLeavesBlock RESOURCES_OAK_LEAVES =
            registerBlock("resources_oak_leaves", properties -> new ResourcesLeavesBlock(0.01f, properties, () -> RESOURCES_OAK_SAPLING), AbstractBlock.Settings.copy(Blocks.OAK_LEAVES));

    public static final ResourcesSaplingBlock RESOURCES_SPRUCE_SAPLING =
            registerBlock("resources_spruce_sapling", properties -> new ResourcesSaplingBlock(SaplingGenerator.SPRUCE, properties, ResourcesTrees.id("resources_spruce_leaves")), AbstractBlock.Settings.copy(Blocks.SPRUCE_SAPLING));
    public static final ResourcesLeavesBlock RESOURCES_SPRUCE_LEAVES =
            registerBlock("resources_spruce_leaves", properties -> new ResourcesLeavesBlock(0.01f, properties, () -> RESOURCES_SPRUCE_SAPLING), AbstractBlock.Settings.copy(Blocks.SPRUCE_LEAVES));

    public static final ResourcesSaplingBlock RESOURCES_BIRCH_SAPLING =
            registerBlock("resources_birch_sapling", properties -> new ResourcesSaplingBlock(SaplingGenerator.BIRCH, properties, ResourcesTrees.id("resources_birch_leaves")), AbstractBlock.Settings.copy(Blocks.BIRCH_SAPLING));
    public static final ResourcesLeavesBlock RESOURCES_BIRCH_LEAVES =
            registerBlock("resources_birch_leaves", properties -> new ResourcesLeavesBlock(0.01f, properties, () -> RESOURCES_BIRCH_SAPLING), AbstractBlock.Settings.copy(Blocks.BIRCH_LEAVES));

    public static final ResourcesSaplingBlock RESOURCES_JUNGLE_SAPLING =
            registerBlock("resources_jungle_sapling", properties -> new ResourcesSaplingBlock(SaplingGenerator.JUNGLE, properties, ResourcesTrees.id("resources_jungle_leaves")), AbstractBlock.Settings.copy(Blocks.JUNGLE_SAPLING));
    public static final ResourcesLeavesBlock RESOURCES_JUNGLE_LEAVES =
            registerBlock("resources_jungle_leaves", properties -> new ResourcesLeavesBlock(0.01f, properties, () -> RESOURCES_JUNGLE_SAPLING), AbstractBlock.Settings.copy(Blocks.JUNGLE_LEAVES));

    public static final ResourcesSaplingBlock RESOURCES_ACACIA_SAPLING =
            registerBlock("resources_acacia_sapling", properties -> new ResourcesSaplingBlock(SaplingGenerator.ACACIA, properties, ResourcesTrees.id("resources_acacia_leaves")), AbstractBlock.Settings.copy(Blocks.ACACIA_SAPLING));
    public static final ResourcesLeavesBlock RESOURCES_ACACIA_LEAVES =
            registerBlock("resources_acacia_leaves", properties -> new ResourcesLeavesBlock(0.01f, properties, () -> RESOURCES_ACACIA_SAPLING), AbstractBlock.Settings.copy(Blocks.ACACIA_LEAVES));

    public static final ResourcesSaplingBlock RESOURCES_DARK_OAK_SAPLING =
            registerBlock("resources_dark_oak_sapling", properties -> new ResourcesSaplingBlock(SaplingGenerator.DARK_OAK, properties, ResourcesTrees.id("resources_dark_oak_leaves")), AbstractBlock.Settings.copy(Blocks.DARK_OAK_SAPLING));
    public static final ResourcesLeavesBlock RESOURCES_DARK_OAK_LEAVES =
            registerBlock("resources_dark_oak_leaves", properties -> new ResourcesLeavesBlock(0.01f, properties, () -> RESOURCES_DARK_OAK_SAPLING), AbstractBlock.Settings.copy(Blocks.DARK_OAK_LEAVES));

    public static final ResourcesSaplingBlock RESOURCES_CHERRY_SAPLING =
            registerBlock("resources_cherry_sapling", properties -> new ResourcesSaplingBlock(SaplingGenerator.CHERRY, properties, ResourcesTrees.id("resources_cherry_leaves")), AbstractBlock.Settings.copy(Blocks.CHERRY_SAPLING));
    public static final ResourcesLeavesBlock RESOURCES_CHERRY_LEAVES =
            registerBlock("resources_cherry_leaves", properties -> new ResourcesLeavesBlock(0.01f, properties, () -> RESOURCES_CHERRY_SAPLING), AbstractBlock.Settings.copy(Blocks.CHERRY_LEAVES));

    public static final ResourcesSaplingBlock RESOURCES_PALE_OAK_SAPLING =
            registerBlock("resources_pale_oak_sapling", properties -> new ResourcesSaplingBlock(SaplingGenerator.PALE_OAK, properties, ResourcesTrees.id("resources_pale_oak_leaves")), AbstractBlock.Settings.copy(Blocks.PALE_OAK_SAPLING));
    public static final ResourcesLeavesBlock RESOURCES_PALE_OAK_LEAVES =
            registerBlock("resources_pale_oak_leaves", properties -> new ResourcesLeavesBlock(0.01f, properties, () -> RESOURCES_PALE_OAK_SAPLING), AbstractBlock.Settings.copy(Blocks.PALE_OAK_LEAVES));

    private static <T extends Block> T registerBlock(String name, Function<AbstractBlock.Settings, ? extends T> func, AbstractBlock.Settings properties){
        T block = Registry.register(Registries.BLOCK, ResourcesTrees.id(name), func.apply(properties.registryKey(RegistryKey.of(RegistryKeys.BLOCK, ResourcesTrees.id(name)))));
        registerBlockItem(name, block);
        return block;
    }

    private static <T extends Block> void registerBlockItem(String name, T block){
        ModItems.registerItem(name, properties -> new ModBlockItem(block, properties.useBlockPrefixedTranslationKey()));
    }

    public static void register(){
        ResourcesTrees.LOGGER.info("Registering blocks.");
    }
}
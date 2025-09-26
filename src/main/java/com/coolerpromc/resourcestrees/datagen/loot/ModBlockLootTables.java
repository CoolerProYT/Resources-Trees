package com.coolerpromc.resourcestrees.datagen.loot;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModBlockLootTables extends FabricBlockLootTableProvider {
    public ModBlockLootTables(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.RESOURCES_OAK_SAPLING);
        shearOrSilkTouchOnlyDrop(ModBlocks.RESOURCES_OAK_LEAVES);

        addDrop(ModBlocks.RESOURCES_SPRUCE_SAPLING);
        shearOrSilkTouchOnlyDrop(ModBlocks.RESOURCES_SPRUCE_LEAVES);

        addDrop(ModBlocks.RESOURCES_BIRCH_SAPLING);
        shearOrSilkTouchOnlyDrop(ModBlocks.RESOURCES_BIRCH_LEAVES);

        addDrop(ModBlocks.RESOURCES_JUNGLE_SAPLING);
        shearOrSilkTouchOnlyDrop(ModBlocks.RESOURCES_JUNGLE_LEAVES);

        addDrop(ModBlocks.RESOURCES_ACACIA_SAPLING);
        shearOrSilkTouchOnlyDrop(ModBlocks.RESOURCES_ACACIA_LEAVES);

        addDrop(ModBlocks.RESOURCES_DARK_OAK_SAPLING);
        shearOrSilkTouchOnlyDrop(ModBlocks.RESOURCES_DARK_OAK_LEAVES);

        addDrop(ModBlocks.RESOURCES_CHERRY_SAPLING);
        shearOrSilkTouchOnlyDrop(ModBlocks.RESOURCES_CHERRY_LEAVES);

        addDrop(ModBlocks.RESOURCES_PALE_OAK_SAPLING);
        shearOrSilkTouchOnlyDrop(ModBlocks.RESOURCES_PALE_OAK_LEAVES);

        addDrop(ModBlocks.TREE_SIMULATOR);
    }

    protected void shearOrSilkTouchOnlyDrop(Block block){
        addDrop(block, dropsWithSilkTouchOrShears(block));
    }
}

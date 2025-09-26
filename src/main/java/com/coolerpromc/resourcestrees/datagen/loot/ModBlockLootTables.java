package com.coolerpromc.resourcestrees.datagen.loot;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.RESOURCES_OAK_SAPLING.get());
        shearOrSilkTouchOnlyDrop(ModBlocks.RESOURCES_OAK_LEAVES.get());

        dropSelf(ModBlocks.RESOURCES_SPRUCE_SAPLING.get());
        shearOrSilkTouchOnlyDrop(ModBlocks.RESOURCES_SPRUCE_LEAVES.get());

        dropSelf(ModBlocks.RESOURCES_BIRCH_SAPLING.get());
        shearOrSilkTouchOnlyDrop(ModBlocks.RESOURCES_BIRCH_LEAVES.get());

        dropSelf(ModBlocks.RESOURCES_JUNGLE_SAPLING.get());
        shearOrSilkTouchOnlyDrop(ModBlocks.RESOURCES_JUNGLE_LEAVES.get());

        dropSelf(ModBlocks.RESOURCES_ACACIA_SAPLING.get());
        shearOrSilkTouchOnlyDrop(ModBlocks.RESOURCES_ACACIA_LEAVES.get());

        dropSelf(ModBlocks.RESOURCES_DARK_OAK_SAPLING.get());
        shearOrSilkTouchOnlyDrop(ModBlocks.RESOURCES_DARK_OAK_LEAVES.get());

        dropSelf(ModBlocks.RESOURCES_CHERRY_SAPLING.get());
        shearOrSilkTouchOnlyDrop(ModBlocks.RESOURCES_CHERRY_LEAVES.get());

        dropSelf(ModBlocks.TREE_SIMULATOR.get());
    }

    protected void shearOrSilkTouchOnlyDrop(Block block){
        add(block, createShearsOrSilkTouchOnlyDrop(block));
    }

    protected LootTable.Builder createShearsOrSilkTouchOnlyDrop(ItemLike item) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(HAS_SHEARS_OR_SILK_TOUCH).add(LootItem.lootTableItem(item)));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.stream()
                .filter(block -> Optional.of(BuiltInRegistries.BLOCK.getKey(block))
                .filter(key -> key.getNamespace().equals(ResourcesTrees.MODID)).isPresent()).collect(Collectors.toSet());
    }
}

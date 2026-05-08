package com.coolerpromc.resourcestrees.datagen.loot;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.item.custom.LeafFragmentItem;
import com.coolerpromc.resourcestrees.platform.util.BlockRegistryHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        ModBlocks.SAPLINGS.stream().map(BlockRegistryHandler::get).forEach(this::dropSelf);
        ModBlocks.LEAVES.stream().map(BlockRegistryHandler::get).forEach(block -> this.add(block, createResourceLeavesDrops(block)));
        dropSelf(ModBlocks.TREE_SIMULATOR.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.stream()
                .filter(block -> Optional.of(BuiltInRegistries.BLOCK.getKey(block))
                .filter(key -> key.getNamespace().equals(Constants.MODID)).isPresent()).collect(Collectors.toSet());
    }

    protected LootTable.Builder createResourceLeavesDrops(ResourcesLeavesBlock original) {
        return this.createSilkTouchOrShearsDispatchTable(original,
                        this.applyExplosionCondition(original, LootItem.lootTableItem(original.getResourcesType().saplingBlock(original.getTreeType().name()).get())
                                .when(LootItemRandomChanceCondition.randomChance(original.getResourcesType().saplingDropChance()))))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when((this.hasShears().or(this.hasSilkTouch())).invert())
                        .add(this.applyExplosionCondition(original, LootItem.lootTableItem(original.getResourcesType().leafFragmentItem().get())
                                .when(LootItemRandomChanceCondition.randomChance(original.getResourcesType().leafDropChance())))))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when((this.hasShears().or(this.hasSilkTouch())).invert())
                        .add(this.applyExplosionCondition(original, LootItem.lootTableItem(original.getResourcesType().leafFragmentItem().get())
                                .when(LootItemRandomChanceCondition.randomChance(original.getResourcesType().leafDropChance() * 0.5F)))));
    }
}
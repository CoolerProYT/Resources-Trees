package com.coolerpromc.resourcestrees.datagen;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends FabricTagProvider

        .BlockTagProvider {
    public ModBlockTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.getOrCreateTagBuilder(BlockTags.SAPLINGS)
                .add(ModBlocks.RESOURCES_OAK_SAPLING)
                .add(ModBlocks.RESOURCES_SPRUCE_SAPLING)
                .add(ModBlocks.RESOURCES_BIRCH_SAPLING)
                .add(ModBlocks.RESOURCES_JUNGLE_SAPLING)
                .add(ModBlocks.RESOURCES_ACACIA_SAPLING)
                .add(ModBlocks.RESOURCES_DARK_OAK_SAPLING)
                .add(ModBlocks.RESOURCES_CHERRY_SAPLING);

        this.getOrCreateTagBuilder(BlockTags.LEAVES)
                .add(ModBlocks.RESOURCES_OAK_LEAVES)
                .add(ModBlocks.RESOURCES_SPRUCE_LEAVES)
                .add(ModBlocks.RESOURCES_BIRCH_LEAVES)
                .add(ModBlocks.RESOURCES_JUNGLE_LEAVES)
                .add(ModBlocks.RESOURCES_ACACIA_LEAVES)
                .add(ModBlocks.RESOURCES_DARK_OAK_LEAVES)
                .add(ModBlocks.RESOURCES_CHERRY_LEAVES);
    }
}

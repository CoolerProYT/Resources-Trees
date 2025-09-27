package com.coolerpromc.resourcestrees.datagen;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, ResourcesTrees.MODID, null);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.SAPLINGS)
                .add(ModBlocks.RESOURCES_OAK_SAPLING.get())
                .add(ModBlocks.RESOURCES_SPRUCE_SAPLING.get())
                .add(ModBlocks.RESOURCES_BIRCH_SAPLING.get())
                .add(ModBlocks.RESOURCES_JUNGLE_SAPLING.get())
                .add(ModBlocks.RESOURCES_ACACIA_SAPLING.get())
                .add(ModBlocks.RESOURCES_DARK_OAK_SAPLING.get())
                .add(ModBlocks.RESOURCES_CHERRY_SAPLING.get())
                .add(ModBlocks.RESOURCES_PALE_OAK_SAPLING.get());

        this.tag(BlockTags.LEAVES)
                .add(ModBlocks.RESOURCES_OAK_LEAVES.get())
                .add(ModBlocks.RESOURCES_SPRUCE_LEAVES.get())
                .add(ModBlocks.RESOURCES_BIRCH_LEAVES.get())
                .add(ModBlocks.RESOURCES_JUNGLE_LEAVES.get())
                .add(ModBlocks.RESOURCES_ACACIA_LEAVES.get())
                .add(ModBlocks.RESOURCES_DARK_OAK_LEAVES.get())
                .add(ModBlocks.RESOURCES_CHERRY_LEAVES.get())
                .add(ModBlocks.RESOURCES_PALE_OAK_LEAVES.get());
    }
}

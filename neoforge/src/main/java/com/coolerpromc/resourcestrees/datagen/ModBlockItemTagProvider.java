package com.coolerpromc.resourcestrees.datagen;

import com.coolerpromc.resourcestrees.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagCopyingItemTagProvider;

import java.util.concurrent.CompletableFuture;

public class ModBlockItemTagProvider extends BlockTagCopyingItemTagProvider {
    public ModBlockItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags, Constants.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        copy(BlockItemTags.SAPLINGS.block(), ItemTags.SAPLINGS);
    }
}

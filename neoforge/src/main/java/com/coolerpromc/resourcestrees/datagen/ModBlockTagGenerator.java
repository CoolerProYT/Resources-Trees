package com.coolerpromc.resourcestrees.datagen;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Constants.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.SAPLINGS).add(ModBlocks.SAPLINGS.stream().map(Supplier::get).toArray(Block[]::new));
        this.tag(BlockTags.LEAVES).add(ModBlocks.LEAVES.stream().map(Supplier::get).toArray(Block[]::new));
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.TREE_SIMULATOR.get());
    }
}

package com.coolerpromc.resourcestrees.datagen;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.platform.util.BlockRegistryHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Constants.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        TagAppender<Block> saplingsTag = this.tag(BlockItemTags.SAPLINGS.block());
        ModBlocks.SAPLINGS.stream().map(BlockRegistryHandler::key).forEach(saplingsTag::add);
        TagAppender<Block> leavesTag = this.tag(BlockTags.LEAVES);
        ModBlocks.LEAVES.stream().map(BlockRegistryHandler::key).forEach(leavesTag::add);
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.TREE_SIMULATOR.key());
    }
}

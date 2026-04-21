package com.coolerpromc.resourcestrees.datagen;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.platform.util.BlockRegistryHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {
    public ModDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        DataMapProvider.Builder<Compostable, Item> compostableBuilder = this.builder(NeoForgeDataMaps.COMPOSTABLES).replace(false);

        for (BlockRegistryHandler<? extends Block> block : ModBlocks.SAPLINGS){
            compostableBuilder.add(block.asItem().builtInRegistryHolder(), new Compostable(0.3F, true), false);
        }

        for (BlockRegistryHandler<? extends Block> block : ModBlocks.LEAVES){
            compostableBuilder.add(block.asItem().builtInRegistryHolder(), new Compostable(0.3F, true), false);
        }
    }
}

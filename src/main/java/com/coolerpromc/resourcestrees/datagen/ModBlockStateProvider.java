package com.coolerpromc.resourcestrees.datagen;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ResourcesTrees.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.generateResourcesLeaves(ModBlocks.RESOURCES_OAK_LEAVES);
        this.generateResourcesSapling(ModBlocks.RESOURCES_OAK_SAPLING);

        this.generateResourcesLeaves(ModBlocks.RESOURCES_SPRUCE_LEAVES);
        this.generateResourcesSapling(ModBlocks.RESOURCES_SPRUCE_SAPLING);

        this.generateResourcesLeaves(ModBlocks.RESOURCES_BIRCH_LEAVES);
        this.generateResourcesSapling(ModBlocks.RESOURCES_BIRCH_SAPLING);

        this.generateResourcesLeaves(ModBlocks.RESOURCES_JUNGLE_LEAVES);
        this.generateResourcesSapling(ModBlocks.RESOURCES_JUNGLE_SAPLING);

        this.generateResourcesLeaves(ModBlocks.RESOURCES_ACACIA_LEAVES);
        this.generateResourcesSapling(ModBlocks.RESOURCES_ACACIA_SAPLING);

        this.generateResourcesLeaves(ModBlocks.RESOURCES_DARK_OAK_LEAVES);
        this.generateResourcesSapling(ModBlocks.RESOURCES_DARK_OAK_SAPLING);

        this.generateResourcesLeaves(ModBlocks.RESOURCES_CHERRY_LEAVES);
        this.generateResourcesSapling(ModBlocks.RESOURCES_CHERRY_SAPLING);

        this.simpleBlockWithItem(ModBlocks.TREE_SIMULATOR.get(), new ModelFile.ExistingModelFile(ResourcesTrees.id("block/tree_simulator"), models().existingFileHelper));
    }

    private <T extends Block> void generateResourcesLeaves(RegistryObject<T> block) {
        models().withExistingParent("block/" + block.getId().getPath(), mcLoc("block/leaves"))
                .texture("all", block.getId().withPrefix("block/"));

        getVariantBuilder(block.get()).partialState()
                .setModels(new ConfiguredModel(models().getExistingFile(modLoc("block/" + block.getId().getPath()))));

        itemModels().withExistingParent(block.getId().getPath(), modLoc("block/" + block.getId().getPath()));
    }

    private void generateResourcesSapling(RegistryObject<ResourcesSaplingBlock> block) {
        models().withExistingParent("block/" + block.getId().getPath(), ResourcesTrees.id("block/cross_tinted"))
                .texture("cross", block.getId().withPrefix("block/"))
                .texture("cross_tinted", block.getId().withPrefix("block/").withSuffix("_layer1"));

        getVariantBuilder(block.get()).partialState()
                .setModels(new ConfiguredModel(models().getExistingFile(modLoc("block/" + block.getId().getPath()))));

        itemModels().withExistingParent(block.getId().getPath(), mcLoc("item/generated"))
                .texture("layer0", block.getId().withPrefix("block/"))
                .texture("layer1", block.getId().withPrefix("block/").withSuffix("_layer1"));
    }
}

package com.coolerpromc.resourcestrees.datagen;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.Optional;

import static net.minecraft.data.client.BlockStateModelGenerator.createSingletonBlockState;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockModels) {
        this.generateResourcesLeaves(blockModels, ModBlocks.RESOURCES_OAK_LEAVES, -12012264);
        this.generateResourcesSapling(blockModels, ModBlocks.RESOURCES_OAK_SAPLING, -12012264);

        this.generateResourcesLeaves(blockModels, ModBlocks.RESOURCES_SPRUCE_LEAVES, -10380959);
        this.generateResourcesSapling(blockModels, ModBlocks.RESOURCES_SPRUCE_SAPLING, -10380959);

        this.generateResourcesLeaves(blockModels, ModBlocks.RESOURCES_BIRCH_LEAVES, -8345771);
        this.generateResourcesSapling(blockModels, ModBlocks.RESOURCES_BIRCH_SAPLING, -8345771);

        this.generateResourcesLeaves(blockModels, ModBlocks.RESOURCES_JUNGLE_LEAVES, -12012264);
        this.generateResourcesSapling(blockModels, ModBlocks.RESOURCES_JUNGLE_SAPLING, -12012264);

        this.generateResourcesLeaves(blockModels, ModBlocks.RESOURCES_ACACIA_LEAVES, -12012264);
        this.generateResourcesSapling(blockModels, ModBlocks.RESOURCES_ACACIA_SAPLING, -12012264);

        this.generateResourcesLeaves(blockModels, ModBlocks.RESOURCES_DARK_OAK_LEAVES, -12012264);
        this.generateResourcesSapling(blockModels, ModBlocks.RESOURCES_DARK_OAK_SAPLING, -12012264);

        this.generateResourcesLeaves(blockModels, ModBlocks.RESOURCES_CHERRY_LEAVES, 0xFFfccbe7);
        this.generateResourcesSapling(blockModels, ModBlocks.RESOURCES_CHERRY_SAPLING, 0xFFfccbe7);


        this.blockWithExistingModel(blockModels, ModBlocks.TREE_SIMULATOR);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModels) {
        this.generateFlatTintedItem(itemModels, ModItems.LEAF_FRAGMENT);

        this.generateTintedEssenceItem(itemModels, ModItems.FIRE_ESSENCE);
        this.generateTintedEssenceItem(itemModels, ModItems.WATER_ESSENCE);
        this.generateTintedEssenceItem(itemModels, ModItems.NATURE_ESSENCE);
        this.generateTintedEssenceItem(itemModels, ModItems.END_ESSENCE);
    }

    public static final TextureKey CROSS_TINTED_SLOT = TextureKey.of("cross_tinted");
    public static final Model CROSS_TINTED = new Model(Optional.of(ResourcesTrees.id("block/cross_tinted")), Optional.empty(), TextureKey.CROSS, CROSS_TINTED_SLOT);

    private void generateTintedEssenceItem(ItemModelGenerator itemModels, Item item){
        Models.GENERATED.upload(getModelLocation(item, ""), TextureMap.layer0(ResourcesTrees.id("item/essence")), itemModels.writer);
    }

    private void generateFlatTintedItem(ItemModelGenerator itemModels, Item item){
        itemModels.register(item, Models.GENERATED);
    }

    private void generateResourcesLeaves(BlockStateModelGenerator blockModels, Block block, int defaultColor){
        Identifier blockModel = TexturedModel.LEAVES.upload(block, blockModels.modelCollector);

        blockModels.blockStateCollector.accept(createSingletonBlockState(block, blockModel));
        blockModels.registerParentedItemModel(block, blockModel);
    }

    private void generateResourcesSapling(BlockStateModelGenerator blockModels, Block block, int defaultColor){
        Identifier cross = getModelLocation(block, "");
        Identifier crossTinted = getModelLocation(block, "_layer1");

        TextureMap textureMapping = new TextureMap()
                .put(TextureKey.CROSS, cross)
                .put(CROSS_TINTED_SLOT, crossTinted);

        Identifier blockModel = CROSS_TINTED.upload(block, textureMapping, blockModels.modelCollector);
        Identifier itemModel = Models.GENERATED_TWO_LAYERS.upload(getModelLocation(block.asItem(), ""), TextureMap.layered(cross, crossTinted), blockModels.modelCollector);

        blockModels.blockStateCollector.accept(createSingletonBlockState(block, blockModel));
    }

    private void blockWithExistingModel(BlockStateModelGenerator blockModels, Block block){
        blockModels.blockStateCollector.accept(createSingletonBlockState(block, getModelLocation(block, "")));
        blockModels.registerParentedItemModel(block, getModelLocation(block, ""));
    }

    private Identifier getModelLocation(Block block, String suffix) {
        return ResourcesTrees.id("block/" + Registries.BLOCK.getId(block).getPath() + suffix);
    }

    private Identifier getModelLocation(Item item, String suffix) {
        return ResourcesTrees.id("item/" + Registries.ITEM.getId(item).getPath() + suffix);
    }
}
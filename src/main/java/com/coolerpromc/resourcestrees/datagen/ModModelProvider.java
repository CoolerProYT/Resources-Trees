package com.coolerpromc.resourcestrees.datagen;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.datagen.model.ResourcesTypeTintSource;
import com.coolerpromc.resourcestrees.item.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.client.render.item.model.BasicItemModel;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.Pool;

import java.util.List;
import java.util.Optional;

import static net.minecraft.client.data.BlockStateModelGenerator.createSingletonBlockState;
import static net.minecraft.client.data.BlockStateModelGenerator.createWeightedVariant;

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

        this.generateResourcesLeaves(blockModels, ModBlocks.RESOURCES_PALE_OAK_LEAVES, 0xFF838880);
        this.generateResourcesSapling(blockModels, ModBlocks.RESOURCES_PALE_OAK_SAPLING, 0xFF838880);

        this.blockWithExistingModel(blockModels, ModBlocks.TREE_SIMULATOR);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModels) {
        this.generateFlatTintedItem(itemModels, ModItems.LEAF_FRAGMENT, new ResourcesTypeTintSource(-1));

        this.generateTintedEssenceItem(itemModels, ModItems.FIRE_ESSENCE, ItemModels.constantTintSource(0xFFE45323));
        this.generateTintedEssenceItem(itemModels, ModItems.WATER_ESSENCE, ItemModels.constantTintSource(0xFF1787D4));
        this.generateTintedEssenceItem(itemModels, ModItems.NATURE_ESSENCE, ItemModels.constantTintSource(0xFF1a6e08));
        this.generateTintedEssenceItem(itemModels, ModItems.END_ESSENCE, ItemModels.constantTintSource(0xFFC5BE8B));
    }

    public static final TextureKey CROSS_TINTED_SLOT = TextureKey.of("cross_tinted");
    public static final Model CROSS_TINTED = new Model(Optional.of(ResourcesTrees.id("block/cross_tinted")), Optional.empty(), TextureKey.CROSS, CROSS_TINTED_SLOT);

    private void generateTintedEssenceItem(ItemModelGenerator itemModels, Item item, TintSource tintSource){
        Identifier itemModel = Models.GENERATED.upload(item, TextureMap.layer0(ResourcesTrees.id("item/essence")), itemModels.modelCollector);
        itemModels.output.accept(item, ItemModels.tinted(itemModel, tintSource));
    }

    private void generateFlatTintedItem(ItemModelGenerator itemModels, Item item, TintSource tintSource){
        Identifier itemModel = itemModels.upload(item, Models.GENERATED);
        itemModels.output.accept(item, ItemModels.tinted(itemModel, tintSource));
    }

    private void generateResourcesLeaves(BlockStateModelGenerator blockModels, Block block, int defaultColor){
        Identifier blockModel = TexturedModel.LEAVES.upload(block, blockModels.modelCollector);

        blockModels.blockStateCollector.accept(createSingletonBlockState(block, createWeightedVariant(blockModel)));
        blockModels.itemModelOutput.accept(block.asItem(), ItemModels.tinted(blockModel, new ResourcesTypeTintSource(defaultColor)));
    }

    private void generateResourcesSapling(BlockStateModelGenerator blockModels, Block block, int defaultColor){
        Identifier cross = getModelLocation(block, "");
        Identifier crossTinted = getModelLocation(block, "_layer1");

        TextureMap textureMapping = new TextureMap()
                .put(TextureKey.CROSS, cross)
                .put(CROSS_TINTED_SLOT, crossTinted);

        Identifier blockModel = CROSS_TINTED.upload(block, textureMapping, blockModels.modelCollector);
        Identifier itemModel = Models.GENERATED_TWO_LAYERS.upload(block.asItem(), TextureMap.layered(cross, crossTinted), blockModels.modelCollector);

        blockModels.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(block, new WeightedVariant(Pool.of(new ModelVariant(blockModel)))));
        blockModels.itemModelOutput.accept(block.asItem(), ItemModels.tinted(itemModel, ItemModels.constantTintSource(-1), new ResourcesTypeTintSource(defaultColor)));
    }

    private void blockWithExistingModel(BlockStateModelGenerator blockModels, Block block){
        blockModels.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(block, new WeightedVariant(Pool.of(new ModelVariant(getModelLocation(block, ""))))));
        blockModels.itemModelOutput.accept(block.asItem(), new BasicItemModel.Unbaked(getModelLocation(block, ""), List.of()));
    }

    private Identifier getModelLocation(Block block, String suffix) {
        return ResourcesTrees.id("block/" + Registries.BLOCK.getId(block).getPath() + suffix);
    }
}
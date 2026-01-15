package com.coolerpromc.resourcestrees.datagen;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.datagen.model.ResourcesTypeTintSource;
import com.coolerpromc.resourcestrees.item.ModItems;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.item.BlockModelWrapper;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static net.minecraft.client.data.models.BlockModelGenerators.createSimpleBlock;
import static net.minecraft.client.data.models.BlockModelGenerators.plainVariant;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, ResourcesTrees.MODID);
    }

    public static final TextureSlot CROSS_TINTED_SLOT = TextureSlot.create("cross_tinted");
    public static final ModelTemplate CROSS_TINTED = ModelTemplates.create(TextureSlot.CROSS, CROSS_TINTED_SLOT).extend().parent(ResourcesTrees.id("block/cross_tinted")).build();

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        this.generateResourcesLeaves(blockModels, itemModels, ModBlocks.RESOURCES_OAK_LEAVES.get(), -12012264);
        this.generateResourcesSapling(blockModels, itemModels, ModBlocks.RESOURCES_OAK_SAPLING.get(), -12012264);

        this.generateResourcesLeaves(blockModels, itemModels, ModBlocks.RESOURCES_SPRUCE_LEAVES.get(), -10380959);
        this.generateResourcesSapling(blockModels, itemModels, ModBlocks.RESOURCES_SPRUCE_SAPLING.get(), -10380959);

        this.generateResourcesLeaves(blockModels, itemModels, ModBlocks.RESOURCES_BIRCH_LEAVES.get(), -8345771);
        this.generateResourcesSapling(blockModels, itemModels, ModBlocks.RESOURCES_BIRCH_SAPLING.get(), -8345771);

        this.generateResourcesLeaves(blockModels, itemModels, ModBlocks.RESOURCES_JUNGLE_LEAVES.get(), -12012264);
        this.generateResourcesSapling(blockModels, itemModels, ModBlocks.RESOURCES_JUNGLE_SAPLING.get(), -12012264);

        this.generateResourcesLeaves(blockModels, itemModels, ModBlocks.RESOURCES_ACACIA_LEAVES.get(), -12012264);
        this.generateResourcesSapling(blockModels, itemModels, ModBlocks.RESOURCES_ACACIA_SAPLING.get(), -12012264);

        this.generateResourcesLeaves(blockModels, itemModels, ModBlocks.RESOURCES_DARK_OAK_LEAVES.get(), -12012264);
        this.generateResourcesSapling(blockModels, itemModels, ModBlocks.RESOURCES_DARK_OAK_SAPLING.get(), -12012264);

        this.generateResourcesLeaves(blockModels, itemModels, ModBlocks.RESOURCES_CHERRY_LEAVES.get(), 0xFFfccbe7);
        this.generateResourcesSapling(blockModels, itemModels, ModBlocks.RESOURCES_CHERRY_SAPLING.get(), 0xFFfccbe7);

        this.generateResourcesLeaves(blockModels, itemModels, ModBlocks.RESOURCES_PALE_OAK_LEAVES.get(), 0xFF838880);
        this.generateResourcesSapling(blockModels, itemModels, ModBlocks.RESOURCES_PALE_OAK_SAPLING.get(), 0xFF838880);

        this.generateFlatTintedItem(itemModels, ModItems.LEAF_FRAGMENT.get(), new ResourcesTypeTintSource(-1));

        this.blockWithExistingModel(blockModels, ModBlocks.TREE_SIMULATOR.get());

        this.generateTintedEssenceItem(itemModels, ModItems.FIRE_ESSENCE.get(), ItemModelUtils.constantTint(0xFFE45323));
        this.generateTintedEssenceItem(itemModels, ModItems.WATER_ESSENCE.get(), ItemModelUtils.constantTint(0xFF1787D4));
        this.generateTintedEssenceItem(itemModels, ModItems.NATURE_ESSENCE.get(), ItemModelUtils.constantTint(0xFF1a6e08));
        this.generateTintedEssenceItem(itemModels, ModItems.END_ESSENCE.get(), ItemModelUtils.constantTint(0xFFC5BE8B));
        this.generateTintedEssenceItem(itemModels, ModItems.BEE_ESSENCE.get(), ItemModelUtils.constantTint(0xFFEDC343));
        this.generateTintedEssenceItem(itemModels, ModItems.SCULK_ESSENCE.get(), ItemModelUtils.constantTint(0xFF041820));
        this.generateTintedEssenceItem(itemModels, ModItems.SKELETON_ESSENCE.get(), ItemModelUtils.constantTint(0xFFeeeeee));
        this.generateTintedEssenceItem(itemModels, ModItems.SPIDER_ESSENCE.get(), ItemModelUtils.constantTint(0xFF1a0c20));
        this.generateTintedEssenceItem(itemModels, ModItems.CHICKEN_ESSENCE.get(), ItemModelUtils.constantTint(0xFFA1A1A1));
        this.generateTintedEssenceItem(itemModels, ModItems.COW_ESSENCE.get(), ItemModelUtils.constantTint(0xFF543936));
        this.generateTintedEssenceItem(itemModels, ModItems.RABBIT_ESSENCE.get(), ItemModelUtils.constantTint(0xFF8B5A2B));
        this.generateTintedEssenceItem(itemModels, ModItems.SQUID_ESSENCE.get(), ItemModelUtils.constantTint(0xFF223B4D));
        this.generateTintedEssenceItem(itemModels, ModItems.TURTLE_ESSENCE.get(), ItemModelUtils.constantTint(0xFF315410));
        this.generateTintedEssenceItem(itemModels, ModItems.BLAZE_ESSENCE.get(), ItemModelUtils.constantTint(0xFFd4ae37));
        this.generateTintedEssenceItem(itemModels, ModItems.BREEZE_ESSENCE.get(), ItemModelUtils.constantTint(0xFFd5d6ff));
        this.generateTintedEssenceItem(itemModels, ModItems.DYE_ESSENCE.get(), ItemModelUtils.constantTint(0xFF72d4b3));
        this.generateTintedEssenceItem(itemModels, ModItems.GHAST_ESSENCE.get(), ItemModelUtils.constantTint(0xFFF9F9F9));
        this.generateTintedEssenceItem(itemModels, ModItems.PIG_ESSENCE.get(), ItemModelUtils.constantTint(0xFFF9A195));
        this.generateTintedEssenceItem(itemModels, ModItems.SHEEP_ESSENCE.get(), ItemModelUtils.constantTint(0xFFFFFFFF));
        this.generateTintedEssenceItem(itemModels, ModItems.FISH_ESSENCE.get(), ItemModelUtils.constantTint(0xFFC1A76A));
        this.generateTintedEssenceItem(itemModels, ModItems.ZOMBIE_ESSENCE.get(), ItemModelUtils.constantTint(0xFF3e692d));

    }

    private void generateTintedEssenceItem(ItemModelGenerators itemModels, Item item, ItemTintSource tintSource){
        Identifier itemModel = ModelTemplates.FLAT_ITEM.create(item, TextureMapping.layer0(ResourcesTrees.id("item/essence")), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(item, ItemModelUtils.tintedModel(itemModel, tintSource));
    }

    private void generateFlatTintedItem(ItemModelGenerators itemModels, Item item, ItemTintSource tintSource){
        Identifier itemModel = itemModels.createFlatItemModel(item, ModelTemplates.FLAT_ITEM);
        itemModels.itemModelOutput.accept(item, ItemModelUtils.tintedModel(itemModel, tintSource));
    }

    private void generateResourcesLeaves(BlockModelGenerators blockModels, ItemModelGenerators itemModels, Block block, int defaultColor){
        Identifier blockModel = TexturedModel.LEAVES.create(block, blockModels.modelOutput);

        blockModels.blockStateOutput.accept(createSimpleBlock(block, plainVariant(blockModel)));
        itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.tintedModel(blockModel, new ResourcesTypeTintSource(defaultColor)));
    }

    private void generateResourcesSapling(BlockModelGenerators blockModels, ItemModelGenerators itemModels, Block block, int defaultColor){
        Identifier cross = getModelLocation(block, "");
        Identifier crossTinted = getModelLocation(block, "_layer1");

        TextureMapping textureMapping = new TextureMapping()
                .put(TextureSlot.CROSS, cross)
                .put(CROSS_TINTED_SLOT, crossTinted);

        Identifier blockModel = CROSS_TINTED.create(block, textureMapping, blockModels.modelOutput);
        Identifier itemModel = itemModels.generateLayeredItem(block.asItem(), cross, crossTinted);

        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, new MultiVariant(WeightedList.of(new Variant(blockModel)))));
        itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.tintedModel(itemModel, ItemModelUtils.constantTint(-1), new ResourcesTypeTintSource(defaultColor)));
    }

    private void blockWithExistingModel(BlockModelGenerators blockModels, Block block){
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, new MultiVariant(WeightedList.of(new Variant(getModelLocation(block, ""))))));
        blockModels.itemModelOutput.accept(block.asItem(), new BlockModelWrapper.Unbaked(getModelLocation(block, ""), List.of()));
    }

    @Override
    protected @NotNull Stream<? extends Holder<Item>> getKnownItems() {
        return BuiltInRegistries.ITEM.listElements().filter(itemReference -> Optional.of(BuiltInRegistries.ITEM.getKey(itemReference.value())).filter(Identifier -> Identifier.getNamespace().equals(ResourcesTrees.MODID)).isPresent());
    }

    private Identifier getModelLocation(Block block, String suffix) {
        return ResourcesTrees.id("block/" + BuiltInRegistries.BLOCK.getKey(block).getPath() + suffix);
    }
}
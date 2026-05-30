package com.coolerpromc.resourcestrees.datagen;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.platform.util.RegistryHandler;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.client.resources.model.sprite.Material;
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

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, Constants.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
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
        Identifier itemModel = ModelTemplates.FLAT_ITEM.create(item, TextureMapping.layer0(new Material(Constants.id("item/essence"))), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(item, ItemModelUtils.tintedModel(itemModel, tintSource));
    }

    private void blockWithExistingModel(BlockModelGenerators blockModels, Block block){
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, new MultiVariant(WeightedList.of(new Variant(getModelLocation(block, ""))))));
        blockModels.itemModelOutput.accept(block.asItem(), new CuboidItemModelWrapper.Unbaked(getModelLocation(block, ""), Optional.empty(), List.of()));
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        List<Identifier> excluded = Stream.concat(ModBlocks.SAPLINGS.stream(), ModBlocks.LEAVES.stream()).map(RegistryHandler::id).toList();
        return super.getKnownBlocks().filter(holder -> !excluded.contains(BuiltInRegistries.BLOCK.getKey(holder.value())));
    }

    @Override
    protected @NotNull Stream<? extends Holder<Item>> getKnownItems() {
        List<Identifier> excluded = Stream.concat(ModBlocks.SAPLINGS.stream(), ModBlocks.LEAVES.stream())
                .map(RegistryHandler::id)
                .toList();

        return BuiltInRegistries.ITEM.listElements().filter(itemReference ->
                Optional.of(BuiltInRegistries.ITEM.getKey(itemReference.value())).filter(identifier ->
                        identifier.getNamespace().equals(Constants.MODID)
                                && !ModItems.LEAF_FRAGMENTS.stream().map(RegistryHandler::id).toList().contains(identifier)
                                && !excluded.contains(identifier)
                ).isPresent()
        );
    }

    private Identifier getModelLocation(Block block, String suffix) {
        return Constants.id("block/" + BuiltInRegistries.BLOCK.getKey(block).getPath() + suffix);
    }
}
package com.coolerpromc.resourcestrees.datagen;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ResourcesTrees.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.generateFlatTintedItem(ModItems.LEAF_FRAGMENT);
        this.generateTintedEssenceItem(ModItems.FIRE_ESSENCE);
        this.generateTintedEssenceItem(ModItems.WATER_ESSENCE);
        this.generateTintedEssenceItem(ModItems.NATURE_ESSENCE);
        this.generateTintedEssenceItem(ModItems.END_ESSENCE);
    }

    private ItemModelBuilder generateTintedEssenceItem(DeferredItem<Item> item){
        return getBuilder(item.getId().getPath())
                .parent(getExistingFile(mcLoc("item/generated")))
                .texture("layer0", ResourcesTrees.id("item/essence"));
    }

    private <T extends Item> ItemModelBuilder generateFlatTintedItem(DeferredItem<T> item){
        return getBuilder(item.getId().getPath())
                .parent(getExistingFile(mcLoc("item/generated")))
                .texture("layer0", item.getId().withPrefix("item/"));
    }
}

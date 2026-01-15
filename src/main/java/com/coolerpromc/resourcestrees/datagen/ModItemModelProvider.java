package com.coolerpromc.resourcestrees.datagen;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

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
        this.generateTintedEssenceItem(ModItems.BEE_ESSENCE);
        this.generateTintedEssenceItem(ModItems.SCULK_ESSENCE);
        this.generateTintedEssenceItem(ModItems.SKELETON_ESSENCE);
        this.generateTintedEssenceItem(ModItems.SPIDER_ESSENCE);
        this.generateTintedEssenceItem(ModItems.CHICKEN_ESSENCE);
        this.generateTintedEssenceItem(ModItems.COW_ESSENCE);
        this.generateTintedEssenceItem(ModItems.RABBIT_ESSENCE);
        this.generateTintedEssenceItem(ModItems.SQUID_ESSENCE);
        this.generateTintedEssenceItem(ModItems.TURTLE_ESSENCE);
        this.generateTintedEssenceItem(ModItems.BLAZE_ESSENCE);
        this.generateTintedEssenceItem(ModItems.DYE_ESSENCE);
        this.generateTintedEssenceItem(ModItems.GHAST_ESSENCE);
        this.generateTintedEssenceItem(ModItems.PIG_ESSENCE);
        this.generateTintedEssenceItem(ModItems.SHEEP_ESSENCE);
        this.generateTintedEssenceItem(ModItems.FISH_ESSENCE);
        this.generateTintedEssenceItem(ModItems.ZOMBIE_ESSENCE);
    }

    private ItemModelBuilder generateTintedEssenceItem(RegistryObject<Item> item){
        return getBuilder(item.getId().getPath())
                .parent(getExistingFile(mcLoc("item/generated")))
                .texture("layer0", ResourcesTrees.id("item/essence"));
    }

    private <T extends Item> ItemModelBuilder generateFlatTintedItem(RegistryObject<T> item){
        return getBuilder(item.getId().getPath())
                .parent(getExistingFile(mcLoc("item/generated")))
                .texture("layer0", item.getId().withPrefix("item/"));
    }
}

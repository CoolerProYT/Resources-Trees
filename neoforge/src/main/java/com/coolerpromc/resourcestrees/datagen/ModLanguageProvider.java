package com.coolerpromc.resourcestrees.datagen;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.api.resources.ResourcesTypes;
import com.coolerpromc.resourcestrees.api.tree.TreeTypes;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.platform.util.RegistryHandler;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output) {
        super(output, Constants.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("creativetab.resourcestrees", "Resources Trees");
        add("tooltip.resourcestrees.tickToGrow", "Grow Time: %s ticks");
        add("tooltip.resourcestrees.output_chance", "Output Chance: %s%%");
        add("tooltip.resourcestrees.max_rolls", "Max Rolls: %s");
        add(ModBlocks.TREE_SIMULATOR.get(), "Tree Simulator");

        add("item.resourcestrees.leaf_fragment", "Leaf Fragment");
        add("item.resourcestrees.sapling", "Sapling");
        add("item.resourcestrees.leaves", "Leaves");

        add("item.resourcestrees.trees", "%s %s %s");
        add("item.resourcestrees.leaf_fragments", "%s %s");

        ResourcesTypes.getTypes().forEach(type -> add("resources_type.resourcestrees." + type.name(), Constants.capitalizeWords(type.name())));
        TreeTypes.getTypes().forEach(type -> add("tree_type.resourcestrees." + type.name(), Constants.capitalizeWords(type.name())));
        forEachItemField(ModItems.class, itemRegistryHandler -> add(itemRegistryHandler.get(), Constants.capitalizeWords(itemRegistryHandler.id().getPath())));
    }

    public void forEachItemField(Class<?> clazz, Consumer<RegistryHandler<Item>> action) {
        for (Field field : clazz.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) continue;

            field.setAccessible(true);

            try {
                Object value = field.get(null);
                if (value instanceof RegistryHandler<?> ro && ro.get() instanceof Item) {
                    action.accept((RegistryHandler<Item>) ro);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}

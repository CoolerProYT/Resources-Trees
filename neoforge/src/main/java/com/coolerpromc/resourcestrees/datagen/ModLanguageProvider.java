package com.coolerpromc.resourcestrees.datagen;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.item.custom.LeafFragmentItem;
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

        ModBlocks.LEAVES.forEach(blockRegistryHandler -> {
            if (blockRegistryHandler.get() instanceof ResourcesLeavesBlock block){
                add(block, capitalizeWords(block.getResourcesType().name()) + " " + capitalizeWords(block.getTreeType().name()) + " Leaves");
            }
        });
        ModBlocks.SAPLINGS.forEach(blockRegistryHandler -> {
            if (blockRegistryHandler.get() instanceof ResourcesSaplingBlock block){
                add(block, capitalizeWords(block.getResourcesType().name()) + " " + capitalizeWords(block.getTreeType().name()) + " Sapling");
            }
        });
        ModItems.LEAF_FRAGMENTS.forEach(registryHandler -> {
            if (registryHandler.get() instanceof LeafFragmentItem item){
                add(item, capitalizeWords(item.getResourcesType().name()) + " Leaf Fragment");
            }
        });
        forEachItemField(ModItems.class, itemRegistryHandler -> {
            add(itemRegistryHandler.get(), capitalizeWords(itemRegistryHandler.id().getPath()));
        });
    }

    private String capitalizeWords(String str) {
        if (str == null || str.isEmpty()) return str;
        String[] words = str.split("_");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1).toLowerCase());
            }
            sb.append(" ");
        }
        return sb.toString().trim();
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

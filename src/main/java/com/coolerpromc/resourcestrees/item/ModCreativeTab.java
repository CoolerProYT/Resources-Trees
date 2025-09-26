package com.coolerpromc.resourcestrees.item;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public class ModCreativeTab {
    public static final ItemGroup RESOURCES_TREES_TAB = Registry.register(Registries.ITEM_GROUP, ResourcesTrees.id("resourcestrees"),
            FabricItemGroup.builder().icon(() -> new ItemStack(Blocks.OAK_SAPLING))
                    .displayName(Text.translatable("creativetab.resourcestrees"))
                    .entries((pParameters, pOutput) -> {
                        pOutput.add(ModBlocks.TREE_SIMULATOR);

                        pOutput.add(ModItems.FIRE_ESSENCE);
                        pOutput.add(ModItems.WATER_ESSENCE);
                        pOutput.add(ModItems.NATURE_ESSENCE);
                        pOutput.add(ModItems.END_ESSENCE);

                        ResourcesTypes.getAllResourcesTypes(pParameters.lookup()).forEach((key, value) -> {
                            Field[] fields = ModBlocks.class.getDeclaredFields();

                            for (Field field : fields){
                                try {
                                    Object obj = field.get(null);
                                    if (obj instanceof ResourcesSaplingBlock resourcesSaplingBlock){
                                        ItemStack sapling = resourcesSaplingBlock.asItem().getDefaultStack();
                                        sapling.set(ModDataComponents.TYPE, key);
                                        pOutput.add(sapling);
                                    }
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });

                        ResourcesTypes.getAllResourcesTypes(pParameters.lookup()).forEach((key, value) -> {
                            Field[] fields = ModBlocks.class.getDeclaredFields();

                            for (Field field : fields){
                                try {
                                    Object obj = field.get(null);
                                    if (obj instanceof ResourcesLeavesBlock resourcesLeavesBlock){
                                        ItemStack leaves = resourcesLeavesBlock.asItem().getDefaultStack();
                                        leaves.set(ModDataComponents.TYPE, key);
                                        pOutput.add(leaves);
                                    }
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });

                        ResourcesTypes.getAllResourcesTypes(pParameters.lookup()).forEach((key, value) -> {
                            ItemStack leaf = ModItems.LEAF_FRAGMENT.getDefaultStack();
                            leaf.set(ModDataComponents.TYPE, key);
                            pOutput.add(leaf);
                        });
                    }).build());

    public static void register() {
        ResourcesTrees.LOGGER.info("Registering creative tabs.");
    }
}
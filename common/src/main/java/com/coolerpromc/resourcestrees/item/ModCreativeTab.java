package com.coolerpromc.resourcestrees.item;

import com.coolerpromc.resourcestrees.CommonClass;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.platform.Services;
import com.coolerpromc.resourcestrees.platform.util.RegistryHandler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModCreativeTab {
    public static final RegistryHandler<CreativeModeTab> RESOURCES_TREES_TAB = Services.REGISTRY.registerCreativeTab(
            "resourcestrees",
            () -> new ItemStack(Blocks.OAK_SAPLING),
            Component.translatable("creativetab.resourcestrees"),
            (pParameters) -> {
                List<ItemStack> items = new ArrayList<>();

                items.add(new ItemStack(ModBlocks.TREE_SIMULATOR.get()));

                items.add(new ItemStack(ModItems.FIRE_ESSENCE.get()));
                items.add(new ItemStack(ModItems.WATER_ESSENCE.get()));
                items.add(new ItemStack(ModItems.NATURE_ESSENCE.get()));
                items.add(new ItemStack(ModItems.END_ESSENCE.get()));
                items.add(new ItemStack(ModItems.BEE_ESSENCE.get()));
                items.add(new ItemStack(ModItems.SCULK_ESSENCE.get()));
                items.add(new ItemStack(ModItems.SKELETON_ESSENCE.get()));
                items.add(new ItemStack(ModItems.SPIDER_ESSENCE.get()));
                items.add(new ItemStack(ModItems.CHICKEN_ESSENCE.get()));
                items.add(new ItemStack(ModItems.COW_ESSENCE.get()));
                items.add(new ItemStack(ModItems.RABBIT_ESSENCE.get()));
                items.add(new ItemStack(ModItems.SQUID_ESSENCE.get()));
                items.add(new ItemStack(ModItems.TURTLE_ESSENCE.get()));
                items.add(new ItemStack(ModItems.BLAZE_ESSENCE.get()));
                items.add(new ItemStack(ModItems.BREEZE_ESSENCE.get()));
                items.add(new ItemStack(ModItems.DYE_ESSENCE.get()));
                items.add(new ItemStack(ModItems.GHAST_ESSENCE.get()));
                items.add(new ItemStack(ModItems.PIG_ESSENCE.get()));
                items.add(new ItemStack(ModItems.SHEEP_ESSENCE.get()));
                items.add(new ItemStack(ModItems.FISH_ESSENCE.get()));
                items.add(new ItemStack(ModItems.ZOMBIE_ESSENCE.get()));

                ResourcesTypes.getAllResourcesTypes(pParameters.holders()).forEach((key, value) -> {
                    for (Block block : CommonClass.saplingBlock()){
                        if (block instanceof ResourcesSaplingBlock resourcesSaplingBlock){
                            ItemStack sapling = resourcesSaplingBlock.asItem().getDefaultInstance();
                            sapling.set(ModDataComponents.TYPE.get(), value);
                            items.add(sapling);
                        }
                    }
                });

                ResourcesTypes.getAllResourcesTypes(pParameters.holders()).forEach((key, value) -> {
                    for (Block block : CommonClass.leavesBlock()){
                        if (block instanceof ResourcesLeavesBlock resourcesLeavesBlock){
                            ItemStack sapling = resourcesLeavesBlock.asItem().getDefaultInstance();
                            sapling.set(ModDataComponents.TYPE.get(), value);
                            items.add(sapling);
                        }
                    }
                });

                ResourcesTypes.getAllResourcesTypes(pParameters.holders()).forEach((key, value) -> {
                    ItemStack leaf = ModItems.LEAF_FRAGMENT.toStack();
                    leaf.set(ModDataComponents.TYPE.get(), value);
                    items.add(leaf);
                });

                return items.toArray(new ItemStack[0]);
            }
    );

    public static void init() {
        // Force class loading to trigger static initializers
    }
}
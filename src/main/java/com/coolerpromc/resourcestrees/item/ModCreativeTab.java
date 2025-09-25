package com.coolerpromc.resourcestrees.item;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MOD_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ResourcesTrees.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RESOURCES_TREES_TAB = CREATIVE_MOD_TABS.register("resourcestrees",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(Blocks.OAK_SAPLING))
                    .title(Component.translatable("creativetab.resourcestrees"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModBlocks.TREE_SIMULATOR);

                        pOutput.accept(ModItems.FIRE_ESSENCE);
                        pOutput.accept(ModItems.WATER_ESSENCE);
                        pOutput.accept(ModItems.NATURE_ESSENCE);
                        pOutput.accept(ModItems.END_ESSENCE);

                        ResourcesTypes.getAllResourcesTypes(pParameters.holders()).forEach((key, value) -> {
                            Field[] fields = ModBlocks.class.getDeclaredFields();

                            for (Field field : fields){
                                try {
                                    Object obj = field.get(null);
                                    if (obj instanceof Supplier<?> supplier){
                                        if (supplier.get() instanceof ResourcesSaplingBlock resourcesSaplingBlock){
                                            ItemStack sapling = resourcesSaplingBlock.asItem().getDefaultInstance();
                                            sapling.set(ModDataComponents.TYPE, key);
                                            pOutput.accept(sapling);
                                        }
                                    }
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });

                        ResourcesTypes.getAllResourcesTypes(pParameters.holders()).forEach((key, value) -> {
                            Field[] fields = ModBlocks.class.getDeclaredFields();

                            for (Field field : fields){
                                try {
                                    Object obj = field.get(null);
                                    if (obj instanceof Supplier<?> supplier){
                                        if (supplier.get() instanceof ResourcesLeavesBlock resourcesLeavesBlock){
                                            ItemStack leaves = resourcesLeavesBlock.asItem().getDefaultInstance();
                                            leaves.set(ModDataComponents.TYPE, key);
                                            pOutput.accept(leaves);
                                        }
                                    }
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });

                        ResourcesTypes.getAllResourcesTypes(pParameters.holders()).forEach((key, value) -> {
                            ItemStack leaf = ModItems.LEAF_FRAGMENT.toStack();
                            leaf.set(ModDataComponents.TYPE, key);
                            pOutput.accept(leaf);
                        });
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MOD_TABS.register(eventBus);
    }
}
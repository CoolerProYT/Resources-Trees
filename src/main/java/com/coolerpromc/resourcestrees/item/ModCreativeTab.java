package com.coolerpromc.resourcestrees.item;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MOD_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ResourcesTrees.MODID);

    public static final Supplier<CreativeModeTab> RESOURCES_TREES_TAB = CREATIVE_MOD_TABS.register("resourcestrees",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(Blocks.OAK_SAPLING))
                    .title(Component.translatable("creativetab.resourcestrees"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModBlocks.TREE_SIMULATOR.get());

                        pOutput.accept(ModItems.FIRE_ESSENCE.get());
                        pOutput.accept(ModItems.WATER_ESSENCE.get());
                        pOutput.accept(ModItems.NATURE_ESSENCE.get());
                        pOutput.accept(ModItems.END_ESSENCE.get());
                        pOutput.accept(ModItems.BEE_ESSENCE.get());
                        pOutput.accept(ModItems.SCULK_ESSENCE.get());
                        pOutput.accept(ModItems.SKELETON_ESSENCE.get());
                        pOutput.accept(ModItems.SPIDER_ESSENCE.get());
                        pOutput.accept(ModItems.CHICKEN_ESSENCE.get());
                        pOutput.accept(ModItems.COW_ESSENCE.get());
                        pOutput.accept(ModItems.RABBIT_ESSENCE.get());
                        pOutput.accept(ModItems.SQUID_ESSENCE.get());
                        pOutput.accept(ModItems.TURTLE_ESSENCE.get());
                        pOutput.accept(ModItems.BLAZE_ESSENCE.get());
                        pOutput.accept(ModItems.BREEZE_ESSENCE.get());
                        pOutput.accept(ModItems.DYE_ESSENCE.get());
                        pOutput.accept(ModItems.GHAST_ESSENCE.get());
                        pOutput.accept(ModItems.PIG_ESSENCE.get());
                        pOutput.accept(ModItems.SHEEP_ESSENCE.get());
                        pOutput.accept(ModItems.FISH_ESSENCE.get());
                        pOutput.accept(ModItems.ZOMBIE_ESSENCE.get());

                        ResourcesTypes.getAllResourcesTypes(pParameters.holders()).forEach((key, value) -> {
                            Field[] fields = ModBlocks.class.getDeclaredFields();

                            for (Field field : fields){
                                try {
                                    Object obj = field.get(null);
                                    if (obj instanceof Supplier<?> supplier){
                                        if (supplier.get() instanceof ResourcesSaplingBlock resourcesSaplingBlock){
                                            ItemStack sapling = resourcesSaplingBlock.asItem().getDefaultInstance();
                                            sapling.set(ModDataComponents.TYPE.get(), value);
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
                                            leaves.set(ModDataComponents.TYPE.get(), value);
                                            pOutput.accept(leaves);
                                        }
                                    }
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });

                        ResourcesTypes.getAllResourcesTypes(pParameters.holders()).forEach((key, value) -> {
                            ItemStack leaf = ModItems.LEAF_FRAGMENT.get().getDefaultInstance();
                            leaf.set(ModDataComponents.TYPE.get(), value);
                            pOutput.accept(leaf);
                        });
                    }).build());

    public static void register(BusGroup eventBus) {
        CREATIVE_MOD_TABS.register(eventBus);
    }
}
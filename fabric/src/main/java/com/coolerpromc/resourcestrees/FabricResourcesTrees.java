package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.api.IResourcesTreesPlugin;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.platform.util.BlockRegistryHandler;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;
import net.fabricmc.fabric.api.registry.CompostableRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.level.block.Block;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FabricResourcesTrees implements ModInitializer {
    public static final List<IResourcesTreesPlugin> PLUGINS = new ArrayList<>();

    @Override
    public void onInitialize() {
        PLUGINS.addAll(FabricLoader.getInstance().getEntrypoints("resources_trees_plugin", IResourcesTreesPlugin.class));

        ResourcesTrees.init();

        Field[] fields = ModBlocks.class.getDeclaredFields();
        for (Field field : fields){
            try {
                Object obj = field.get(null);
                if (obj instanceof Supplier<?> supplier) {
                    if (supplier.get() instanceof ResourcesLeavesBlock block) {
                        FlammableBlockRegistry.getDefaultInstance().add(block, 60, 30);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        RecipeSynchronization.synchronizeRecipeSerializer(ModRecipes.TREE_SIMULATOR_SERIALIZER.get());
        RecipeSynchronization.synchronizeRecipeSerializer(ModRecipes.STRICT_SHAPED.get());

        for (BlockRegistryHandler<? extends Block> block : ModBlocks.SAPLINGS){
            CompostableRegistry.INSTANCE.add(block, 0.3F);
        }

        for (BlockRegistryHandler<? extends Block> block : ModBlocks.LEAVES){
            CompostableRegistry.INSTANCE.add(block, 0.3F);
        }

        ItemStorage.SIDED.registerForBlockEntity((be, direction) -> {
            var container = be.getHandlerForSide(direction);
            return container != null ? ContainerStorage.of(container, direction) : null;
        }, ModBlockEntities.TREE_SIMULATOR_BE.get());
    }
}
package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.api.resources.ResourcesType;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.block.entity.custom.TreeSimulatorBlockEntity;
import com.coolerpromc.resourcestrees.platform.util.BlockRegistryHandler;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.registry.ModRegistries;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;
import net.fabricmc.fabric.api.registry.CompostableRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public class ResourcesTrees implements ModInitializer {
    @Override
    public void onInitialize() {
        CommonClass.init();
        TreeSimulatorBlockEntity.CONFIG.load();

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

        DynamicRegistries.registerSynced(ModRegistries.RESOURCES_TYPES_KEY, ResourcesType.LEGACY_CODEC, ResourcesType.LEGACY_CODEC);

        // TODO: Remove it after step out BETA
        ServerPlayerEvents.JOIN.register(player -> {
            player.sendSystemMessage(
                    Component.literal("[Resources Trees] ")
                            .withStyle(ChatFormatting.GOLD)
                            .append(Component.literal("Resources Type datapacks no longer work from 26.1.2.100 and are only kept for legacy migration. Please see the wiki for the new config-based system.").withStyle(ChatFormatting.YELLOW))
            );
        });
    }
}
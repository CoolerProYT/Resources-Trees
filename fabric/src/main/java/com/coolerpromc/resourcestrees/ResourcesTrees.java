package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.entity.custom.TreeSimulatorBlockEntity;
import com.coolerpromc.resourcestrees.compat.treeharvester.TreeHarvesterCompat;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.network.packet.ResourceTypeSyncS2CPacket;
import com.coolerpromc.resourcestrees.platform.FabricRegistryHelper;
import com.coolerpromc.resourcestrees.platform.Services;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.registry.ModRegistries;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.server.TickTask;
import net.minecraft.world.entity.item.ItemEntity;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public class ResourcesTrees implements ModInitializer {
    @Override
    public void onInitialize() {
        CommonClass.init();
        TreeSimulatorBlockEntity.CONFIG.load();

        CustomIngredientSerializer.register(FabricRegistryHelper.FabricResourcesTypeIngredient.SERIALIZER);

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

        PayloadTypeRegistry.clientboundPlay().register(ResourceTypeSyncS2CPacket.TYPE, ResourceTypeSyncS2CPacket.STREAM_CODEC);

        RecipeSynchronization.synchronizeRecipeSerializer(ModRecipes.TREE_SIMULATOR_SERIALIZER.get());
        RecipeSynchronization.synchronizeRecipeSerializer(ModRecipes.STRICT_SHAPED.get());

        DynamicRegistries.registerSynced(ModRegistries.RESOURCES_TYPES_KEY, ResourcesTypes.CODEC, ResourcesTypes.CODEC);

        ServerEntityEvents.ENTITY_LOAD.register(id("tree"), (entity, level) -> {
            if (!Services.PLATFORM.isModLoaded("treeharvester")) return;
            if (!(entity instanceof ItemEntity itemEntity)) return;

            level.getServer().doRunTask(new TickTask(5, () -> TreeHarvesterCompat.handleItemEntitySpawn(itemEntity, level)));
        });
    }

    public static Identifier id(String path){
        return Constants.id(path);
    }
}
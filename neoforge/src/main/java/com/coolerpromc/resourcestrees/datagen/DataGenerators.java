package com.coolerpromc.resourcestrees.datagen;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.registry.ModRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Constants.MODID)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event){
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();

        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ModDatapackProvider datapackProvider = new ModDatapackProvider(packOutput, lookupProvider);

        event.addProvider(datapackProvider);
        event.addProvider(new ModModelProvider(packOutput));
        event.addProvider(new ModBlockTagGenerator(packOutput, lookupProvider));
        event.addProvider(new ModRecipeProvider.Runner(packOutput, datapackProvider.getRegistryProvider()));
        event.addProvider(new ModLootTableProvider(packOutput, lookupProvider));
        event.addProvider(new ModDataMapProvider(packOutput, lookupProvider));
        event.createProvider(ModItemTagProvider::new);
    }

    @SubscribeEvent
    public static void onDataPackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(ModRegistries.RESOURCES_TYPES_KEY, ResourcesTypes.CODEC, ResourcesTypes.CODEC, builder -> builder.maxId(500));
    }
}

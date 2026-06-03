package com.coolerpromc.resourcestrees.datagen;

import com.coolerpromc.resourcestrees.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Constants.MODID)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event){
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();

        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        event.addProvider(new ModModelProvider(packOutput));
        event.addProvider(new ModLanguageProvider(packOutput));
        ModBlockTagGenerator blockTagGenerator = event.addProvider(new ModBlockTagGenerator(packOutput, lookupProvider));
        event.addProvider(new ModRecipeProvider.Runner(packOutput, lookupProvider));
        event.addProvider(new ModLootTableProvider(packOutput, lookupProvider));
        event.addProvider(new ModDataMapProvider(packOutput, lookupProvider));
        event.addProvider(new ModBlockItemTagProvider(packOutput, lookupProvider, blockTagGenerator.contentsGetter()));
    }
}

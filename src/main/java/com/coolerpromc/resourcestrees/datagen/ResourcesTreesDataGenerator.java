package com.coolerpromc.resourcestrees.datagen;

import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datagen.loot.ModBlockLootTables;
import com.coolerpromc.resourcestrees.registry.ModRegistries;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ResourcesTreesDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture = fabricDataGenerator.getRegistries();

		pack.addProvider(ModDatapackProvider::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModBlockTagGenerator::new);
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(ModBlockLootTables::new);
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder.addRegistry(ModRegistries.RESOURCES_TYPES_KEY, ResourcesTypes::bootstrap);
	}
}

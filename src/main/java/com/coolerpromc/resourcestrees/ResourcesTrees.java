package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.item.ModCreativeTab;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.networking.RecipeSyncPayload;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.registry.ModRegistries;
import com.coolerpromc.resourcestrees.screen.ModMenuTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ResourcesTrees implements ModInitializer {
	public static final String MODID = "resourcestrees";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		ModBlocks.register();
		ModItems.register();
		ModDataComponents.register();
		ModBlockEntities.register();
		ModRecipes.register();
		ModCreativeTab.register();
		ModMenuTypes.register();

		Field[] fields = ModBlocks.class.getDeclaredFields();
		for (Field field : fields){
			try {
				Object obj = field.get(null);
				if (obj instanceof ResourcesLeavesBlock block){
					FlammableBlockRegistry.getDefaultInstance().add(block, 60, 30);
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		PayloadTypeRegistry.playS2C().register(RecipeSyncPayload.ID, RecipeSyncPayload.PACKET_CODEC);

		ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, b) -> {
			List<RecipeEntry<?>> recipeEntry = new ArrayList<>(player.getWorld().getRecipeManager().getAllOfType(ModRecipes.TREE_SIMULATOR_TYPE));
			RecipeSyncPayload payload = new RecipeSyncPayload(recipeEntry);
			ServerPlayNetworking.send(player, payload);
		});

		ServerLifecycleEvents.SERVER_STARTED.register(minecraftServer -> {
			ResourcesTypes.LOADED_TYPES.clear();
			ResourcesTypes.LOADED_TYPES.putAll(ResourcesTypes.getAllResourcesTypes(minecraftServer.getOverworld()));
		});

		DynamicRegistries.registerSynced(ModRegistries.RESOURCES_TYPES_KEY, ResourcesTypes.CODEC, ResourcesTypes.CODEC);
	}

	public static Identifier id(String path){
		return Identifier.of(MODID, path);
	}
}
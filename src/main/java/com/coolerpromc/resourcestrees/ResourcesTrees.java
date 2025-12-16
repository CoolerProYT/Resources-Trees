package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.item.ModCreativeTab;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.item.custom.LeafFragmentItem;
import com.coolerpromc.resourcestrees.network.packet.RecipeSyncPayload;
import com.coolerpromc.resourcestrees.network.packet.ResourceTypeSyncS2CPacket;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.registry.ModRegistries;
import com.coolerpromc.resourcestrees.screen.ModMenuTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.ServerTask;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
		PayloadTypeRegistry.playS2C().register(ResourceTypeSyncS2CPacket.TYPE, ResourceTypeSyncS2CPacket.STREAM_CODEC);

        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, b) -> {
            List<RecipeEntry<?>> recipeEntry = new ArrayList<>(player.getEntityWorld().getRecipeManager().getAllOfType(ModRecipes.TREE_SIMULATOR_TYPE));
            RecipeSyncPayload payload = new RecipeSyncPayload(recipeEntry);
            ServerPlayNetworking.send(player, payload);
        });

		DynamicRegistries.registerSynced(ModRegistries.RESOURCES_TYPES_KEY, ResourcesTypes.CODEC, ResourcesTypes.CODEC);

		ServerEntityEvents.ENTITY_LOAD.register(id("tree"), (entity, level) -> {
			if (!FabricLoader.getInstance().isModLoaded("treeharvester")) return;
			if (!(entity instanceof ItemEntity itemEntity)) return;

			level.getServer().executeTask(new ServerTask(5, () -> {
                ItemStack itemStack = itemEntity.getStack();
                Item item = itemStack.getItem();
                if (!(item instanceof BlockItem || item instanceof LeafFragmentItem)) return;

                if (item instanceof BlockItem){
                    Block block = Block.getBlockFromItem(item);
                    if (!(block instanceof ResourcesSaplingBlock)) return;
                }
                if (!itemStack.contains(ModDataComponents.TYPE)) return;

                RegistryEntry<ResourcesTypes> type = itemStack.get(ModDataComponents.TYPE);

                if (type != null){
                    if (itemEntity.isRemoved()) return;

                    BlockPos pos = findNearbyBlock(level, itemEntity.getSteppingPos(), 5, 50);
                    if (pos.equals(BlockPos.ZERO)) {
                        return;
                    }

                    if (!(level.getBlockState(pos).getBlock() instanceof ResourcesSaplingBlock)) {
                        return;
                    }

                    if (!level.isPosLoaded(pos)) {
                        return;
                    }

                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (!(blockEntity instanceof ResourcesTypesBlockEntity be)) {
                        return;
                    }

                    be.setResourcesType(type);
                    be.markDirty();
                    level.getServer().getPlayerManager().getPlayerList().forEach(player -> ServerPlayNetworking.send(player, new ResourceTypeSyncS2CPacket(be.getPos(), type)));

                    List<BlockPos> nearbyPos = findAllNearbyBlock(level, pos, 1);
                    for (BlockPos blockPos : nearbyPos) {
                        if (blockPos.equals(pos)) continue;

                        BlockEntity neighbourBe = level.getBlockEntity(blockPos);
                        if (neighbourBe instanceof ResourcesTypesBlockEntity be2) {
                            be2.setResourcesType(type);
                            be2.markDirty();
                            level.getServer().getPlayerManager().getPlayerList().forEach(player -> ServerPlayNetworking.send(player, new ResourceTypeSyncS2CPacket(be.getPos(), type)));
                        }
                    }
                }
            }));
		});
	}

	public static BlockPos findNearbyBlock(World level, BlockPos center, int radiusXZ, int radiusY) {
		for (int x = -radiusXZ; x <= radiusXZ; x++) {
			for (int y = -radiusY; y <= radiusY; y++) {
				for (int z = -radiusXZ; z <= radiusXZ; z++) {
					BlockPos checkPos = center.add(x, y, z);
					if (level.getBlockState(checkPos).getBlock() instanceof ResourcesSaplingBlock) {
						BlockEntity entity = level.getBlockEntity(checkPos);
						if (entity instanceof ResourcesTypesBlockEntity be && be.getResourcesType() == null){
							return checkPos;
						}
					}
				}
			}
		}
		return BlockPos.ORIGIN;
	}

	public static List<BlockPos> findAllNearbyBlock(World level, BlockPos center, int radius) {
		List<BlockPos> pos = new ArrayList<>();

		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				for (int z = -radius; z <= radius; z++) {
					BlockPos checkPos = center.add(x, y, z);
					if (level.getBlockState(checkPos).getBlock() instanceof ResourcesSaplingBlock) {
						pos.add(checkPos);
					}
				}
			}
		}
		return pos;
	}

	public static Identifier id(String path){
		return Identifier.of(MODID, path);
	}
}
package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.config.ModConfig;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.item.ModCreativeTab;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.item.custom.LeafFragmentItem;
import com.coolerpromc.resourcestrees.network.packet.ResourceTypeSyncS2CPacket;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.recipe.ingredient.ResourcesTypeIngredient;
import com.coolerpromc.resourcestrees.registry.ModRegistries;
import com.coolerpromc.resourcestrees.screen.ModMenuTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.TickTask;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ResourcesTrees implements ModInitializer {
    public static ModConfig CONFIG = new ModConfig();

    @Override
    public void onInitialize() {
        ModBlocks.register();
        ModItems.register();
        ModDataComponents.register();
        ModBlockEntities.register();
        ModRecipes.register();
        ModCreativeTab.register();
        ModMenuTypes.register();

        CONFIG.load();

        CustomIngredientSerializer.register(ResourcesTypeIngredient.SERIALIZER);

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

        PayloadTypeRegistry.clientboundPlay().register(ResourceTypeSyncS2CPacket.TYPE, ResourceTypeSyncS2CPacket.STREAM_CODEC);

        RecipeSynchronization.synchronizeRecipeSerializer(ModRecipes.TREE_SIMULATOR_SERIALIZER);
        RecipeSynchronization.synchronizeRecipeSerializer(ModRecipes.STRICT_SHAPED);

        DynamicRegistries.registerSynced(ModRegistries.RESOURCES_TYPES_KEY, ResourcesTypes.CODEC, ResourcesTypes.CODEC);

        ServerEntityEvents.ENTITY_LOAD.register(id("tree"), (entity, level) -> {
            if (!FabricLoader.getInstance().isModLoaded("treeharvester")) return;
            if (!(entity instanceof ItemEntity itemEntity)) return;

            level.getServer().doRunTask(new TickTask(5, () -> {
                ItemStack itemStack = itemEntity.getItem();
                Item item = itemStack.getItem();
                if (!(item instanceof BlockItem || item instanceof LeafFragmentItem)) return;

                if (item instanceof BlockItem){
                    Block block = Block.byItem(item);
                    if (!(block instanceof ResourcesSaplingBlock)) return;
                }
                if (!itemStack.has(ModDataComponents.TYPE)) return;

                Holder<ResourcesTypes> type = itemStack.get(ModDataComponents.TYPE);

                if (type != null){
                    if (itemEntity.isRemoved()) return;

                    BlockPos pos = findNearbyBlock(level, itemEntity.getOnPos(), 5, 50);
                    if (pos.equals(BlockPos.ZERO)) {
                        return;
                    }

                    if (!(level.getBlockState(pos).getBlock() instanceof ResourcesSaplingBlock)) {
                        return;
                    }

                    if (!level.isLoaded(pos)) {
                        return;
                    }

                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (!(blockEntity instanceof ResourcesTypesBlockEntity be)) {
                        return;
                    }

                    be.setResourcesType(type);
                    be.setChanged();
                    level.getServer().getPlayerList().getPlayers().forEach(player -> ServerPlayNetworking.send(player, new ResourceTypeSyncS2CPacket(be.getBlockPos(), type)));

                    List<BlockPos> nearbyPos = findAllNearbyBlock(level, pos, 1);
                    for (BlockPos blockPos : nearbyPos) {
                        if (blockPos.equals(pos)) continue;

                        BlockEntity neighbourBe = level.getBlockEntity(blockPos);
                        if (neighbourBe instanceof ResourcesTypesBlockEntity be2) {
                            be2.setResourcesType(type);
                            be2.setChanged();
                            level.getServer().getPlayerList().getPlayers().forEach(player -> ServerPlayNetworking.send(player, new ResourceTypeSyncS2CPacket(be.getBlockPos(), type)));
                        }
                    }
                }
            }));
        });
    }

    public static BlockPos findNearbyBlock(Level level, BlockPos center, int radiusXZ, int radiusY) {
        for (int x = -radiusXZ; x <= radiusXZ; x++) {
            for (int y = -radiusY; y <= radiusY; y++) {
                for (int z = -radiusXZ; z <= radiusXZ; z++) {
                    BlockPos checkPos = center.offset(x, y, z);
                    if (level.getBlockState(checkPos).getBlock() instanceof ResourcesSaplingBlock) {
                        BlockEntity entity = level.getBlockEntity(checkPos);
                        if (entity instanceof ResourcesTypesBlockEntity be && be.getResourcesType() == null){
                            return checkPos;
                        }
                    }
                }
            }
        }
        return BlockPos.ZERO;
    }

    public static List<BlockPos> findAllNearbyBlock(Level level, BlockPos center, int radius) {
        List<BlockPos> pos = new ArrayList<>();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos checkPos = center.offset(x, y, z);
                    if (level.getBlockState(checkPos).getBlock() instanceof ResourcesSaplingBlock) {
                        pos.add(checkPos);
                    }
                }
            }
        }
        return pos;
    }

    public static Identifier id(String path){
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
}
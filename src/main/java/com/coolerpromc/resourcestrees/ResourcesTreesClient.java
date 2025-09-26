package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.block.entity.renderer.TreeSimulatorBlockEntityRenderer;
import com.coolerpromc.resourcestrees.datagen.model.ResourcesTypeTintSource;
import com.coolerpromc.resourcestrees.networking.RecipeSyncPayload;
import com.coolerpromc.resourcestrees.screen.ModMenuTypes;
import com.coolerpromc.resourcestrees.screen.custom.TreeSimulatorScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.item.tint.TintSourceTypes;
import net.minecraft.recipe.PreparedRecipes;

public class ResourcesTreesClient implements ClientModInitializer {
    public static PreparedRecipes recipeMap;

    @Override
    public void onInitializeClient() {
        TintSourceTypes.ID_MAPPER.put(ResourcesTrees.id("resources_type_tint"), ResourcesTypeTintSource.MAP_CODEC);

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if (world != null){
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof ResourcesTypesBlockEntity be){
                    if (state.getBlock() instanceof ResourcesSaplingBlock){
                        if (tintIndex == 1){
                            return be.getColor();
                        }
                    }
                    else{
                        return be.getColor();
                    }
                }
            }
            return 0xFFFFFFFF;
        },
                ModBlocks.RESOURCES_OAK_SAPLING,
                ModBlocks.RESOURCES_SPRUCE_SAPLING,
                ModBlocks.RESOURCES_BIRCH_SAPLING,
                ModBlocks.RESOURCES_JUNGLE_SAPLING,
                ModBlocks.RESOURCES_ACACIA_SAPLING,
                ModBlocks.RESOURCES_DARK_OAK_SAPLING,
                ModBlocks.RESOURCES_CHERRY_SAPLING,
                ModBlocks.RESOURCES_PALE_OAK_SAPLING,
                ModBlocks.RESOURCES_OAK_LEAVES,
                ModBlocks.RESOURCES_SPRUCE_LEAVES,
                ModBlocks.RESOURCES_BIRCH_LEAVES,
                ModBlocks.RESOURCES_JUNGLE_LEAVES,
                ModBlocks.RESOURCES_ACACIA_LEAVES,
                ModBlocks.RESOURCES_DARK_OAK_LEAVES,
                ModBlocks.RESOURCES_CHERRY_LEAVES,
                ModBlocks.RESOURCES_PALE_OAK_LEAVES);

        HandledScreens.register(ModMenuTypes.TREE_SIMULATOR, TreeSimulatorScreen::new);

        BlockEntityRendererFactories.register(ModBlockEntities.TREE_SIMULATOR_BE, TreeSimulatorBlockEntityRenderer::new);

        ClientPlayNetworking.registerGlobalReceiver(RecipeSyncPayload.ID, (recipeSyncPayload, context) -> {
            ResourcesTreesClient.recipeMap = PreparedRecipes.of(recipeSyncPayload.recipes());
        });

        BlockRenderLayerMap.putBlocks(BlockRenderLayer.CUTOUT,  ModBlocks.RESOURCES_OAK_SAPLING,
                ModBlocks.RESOURCES_SPRUCE_SAPLING,
                ModBlocks.RESOURCES_BIRCH_SAPLING,
                ModBlocks.RESOURCES_JUNGLE_SAPLING,
                ModBlocks.RESOURCES_ACACIA_SAPLING,
                ModBlocks.RESOURCES_DARK_OAK_SAPLING,
                ModBlocks.RESOURCES_CHERRY_SAPLING,
                ModBlocks.RESOURCES_PALE_OAK_SAPLING);
    }
}

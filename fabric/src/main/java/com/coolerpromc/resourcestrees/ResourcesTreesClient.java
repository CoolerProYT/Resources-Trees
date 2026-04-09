package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.block.entity.renderer.TreeSimulatorBlockEntityRenderer;
import com.coolerpromc.resourcestrees.client.tint.ResourcesTypesTintSource;
import com.coolerpromc.resourcestrees.datagen.model.ResourcesTypeTintSource;
import com.coolerpromc.resourcestrees.network.packet.ResourceTypeSyncS2CPacket;
import com.coolerpromc.resourcestrees.screen.ModMenuTypes;
import com.coolerpromc.resourcestrees.screen.custom.TreeSimulatorScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.recipe.v1.sync.ClientRecipeSynchronizedEvent;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.item.crafting.RecipeMap;

import java.util.List;

public class ResourcesTreesClient implements ClientModInitializer {
    public static RecipeMap recipeMap;

    @Override
    public void onInitializeClient() {
        ItemTintSources.ID_MAPPER.put(ResourcesTrees.id("resources_type_tint"), ResourcesTypeTintSource.MAP_CODEC);

        BlockColorRegistry.register(List.of(new ResourcesTypesTintSource()),
                ModBlocks.RESOURCES_OAK_LEAVES,
                ModBlocks.RESOURCES_SPRUCE_LEAVES,
                ModBlocks.RESOURCES_BIRCH_LEAVES,
                ModBlocks.RESOURCES_JUNGLE_LEAVES,
                ModBlocks.RESOURCES_ACACIA_LEAVES,
                ModBlocks.RESOURCES_DARK_OAK_LEAVES,
                ModBlocks.RESOURCES_CHERRY_LEAVES,
                ModBlocks.RESOURCES_PALE_OAK_LEAVES);

        BlockColorRegistry.register(List.of((_) -> 0xFFFFFFFF , new ResourcesTypesTintSource()),
                ModBlocks.RESOURCES_OAK_SAPLING,
                ModBlocks.RESOURCES_SPRUCE_SAPLING,
                ModBlocks.RESOURCES_BIRCH_SAPLING,
                ModBlocks.RESOURCES_JUNGLE_SAPLING,
                ModBlocks.RESOURCES_ACACIA_SAPLING,
                ModBlocks.RESOURCES_DARK_OAK_SAPLING,
                ModBlocks.RESOURCES_CHERRY_SAPLING,
                ModBlocks.RESOURCES_PALE_OAK_SAPLING);

        MenuScreens.register(ModMenuTypes.TREE_SIMULATOR, TreeSimulatorScreen::new);

        BlockEntityRenderers.register(ModBlockEntities.TREE_SIMULATOR_BE, TreeSimulatorBlockEntityRenderer::new);

        ClientPlayNetworking.registerGlobalReceiver(ResourceTypeSyncS2CPacket.TYPE, ResourceTypeSyncS2CPacket::handle);

        ClientRecipeSynchronizedEvent.EVENT.register((client, recipes) -> recipeMap = RecipeMap.create(recipes.recipes()));
    }
}

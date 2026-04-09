package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.block.entity.renderer.TreeSimulatorBlockEntityRenderer;
import com.coolerpromc.resourcestrees.client.tint.ResourcesTypesTintSource;
import com.coolerpromc.resourcestrees.client.tint.ResourcesTypeTintSource;
import com.coolerpromc.resourcestrees.event.ModRecipeReceived;
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
    @Override
    public void onInitializeClient() {
        ItemTintSources.ID_MAPPER.put(Constants.id("resources_type_tint"), ResourcesTypeTintSource.MAP_CODEC);

        BlockColorRegistry.register(List.of(new ResourcesTypesTintSource()),
                ModBlocks.RESOURCES_OAK_LEAVES.get(),
                ModBlocks.RESOURCES_SPRUCE_LEAVES.get(),
                ModBlocks.RESOURCES_BIRCH_LEAVES.get(),
                ModBlocks.RESOURCES_JUNGLE_LEAVES.get(),
                ModBlocks.RESOURCES_ACACIA_LEAVES.get(),
                ModBlocks.RESOURCES_DARK_OAK_LEAVES.get(),
                ModBlocks.RESOURCES_CHERRY_LEAVES.get(),
                ModBlocks.RESOURCES_PALE_OAK_LEAVES.get());

        BlockColorRegistry.register(List.of((_) -> 0xFFFFFFFF , new ResourcesTypesTintSource()),
                ModBlocks.RESOURCES_OAK_SAPLING.get(),
                ModBlocks.RESOURCES_SPRUCE_SAPLING.get(),
                ModBlocks.RESOURCES_BIRCH_SAPLING.get(),
                ModBlocks.RESOURCES_JUNGLE_SAPLING.get(),
                ModBlocks.RESOURCES_ACACIA_SAPLING.get(),
                ModBlocks.RESOURCES_DARK_OAK_SAPLING.get(),
                ModBlocks.RESOURCES_CHERRY_SAPLING.get(),
                ModBlocks.RESOURCES_PALE_OAK_SAPLING.get());

        MenuScreens.register(ModMenuTypes.TREE_SIMULATOR.get(), TreeSimulatorScreen::new);

        BlockEntityRenderers.register(ModBlockEntities.TREE_SIMULATOR_BE.get(), TreeSimulatorBlockEntityRenderer::new);

        ClientPlayNetworking.registerGlobalReceiver(ResourceTypeSyncS2CPacket.TYPE, (packet, context) -> {
            context.client().execute(() -> packet.handleOnClient(context.player().level()));
        });

        ClientRecipeSynchronizedEvent.EVENT.register((client, recipes) -> {
            ModRecipeReceived.recipeMap = RecipeMap.create(recipes.recipes());
        });
    }
}

package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.block.entity.renderer.TreeSimulatorBlockEntityRenderer;
import com.coolerpromc.resourcestrees.datagen.model.ResourcesTypeTintSource;
import com.coolerpromc.resourcestrees.screen.ModMenuTypes;
import com.coolerpromc.resourcestrees.screen.custom.TreeSimulatorScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import static com.coolerpromc.resourcestrees.ResourcesTrees.MODID;

@Mod(value = MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class ResourcesTreesClient {
    public ResourcesTreesClient(ModContainer container) {

    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {

    }

    @SubscribeEvent
    public static void onRegisterColorHandlersItemTintSources(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(ResourceLocation.fromNamespaceAndPath(MODID, "resources_type_tint"), ResourcesTypeTintSource.MAP_CODEC);
    }

    @SubscribeEvent
    public static void onRegisterColorHandlers(RegisterColorHandlersEvent.Block event) {
        event.register((blockState, blockAndTintGetter, blockPos, i) -> {
            if (blockAndTintGetter != null){
                BlockEntity blockEntity = blockAndTintGetter.getBlockEntity(blockPos);
                if (blockEntity instanceof ResourcesTypesBlockEntity be){
                    return be.getColor();
                }
            }
           return -12012264;
        },
                ModBlocks.RESOURCES_OAK_SAPLING.get(),
                ModBlocks.RESOURCES_SPRUCE_SAPLING.get(),
                ModBlocks.RESOURCES_BIRCH_SAPLING.get(),
                ModBlocks.RESOURCES_JUNGLE_SAPLING.get(),
                ModBlocks.RESOURCES_ACACIA_SAPLING.get(),
                ModBlocks.RESOURCES_DARK_OAK_SAPLING.get(),
                ModBlocks.RESOURCES_CHERRY_SAPLING.get(),
                ModBlocks.RESOURCES_PALE_OAK_SAPLING.get(),
                ModBlocks.RESOURCES_OAK_LEAVES.get(),
                ModBlocks.RESOURCES_SPRUCE_LEAVES.get(),
                ModBlocks.RESOURCES_BIRCH_LEAVES.get(),
                ModBlocks.RESOURCES_JUNGLE_LEAVES.get(),
                ModBlocks.RESOURCES_ACACIA_LEAVES.get(),
                ModBlocks.RESOURCES_DARK_OAK_LEAVES.get(),
                ModBlocks.RESOURCES_CHERRY_LEAVES.get(),
                ModBlocks.RESOURCES_PALE_OAK_LEAVES.get()
        );
    }

    @SubscribeEvent
    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.TREE_SIMULATOR.get(), TreeSimulatorScreen::new);
    }

    @SubscribeEvent
    public static void onEntityRenderersRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.TREE_SIMULATOR_BE.get(), TreeSimulatorBlockEntityRenderer::new);
    }
}

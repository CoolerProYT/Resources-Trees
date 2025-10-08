package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.block.entity.renderer.TreeSimulatorBlockEntityRenderer;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.datagen.model.ResourcesTypeTintSource;
import com.coolerpromc.resourcestrees.item.ModCreativeTab;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.screen.ModMenuTypes;
import com.coolerpromc.resourcestrees.screen.custom.TreeSimulatorScreen;
import com.coolerpromc.resourcestrees.util.DataComponentIngredient;
import com.mojang.logging.LogUtils;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(ResourcesTrees.MODID)
public final class ResourcesTrees {
    public static final String MODID = "resourcestrees";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<IIngredientSerializer<?>> INGREDIENT_SERIALIZERS = DeferredRegister.create(ForgeRegistries.INGREDIENT_SERIALIZERS, MODID);
    public static final RegistryObject<IIngredientSerializer<DataComponentIngredient>> DATA_COMPONENT_INGREDIENT = INGREDIENT_SERIALIZERS.register("data_component", DataComponentIngredient.Serializer::new);

    public ResourcesTrees(FMLJavaModLoadingContext context) {
        var modBusGroup = context.getModBusGroup();
        ServerStartedEvent.BUS.addListener(ResourcesTrees::onServerStarted);

        ModItems.register(modBusGroup);
        ModBlocks.register(modBusGroup);
        ModBlockEntities.register(modBusGroup);
        ModCreativeTab.register(modBusGroup);
        ModDataComponents.register(modBusGroup);
        ModMenuTypes.register(modBusGroup);
        ModRecipes.register(modBusGroup);
        INGREDIENT_SERIALIZERS.register(modBusGroup);
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        ServerLevel level = event.getServer().overworld();
        ResourcesTypes.LOADED_TYPES.clear();
        ResourcesTypes.LOADED_TYPES.putAll(ResourcesTypes.getAllResourcesTypes(level));
    }

    public static ResourceLocation id(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                MenuScreens.register(ModMenuTypes.TREE_SIMULATOR.get(), TreeSimulatorScreen::new);
            });
        }

        @SubscribeEvent
        public static void onRegisterColorHandlers(RegisterColorHandlersEvent.Block event) {
            ItemTintSources.ID_MAPPER.put(ResourceLocation.fromNamespaceAndPath(MODID, "resources_type_tint"), ResourcesTypeTintSource.MAP_CODEC);
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
        public static void onEntityRenderersRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.TREE_SIMULATOR_BE.get(), TreeSimulatorBlockEntityRenderer::new);
        }
    }
}

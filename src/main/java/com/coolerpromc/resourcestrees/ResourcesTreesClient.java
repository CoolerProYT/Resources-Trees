package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.block.entity.renderer.TreeSimulatorBlockEntityRenderer;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.item.custom.EssenceItem;
import com.coolerpromc.resourcestrees.item.custom.LeafFragmentItem;
import com.coolerpromc.resourcestrees.screen.ModMenuTypes;
import com.coolerpromc.resourcestrees.screen.custom.TreeSimulatorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.coolerpromc.resourcestrees.ResourcesTrees.MODID;

@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ResourcesTreesClient {
    @SubscribeEvent
    public static void onFMLClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenuTypes.TREE_SIMULATOR.get(), TreeSimulatorScreen::new);
        });
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
                ModBlocks.RESOURCES_OAK_LEAVES.get(),
                ModBlocks.RESOURCES_SPRUCE_LEAVES.get(),
                ModBlocks.RESOURCES_BIRCH_LEAVES.get(),
                ModBlocks.RESOURCES_JUNGLE_LEAVES.get(),
                ModBlocks.RESOURCES_ACACIA_LEAVES.get(),
                ModBlocks.RESOURCES_DARK_OAK_LEAVES.get(),
                ModBlocks.RESOURCES_CHERRY_LEAVES.get()
        );
    }

    @SubscribeEvent
    public static void onRegisterColorHandlers(RegisterColorHandlersEvent.Item event) {
        event.register((itemStack, i) -> {
            if (itemStack.hasTag() && itemStack.getTag().contains("type")){
                ResourceLocation type = new ResourceLocation(itemStack.getTag().getString("type"));
                if ((Block.byItem(itemStack.getItem()) instanceof ResourcesSaplingBlock && i == 1) || Block.byItem(itemStack.getItem()) instanceof ResourcesLeavesBlock || itemStack.getItem() instanceof LeafFragmentItem){
                    try{
                        return ResourcesTypes.asHolder(Minecraft.getInstance().level, type).value().color();
                    }
                    catch (Exception ignored){
                        return -1;
                    }
                }
            }
            if (itemStack.getItem() instanceof EssenceItem essenceItem){
                return essenceItem.getColor();
            }
            return -1;
        },
                ModBlocks.RESOURCES_OAK_SAPLING.get(),
                ModBlocks.RESOURCES_SPRUCE_SAPLING.get(),
                ModBlocks.RESOURCES_BIRCH_SAPLING.get(),
                ModBlocks.RESOURCES_JUNGLE_SAPLING.get(),
                ModBlocks.RESOURCES_ACACIA_SAPLING.get(),
                ModBlocks.RESOURCES_DARK_OAK_SAPLING.get(),
                ModBlocks.RESOURCES_CHERRY_SAPLING.get(),
                ModBlocks.RESOURCES_OAK_LEAVES.get(),
                ModBlocks.RESOURCES_SPRUCE_LEAVES.get(),
                ModBlocks.RESOURCES_BIRCH_LEAVES.get(),
                ModBlocks.RESOURCES_JUNGLE_LEAVES.get(),
                ModBlocks.RESOURCES_ACACIA_LEAVES.get(),
                ModBlocks.RESOURCES_DARK_OAK_LEAVES.get(),
                ModBlocks.RESOURCES_CHERRY_LEAVES.get(),
                ModItems.LEAF_FRAGMENT.get(),
                ModItems.FIRE_ESSENCE.get(),
                ModItems.WATER_ESSENCE.get(),
                ModItems.NATURE_ESSENCE.get(),
                ModItems.END_ESSENCE.get()
        );
    }

    @SubscribeEvent
    public static void onEntityRenderersRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.TREE_SIMULATOR_BE.get(), TreeSimulatorBlockEntityRenderer::new);
    }
}

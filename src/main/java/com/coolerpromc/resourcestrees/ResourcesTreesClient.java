package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.block.entity.renderer.TreeSimulatorBlockEntityRenderer;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.item.custom.EssenceItem;
import com.coolerpromc.resourcestrees.item.custom.LeafFragmentItem;
import com.coolerpromc.resourcestrees.screen.ModMenuTypes;
import com.coolerpromc.resourcestrees.screen.custom.TreeSimulatorScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.registry.entry.RegistryEntry;

public class ResourcesTreesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
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
                ModBlocks.RESOURCES_OAK_LEAVES,
                ModBlocks.RESOURCES_SPRUCE_LEAVES,
                ModBlocks.RESOURCES_BIRCH_LEAVES,
                ModBlocks.RESOURCES_JUNGLE_LEAVES,
                ModBlocks.RESOURCES_ACACIA_LEAVES,
                ModBlocks.RESOURCES_DARK_OAK_LEAVES,
                ModBlocks.RESOURCES_CHERRY_LEAVES
        );

        ColorProviderRegistry.ITEM.register((itemStack, i) -> {
                    if (itemStack.contains(ModDataComponents.TYPE)){
                        RegistryEntry<ResourcesTypes> type = itemStack.get(ModDataComponents.TYPE);
                        if (type != null && ((Block.getBlockFromItem(itemStack.getItem()) instanceof ResourcesSaplingBlock && i == 1) || Block.getBlockFromItem(itemStack.getItem()) instanceof ResourcesLeavesBlock || itemStack.getItem() instanceof LeafFragmentItem)){
                            return type.value().color();
                        }
                    }
                    if (itemStack.getItem() instanceof EssenceItem essenceItem){
                        return essenceItem.getColor();
                    }
                    return -1;
        },
                ModBlocks.RESOURCES_OAK_SAPLING,
                ModBlocks.RESOURCES_SPRUCE_SAPLING,
                ModBlocks.RESOURCES_BIRCH_SAPLING,
                ModBlocks.RESOURCES_JUNGLE_SAPLING,
                ModBlocks.RESOURCES_ACACIA_SAPLING,
                ModBlocks.RESOURCES_DARK_OAK_SAPLING,
                ModBlocks.RESOURCES_CHERRY_SAPLING,
                ModBlocks.RESOURCES_OAK_LEAVES,
                ModBlocks.RESOURCES_SPRUCE_LEAVES,
                ModBlocks.RESOURCES_BIRCH_LEAVES,
                ModBlocks.RESOURCES_JUNGLE_LEAVES,
                ModBlocks.RESOURCES_ACACIA_LEAVES,
                ModBlocks.RESOURCES_DARK_OAK_LEAVES,
                ModBlocks.RESOURCES_CHERRY_LEAVES,
                ModItems.LEAF_FRAGMENT,
                ModItems.FIRE_ESSENCE,
                ModItems.WATER_ESSENCE,
                ModItems.NATURE_ESSENCE,
                ModItems.END_ESSENCE
        );

        HandledScreens.register(ModMenuTypes.TREE_SIMULATOR, TreeSimulatorScreen::new);

        BlockEntityRendererFactories.register(ModBlockEntities.TREE_SIMULATOR_BE, TreeSimulatorBlockEntityRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.RESOURCES_OAK_SAPLING,
                ModBlocks.RESOURCES_SPRUCE_SAPLING,
                ModBlocks.RESOURCES_BIRCH_SAPLING,
                ModBlocks.RESOURCES_JUNGLE_SAPLING,
                ModBlocks.RESOURCES_ACACIA_SAPLING,
                ModBlocks.RESOURCES_DARK_OAK_SAPLING,
                ModBlocks.RESOURCES_CHERRY_SAPLING
        );
    }
}

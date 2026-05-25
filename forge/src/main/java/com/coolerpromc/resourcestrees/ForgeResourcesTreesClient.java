package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.api.tree.TreeTypes;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.block.entity.renderer.TreeSimulatorBlockEntityRenderer;
import com.coolerpromc.resourcestrees.client.tint.ResourcesTypesTintSource;
import com.coolerpromc.resourcestrees.pack.ResourcesTreesModelPack;
import com.coolerpromc.resourcestrees.screen.ModMenuTypes;
import com.coolerpromc.resourcestrees.screen.custom.TreeSimulatorScreen;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT)
public class ForgeResourcesTreesClient {
    @SubscribeEvent
    public static void onRegisterColorHandlers(RegisterColorHandlersEvent.Block event) {
        event.register(List.of(new ResourcesTypesTintSource()), ModBlocks.LEAVES.stream().map(Supplier::get).toArray(Block[]::new));
        event.register(List.of(BlockTintSources.constant(-1), new ResourcesTypesTintSource()), ModBlocks.SAPLINGS.stream().map(Supplier::get).toArray(Block[]::new));
    }

    @SubscribeEvent
    public static void onRegisterMenuScreens(FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(ModMenuTypes.TREE_SIMULATOR.get(), TreeSimulatorScreen::new));
    }

    @SubscribeEvent
    public static void onEntityRenderersRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.TREE_SIMULATOR_BE.get(), TreeSimulatorBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void onAddPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.CLIENT_RESOURCES) return;
        ResourcesTreesModelPack pack = new ResourcesTreesModelPack();
        TreeTypes.getTypes().forEach(treeType -> {
            pack.addLeavesModel(treeType.leavesTexture());
            pack.addSaplingBlockModel(treeType.saplingTexture());
            pack.addSaplingItemModel(treeType.saplingTexture());
        });
        ModBlocks.SAPLINGS.forEach(handler -> {
            ResourcesSaplingBlock saplingBlock = handler.get();
            pack.addBlockstateEntry(handler.id(), saplingBlock.getTreeType().saplingTexture());
        });
        ModBlocks.LEAVES.forEach(handler -> {
            ResourcesLeavesBlock leavesBlock = handler.get();
            pack.addBlockstateEntry(handler.id(), leavesBlock.getTreeType().leavesTexture());
        });
        event.addRepositorySource(consumer -> consumer.accept(Constants.getPack(pack, pack.getDescription())));
    }
}

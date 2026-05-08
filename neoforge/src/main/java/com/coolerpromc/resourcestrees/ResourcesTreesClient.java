package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.api.tree.TreeType;
import com.coolerpromc.resourcestrees.api.tree.TreeTypes;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.block.entity.renderer.TreeSimulatorBlockEntityRenderer;
import com.coolerpromc.resourcestrees.client.tint.ResourcesTypeTintSource;
import com.coolerpromc.resourcestrees.client.tint.ResourcesTypesTintSource;
import com.coolerpromc.resourcestrees.event.ModRecipeReceived;
import com.coolerpromc.resourcestrees.screen.ModMenuTypes;
import com.coolerpromc.resourcestrees.screen.custom.TreeSimulatorScreen;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.coolerpromc.resourcestrees.Constants.MODID;

@Mod(value = MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class ResourcesTreesClient {
    public ResourcesTreesClient(ModContainer container) {

    }

    @SubscribeEvent
    public static void onRegisterColorHandlersItemTintSources(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(Identifier.fromNamespaceAndPath(MODID, "resources_type_tint"), ResourcesTypeTintSource.MAP_CODEC);
    }

    @SubscribeEvent
    public static void onRegisterColorHandlers(RegisterColorHandlersEvent.BlockTintSources event) {
        event.register(List.of(new ResourcesTypesTintSource()), ModBlocks.LEAVES.stream().map(Supplier::get).toArray(Block[]::new));
        event.register(List.of(BlockTintSources.constant(-1), new ResourcesTypesTintSource()), ModBlocks.SAPLINGS.stream().map(Supplier::get).toArray(Block[]::new));
    }

    @SubscribeEvent
    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.TREE_SIMULATOR.get(), TreeSimulatorScreen::new);
    }

    @SubscribeEvent
    public static void onEntityRenderersRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.TREE_SIMULATOR_BE.get(), TreeSimulatorBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void onRecipesReceived(RecipesReceivedEvent event) {
        ModRecipeReceived.recipeMap = event.getRecipeMap();
    }

    static Map<TreeType, StandaloneModelKey<BlockStateModel>> saplingBlockStateMap = new HashMap<>();
    static Map<TreeType, StandaloneModelKey<BlockStateModel>> leavesBlockStateMap = new HashMap<>();

    @SubscribeEvent
    public static void onRegisterAdditionalModels(ModelEvent.RegisterStandalone event) {
        event.register(new StandaloneModelKey<>(() -> "leaf_fragment"), SimpleUnbakedStandaloneModel.quadCollection(Constants.id("item/leaf_fragment")));
        TreeTypes.getTypes().forEach(treeType -> {
            saplingBlockStateMap.put(treeType, new StandaloneModelKey<>(() -> treeType.saplingTexture().getPath()));
            leavesBlockStateMap.put(treeType, new StandaloneModelKey<>(() -> treeType.leavesTexture().getPath()));
            event.register(saplingBlockStateMap.get(treeType), SimpleUnbakedStandaloneModel.blockStateModel(treeType.saplingTexture()));
            event.register(leavesBlockStateMap.get(treeType), SimpleUnbakedStandaloneModel.blockStateModel(treeType.leavesTexture()));
        });
    }

    @SubscribeEvent
    public static void onModelModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        ModelBakery.BakingResult result = event.getBakingResult();

        ModBlocks.SAPLINGS.forEach(block -> {
            if (block.get() instanceof ResourcesSaplingBlock saplingBlock){
                BlockState blockState = saplingBlock.defaultBlockState();
                if (result.getBlockStateModel(blockState) == result.missingModels().block()){
                    BlockStateModel model = result.standaloneModels().get(saplingBlockStateMap.get(saplingBlock.getTreeType()));
                    saplingBlock.getStateDefinition().getPossibleStates().forEach(state -> {
                        if (model != null){
                            result.blockStateModels().put(state, model);
                        }
                    });
                }
            }
        });

        ModBlocks.LEAVES.forEach(block -> {
            if (block.get() instanceof ResourcesLeavesBlock leavesBlock){
                BlockState blockState = leavesBlock.defaultBlockState();
                BlockStateModel bakedModel = result.getBlockStateModel(blockState);
                if (bakedModel == result.missingModels().block()){
                    BlockStateModel model = result.standaloneModels().get(leavesBlockStateMap.get(leavesBlock.getTreeType()));
                    leavesBlock.getStateDefinition().getPossibleStates().forEach(state -> {
                        if (model != null){
                            result.blockStateModels().put(state, model);
                        }
                    });
                }
            }
        });
    }
}

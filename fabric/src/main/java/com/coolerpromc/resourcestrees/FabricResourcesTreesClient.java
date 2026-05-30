package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.api.tree.TreeTypes;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.block.entity.renderer.TreeSimulatorBlockEntityRenderer;
import com.coolerpromc.resourcestrees.client.tint.ResourcesTypeTintSource;
import com.coolerpromc.resourcestrees.client.tint.ResourcesTypesTintSource;
import com.coolerpromc.resourcestrees.event.ModRecipeReceived;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.item.custom.LeafFragmentItem;
import com.coolerpromc.resourcestrees.screen.ModMenuTypes;
import com.coolerpromc.resourcestrees.screen.custom.TreeSimulatorScreen;
import net.fabricmc.api.ClientModInitializer;
/*import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;*/
import net.fabricmc.fabric.api.client.recipe.v1.sync.ClientRecipeSynchronizedEvent;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.SingleVariant;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.level.block.Block;

import java.util.*;
import java.util.function.Supplier;

public class FabricResourcesTreesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemTintSources.ID_MAPPER.put(Constants.id("resources_type_tint"), ResourcesTypeTintSource.MAP_CODEC);

        BlockColorRegistry.register(List.of(new ResourcesTypesTintSource()), ModBlocks.LEAVES.stream().map(Supplier::get).toArray(Block[]::new));

        BlockColorRegistry.register(List.of((_) -> 0xFFFFFFFF, new ResourcesTypesTintSource()), ModBlocks.SAPLINGS.stream().map(Supplier::get).toArray(Block[]::new));

        MenuScreens.register(ModMenuTypes.TREE_SIMULATOR.get(), TreeSimulatorScreen::new);

        BlockEntityRenderers.register(ModBlockEntities.TREE_SIMULATOR_BE.get(), TreeSimulatorBlockEntityRenderer::new);

        ClientRecipeSynchronizedEvent.EVENT.register((client, recipes) -> {
            ModRecipeReceived.recipeMap = RecipeMap.create(recipes.recipes());
        });

        //TODO: Restore model-loading-api when it is re-enabled by fabric api
        /*ModelLoadingPlugin.register(pluginContext -> {
            TreeTypes.getTypes().forEach(treeType -> pluginContext.addModel(ExtraModelKey.create(() -> treeType.saplingTexture().getPath()), SimpleUnbakedExtraModel.blockStateModel(treeType.saplingTexture().withPath(s -> s.replace("block", "item")))));

            Map<Identifier, ResourcesSaplingBlock> saplingItemMap = new HashMap<>();
            Map<Identifier, ResourcesLeavesBlock> leavesItemMap = new HashMap<>();
            Set<Identifier> leafFragmentIds = new HashSet<>();

            ModItems.LEAF_FRAGMENTS.forEach(handler -> {
                if (handler.get() instanceof LeafFragmentItem) {
                    leafFragmentIds.add(handler.id());
                }
            });

            ModBlocks.SAPLINGS.forEach(handler -> {
                Block block = handler.get();
                if (block instanceof ResourcesSaplingBlock saplingBlock) {
                    saplingItemMap.put(handler.id(), saplingBlock);
                    pluginContext.registerBlockStateResolver(block, context ->
                        block.getStateDefinition().getPossibleStates().forEach(state ->
                            context.setModel(state, new BlockStateModel.SimpleCachedUnbakedRoot(
                                new SingleVariant.Unbaked(new Variant(saplingBlock.getTreeType().saplingTexture()))
                            ))
                        )
                    );
                }
            });

            ModBlocks.LEAVES.forEach(handler -> {
                Block block = handler.get();
                if (block instanceof ResourcesLeavesBlock leavesBlock) {
                    leavesItemMap.put(handler.id(), leavesBlock);
                    pluginContext.registerBlockStateResolver(block, context ->
                        block.getStateDefinition().getPossibleStates().forEach(state ->
                            context.setModel(state, new BlockStateModel.SimpleCachedUnbakedRoot(
                                new SingleVariant.Unbaked(new Variant(leavesBlock.getTreeType().leavesTexture()))
                            ))
                        )
                    );
                }
            });

            pluginContext.modifyItemModelBeforeBake().register(ModelModifier.DEFAULT_PHASE, (model, context) -> {
                Identifier itemId = context.itemId();

                if (leafFragmentIds.contains(itemId)) {
                    model = ItemModelUtils.tintedModel(Constants.id("item/leaf_fragment"), new ResourcesTypeTintSource(-1));
                }

                ResourcesSaplingBlock saplingBlock = saplingItemMap.get(itemId);
                if (saplingBlock != null) {
                    model = ItemModelUtils.tintedModel(
                            saplingBlock.getTreeType().saplingTexture().withPath(s -> s.replace("block", "item")),
                            ItemModelUtils.constantTint(-1),
                            new ResourcesTypeTintSource(-1)
                    );
                }

                ResourcesLeavesBlock leavesBlock = leavesItemMap.get(itemId);
                if (leavesBlock != null) {
                    model = ItemModelUtils.tintedModel(
                        leavesBlock.getTreeType().leavesTexture(),
                        new ResourcesTypeTintSource(-1)
                    );
                }
                ResourcesTreesClient.CLIENT_ITEM_MAP.put(context.itemId(), new ClientItem(model, ClientItem.Properties.DEFAULT, context.bakingContext().contextSwapper()));
                return model;
            });
        });*/
    }
}
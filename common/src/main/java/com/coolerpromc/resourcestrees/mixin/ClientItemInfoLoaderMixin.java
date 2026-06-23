package com.coolerpromc.resourcestrees.mixin;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.api.tree.TreeType;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.AbstractResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.client.tint.ResourcesTypeTintSource;
import com.coolerpromc.resourcestrees.item.ModItems;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.resources.model.ClientItemInfoLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ClientItemInfoLoader.class)
public class ClientItemInfoLoaderMixin {
    @Inject(method = "scheduleLoad", at = @At("RETURN"), cancellable = true)
    private static void injectDynamicItemModels(ResourceManager manager, Executor executor, CallbackInfoReturnable<CompletableFuture<ClientItemInfoLoader.LoadedClientInfos>> cir) {
        cir.setReturnValue(cir.getReturnValue().thenApply(loaded -> {
            Map<Identifier, ClientItem> contents = loaded.contents();

            ItemModel.Unbaked leafFragmentModel = ItemModelUtils.tintedModel(
                    Constants.id("item/leaf_fragment"),
                    new ResourcesTypeTintSource(-1));
            ModItems.LEAF_FRAGMENTS.forEach(handler -> {
                Identifier itemId = BuiltInRegistries.ITEM.getKey(handler.get());
                if (!contents.containsKey(itemId)) {
                    contents.put(itemId, new ClientItem(leafFragmentModel, ClientItem.Properties.DEFAULT));
                }
            });

            ModBlocks.SAPLINGS.forEach(handler -> {
                Block block = handler.get();
                if (block instanceof ResourcesSaplingBlock saplingBlock) {
                    Identifier itemId = handler.id();
                    if (!contents.containsKey(itemId)) {
                        TreeType treeType = saplingBlock.getTreeType();
                        contents.put(itemId, new ClientItem(ItemModelUtils.tintedModel(
                                treeType.saplingTexture().withPath(s -> s.replace("block", "item")),
                                ItemModelUtils.constantTint(-1),
                                new ResourcesTypeTintSource(-1)
                        ), ClientItem.Properties.DEFAULT));
                    }
                }
            });

            ModBlocks.LEAVES.forEach(handler -> {
                Block block = handler.get();
                if (block instanceof AbstractResourcesLeavesBlock leavesBlock) {
                    Identifier itemId = handler.id();
                    if (!contents.containsKey(itemId)) {
                        contents.put(itemId, new ClientItem(
                                ItemModelUtils.tintedModel(
                                        leavesBlock.getTreeType().leavesTexture(),
                                        new ResourcesTypeTintSource(-1)),
                                ClientItem.Properties.DEFAULT));
                    }
                }
            });

            return loaded;
        }));
    }
}

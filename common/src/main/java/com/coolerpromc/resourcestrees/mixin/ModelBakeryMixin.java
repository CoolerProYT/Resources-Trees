package com.coolerpromc.resourcestrees.mixin;

import com.coolerpromc.resourcestrees.CommonClientClass;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ModelBakery.class)
public class ModelBakeryMixin {
    @Inject(
            at = @At("RETURN"),
            method = "<init>(Lnet/minecraft/client/model/geom/EntityModelSet;Lnet/minecraft/client/resources/model/sprite/SpriteGetter;Lnet/minecraft/client/renderer/PlayerSkinRenderCache;Ljava/util/Map;Ljava/util/Map;Ljava/util/Map;Lnet/minecraft/client/resources/model/ResolvedModel;)V"
    )
    public void init(EntityModelSet entityModelSet, SpriteGetter sprites, PlayerSkinRenderCache playerSkinRenderCache, Map<BlockState, BlockStateModel.UnbakedRoot> unbakedBlockStateModels, Map<Identifier, ClientItem> clientInfos, Map<Identifier, ResolvedModel> resolvedModels, ResolvedModel missingModel, CallbackInfo ci) {
        clientInfos.putAll(CommonClientClass.CLIENT_ITEM_MAP);
    }
}
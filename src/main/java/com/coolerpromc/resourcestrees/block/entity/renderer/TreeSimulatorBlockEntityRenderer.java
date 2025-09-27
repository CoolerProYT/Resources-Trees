/*
package com.coolerpromc.resourcestrees.block.entity.renderer;

import com.coolerpromc.resourcestrees.block.entity.custom.TreeSimulatorBlockEntity;
import com.coolerpromc.resourcestrees.block.entity.renderstate.TreeSimulatorRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

public record TreeSimulatorBlockEntityRenderer(BlockEntityRendererProvider.Context context) implements BlockEntityRenderer<TreeSimulatorBlockEntity, TreeSimulatorRenderState> {
    @Override
    public TreeSimulatorRenderState createRenderState() {
        return new TreeSimulatorRenderState();
    }

    @Override
    public void extractRenderState(TreeSimulatorBlockEntity blockEntity, TreeSimulatorRenderState renderState, float p_446851_, Vec3 p_445788_, @Nullable ModelFeatureRenderer.CrumblingOverlay p_446944_) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, p_446851_, p_445788_, p_446944_);
        renderState.blockEntity = blockEntity;

        ItemStackRenderState itemStackRenderState = new ItemStackRenderState();
        context.itemModelResolver().updateForTopItem(itemStackRenderState, blockEntity.getSapling(), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 1);
        renderState.itemStackRenderState = itemStackRenderState;
    }

    @Override
    public void submit(TreeSimulatorRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        TreeSimulatorBlockEntity blockEntity = renderState.blockEntity;

        float scale = (float) blockEntity.getData().get(0) / (float) blockEntity.getData().get(1);

        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0.0, 0.6, 0.0);
        poseStack.last().rotate(new Quaternionf().rotationY((float)Math.toRadians(45)));
        renderState.itemStackRenderState.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY,0);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0.0, 0.6, 0.0);
        poseStack.last().rotate(new Quaternionf().rotationY((float)Math.toRadians(135)));
        renderState.itemStackRenderState.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY,0);
        poseStack.popPose();
    }
}
*/
package com.coolerpromc.resourcestrees.block.entity.renderer;

import com.coolerpromc.resourcestrees.block.entity.custom.TreeSimulatorBlockEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;



import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;

import org.joml.Quaternionf;

public record TreeSimulatorBlockEntityRenderer(BlockEntityRendererProvider.Context context) implements BlockEntityRenderer<TreeSimulatorBlockEntity> {
    @Override
    public void render(TreeSimulatorBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay, Vec3 cameraPos) {
        ItemRenderer itemRenderer = context.getItemRenderer();

        float scale = (float) blockEntity.getData().get(0) / (float) blockEntity.getData().get(1);

        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0.0, 0.6, 0.0);
        poseStack.last().rotate(new Quaternionf().rotationY((float)Math.toRadians(45)));
        itemRenderer.renderStatic(blockEntity.getSapling(), ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, multiBufferSource, blockEntity.getLevel(), 0);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0.0, 0.6, 0.0);
        poseStack.last().rotate(new Quaternionf().rotationY((float)Math.toRadians(135)));
        itemRenderer.renderStatic(blockEntity.getSapling(), ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, multiBufferSource, blockEntity.getLevel(), 0);
        poseStack.popPose();
    }
}
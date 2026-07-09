package com.coolerpromc.resourcestrees.block.entity.renderer;

import com.coolerpromc.resourcestrees.block.entity.custom.TreeSimulatorBlockEntity;
import com.coolerpromc.resourcestrees.block.entity.renderstate.TreeSimulatorRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public record TreeSimulatorBlockEntityRenderer(BlockEntityRendererProvider.Context context) implements BlockEntityRenderer<TreeSimulatorBlockEntity, TreeSimulatorRenderState> {
    @Override
    public TreeSimulatorRenderState createRenderState() {
        return new TreeSimulatorRenderState();
    }

    @Override
    public void extractRenderState(TreeSimulatorBlockEntity blockEntity, TreeSimulatorRenderState renderState, float p_446851_, Vec3 p_445788_, @Nullable ModelFeatureRenderer.CrumblingOverlay p_446944_) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, p_446851_, p_445788_, p_446944_);
        renderState.blockEntity = blockEntity;

        BlockModelRenderState blockModelRenderState = new BlockModelRenderState();
        context.blockModelResolver().update(blockModelRenderState, Block.byItem(blockEntity.getSapling().getItem()).defaultBlockState(), BlockDisplayContext.create());
        renderState.blockModelRenderState = blockModelRenderState;
    }

    @Override
    public void submit(TreeSimulatorRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        TreeSimulatorBlockEntity blockEntity = renderState.blockEntity;

        float scale = (float) blockEntity.getData().get(0) / (float) blockEntity.getData().get(1);

        if (scale > 0){
            poseStack.pushPose();
            poseStack.translate(0.5, 1f / 16f * 2f, 0.5);
            poseStack.scale(scale, scale, scale);
            poseStack.translate(-0.5, 0.0, -0.5);
            renderState.blockModelRenderState.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY,0);
            poseStack.popPose();
        }
    }
}

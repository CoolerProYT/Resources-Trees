package com.coolerpromc.resourcestrees.block.entity.renderer;

import com.coolerpromc.resourcestrees.block.entity.custom.TreeSimulatorBlockEntity;
import com.coolerpromc.resourcestrees.block.entity.renderstate.TreeSimulatorRenderState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

public record TreeSimulatorBlockEntityRenderer(BlockEntityRendererFactory.Context context) implements BlockEntityRenderer<TreeSimulatorBlockEntity, TreeSimulatorRenderState> {
    @Override
    public void updateRenderState(TreeSimulatorBlockEntity blockEntity, TreeSimulatorRenderState renderState, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderer.super.updateRenderState(blockEntity, renderState, tickProgress, cameraPos, crumblingOverlay);
        renderState.blockEntity = blockEntity;

        ItemRenderState itemStackRenderState = new ItemRenderState();
        context.itemModelManager().clearAndUpdate(itemStackRenderState, blockEntity.getSapling(), ItemDisplayContext.FIXED, blockEntity.getWorld(), null, 1);
        renderState.itemStackRenderState = itemStackRenderState;
    }

    @Override
    public TreeSimulatorRenderState createRenderState() {
        return new TreeSimulatorRenderState();
    }

    @Override
    public void render(TreeSimulatorRenderState renderState, MatrixStack poseStack, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        TreeSimulatorBlockEntity blockEntity = renderState.blockEntity;

        float scale = (float) blockEntity.getData().get(0) / (float) blockEntity.getData().get(1);

        poseStack.push();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0.0, 0.6, 0.0);
        poseStack.peek().rotate(new Quaternionf().rotationY((float)Math.toRadians(45)));
        renderState.itemStackRenderState.render(poseStack, queue, renderState.lightmapCoordinates, OverlayTexture.DEFAULT_UV, 0);
        poseStack.pop();

        poseStack.push();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0.0, 0.6, 0.0);
        poseStack.peek().rotate(new Quaternionf().rotationY((float)Math.toRadians(135)));
        renderState.itemStackRenderState.render(poseStack, queue, renderState.lightmapCoordinates, OverlayTexture.DEFAULT_UV, 0);
        poseStack.pop();
    }
}

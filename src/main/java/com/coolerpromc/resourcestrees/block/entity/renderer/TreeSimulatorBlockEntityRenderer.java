package com.coolerpromc.resourcestrees.block.entity.renderer;

import com.coolerpromc.resourcestrees.block.entity.custom.TreeSimulatorBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Quaternionf;

public record TreeSimulatorBlockEntityRenderer(BlockEntityRendererProvider.Context context) implements BlockEntityRenderer<TreeSimulatorBlockEntity> {
    @Override
    public void render(TreeSimulatorBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        ItemRenderer itemRenderer = context.getItemRenderer();

        float scale = (float) blockEntity.getData().get(0) / (float) blockEntity.getData().get(1);

        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0.0, 0.6, 0.0);
        poseStack.mulPose(new Quaternionf().rotationY((float)Math.toRadians(45)));
        itemRenderer.renderStatic(blockEntity.getSapling(), ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, multiBufferSource, blockEntity.getLevel(), 0);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0.0, 0.6, 0.0);
        poseStack.mulPose(new Quaternionf().rotationY((float)Math.toRadians(135)));
        itemRenderer.renderStatic(blockEntity.getSapling(), ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, multiBufferSource, blockEntity.getLevel(), 0);
        poseStack.popPose();
    }
}

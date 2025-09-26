package com.coolerpromc.resourcestrees.block.entity.renderer;

import com.coolerpromc.resourcestrees.block.entity.custom.TreeSimulatorBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;

public record TreeSimulatorBlockEntityRenderer(BlockEntityRendererFactory.Context context) implements BlockEntityRenderer<TreeSimulatorBlockEntity> {
    @Override
    public void render(TreeSimulatorBlockEntity blockEntity, float partialTick, MatrixStack poseStack, VertexConsumerProvider multiBufferSource, int packedLight, int packedOverlay, Vec3d cameraPos) {
        ItemRenderer itemRenderer = context.getItemRenderer();

        float scale = (float) blockEntity.getData().get(0) / (float) blockEntity.getData().get(1);

        poseStack.push();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0.0, 0.6, 0.0);
        poseStack.peek().rotate(new Quaternionf().rotationY((float)Math.toRadians(45)));
        itemRenderer.renderItem(blockEntity.getSapling(), ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, multiBufferSource, blockEntity.getWorld(), 0);
        poseStack.pop();

        poseStack.push();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0.0, 0.6, 0.0);
        poseStack.peek().rotate(new Quaternionf().rotationY((float)Math.toRadians(135)));
        itemRenderer.renderItem(blockEntity.getSapling(), ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, multiBufferSource, blockEntity.getWorld(), 0);
        poseStack.pop();
    }
}

package com.coolerpromc.resourcestrees.block.entity.renderer;

import com.coolerpromc.resourcestrees.block.entity.custom.TreeSimulatorBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Quaternionf;

public record TreeSimulatorBlockEntityRenderer(BlockEntityRendererFactory.Context context) implements BlockEntityRenderer<TreeSimulatorBlockEntity> {
    @Override
    public void render(TreeSimulatorBlockEntity blockEntity, float partialTick, MatrixStack poseStack, VertexConsumerProvider multiBufferSource, int packedLight, int packedOverlay) {
        ItemRenderer itemRenderer = context.getItemRenderer();

        float scale = (float) blockEntity.getData().get(0) / (float) blockEntity.getData().get(1);

        poseStack.push();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0.0, 0.6, 0.0);
        poseStack.multiply(new Quaternionf().rotationY((float)Math.toRadians(45)));
        itemRenderer.renderItem(blockEntity.getSapling(), ModelTransformationMode.FIXED, packedLight, packedOverlay, poseStack, multiBufferSource, blockEntity.getWorld(), 0);
        poseStack.pop();

        poseStack.push();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0.0, 0.6, 0.0);
        poseStack.multiply(new Quaternionf().rotationY((float)Math.toRadians(135)));
        itemRenderer.renderItem(blockEntity.getSapling(), ModelTransformationMode.FIXED, packedLight, packedOverlay, poseStack, multiBufferSource, blockEntity.getWorld(), 0);
        poseStack.pop();
    }
}

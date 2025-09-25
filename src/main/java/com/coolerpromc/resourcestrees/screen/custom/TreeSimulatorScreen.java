package com.coolerpromc.resourcestrees.screen.custom;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TreeSimulatorScreen extends AbstractContainerScreen<TreeSimulatorMenu> {
    public static final ResourceLocation TEXTURE = ResourcesTrees.id("textures/gui/tree_simulator.png");

    public TreeSimulatorScreen(TreeSimulatorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        this.imageHeight = 184;
        this.inventoryLabelY = this.imageHeight - 94;
        super.init();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        renderProgressArrow(guiGraphics);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics){
        ResourceLocation texture = ResourcesTrees.id("textures/gui/progress.png");
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, this.leftPos + 59, this.topPos + 35, 0, 0, this.menu.getProgress(), 16, 22, 16);
    }
}

package com.coolerpromc.resourcestrees.screen.custom;

import com.coolerpromc.resourcestrees.Constants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class TreeSimulatorScreen extends AbstractContainerScreen<TreeSimulatorMenu> {
    public static final Identifier TEXTURE = Constants.id("textures/gui/tree_simulator.png");

    public TreeSimulatorScreen(TreeSimulatorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 184);
    }

    @Override
    protected void init() {
        this.inventoryLabelY = this.imageHeight - 94;
        super.init();
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);

        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        renderProgressArrow(graphics);
    }

    private void renderProgressArrow(GuiGraphicsExtractor guiGraphics){
        Identifier texture = Constants.id("textures/gui/progress.png");
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, this.leftPos + 59, this.topPos + 35, 0, 0, this.menu.getProgress(), 16, 22, 16);
    }
}

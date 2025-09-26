package com.coolerpromc.resourcestrees.screen.custom;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TreeSimulatorScreen extends HandledScreen<TreeSimulatorMenu> {
    public static final Identifier TEXTURE = ResourcesTrees.id("textures/gui/tree_simulator.png");

    public TreeSimulatorScreen(TreeSimulatorMenu menu, PlayerInventory playerInventory, Text title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        this.backgroundHeight = 184;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
        super.init();
    }

    @Override
    protected void drawBackground(DrawContext guiGraphics, float v, int i, int i1) {
        guiGraphics.drawTexture(TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight, 256, 256);
        renderProgressArrow(guiGraphics);
    }

    @Override
    public void render(DrawContext guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        drawMouseoverTooltip(guiGraphics, mouseX, mouseY);
    }

    private void renderProgressArrow(DrawContext guiGraphics){
        Identifier texture = ResourcesTrees.id("textures/gui/progress.png");
        guiGraphics.drawTexture(texture, this.x + 59, this.y + 35, 0, 0, this.handler.getProgress(), 16, 22, 16);
    }
}

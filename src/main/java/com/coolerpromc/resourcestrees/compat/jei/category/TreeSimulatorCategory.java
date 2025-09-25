package com.coolerpromc.resourcestrees.compat.jei.category;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.compat.jei.ModJEIPlugin;
import com.coolerpromc.resourcestrees.recipe.custom.TreeSimulatorRecipe;
import com.coolerpromc.resourcestrees.recipe.output.TreeSimulatorOutput;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record TreeSimulatorCategory(IGuiHelper guiHelper) implements IRecipeCategory<TreeSimulatorRecipe> {
    public static final ResourceLocation TEXTURE = ResourcesTrees.id("textures/gui/tree_simulator.png");
    private static int tickCount = 0;

    @Override
    public void draw(TreeSimulatorRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        tickCount++;
        int arrowWidth = (tickCount % 600) * 23 / 600;

        ResourceLocation texture = ResourcesTrees.id("textures/gui/progress.png");
        guiGraphics.blit(texture, 39, 20, 0, 0, arrowWidth, 16, 22, 16);

        guiGraphics.fill(41, 43, 60, 62, 0xFFC6C6C6);

        Component text = Component.translatable("tooltip.resourcestrees.tickToGrow", recipe.ticksToGrow());
        guiGraphics.drawString(Minecraft.getInstance().font, text, 0, 58, 0xFF666666, false);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, TreeSimulatorRecipe recipe, IFocusGroup iFocusGroup) {
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT,6,20).addItemStack(recipe.tree());

        List<TreeSimulatorOutput> drops = recipe.drops();
        for (int i = 0; i < drops.size(); i ++){
            int finalI = i;
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 78 + 18 * (i % 3), 2 + (i / 3) * 18).addItemStack(drops.get(i).output()).addRichTooltipCallback((r, t) -> tooltipCallback(r, t, drops.get(finalI)));
        }
    }

    private void tooltipCallback(IRecipeSlotView ignored, ITooltipBuilder tooltipBuilder, TreeSimulatorOutput output){
        int chance = (int) (output.chance() * 100);
        String chanceStr = String.format("Output Chance: %s", chance);
        tooltipBuilder.add(Component.literal(chanceStr).append("%").withColor(ChatFormatting.GRAY.getColor()));
    }

    @Override
    public RecipeType<TreeSimulatorRecipe> getRecipeType() {
        return ModJEIPlugin.TREE_SIMULATOR_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.resourcestrees.tree_simulator");
    }

    @Override
    public IDrawable getBackground() {
        return guiHelper.createDrawable(TEXTURE, 20, 15, 137, 65);
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.TREE_SIMULATOR.get()));
    }
}

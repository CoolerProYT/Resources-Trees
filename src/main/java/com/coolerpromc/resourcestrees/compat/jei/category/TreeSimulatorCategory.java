/*
package com.coolerpromc.resourcestrees.compat.jei.category;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.recipe.custom.TreeSimulatorRecipe;
import com.coolerpromc.resourcestrees.recipe.output.TreeSimulatorOutput;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

public class TreeSimulatorCategory extends AbstractRecipeCategory<RecipeHolder<TreeSimulatorRecipe>> {
    public static final ResourceLocation UID = ResourcesTrees.id("dna_extracting");
    public static final ResourceLocation TEXTURE = ResourcesTrees.id("textures/gui/tree_simulator.png");
    public static final IRecipeHolderType<TreeSimulatorRecipe> TREE_SIMULATOR_TYPE = IRecipeHolderType.create(ModRecipes.TREE_SIMULATOR_TYPE.get());
    private int tickCount = 0;

    public TreeSimulatorCategory(IGuiHelper helper) {
        super(TREE_SIMULATOR_TYPE, Component.translatable("block.resourcestrees.tree_simulator"), helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.TREE_SIMULATOR.get())), 168, 77);
    }

    @Override
    public void draw(RecipeHolder<TreeSimulatorRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, 0, 0, 5, 5, 168, 77, 256, 256);

        tickCount++;
        int arrowWidth = (tickCount % 600) * 23 / 600;

        ResourceLocation texture = ResourcesTrees.id("textures/gui/progress.png");
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, 54, 30, 0, 0, arrowWidth, 16, 22, 16);

        guiGraphics.fill(56, 53, 75, 72, 0xFFC6C6C6);

        Component text = Component.translatable("tooltip.resourcestrees.tickToGrow", recipe.value().ticksToGrow());
        guiGraphics.drawString(Minecraft.getInstance().font, text, 0, 70, 0xFF666666, false);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, RecipeHolder<TreeSimulatorRecipe> recipeHolder, IFocusGroup iFocusGroup) {
        TreeSimulatorRecipe recipe = recipeHolder.value();
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT,21,30).add(recipe.tree());

        List<TreeSimulatorOutput> drops = recipe.drops();
        for (int i = 0; i < drops.size(); i ++){
            int finalI = i;
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 93 + 18 * (i % 3), 12 + (i / 3) * 18).add(drops.get(i).output()).addRichTooltipCallback((r, t) -> tooltipCallback(r, t, drops.get(finalI)));
        }
    }

    private void tooltipCallback(IRecipeSlotView ignored, ITooltipBuilder tooltipBuilder, TreeSimulatorOutput output){
        int chance = (int) (output.chance() * 100);
        String chanceStr = String.format("Output Chance: %s", chance);
        tooltipBuilder.add(Component.literal(chanceStr).append("%").withColor(ChatFormatting.GRAY.getColor()));
    }
}
*/

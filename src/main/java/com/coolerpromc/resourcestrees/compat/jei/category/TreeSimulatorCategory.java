package com.coolerpromc.resourcestrees.compat.jei.category;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.recipe.custom.TreeSimulatorRecipe;
import com.coolerpromc.resourcestrees.recipe.output.TreeSimulatorOutput;
import mezz.jei.api.constants.VanillaTypes;
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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("removal")
public record TreeSimulatorCategory(IGuiHelper helper) implements IRecipeCategory<TreeSimulatorRecipe> {
    public static final Identifier UID = ResourcesTrees.id("dna_extracting");
    public static final Identifier TEXTURE = ResourcesTrees.id("textures/gui/tree_simulator.png");
    public static final RecipeType<TreeSimulatorRecipe> TREE_SIMULATOR_TYPE = RecipeType.create(ResourcesTrees.MODID, "tree_simulator", TreeSimulatorRecipe.class);
    private static int tickCount = 0;

    @Override
    public void draw(TreeSimulatorRecipe recipe, IRecipeSlotsView recipeSlotsView, DrawContext guiGraphics, double mouseX, double mouseY) {
        tickCount++;
        int arrowWidth = (tickCount % 600) * 23 / 600;

        Identifier texture = ResourcesTrees.id("textures/gui/progress.png");
        guiGraphics.drawTexture(texture, 39, 20, 0, 0, arrowWidth, 16, 22, 16);

        guiGraphics.fill(41, 43, 60, 62, 0xFFC6C6C6);

        Text text = Text.translatable("tooltip.resourcestrees.tickToGrow", recipe.ticksToGrow());
        guiGraphics.drawText(MinecraftClient.getInstance().textRenderer, text, 0, 58, 0xFF666666, false);
    }

    @Override
    public RecipeType<TreeSimulatorRecipe> getRecipeType() {
        return TREE_SIMULATOR_TYPE;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("block.resourcestrees.tree_simulator");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.TREE_SIMULATOR));
    }

    @Override
    public @Nullable IDrawable getBackground() {
        return helper.createDrawable(TEXTURE, 20, 15, 137, 65);
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
        tooltipBuilder.add(Text.literal(chanceStr).append("%").withColor(Formatting.GRAY.getColorValue()));
    }
}

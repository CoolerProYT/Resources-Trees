package com.coolerpromc.resourcestrees.compat.rei.category;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.compat.rei.display.TreeSimulatorDisplay;
import com.coolerpromc.resourcestrees.recipe.custom.TreeSimulatorRecipe;
import com.coolerpromc.resourcestrees.recipe.output.TreeSimulatorOutput;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TreeSimulatorCategory implements DisplayCategory<TreeSimulatorDisplay> {
    @Override
    public CategoryIdentifier<? extends TreeSimulatorDisplay> getCategoryIdentifier() {
        return TreeSimulatorDisplay.CATEGORY_IDENTIFIER;
    }

    @Override
    public List<Widget> setupDisplay(TreeSimulatorDisplay display, Rectangle bounds) {
        TreeSimulatorRecipe recipe = display.treeSimulatorRecipe();
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createCategoryBase(new Rectangle(bounds.x, bounds.y, 135, 64)));
        widgets.add(Widgets.createArrow(new Point(bounds.x + 35, bounds.y + 24)));
        bounds.setSize(135, 56);
        widgets.add(Widgets.createSlotBackground(new Point(bounds.x + 10, bounds.y + 24)));
        for (int i = 0; i < 9; i ++){
            widgets.add(Widgets.createSlotBackground(new Point(bounds.x + 72 + 18 * (i % 3), bounds.y + 6 + (i / 3) * 18)));
        }

        widgets.add(Widgets.createSlot(new Point(bounds.x + 10, bounds.y + 24)).entries(List.of(EntryStacks.of(recipe.tree()))).markInput());
        for (int i = 0; i < recipe.drops().size(); i ++){
            TreeSimulatorOutput output = recipe.drops().get(i);
            EntryStack<ItemStack> drop = EntryStacks.of(output.output());
            int chance = (int) (output.chance() * 100);
            String chanceStr = String.format("Output Chance: %s", chance);
            drop.tooltip(Component.literal(chanceStr).append("%").withStyle(ChatFormatting.GRAY));
            widgets.add(Widgets.createSlot(new Point(bounds.x + 72 + 18 * (i % 3), bounds.y + 6 + (i / 3) * 18)).entries(List.of(drop)).markOutput());
        }

        return widgets;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.resourcestrees.tree_simulator");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModBlocks.TREE_SIMULATOR.get());
    }
}

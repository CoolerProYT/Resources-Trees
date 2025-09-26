package com.coolerpromc.resourcestrees.datagen.recipebuilder;

import com.coolerpromc.resourcestrees.recipe.custom.TreeSimulatorRecipe;
import com.coolerpromc.resourcestrees.recipe.output.TreeSimulatorOutput;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TreeSimulatorRecipeBuilder implements CraftingRecipeJsonBuilder {
    private ItemStack tree;
    private final List<TreeSimulatorOutput> drops = new ArrayList<>();
    private int ticksToGrow;
    private final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;

    public static TreeSimulatorRecipeBuilder builder(){
        return new TreeSimulatorRecipeBuilder();
    }

    private TreeSimulatorRecipeBuilder(){}

    public TreeSimulatorRecipeBuilder setTree(ItemStack itemStack){
        this.tree = itemStack;
        return this;
    }

    public TreeSimulatorRecipeBuilder addDrops(TreeSimulatorOutput drop){
        this.drops.add(drop);
        return this;
    }

    public TreeSimulatorRecipeBuilder setTicksToGrow(int ticksToGrow){
        this.ticksToGrow = ticksToGrow;
        return this;
    }

    @Override
    public TreeSimulatorRecipeBuilder criterion(String s, AdvancementCriterion<?> criterion) {
        this.criteria.put(s, criterion);
        return this;
    }

    @Override
    public TreeSimulatorRecipeBuilder group(@Nullable String s) {
        this.group = s;
        return this;
    }

    @Override
    public Item getOutputItem() {
        return Items.AIR;
    }

    @Override
    public void offerTo(RecipeExporter exporter, Identifier recipeId) {
        Advancement.Builder advancement = exporter.getAdvancementBuilder()
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId))
                .criteriaMerger(AdvancementRequirements.CriterionMerger.OR);
        this.criteria.forEach(advancement::criterion);

        TreeSimulatorRecipe recipe = new TreeSimulatorRecipe(tree, drops, ticksToGrow);
        exporter.accept(recipeId, recipe, advancement.build(recipeId));
    }
}

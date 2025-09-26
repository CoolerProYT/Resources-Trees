package com.coolerpromc.resourcestrees.datagen.recipebuilder;

import com.coolerpromc.resourcestrees.recipe.custom.TreeSimulatorRecipe;
import com.coolerpromc.resourcestrees.recipe.output.TreeSimulatorOutput;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.RegistryKey;
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
    public void offerTo(RecipeExporter exporter, RegistryKey<Recipe<?>> recipeKey) {
        Advancement.Builder advancement = exporter.getAdvancementBuilder()
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeKey))
                .rewards(AdvancementRewards.Builder.recipe(recipeKey))
                .criteriaMerger(AdvancementRequirements.CriterionMerger.OR);
        this.criteria.forEach(advancement::criterion);

        TreeSimulatorRecipe recipe = new TreeSimulatorRecipe(tree, drops, ticksToGrow);
        exporter.accept(recipeKey, recipe, advancement.build(recipeKey.getValue()));
    }
}

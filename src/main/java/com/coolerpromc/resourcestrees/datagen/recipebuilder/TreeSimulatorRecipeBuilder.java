package com.coolerpromc.resourcestrees.datagen.recipebuilder;

import com.coolerpromc.resourcestrees.recipe.custom.TreeSimulatorRecipe;
import com.coolerpromc.resourcestrees.recipe.output.TreeSimulatorOutput;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TreeSimulatorRecipeBuilder implements RecipeBuilder {
    private ItemStack tree;
    private final List<TreeSimulatorOutput> drops = new ArrayList<>();
    private int ticksToGrow;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
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
    public TreeSimulatorRecipeBuilder unlockedBy(String s, Criterion<?> criterion) {
        this.criteria.put(s, criterion);
        return this;
    }

    @Override
    public TreeSimulatorRecipeBuilder group(@Nullable String s) {
        this.group = s;
        return this;
    }
    @Override
    public Item getResult() {
        return Items.AIR;
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceKey<Recipe<?>> resourceKey) {
        Advancement.Builder advancement = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceKey))
                .rewards(AdvancementRewards.Builder.recipe(resourceKey))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement::addCriterion);

        TreeSimulatorRecipe recipe = new TreeSimulatorRecipe(tree, drops, ticksToGrow);
        recipeOutput.accept(resourceKey, recipe, advancement.build(resourceKey.location()));
    }
}

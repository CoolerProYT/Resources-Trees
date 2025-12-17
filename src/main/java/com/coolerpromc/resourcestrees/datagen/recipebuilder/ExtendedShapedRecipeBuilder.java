package com.coolerpromc.resourcestrees.datagen.recipebuilder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ExtendedShapedRecipeBuilder implements RecipeBuilder {
    private final HolderGetter<Item> items;
    private final RecipeCategory category;
    private final Item result;
    private final int count;
    private final ItemStack resultStack;
    private final List<String> rows;
    private final Map<Character, Ingredient> key;
    private final Map<String, Criterion<?>> criteria;
    @Nullable
    private String group;
    private boolean showNotification;

    private ExtendedShapedRecipeBuilder(HolderGetter<Item> items, RecipeCategory category, ItemLike result, int count) {
        this(items, category, new ItemStack(result, count));
    }

    private ExtendedShapedRecipeBuilder(HolderGetter<Item> p_365072_, RecipeCategory p_249996_, ItemStack result) {
        this.rows = Lists.newArrayList();
        this.key = Maps.newLinkedHashMap();
        this.criteria = new LinkedHashMap<>();
        this.showNotification = true;
        this.items = p_365072_;
        this.category = p_249996_;
        this.result = result.getItem();
        this.count = result.getCount();
        this.resultStack = result;
    }

    public static ExtendedShapedRecipeBuilder shaped(HolderGetter<Item> items, RecipeCategory category, ItemLike result) {
        return shaped(items, category, result, 1);
    }

    public static ExtendedShapedRecipeBuilder shaped(HolderGetter<Item> items, RecipeCategory category, ItemLike result, int count) {
        return new ExtendedShapedRecipeBuilder(items, category, result, count);
    }

    public static ExtendedShapedRecipeBuilder shaped(HolderGetter<Item> p_365019_, RecipeCategory p_251325_, ItemStack result) {
        return new ExtendedShapedRecipeBuilder(p_365019_, p_251325_, result);
    }

    public ExtendedShapedRecipeBuilder define(Character p_206417_, TagKey<Item> p_206418_) {
        return this.define(p_206417_, Ingredient.of(this.items.getOrThrow(p_206418_)));
    }

    public ExtendedShapedRecipeBuilder define(Character p_126128_, ItemLike p_126129_) {
        return this.define(p_126128_, Ingredient.of(p_126129_));
    }

    public ExtendedShapedRecipeBuilder define(Character p_126125_, Ingredient p_126126_) {
        if (this.key.containsKey(p_126125_)) {
            throw new IllegalArgumentException("Symbol '" + p_126125_ + "' is already defined!");
        } else if (p_126125_ == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.key.put(p_126125_, p_126126_);
            return this;
        }
    }

    public ExtendedShapedRecipeBuilder pattern(String p_126131_) {
        if (!this.rows.isEmpty() && p_126131_.length() != this.rows.get(0).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.rows.add(p_126131_);
            return this;
        }
    }

    public ExtendedShapedRecipeBuilder unlockedBy(String p_176751_, Criterion<?> p_300780_) {
        this.criteria.put(p_176751_, p_300780_);
        return this;
    }

    public ExtendedShapedRecipeBuilder group(@Nullable String p_126146_) {
        this.group = p_126146_;
        return this;
    }

    public ExtendedShapedRecipeBuilder showNotification(boolean p_273326_) {
        this.showNotification = p_273326_;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    @Override
    public void save(RecipeOutput p_298334_, ResourceKey<Recipe<?>> p_366310_) {
        ShapedRecipePattern shapedrecipepattern = this.ensureValid(p_366310_);
        Advancement.Builder advancement$builder = p_298334_.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(p_366310_))
                .rewards(AdvancementRewards.Builder.recipe(p_366310_))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement$builder::addCriterion);
        ShapedRecipe shapedrecipe = new ShapedRecipe(
                Objects.requireNonNullElse(this.group, ""),
                RecipeBuilder.determineBookCategory(this.category),
                shapedrecipepattern,
                this.resultStack,
                this.showNotification
        );
        p_298334_.accept(
                p_366310_, shapedrecipe, advancement$builder.build(p_366310_.identifier().withPrefix("recipes/" + this.category.getFolderName() + "/"))
        );
    }

    private ShapedRecipePattern ensureValid(ResourceKey<Recipe<?>> p_362567_) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + p_362567_.identifier());
        } else {
            return ShapedRecipePattern.of(this.key, this.rows);
        }
    }
}

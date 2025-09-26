package com.coolerpromc.resourcestrees.datagen.recipebuilder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementRequirements.CriterionMerger;
import net.minecraft.advancement.AdvancementRewards.Builder;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RawShapedRecipe;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ExtendedShapedRecipeBuilder implements CraftingRecipeJsonBuilder {
    private final RecipeCategory category;
    private final Item output;
    private final int count;
    private final ItemStack resultStack;
    private final List<String> pattern = Lists.newArrayList();
    private final Map<Character, Ingredient> inputs = Maps.newLinkedHashMap();
    private final Map<String, AdvancementCriterion<?>> criteria = new LinkedHashMap();
    @Nullable
    private String group;
    private boolean showNotification = true;

    private ExtendedShapedRecipeBuilder(RecipeCategory category, ItemConvertible output, int count) {
        this(category, new ItemStack(output, count));
    }

    private ExtendedShapedRecipeBuilder(RecipeCategory category, ItemStack result) {
        this.category = category;
        this.output = result.getItem();
        this.count = result.getCount();
        this.resultStack = result;
    }

    public static ExtendedShapedRecipeBuilder create(RecipeCategory category, ItemConvertible output) {
        return create(category, output, 1);
    }

    public static ExtendedShapedRecipeBuilder create(RecipeCategory category, ItemConvertible output, int count) {
        return new ExtendedShapedRecipeBuilder(category, output, count);
    }

    public static ExtendedShapedRecipeBuilder create(RecipeCategory p_251325_, ItemStack result) {
        return new ExtendedShapedRecipeBuilder(p_251325_, result);
    }

    public ExtendedShapedRecipeBuilder input(Character c, TagKey<Item> tag) {
        return this.input(c, Ingredient.fromTag(tag));
    }

    public ExtendedShapedRecipeBuilder input(Character c, ItemConvertible item) {
        return this.input(c, Ingredient.ofItems(item));
    }

    public ExtendedShapedRecipeBuilder input(Character c, Ingredient ingredient) {
        if (this.inputs.containsKey(c)) {
            throw new IllegalArgumentException("Symbol '" + c + "' is already defined!");
        } else if (c == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.inputs.put(c, ingredient);
            return this;
        }
    }

    public ExtendedShapedRecipeBuilder pattern(String patternStr) {
        if (!this.pattern.isEmpty() && patternStr.length() != ((String)this.pattern.get(0)).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.pattern.add(patternStr);
            return this;
        }
    }

    public ExtendedShapedRecipeBuilder criterion(String string, AdvancementCriterion<?> advancementCriterion) {
        this.criteria.put(string, advancementCriterion);
        return this;
    }

    public ExtendedShapedRecipeBuilder group(@Nullable String string) {
        this.group = string;
        return this;
    }

    public ExtendedShapedRecipeBuilder showNotification(boolean showNotification) {
        this.showNotification = showNotification;
        return this;
    }

    public Item getOutputItem() {
        return this.output;
    }

    @Override
    public void offerTo(RecipeExporter exporter, Identifier recipeId) {
        RawShapedRecipe rawShapedRecipe = this.validate(recipeId);
        Advancement.Builder builder = exporter.getAdvancementBuilder().criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId)).rewards(Builder.recipe(recipeId)).criteriaMerger(CriterionMerger.OR);
        Map<String, AdvancementCriterion<?>> var10000 = this.criteria;
        Objects.requireNonNull(builder);
        var10000.forEach(builder::criterion);
        ShapedRecipe shapedRecipe = new ShapedRecipe(Objects.requireNonNullElse(this.group, ""), CraftingRecipeJsonBuilder.toCraftingCategory(this.category), rawShapedRecipe, resultStack, this.showNotification);
        exporter.accept(recipeId, shapedRecipe, builder.build(recipeId.withPrefixedPath("recipes/" + this.category.getName() + "/")));
    }

    private RawShapedRecipe validate(Identifier recipeKey) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + String.valueOf(recipeKey));
        } else {
            return RawShapedRecipe.create(this.inputs, this.pattern);
        }
    }
}

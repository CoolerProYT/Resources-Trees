package com.coolerpromc.resourcestrees.datagen.recipebuilder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.CraftingRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class ShapedRecipeNBTOutputBuilder extends CraftingRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final ItemStack result;
    private final int count;
    private final List<String> rows = Lists.newArrayList();
    private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
    @Nullable
    private String group;
    private boolean showNotification = true;

    public ShapedRecipeNBTOutputBuilder(RecipeCategory category, ItemStack result, int count) {
        this.category = category;
        this.result = result;
        this.count = count;
    }

    public static ShapedRecipeNBTOutputBuilder shaped(RecipeCategory category, ItemStack result) {
        return shaped(category, result, 1);
    }

    public static ShapedRecipeNBTOutputBuilder shaped(RecipeCategory category, ItemStack result, int count) {
        return new ShapedRecipeNBTOutputBuilder(category, result, count);
    }

    public ShapedRecipeNBTOutputBuilder define(Character symbol, TagKey<Item> tag) {
        return this.define(symbol, Ingredient.of(tag));
    }

    public ShapedRecipeNBTOutputBuilder define(Character symbol, ItemLike item) {
        return this.define(symbol, Ingredient.of(new ItemLike[]{item}));
    }

    public ShapedRecipeNBTOutputBuilder define(Character symbol, Ingredient ingredient) {
        if (this.key.containsKey(symbol)) {
            throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!");
        } else if (symbol == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.key.put(symbol, ingredient);
            return this;
        }
    }

    public ShapedRecipeNBTOutputBuilder pattern(String pattern) {
        if (!this.rows.isEmpty() && pattern.length() != ((String)this.rows.get(0)).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.rows.add(pattern);
            return this;
        }
    }

    public ShapedRecipeNBTOutputBuilder unlockedBy(String p_126133_, CriterionTriggerInstance p_126134_) {
        this.advancement.addCriterion(p_126133_, p_126134_);
        return this;
    }

    public ShapedRecipeNBTOutputBuilder group(@Nullable String groupName) {
        this.group = groupName;
        return this;
    }

    public ShapedRecipeNBTOutputBuilder showNotification(boolean showNotification) {
        this.showNotification = showNotification;
        return this;
    }

    public Item getResult() {
        return Items.AIR;
    }

    public void save(Consumer<FinishedRecipe> p_126141_, ResourceLocation p_126142_) {
        this.ensureValid(p_126142_);
        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(p_126142_)).rewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(p_126142_)).requirements(RequirementsStrategy.OR);
        p_126141_.accept(new Result(p_126142_, this.result, this.count, this.group == null ? "" : this.group, determineBookCategory(this.category), this.rows, this.key, this.advancement, p_126142_.withPrefix("recipes/" + this.category.getFolderName() + "/"), this.showNotification));
    }

    private void ensureValid(ResourceLocation p_126144_) {
        if (this.rows.isEmpty()) {
            throw new IllegalStateException("No pattern is defined for shaped recipe " + String.valueOf(p_126144_) + "!");
        } else {
            Set<Character> set = Sets.newHashSet(this.key.keySet());
            set.remove(' ');

            for(String s : this.rows) {
                for(int i = 0; i < s.length(); ++i) {
                    char c0 = s.charAt(i);
                    if (!this.key.containsKey(c0) && c0 != ' ') {
                        String var10002 = String.valueOf(p_126144_);
                        throw new IllegalStateException("Pattern in recipe " + var10002 + " uses undefined symbol '" + c0 + "'");
                    }

                    set.remove(c0);
                }
            }

            if (!set.isEmpty()) {
                throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + String.valueOf(p_126144_));
            } else if (this.rows.size() == 1 && ((String)this.rows.get(0)).length() == 1) {
                throw new IllegalStateException("Shaped recipe " + String.valueOf(p_126144_) + " only takes in a single item - should it be a shapeless recipe instead?");
            } else if (this.advancement.getCriteria().isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + String.valueOf(p_126144_));
            }
        }
    }

    public static class Result extends CraftingRecipeBuilder.CraftingResult {
        private final ResourceLocation id;
        private final ItemStack result;
        private final int count;
        private final String group;
        private final List<String> pattern;
        private final Map<Character, Ingredient> key;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;
        private final boolean showNotification;

        public Result(ResourceLocation p_273548_, ItemStack p_273530_, int p_272738_, String p_273549_, CraftingBookCategory p_273500_, List<String> p_273744_, Map<Character, Ingredient> p_272991_, Advancement.Builder p_273260_, ResourceLocation p_273106_, boolean p_272862_) {
            super(p_273500_);
            this.id = p_273548_;
            this.result = p_273530_;
            this.count = p_272738_;
            this.group = p_273549_;
            this.pattern = p_273744_;
            this.key = p_272991_;
            this.advancement = p_273260_;
            this.advancementId = p_273106_;
            this.showNotification = p_272862_;
        }

        public void serializeRecipeData(JsonObject p_126167_) {
            super.serializeRecipeData(p_126167_);
            if (!this.group.isEmpty()) {
                p_126167_.addProperty("group", this.group);
            }

            JsonArray jsonarray = new JsonArray();

            for(String s : this.pattern) {
                jsonarray.add(s);
            }

            p_126167_.add("pattern", jsonarray);
            JsonObject jsonobject = new JsonObject();

            for(Map.Entry<Character, Ingredient> entry : this.key.entrySet()) {
                jsonobject.add(String.valueOf(entry.getKey()), ((Ingredient)entry.getValue()).toJson());
            }

            p_126167_.add("key", jsonobject);
            JsonObject jsonobject1 = new JsonObject();
            if (this.count > 1) {
                jsonobject1.addProperty("count", this.count);
            }

            p_126167_.add("result", TreeSimulatorRecipeBuilder.itemToJson(this.result));
            p_126167_.addProperty("show_notification", this.showNotification);
        }

        public RecipeSerializer<?> getType() {
            return RecipeSerializer.SHAPED_RECIPE;
        }

        public ResourceLocation getId() {
            return this.id;
        }

        @Nullable
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}

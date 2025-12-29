package com.coolerpromc.resourcestrees.recipe.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

/**
 * A shaped recipe that doesn't allow mirroring - useful for recipes with
 * DataComponentIngredient where pattern matters
 */
public class StrictShapedRecipe extends ShapedRecipe {

    public StrictShapedRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack result, boolean showNotification) {
        super(group, category, pattern, result, showNotification);
    }

    public StrictShapedRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack result) {
        super(group, category, pattern, result);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        // Use strict matching - no mirroring allowed
        int recipeWidth = this.getWidth();
        int recipeHeight = this.getHeight();

        // Try all possible positions in the crafting grid
        for (int startX = 0; startX <= input.width() - recipeWidth; startX++) {
            for (int startY = 0; startY <= input.height() - recipeHeight; startY++) {
                if (matchesAtPosition(input, startX, startY)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean matchesAtPosition(CraftingInput input, int startX, int startY) {
        NonNullList<Ingredient> ingredients = this.getIngredients();
        int recipeWidth = this.getWidth();
        int recipeHeight = this.getHeight();

        // Check every position in the input grid
        for (int inputY = 0; inputY < input.height(); inputY++) {
            for (int inputX = 0; inputX < input.width(); inputX++) {
                int recipeX = inputX - startX;
                int recipeY = inputY - startY;

                ItemStack inputStack = input.getItem(inputX, inputY);

                if (recipeX >= 0 && recipeX < recipeWidth && recipeY >= 0 && recipeY < recipeHeight) {
                    // This position is within recipe bounds
                    int ingredientIndex = recipeY * recipeWidth + recipeX;

                    if (ingredientIndex < ingredients.size()) {
                        Ingredient ingredient = ingredients.get(ingredientIndex);

                        if (!ingredient.test(inputStack)) {
                            return false; // Item doesn't match required ingredient
                        }
                    }
                } else {
                    // Outside recipe bounds - must be empty
                    if (!inputStack.isEmpty()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<StrictShapedRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        private static final MapCodec<StrictShapedRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Codec.STRING.optionalFieldOf("group", "").forGetter(ShapedRecipe::getGroup),
                        CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(ShapedRecipe::category),
                        ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.pattern),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                        Codec.BOOL.optionalFieldOf("show_notification", true).forGetter(ShapedRecipe::showNotification)
                ).apply(instance, StrictShapedRecipe::new)
        );

        private static final StreamCodec<RegistryFriendlyByteBuf, StrictShapedRecipe> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                ShapedRecipe::getGroup,
                CraftingBookCategory.STREAM_CODEC,
                ShapedRecipe::category,
                ShapedRecipePattern.STREAM_CODEC,
                recipe -> recipe.pattern,
                ItemStack.STREAM_CODEC,
                recipe -> recipe.result,
                ByteBufCodecs.BOOL,
                ShapedRecipe::showNotification,
                StrictShapedRecipe::new
        );

        @Override
        public MapCodec<StrictShapedRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, StrictShapedRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
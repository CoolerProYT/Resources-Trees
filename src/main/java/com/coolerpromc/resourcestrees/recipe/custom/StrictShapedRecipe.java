package com.coolerpromc.resourcestrees.recipe.custom;

import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RawShapedRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class StrictShapedRecipe extends ShapedRecipe {
    public StrictShapedRecipe(String group, CraftingRecipeCategory category, RawShapedRecipe pattern, ItemStack result, boolean showNotification) {
        super(group, category, pattern, result, showNotification);
    }

    public StrictShapedRecipe(String group, CraftingRecipeCategory category, RawShapedRecipe pattern, ItemStack result) {
        super(group, category, pattern, result);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World level) {
        int recipeWidth = this.getWidth();
        int recipeHeight = this.getHeight();

        // Try all possible positions in the crafting grid
        for (int startX = 0; startX <= input.getWidth() - recipeWidth; startX++) {
            for (int startY = 0; startY <= input.getHeight() - recipeHeight; startY++) {
                if (matchesAtPosition(input, startX, startY)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean matchesAtPosition(CraftingRecipeInput input, int startX, int startY) {
        DefaultedList<Ingredient> ingredients = this.getIngredients();
        int recipeWidth = this.getWidth();
        int recipeHeight = this.getHeight();

        for (int inputY = 0; inputY < input.getHeight(); inputY++) {
            for (int inputX = 0; inputX < input.getWidth(); inputX++) {
                int recipeX = inputX - startX;
                int recipeY = inputY - startY;

                ItemStack inputStack = input.getStackInSlot(inputX, inputY);

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
    public RecipeSerializer<? extends ShapedRecipe> getSerializer() {
        return ModRecipes.STRICT_SHAPED;
    }

    public static class Serializer implements RecipeSerializer<StrictShapedRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        private static final MapCodec<StrictShapedRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Codec.STRING.optionalFieldOf("group", "").forGetter(ShapedRecipe::getGroup),
                        CraftingRecipeCategory.CODEC.fieldOf("category").orElse(CraftingRecipeCategory.MISC).forGetter(ShapedRecipe::getCategory),
                        RawShapedRecipe.CODEC.forGetter(recipe -> recipe.raw),
                        ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                        Codec.BOOL.optionalFieldOf("show_notification", true).forGetter(ShapedRecipe::showNotification)
                ).apply(instance, StrictShapedRecipe::new)
        );

        private static final PacketCodec<RegistryByteBuf, StrictShapedRecipe> STREAM_CODEC = PacketCodec.tuple(
                PacketCodecs.STRING,
                ShapedRecipe::getGroup,
                CraftingRecipeCategory.PACKET_CODEC,
                ShapedRecipe::getCategory,
                RawShapedRecipe.PACKET_CODEC,
                recipe -> recipe.raw,
                ItemStack.PACKET_CODEC,
                recipe -> recipe.result,
                PacketCodecs.BOOL,
                ShapedRecipe::showNotification,
                StrictShapedRecipe::new
        );

        @Override
        public MapCodec<StrictShapedRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, StrictShapedRecipe> packetCodec() {
            return STREAM_CODEC;
        }
    }
}
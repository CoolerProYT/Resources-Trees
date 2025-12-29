package com.coolerpromc.resourcestrees.recipe.custom;

import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.world.World;

import java.util.List;

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
        IngredientPlacement placementInfo = this.getIngredientPlacement();
        List<Ingredient> ingredients = placementInfo.getIngredients();
        IntList slotsToIngredientIndex = placementInfo.getPlacementSlots();

        int recipeWidth = this.getWidth();
        int recipeHeight = this.getHeight();

        for (int inputY = 0; inputY < input.getHeight(); inputY++) {
            for (int inputX = 0; inputX < input.getWidth(); inputX++) {
                int recipeX = inputX - startX;
                int recipeY = inputY - startY;

                ItemStack inputStack = input.getStackInSlot(inputX, inputY);

                if (recipeX >= 0 && recipeX < recipeWidth && recipeY >= 0 && recipeY < recipeHeight) {
                    int recipeSlot = recipeY * recipeWidth + recipeX;

                    if (recipeSlot < slotsToIngredientIndex.size()) {
                        int ingredientIndex = slotsToIngredientIndex.getInt(recipeSlot);

                        if (ingredientIndex >= 0 && ingredientIndex < ingredients.size()) {
                            Ingredient ingredient = ingredients.get(ingredientIndex);

                            if (!ingredient.test(inputStack)) {
                                return false;
                            }
                        } else {
                            if (!inputStack.isEmpty()) {
                                return false;
                            }
                        }
                    }
                } else {
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
                PacketCodecs.BOOLEAN,
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
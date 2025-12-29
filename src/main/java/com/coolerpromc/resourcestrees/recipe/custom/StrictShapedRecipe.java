package com.coolerpromc.resourcestrees.recipe.custom;

import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

public class StrictShapedRecipe extends ShapedRecipe {
    public StrictShapedRecipe(ResourceLocation id, String group, CraftingBookCategory category, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result) {
        super(id, group, category, width, height, ingredients, result);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        int recipeWidth = this.getWidth();
        int recipeHeight = this.getHeight();

        for (int startX = 0; startX <= container.getWidth() - recipeWidth; startX++) {
            for (int startY = 0; startY <= container.getHeight() - recipeHeight; startY++) {
                if (matchesAtPosition(container, startX, startY)) {
                    return true;
                }
            }
        }
        
        return false;
    }

    private boolean matchesAtPosition(CraftingContainer container, int startX, int startY) {
        NonNullList<Ingredient> ingredients = this.getIngredients();
        int recipeWidth = this.getWidth();
        int recipeHeight = this.getHeight();
        
        // Check every position in the input grid
        for (int containerY = 0; containerY < container.getHeight(); containerY++) {
            for (int containerX = 0; containerX < container.getWidth(); containerX++) {
                int recipeX = containerX - startX;
                int recipeY = containerY - startY;
                
                ItemStack containerStack = container.getItem(containerX + containerY * container.getWidth());
                
                if (recipeX >= 0 && recipeX < recipeWidth && recipeY >= 0 && recipeY < recipeHeight) {
                    // This position is within recipe bounds
                    int ingredientIndex = recipeY * recipeWidth + recipeX;
                    
                    if (ingredientIndex < ingredients.size()) {
                        Ingredient ingredient = ingredients.get(ingredientIndex);
                        
                        if (!ingredient.test(containerStack)) {
                            return false; // Item doesn't match required ingredient
                        }
                    }
                } else {
                    // Outside recipe bounds - must be empty
                    if (!containerStack.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.STRICT_SHAPED.get();
    }

    public static class Serializer implements RecipeSerializer<StrictShapedRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        private static final ShapedRecipe.Serializer VANILLA_SERIALIZER = new ShapedRecipe.Serializer();

        @Override
        public StrictShapedRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ShapedRecipe vanillaRecipe = VANILLA_SERIALIZER.fromJson(recipeId, json);
            
            return new StrictShapedRecipe(
                recipeId,
                vanillaRecipe.getGroup(),
                vanillaRecipe.category(),
                vanillaRecipe.getWidth(),
                vanillaRecipe.getHeight(),
                vanillaRecipe.getIngredients(),
                vanillaRecipe.getResultItem(null)
            );
        }

        @Override
        public StrictShapedRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            ShapedRecipe vanillaRecipe = VANILLA_SERIALIZER.fromNetwork(recipeId, buffer);
            
            if (vanillaRecipe == null) {
                return null;
            }
            
            return new StrictShapedRecipe(
                recipeId,
                vanillaRecipe.getGroup(),
                vanillaRecipe.category(),
                vanillaRecipe.getWidth(),
                vanillaRecipe.getHeight(),
                vanillaRecipe.getIngredients(),
                vanillaRecipe.getResultItem(null)
            );
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, StrictShapedRecipe recipe) {
            VANILLA_SERIALIZER.toNetwork(buffer, recipe);
        }
    }
}
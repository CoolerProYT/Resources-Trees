package com.coolerpromc.resourcestrees.mixin;

import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.fabric.impl.recipe.ingredient.builtin.ComponentsIngredient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.book.RecipeBook;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Set;

@Mixin(RecipeResultCollection.class)
public class RecipeDisplayEntryMixin {

    @Shadow
    @Final
    private List<RecipeEntry<?>> recipes;

    @Shadow
    @Final
    private Set<RecipeEntry<?>> craftableRecipes;

    @Shadow
    @Final
    private Set<RecipeEntry<?>> fittingRecipes;

    @Inject(method = "computeCraftables", at = @At("HEAD"), cancellable = true)
    private void onCanCraft(RecipeMatcher handler, int width, int height, RecipeBook book, CallbackInfo ci) {
        boolean hasDataComponentRecipe = false;

        for (RecipeEntry<?> recipeHolder : this.recipes) {
            if (recipeHolder.value() instanceof CraftingRecipe craftingRecipe) {
                List<Ingredient> ingredients = craftingRecipe.getIngredients();

                boolean hasDataComponentIngredient = ingredients.stream()
                        .anyMatch(ing -> ing.getCustomIngredient() != null && ing.getCustomIngredient() instanceof ComponentsIngredient);

                if (hasDataComponentIngredient) {
                    hasDataComponentRecipe = true;
                    break;
                }
            }
        }

        if (!hasDataComponentRecipe) {
            return;
        }

        for (RecipeEntry<?> recipeHolder : this.recipes) {
            boolean flag = recipeHolder.value().fits(width, height) && book.contains(recipeHolder);

            if (flag) {
                this.fittingRecipes.add(recipeHolder);
            } else {
                this.fittingRecipes.remove(recipeHolder);
            }

            if (flag) {
                boolean canCraft = false;

                if (recipeHolder.value() instanceof CraftingRecipe craftingRecipe) {
                    List<Ingredient> ingredients = craftingRecipe.getIngredients();

                    boolean hasDataComponentIngredient = ingredients.stream()
                            .anyMatch(ing -> ing.getCustomIngredient() != null && ing.getCustomIngredient() instanceof ComponentsIngredient);

                    if (hasDataComponentIngredient) {
                        canCraft = canCraftWithDataComponents(ingredients);
                    } else {
                        canCraft = handler.match(recipeHolder.value(), (IntList) null);
                    }
                } else {
                    canCraft = handler.match(recipeHolder.value(), (IntList) null);
                }

                if (canCraft) {
                    this.craftableRecipes.add(recipeHolder);
                } else {
                    this.craftableRecipes.remove(recipeHolder);
                }
            } else {
                this.craftableRecipes.remove(recipeHolder);
            }
        }

        ci.cancel();
    }

    private boolean canCraftWithDataComponents(List<Ingredient> ingredients) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            return false;
        }

        int[] allocatedCounts = new int[player.getInventory().size()];

        for (Ingredient ingredient : ingredients) {
            if (ingredient.isEmpty()) {
                continue;
            }

            boolean found = false;

            for (int invSlot = 0; invSlot < player.getInventory().size(); invSlot++) {
                ItemStack stack = player.getInventory().getStack(invSlot);

                if (!stack.isEmpty() && ingredient.test(stack)) {
                    int available = stack.getCount() - allocatedCounts[invSlot];

                    if (available > 0) {
                        allocatedCounts[invSlot]++;
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                return false;
            }
        }

        return true;
    }
}
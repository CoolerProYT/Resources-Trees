package com.coolerpromc.resourcestrees.mixin;

import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.fabric.impl.recipe.ingredient.builtin.ComponentsIngredient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(InputSlotFiller.class)
public abstract class ServerPlaceRecipeMixin<R extends Recipe<?>> {
    @Final
    @Shadow
    private PlayerInventory inventory;

    @Final
    @Shadow
    private List<Slot> inputSlots;

    @Final
    @Shadow
    private int width;

    @Final
    @Shadow
    private int height;

    @Inject(method = "tryFill", at = @At("HEAD"), cancellable = true)
    private void onTryPlaceRecipe(RecipeEntry<@NotNull R> recipe, RecipeFinder stackedItemContents, CallbackInfoReturnable<AbstractRecipeScreenHandler.PostFillAction> cir) {
        Recipe<?> recipeValue = recipe.value();
        if (recipeValue instanceof CraftingRecipe craftingRecipe) {
            boolean hasDataComponentIngredient = craftingRecipe.getIngredientPlacement().getIngredients().stream().anyMatch(ing -> ing.getCustomIngredient() != null && ing.getCustomIngredient() instanceof ComponentsIngredient);

            if (hasDataComponentIngredient) {
                if (resourcesTrees$canCraftWithDataComponents(craftingRecipe)) {
                    this.returnInputs();
                    this.resourcesTrees$placeRecipeWithDataComponents(recipe);
                    this.inventory.markDirty();
                    cir.setReturnValue(AbstractRecipeScreenHandler.PostFillAction.NOTHING);
                } else {
                    this.returnInputs();
                    this.inventory.markDirty();
                    cir.setReturnValue(AbstractRecipeScreenHandler.PostFillAction.PLACE_GHOST_RECIPE);
                }
            }
        }
    }

    @Unique
    private boolean resourcesTrees$canCraftWithDataComponents(CraftingRecipe recipe) {
        IngredientPlacement placementInfo = recipe.getIngredientPlacement();
        List<Ingredient> ingredients = placementInfo.getIngredients();
        List<Integer> slotsToIngredientIndex = placementInfo.getPlacementSlots();

        int[] allocatedCounts = new int[inventory.size()];
        for (int ingredientIndex : slotsToIngredientIndex) {
            if (ingredientIndex >= 0 && ingredientIndex < ingredients.size()) {
                Ingredient ingredient = ingredients.get(ingredientIndex);

                if (!ingredient.isEmpty()) {
                    boolean found = false;

                    for (int invSlot = 0; invSlot < inventory.size(); invSlot++) {
                        ItemStack stack = inventory.getStack(invSlot);

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
            }
        }

        return true;
    }

    @Unique
    private void resourcesTrees$placeRecipeWithDataComponents(RecipeEntry<@NotNull R> recipe) {
        Recipe<?> recipeValue = recipe.value();

        IngredientPlacement placementInfo = recipeValue.getIngredientPlacement();
        List<Ingredient> ingredients = placementInfo.getIngredients();
        IntList slotsToIngredientIndex = placementInfo.getPlacementSlots();

        int[] takenCounts = new int[inventory.size()];

        int recipeWidth, recipeHeight;
        if (recipeValue instanceof ShapedRecipe shapedRecipe) {
            recipeWidth = shapedRecipe.getWidth();
            recipeHeight = shapedRecipe.getHeight();
        } else {
            recipeWidth = Math.min(slotsToIngredientIndex.size(), this.width);
            recipeHeight = (slotsToIngredientIndex.size() + recipeWidth - 1) / recipeWidth;
        }

        int offsetX = (this.width - recipeWidth) / 2;
        int offsetY = (this.height - recipeHeight) / 2;

        for (int i = 0; i < slotsToIngredientIndex.size(); i++) {
            int ingredientIndex = slotsToIngredientIndex.getInt(i);

            if (ingredientIndex >= 0 && ingredientIndex < ingredients.size()) {
                Ingredient ingredient = ingredients.get(ingredientIndex);

                if (!ingredient.isEmpty()) {
                    int recipeX = i % recipeWidth;
                    int recipeY = i / recipeWidth;

                    int gridX = offsetX + recipeX;
                    int gridY = offsetY + recipeY;
                    int gridSlot = gridY * this.width + gridX;

                    if (gridSlot >= 0 && gridSlot < inputSlots.size()) {
                        Slot targetSlot = inputSlots.get(gridSlot);

                        int invSlot = resourcesTrees$findSlotMatchingIngredient(ingredient, takenCounts);

                        if (invSlot != -1) {
                            ItemStack sourceStack = inventory.getStack(invSlot);
                            ItemStack toPlace = sourceStack.split(1);

                            targetSlot.setStack(toPlace);
                            takenCounts[invSlot]++;
                        }
                    }
                }
            }
        }

        this.inventory.markDirty();
    }

    @Unique
    private int resourcesTrees$findSlotMatchingIngredient(Ingredient ingredient, int[] takenCounts) {
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);

            if (!stack.isEmpty() && ingredient.test(stack)) {
                int available = stack.getCount() - takenCounts[i];

                if (available > 0) {
                    return i;
                }
            }
        }

        return -1;
    }

    @Shadow
    protected abstract void returnInputs();
}
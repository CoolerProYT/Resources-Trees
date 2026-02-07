package com.coolerpromc.resourcestrees.mixin;

import com.coolerpromc.resourcestrees.recipe.ingredient.ResourcesTypeIngredient;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ServerPlaceRecipe.class)
public abstract class ServerPlaceRecipeMixin<R extends Recipe<?>> {
    @Final
    @Shadow
    private Inventory inventory;

    @Final
    @Shadow
    private List<Slot> inputGridSlots;

    @Final
    @Shadow
    private int gridWidth;

    @Final
    @Shadow
    private int gridHeight;

    @Final
    @Shadow
    private boolean useMaxItems;

    @Final
    @Shadow
    private ServerPlaceRecipe.CraftingMenuAccess<R> menu;

    @Inject(method = "tryPlaceRecipe", at = @At("HEAD"), cancellable = true)
    private void onTryPlaceRecipe(RecipeHolder<@NotNull R> recipe, StackedItemContents stackedItemContents, CallbackInfoReturnable<RecipeBookMenu.PostPlaceAction> cir) {
        Recipe<?> recipeValue = recipe.value();
        if (recipeValue instanceof CraftingRecipe craftingRecipe) {
            boolean hasDataComponentIngredient = craftingRecipe.placementInfo().ingredients().stream().anyMatch(ing -> !ing.isSimple() && ing instanceof ResourcesTypeIngredient);

            if (hasDataComponentIngredient) {
                if (resourcesTrees$canCraftWithDataComponents(craftingRecipe)) {
                    this.clearGrid();
                    this.resourcesTrees$placeRecipeWithDataComponents(recipe);
                    this.inventory.setChanged();
                    cir.setReturnValue(RecipeBookMenu.PostPlaceAction.NOTHING);
                } else {
                    this.clearGrid();
                    this.inventory.setChanged();
                    cir.setReturnValue(RecipeBookMenu.PostPlaceAction.PLACE_GHOST_RECIPE);
                }
            }
        }
    }

    @Unique
    private boolean resourcesTrees$canCraftWithDataComponents(CraftingRecipe recipe) {
        PlacementInfo placementInfo = recipe.placementInfo();
        List<Ingredient> ingredients = placementInfo.ingredients();
        List<Integer> slotsToIngredientIndex = placementInfo.slotsToIngredientIndex();

        int[] allocatedCounts = new int[inventory.getContainerSize()];
        for (int ingredientIndex : slotsToIngredientIndex) {
            if (ingredientIndex >= 0 && ingredientIndex < ingredients.size()) {
                Ingredient ingredient = ingredients.get(ingredientIndex);

                if (!ingredient.isEmpty()) {
                    boolean found = false;

                    for (int invSlot = 0; invSlot < inventory.getContainerSize(); invSlot++) {
                        ItemStack stack = inventory.getItem(invSlot);

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
    private void resourcesTrees$placeRecipeWithDataComponents(RecipeHolder<@NotNull R> recipe) {
        Recipe<?> recipeValue = recipe.value();

        PlacementInfo placementInfo = recipeValue.placementInfo();
        List<Ingredient> ingredients = placementInfo.ingredients();
        IntList slotsToIngredientIndex = placementInfo.slotsToIngredientIndex();

        int recipeWidth, recipeHeight;
        if (recipeValue instanceof ShapedRecipe shapedRecipe) {
            recipeWidth = shapedRecipe.getWidth();
            recipeHeight = shapedRecipe.getHeight();
        } else {
            recipeWidth = Math.min(slotsToIngredientIndex.size(), this.gridWidth);
            recipeHeight = (slotsToIngredientIndex.size() + recipeWidth - 1) / recipeWidth;
        }

        int offsetX = (this.gridWidth - recipeWidth) / 2;
        int offsetY = (this.gridHeight - recipeHeight) / 2;

        int amountPerSlot = 1;

        if (this.useMaxItems) {
            boolean recipeMatches = this.menu.recipeMatches(recipe);

            if (recipeMatches) {
                int minCount = Integer.MAX_VALUE;
                for (int i = 0; i < slotsToIngredientIndex.size(); i++) {
                    int ingredientIndex = slotsToIngredientIndex.getInt(i);
                    if (ingredientIndex >= 0 && ingredientIndex < ingredients.size()) {
                        Ingredient ingredient = ingredients.get(ingredientIndex);
                        if (!ingredient.isEmpty()) {
                            int recipeX = i % recipeWidth;
                            int recipeY = i / recipeWidth;
                            int gridX = offsetX + recipeX;
                            int gridY = offsetY + recipeY;
                            int gridSlot = gridY * this.gridWidth + gridX;

                            if (gridSlot >= 0 && gridSlot < inputGridSlots.size()) {
                                ItemStack stack = inputGridSlots.get(gridSlot).getItem();
                                if (!stack.isEmpty()) {
                                    minCount = Math.min(minCount, stack.getCount());
                                }
                            }
                        }
                    }
                }

                if (minCount < 64) {
                    amountPerSlot = minCount + 1;
                }
            } else {
                amountPerSlot = resourcesTrees$calculateMaxCraftsFromSlots(ingredients, slotsToIngredientIndex);
            }
        }

        for (int i = 0; i < slotsToIngredientIndex.size(); i++) {
            int ingredientIndex = slotsToIngredientIndex.getInt(i);

            if (ingredientIndex >= 0 && ingredientIndex < ingredients.size()) {
                Ingredient ingredient = ingredients.get(ingredientIndex);

                if (!ingredient.isEmpty()) {
                    int recipeX = i % recipeWidth;
                    int recipeY = i / recipeWidth;

                    int gridX = offsetX + recipeX;
                    int gridY = offsetY + recipeY;
                    int gridSlot = gridY * this.gridWidth + gridX;

                    if (gridSlot >= 0 && gridSlot < inputGridSlots.size()) {
                        Slot targetSlot = inputGridSlots.get(gridSlot);

                        int placed = 0;
                        while (placed < amountPerSlot) {
                            int invSlot = resourcesTrees$findSlotMatchingIngredient(ingredient);

                            if (invSlot == -1) {
                                break;
                            }

                            ItemStack sourceStack = inventory.getItem(invSlot);
                            int available = sourceStack.getCount();
                            int toTake = Math.min(amountPerSlot - placed, available);

                            if (toTake <= 0) {
                                break;
                            }

                            ItemStack taken = sourceStack.split(toTake);

                            if (targetSlot.getItem().isEmpty()) {
                                targetSlot.set(taken);
                            } else {
                                targetSlot.getItem().grow(toTake);
                            }

                            placed += toTake;
                        }
                    }
                }
            }
        }

        this.inventory.setChanged();
    }

    @Unique
    private int resourcesTrees$calculateMaxCraftsFromSlots(List<Ingredient> ingredients, IntList slotsToIngredientIndex) {
        int totalSlotsNeeded = 0;
        for (int i = 0; i < slotsToIngredientIndex.size(); i++) {
            int ingredientIndex = slotsToIngredientIndex.getInt(i);
            if (ingredientIndex >= 0 && ingredientIndex < ingredients.size()) {
                Ingredient ingredient = ingredients.get(ingredientIndex);
                if (!ingredient.isEmpty()) {
                    totalSlotsNeeded++;
                }
            }
        }

        if (totalSlotsNeeded == 0) {
            return 1;
        }

        int totalAvailable = 0;
        boolean[] countedSlots = new boolean[inventory.getContainerSize()];

        for (int invSlot = 0; invSlot < inventory.getContainerSize(); invSlot++) {
            ItemStack stack = inventory.getItem(invSlot);
            if (stack.isEmpty()) continue;

            for (Ingredient ingredient : ingredients) {
                if (!ingredient.isEmpty() && ingredient.test(stack)) {
                    if (!countedSlots[invSlot]) {
                        totalAvailable += stack.getCount();
                        countedSlots[invSlot] = true;
                    }
                    break;
                }
            }
        }

        int maxCrafts = totalAvailable / totalSlotsNeeded;

        return Math.max(1, Math.min(maxCrafts, 64));
    }

    @Unique
    private int resourcesTrees$findSlotMatchingIngredient(Ingredient ingredient) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);

            if (!stack.isEmpty() && ingredient.test(stack)) {
                return i;
            }
        }

        return -1;
    }

    @Shadow
    protected abstract void clearGrid();
}
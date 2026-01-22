package com.coolerpromc.resourcestrees.mixin;

import com.coolerpromc.resourcestrees.recipe.ingredient.ResourcesTypeIngredient;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.CraftFailedResponseS2CPacket;
import net.minecraft.recipe.*;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InputSlotFiller.class)
public abstract class ServerPlaceRecipeMixin<I extends RecipeInput, R extends Recipe<I>> {

    @Shadow
    protected RecipeMatcher matcher;

    @Shadow
    protected PlayerInventory inventory;

    @Shadow
    protected AbstractRecipeScreenHandler<I, R> handler;

    @Shadow
    protected abstract void returnInputs();

    @Inject(method = "fillInputSlots(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/recipe/RecipeEntry;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/RecipeMatcher;match(Lnet/minecraft/recipe/Recipe;Lit/unimi/dsi/fastutil/ints/IntList;)Z"), cancellable = true)
    private void onRecipeClicked(ServerPlayerEntity player, @Nullable RecipeEntry<R> recipe, boolean placeAll, CallbackInfo ci) {
        if (recipe == null) {
            return;
        }

        Recipe<I> recipeValue = recipe.value();
        if (recipeValue instanceof CraftingRecipe craftingRecipe) {
            DefaultedList<Ingredient> ingredients = craftingRecipe.getIngredients();

            boolean hasDataComponentIngredient = ingredients.stream()
                    .anyMatch(ing -> ing.getCustomIngredient() != null && ing.getCustomIngredient() instanceof ResourcesTypeIngredient);

            if (hasDataComponentIngredient) {
                if (resourcesTrees$canCraftWithDataComponents(ingredients)) {
                    this.returnInputs();

                    int amountPerSlot = 1;
                    if (placeAll) {
                        amountPerSlot = resourcesTrees$calculateMaxCrafts(ingredients);
                    }

                    resourcesTrees$placeRecipeManually(craftingRecipe, amountPerSlot);
                    player.getInventory().markDirty();
                } else {
                    this.returnInputs();
                    player.networkHandler.sendPacket(new CraftFailedResponseS2CPacket(player.currentScreenHandler.syncId, recipe));
                    player.getInventory().markDirty();
                }
                ci.cancel();
            }
        }
    }

    @Unique
    private boolean resourcesTrees$canCraftWithDataComponents(DefaultedList<Ingredient> ingredients) {
        int[] allocatedCounts = new int[inventory.size()];

        for (Ingredient ingredient : ingredients) {
            if (ingredient.isEmpty()) {
                continue;
            }

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

        return true;
    }

    @Unique
    private int resourcesTrees$calculateMaxCrafts(DefaultedList<Ingredient> ingredients) {
        int totalSlotsNeeded = (int) ingredients.stream()
                .filter(ing -> !ing.isEmpty())
                .count();

        if (totalSlotsNeeded == 0) {
            return 1;
        }

        int totalAvailable = 0;
        boolean[] countedSlots = new boolean[inventory.size()];

        for (int invSlot = 0; invSlot < inventory.size(); invSlot++) {
            ItemStack stack = inventory.getStack(invSlot);
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
    private void resourcesTrees$placeRecipeManually(CraftingRecipe craftingRecipe, int amountPerSlot) {
        int gridWidth = this.handler.getCraftingWidth();
        int gridHeight = this.handler.getCraftingHeight();

        DefaultedList<Ingredient> ingredients = craftingRecipe.getIngredients();

        int recipeWidth;
        int recipeHeight;

        if (craftingRecipe instanceof ShapedRecipe shapedRecipe) {
            recipeWidth = shapedRecipe.getWidth();
            recipeHeight = shapedRecipe.getHeight();
        } else {
            int totalIngredients = (int) ingredients.stream().filter(ing -> !ing.isEmpty()).count();
            recipeWidth = Math.min(totalIngredients, gridWidth);
            recipeHeight = (totalIngredients + recipeWidth - 1) / recipeWidth;
        }

        int startX = (gridWidth - recipeWidth) / 2;
        int startY = (gridHeight - recipeHeight) / 2;

        int resultSlotIndex = this.handler.getCraftingResultSlotIndex();

        if (craftingRecipe instanceof ShapedRecipe) {
            for (int ingredientIdx = 0; ingredientIdx < ingredients.size(); ingredientIdx++) {
                Ingredient ingredient = ingredients.get(ingredientIdx);

                if (!ingredient.isEmpty()) {
                    int recipeX = ingredientIdx % recipeWidth;
                    int recipeY = ingredientIdx / recipeWidth;

                    int gridX = startX + recipeX;
                    int gridY = startY + recipeY;
                    int slotIndex = gridY * gridWidth + gridX;

                    int actualSlot = resourcesTrees$getActualCraftingSlot(slotIndex, gridWidth, gridHeight, resultSlotIndex);

                    if (actualSlot != -1 && actualSlot < this.handler.slots.size()) {
                        Slot slot = this.handler.getSlot(actualSlot);

                        int placed = 0;
                        while (placed < amountPerSlot) {
                            int invSlot = resourcesTrees$findSlotMatchingIngredient(ingredient);

                            if (invSlot == -1) {
                                break;
                            }

                            ItemStack sourceStack = inventory.getStack(invSlot);
                            int available = sourceStack.getCount();
                            int toTake = Math.min(amountPerSlot - placed, available);

                            ItemStack taken = sourceStack.split(toTake);

                            if (slot.getStack().isEmpty()) {
                                slot.setStack(taken);
                            } else {
                                slot.getStack().increment(toTake);
                            }

                            placed += toTake;
                        }

                        this.handler.onContentChanged(slot.inventory);
                    }
                }
            }
        } else {
            int placedCount = 0;
            for (Ingredient ingredient : ingredients) {
                if (!ingredient.isEmpty()) {
                    int x = placedCount % recipeWidth;
                    int y = placedCount / recipeWidth;

                    int gridX = startX + x;
                    int gridY = startY + y;
                    int slotIndex = gridY * gridWidth + gridX;

                    int actualSlot = resourcesTrees$getActualCraftingSlot(slotIndex, gridWidth, gridHeight, resultSlotIndex);

                    if (actualSlot != -1 && actualSlot < this.handler.slots.size()) {
                        Slot slot = this.handler.getSlot(actualSlot);

                        int placed = 0;
                        while (placed < amountPerSlot) {
                            int invSlot = resourcesTrees$findSlotMatchingIngredient(ingredient);

                            if (invSlot == -1) {
                                break;
                            }

                            ItemStack sourceStack = inventory.getStack(invSlot);
                            int available = sourceStack.getCount();
                            int toTake = Math.min(amountPerSlot - placed, available);

                            ItemStack taken = sourceStack.split(toTake);

                            if (slot.getStack().isEmpty()) {
                                slot.setStack(taken);
                            } else {
                                slot.getStack().increment(toTake);
                            }

                            placed += toTake;
                        }

                        this.handler.onContentChanged(slot.inventory);
                    }
                    placedCount++;
                }
            }
        }
    }

    @Unique
    private int resourcesTrees$getActualCraftingSlot(int gridPosition, int gridWidth, int gridHeight, int resultSlot) {
        if (gridPosition < 0 || gridPosition >= gridWidth * gridHeight) {
            return -1;
        }

        if (resultSlot >= gridWidth * gridHeight) {
            return gridPosition;
        }

        if (gridPosition >= resultSlot) {
            return gridPosition + 1;
        }

        return gridPosition;
    }

    @Unique
    private int resourcesTrees$findSlotMatchingIngredient(Ingredient ingredient) {
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);

            if (!stack.isEmpty() && ingredient.test(stack)) {
                return i;
            }
        }

        return -1;
    }
}
package com.coolerpromc.resourcestrees.mixin;

import com.coolerpromc.resourcestrees.recipe.ingredient.ResourcesTypeIngredient;
import net.minecraft.core.NonNullList;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlaceRecipe.class)
public abstract class ServerPlaceRecipeMixin<I extends RecipeInput, R extends Recipe<I>> {

    @Shadow
    protected StackedContents stackedContents;

    @Shadow
    protected Inventory inventory;

    @Shadow
    protected RecipeBookMenu<I, R> menu;

    @Shadow
    protected abstract void clearGrid();

    @Inject(
            method = "recipeClicked",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/StackedContents;canCraft(Lnet/minecraft/world/item/crafting/Recipe;Lit/unimi/dsi/fastutil/ints/IntList;)Z"
            ),
            cancellable = true
    )
    private void onRecipeClicked(ServerPlayer player, @Nullable RecipeHolder<R> recipe, boolean placeAll, CallbackInfo ci) {
        if (recipe == null) {
            return;
        }

        Recipe<I> recipeValue = recipe.value();
        if (recipeValue instanceof CraftingRecipe craftingRecipe) {
            NonNullList<Ingredient> ingredients = craftingRecipe.getIngredients();

            boolean hasDataComponentIngredient = ingredients.stream()
                    .anyMatch(ing -> ing.isCustom() && ing.getCustomIngredient() instanceof ResourcesTypeIngredient);

            if (hasDataComponentIngredient) {
                if (resourcesTrees$canCraftWithDataComponents(ingredients)) {
                    this.clearGrid();

                    int amountPerSlot = 1;
                    if (placeAll) {
                        amountPerSlot = resourcesTrees$calculateMaxCrafts(ingredients);
                    }

                    resourcesTrees$placeRecipeManually(craftingRecipe, amountPerSlot);
                    player.getInventory().setChanged();
                } else {
                    this.clearGrid();
                    player.connection.send(new net.minecraft.network.protocol.game.ClientboundPlaceGhostRecipePacket(player.containerMenu.containerId, recipe));
                    player.getInventory().setChanged();
                }
                ci.cancel();
            }
        }
    }

    @Unique
    private boolean resourcesTrees$canCraftWithDataComponents(NonNullList<Ingredient> ingredients) {
        int[] allocatedCounts = new int[inventory.getContainerSize()];

        for (Ingredient ingredient : ingredients) {
            if (ingredient.isEmpty()) {
                continue;
            }

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

        return true;
    }

    @Unique
    private int resourcesTrees$calculateMaxCrafts(NonNullList<Ingredient> ingredients) {
        int totalSlotsNeeded = (int) ingredients.stream()
                .filter(ing -> !ing.isEmpty())
                .count();

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
    private void resourcesTrees$placeRecipeManually(CraftingRecipe craftingRecipe, int amountPerSlot) {
        int gridWidth = this.menu.getGridWidth();
        int gridHeight = this.menu.getGridHeight();

        NonNullList<Ingredient> ingredients = craftingRecipe.getIngredients();

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

        int resultSlotIndex = this.menu.getResultSlotIndex();

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

                    if (actualSlot != -1 && actualSlot < this.menu.slots.size()) {
                        Slot slot = this.menu.getSlot(actualSlot);

                        int placed = 0;
                        while (placed < amountPerSlot) {
                            int invSlot = resourcesTrees$findSlotMatchingIngredient(ingredient);

                            if (invSlot == -1) {
                                break;
                            }

                            ItemStack sourceStack = inventory.getItem(invSlot);
                            int available = sourceStack.getCount();
                            int toTake = Math.min(amountPerSlot - placed, available);

                            ItemStack taken = sourceStack.split(toTake);

                            if (slot.getItem().isEmpty()) {
                                slot.set(taken);
                            } else {
                                slot.getItem().grow(toTake);
                            }

                            placed += toTake;
                        }

                        this.menu.slotsChanged(slot.container);
                    }
                }
            }
        } else {
            // Shapeless recipe - place in order
            int placedCount = 0;
            for (Ingredient ingredient : ingredients) {
                if (!ingredient.isEmpty()) {
                    int x = placedCount % recipeWidth;
                    int y = placedCount / recipeWidth;

                    int gridX = startX + x;
                    int gridY = startY + y;
                    int slotIndex = gridY * gridWidth + gridX;

                    int actualSlot = resourcesTrees$getActualCraftingSlot(slotIndex, gridWidth, gridHeight, resultSlotIndex);

                    if (actualSlot != -1 && actualSlot < this.menu.slots.size()) {
                        Slot slot = this.menu.getSlot(actualSlot);

                        int placed = 0;
                        while (placed < amountPerSlot) {
                            int invSlot = resourcesTrees$findSlotMatchingIngredient(ingredient);

                            if (invSlot == -1) {
                                break;
                            }

                            ItemStack sourceStack = inventory.getItem(invSlot);
                            int available = sourceStack.getCount();
                            int toTake = Math.min(amountPerSlot - placed, available);

                            ItemStack taken = sourceStack.split(toTake);

                            if (slot.getItem().isEmpty()) {
                                slot.set(taken);
                            } else {
                                slot.getItem().grow(toTake);
                            }

                            placed += toTake;
                        }

                        this.menu.slotsChanged(slot.container);
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
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);

            if (!stack.isEmpty() && ingredient.test(stack)) {
                return i;
            }
        }

        return -1;
    }
}
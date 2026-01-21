package com.coolerpromc.resourcestrees.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.stats.RecipeBook;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.common.crafting.PartialNBTIngredient;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Set;

@Mixin(value = RecipeCollection.class)
public class RecipeCollectionMixin {

    @Shadow
    @Final
    private List<Recipe<?>> recipes;

    @Shadow
    @Final
    private Set<Recipe<?>> craftable;

    @Shadow
    @Final
    private Set<Recipe<?>> fitsDimensions;

    @Inject(method = "canCraft", at = @At("HEAD"), cancellable = true)
    private void onCanCraft(StackedContents handler, int width, int height, RecipeBook book, CallbackInfo ci) {
        boolean hasDataComponentRecipe = false;

        for (Recipe<?> recipeHolder : this.recipes) {
            if (recipeHolder instanceof CraftingRecipe craftingRecipe) {
                List<Ingredient> ingredients = craftingRecipe.getIngredients();

                boolean hasDataComponentIngredient = ingredients.stream().anyMatch(ing -> !ing.isSimple() && (ing instanceof StrictNBTIngredient || ing instanceof PartialNBTIngredient));

                if (hasDataComponentIngredient) {
                    hasDataComponentRecipe = true;
                    break;
                }
            }
        }

        if (!hasDataComponentRecipe) {
            return;
        }

        for (Recipe<?> recipeHolder : this.recipes) {
            boolean flag = recipeHolder.canCraftInDimensions(width, height) && book.contains(recipeHolder);

            if (flag) {
                this.fitsDimensions.add(recipeHolder);
            } else {
                this.fitsDimensions.remove(recipeHolder);
            }

            if (flag) {
                boolean canCraft = false;

                if (recipeHolder instanceof CraftingRecipe craftingRecipe) {
                    List<Ingredient> ingredients = craftingRecipe.getIngredients();

                    boolean hasDataComponentIngredient = ingredients.stream().anyMatch(ing -> !ing.isSimple() && (ing instanceof StrictNBTIngredient || ing instanceof PartialNBTIngredient));

                    if (hasDataComponentIngredient) {
                        canCraft = canCraftWithDataComponents(ingredients);
                    } else {
                        canCraft = handler.canCraft(recipeHolder, null);
                    }
                } else {
                    canCraft = handler.canCraft(recipeHolder, null);
                }

                if (canCraft) {
                    this.craftable.add(recipeHolder);
                } else {
                    this.craftable.remove(recipeHolder);
                }
            } else {
                this.craftable.remove(recipeHolder);
            }
        }

        ci.cancel();
    }

    private boolean canCraftWithDataComponents(List<Ingredient> ingredients) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return false;
        }

        int[] allocatedCounts = new int[player.getInventory().getContainerSize()];

        for (Ingredient ingredient : ingredients) {
            if (ingredient.isEmpty()) {
                continue;
            }

            boolean found = false;

            for (int invSlot = 0; invSlot < player.getInventory().getContainerSize(); invSlot++) {
                ItemStack stack = player.getInventory().getItem(invSlot);

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
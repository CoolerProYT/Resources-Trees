package com.coolerpromc.resourcestrees.mixin;

import com.coolerpromc.resourcestrees.recipe.ingredient.ResourcesTypeIngredient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeDisplayEntry;
import net.minecraft.recipe.RecipeFinder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(RecipeDisplayEntry.class)
public abstract class RecipeDisplayEntryMixin {
    @Inject(method = "isCraftable", at = @At("HEAD"), cancellable = true)
    private void onCanCraft(RecipeFinder stackedItemContents, CallbackInfoReturnable<Boolean> cir) {
        RecipeDisplayEntry entry = (RecipeDisplayEntry) (Object) this;
        Optional<List<Ingredient>> craftingReqs = entry.craftingRequirements();

        if (craftingReqs.isEmpty()) {
            return;
        }

        List<Ingredient> ingredients = craftingReqs.get();

        boolean hasDataComponentIngredient = ingredients.stream().anyMatch(ing -> ing.getCustomIngredient() != null && ing.getCustomIngredient() instanceof ResourcesTypeIngredient);

        if (hasDataComponentIngredient) {
            boolean craftable = canCraftWithDataComponents(ingredients);
            cir.setReturnValue(craftable);
        }
    }

    @Unique
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
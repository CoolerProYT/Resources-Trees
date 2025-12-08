package com.coolerpromc.resourcestrees.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(RecipeDisplayEntry.class)
public abstract class RecipeDisplayEntryMixin {
    @Inject(method = "canCraft", at = @At("HEAD"), cancellable = true)
    private void onCanCraft(StackedItemContents stackedItemContents, CallbackInfoReturnable<Boolean> cir) {
        RecipeDisplayEntry entry = (RecipeDisplayEntry) (Object) this;
        Optional<List<Ingredient>> craftingReqs = entry.craftingRequirements();

        if (craftingReqs.isEmpty()) {
            return;
        }

        List<Ingredient> ingredients = craftingReqs.get();

        boolean hasDataComponentIngredient = ingredients.stream().anyMatch(ing -> ing.isCustom() && ing.getCustomIngredient() instanceof DataComponentIngredient);

        if (hasDataComponentIngredient) {
            boolean craftable = canCraftWithDataComponents(ingredients);
            cir.setReturnValue(craftable);
        }
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
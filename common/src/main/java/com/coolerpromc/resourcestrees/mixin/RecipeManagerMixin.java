package com.coolerpromc.resourcestrees.mixin;

import com.coolerpromc.resourcestrees.recipe.RecipeMapItemLookupHolder;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Redirect(
            method = "<init>(Lnet/minecraft/core/HolderLookup$Provider;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/crafting/RecipeMap;create(Lnet/minecraft/core/HolderLookup;)Lnet/minecraft/world/item/crafting/RecipeMap;"
            )
    )
    private static RecipeMap resourcestrees$create(HolderLookup<Recipe<?>> recipes, @Local HolderLookup.Provider registries) {
        RecipeMapItemLookupHolder.CURRENT.set(registries.lookupOrThrow(Registries.ITEM));
        try {
            return RecipeMap.create(recipes);
        } finally {
            RecipeMapItemLookupHolder.CURRENT.remove();
        }
    }
}

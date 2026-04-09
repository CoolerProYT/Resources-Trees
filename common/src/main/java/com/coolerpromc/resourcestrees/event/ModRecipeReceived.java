package com.coolerpromc.resourcestrees.event;

import net.minecraft.world.item.crafting.RecipeMap;

/**
 * Holds the client-side RecipeMap. Set from platform-specific event handlers.
 */
public class ModRecipeReceived {
    public static RecipeMap recipeMap = RecipeMap.EMPTY;
}

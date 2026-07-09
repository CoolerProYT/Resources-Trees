package com.coolerpromc.resourcestrees.platform.services;

import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.platform.util.BlockRegistryHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;
import java.util.Set;

public interface ICompatHelper {
    void injectAgritechEvolvedRecipe(BlockRegistryHandler<ResourcesSaplingBlock> handler, List<RecipeHolder<?>> recipeHolders, Set<ResourceKey<Recipe<?>>> existingKeys, HolderLookup.RegistryLookup<Item> items);
    void injectAgritechRecipe(BlockRegistryHandler<ResourcesSaplingBlock> handler, List<RecipeHolder<?>> recipeHolders, Set<ResourceKey<Recipe<?>>> existingKeys, HolderLookup.RegistryLookup<Item> items);
}

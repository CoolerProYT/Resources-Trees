package com.coolerpromc.resourcestrees.platform;

import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.platform.services.ICompatHelper;
import com.coolerpromc.resourcestrees.platform.util.BlockRegistryHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;
import java.util.Set;

public class NeoForgeCompatHelper implements ICompatHelper {
    @Override
    public void injectAgritechEvolvedRecipe(BlockRegistryHandler<ResourcesSaplingBlock> handler, List<RecipeHolder<?>> recipeHolders, Set<ResourceKey<Recipe<?>>> existingKeys, HolderLookup.RegistryLookup<Item> items) {
        ResourcesSaplingBlock block = handler.get();
        ResourceKey<Recipe<?>> key = ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath("agritechevolved", "planter/tree/" + handler.id().getPath()));

        com.misterd.agritechevolved.recipe.TreeRecipe recipe = new com.misterd.agritechevolved.recipe.TreeRecipe(
            Ingredient.of(block),
            List.of(Ingredient.of(items.getOrThrow(com.misterd.agritechevolved.util.ATETags.Items.TREE_SOILS))),
            List.of(
                new com.misterd.agritechevolved.recipe.DropEntry(items.getOrThrow(ResourceKey.create(Registries.ITEM, Identifier.parse(block.getTreeType().log()))).value(), 2, 4, 1f),
                new com.misterd.agritechevolved.recipe.DropEntry(block.getResourcesType().leafFragmentItem().get(), 8, 20, block.getResourcesType().leafDropChance()),
                new com.misterd.agritechevolved.recipe.DropEntry(block.getResourcesType().leafFragmentItem().get(), 4, 10, block.getResourcesType().leafDropChance() * 0.5f),
                new com.misterd.agritechevolved.recipe.DropEntry(block.getResourcesType().saplingBlock(block.getTreeType().name()).get().asItem(), 1, 3, block.getResourcesType().saplingDropChance())
            )
        );
        recipeHolders.add(new RecipeHolder<>(key, recipe));
    }

    @Override
    public void injectAgritechRecipe(BlockRegistryHandler<ResourcesSaplingBlock> handler, List<RecipeHolder<?>> recipeHolders, Set<ResourceKey<Recipe<?>>> existingKeys, HolderLookup.RegistryLookup<Item> items) {
        ResourcesSaplingBlock block = handler.get();
        ResourceKey<Recipe<?>> key = ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath("agritechtwo", "planter/tree/" + handler.id().getPath()));

        com.misterd.agritechtwo.recipe.TreeRecipe recipe = new com.misterd.agritechtwo.recipe.TreeRecipe(
            Ingredient.of(block),
            List.of(Ingredient.of(items.getOrThrow(com.misterd.agritechtwo.util.ATTags.Items.TREE_SOILS))),
            List.of(
                new com.misterd.agritechtwo.recipe.DropEntry(items.getOrThrow(ResourceKey.create(Registries.ITEM, Identifier.parse(block.getTreeType().log()))).value(), 2, 4, 1f),
                new com.misterd.agritechtwo.recipe.DropEntry(block.getResourcesType().leafFragmentItem().get(), 8, 20, block.getResourcesType().leafDropChance()),
                new com.misterd.agritechtwo.recipe.DropEntry(block.getResourcesType().leafFragmentItem().get(), 4, 10, block.getResourcesType().leafDropChance() * 0.5f),
                new com.misterd.agritechtwo.recipe.DropEntry(block.getResourcesType().saplingBlock(block.getTreeType().name()).get().asItem(), 1, 3, block.getResourcesType().saplingDropChance())
            )
        );
        recipeHolders.add(new RecipeHolder<>(key, recipe));
    }
}

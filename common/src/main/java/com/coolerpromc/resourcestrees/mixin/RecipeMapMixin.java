package com.coolerpromc.resourcestrees.mixin;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.platform.Services;
import com.coolerpromc.resourcestrees.recipe.RecipeMapItemLookupHolder;
import com.coolerpromc.resourcestrees.recipe.custom.TreeSimulatorRecipe;
import com.coolerpromc.resourcestrees.recipe.output.TreeSimulatorOutput;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(RecipeMap.class)
public class RecipeMapMixin {
    @Inject(
            method = "create(Lnet/minecraft/core/HolderLookup;)Lnet/minecraft/world/item/crafting/RecipeMap;",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/stream/Stream;forEach(Ljava/util/function/Consumer;)V",
                    shift = At.Shift.AFTER
            )
    )
    private static void resourcestrees$injectRecipes(
            HolderLookup<Recipe<?>> recipes,
            CallbackInfoReturnable<RecipeMap> cir,
            @Local(name = "byType") ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> byType,
            @Local(name = "byKey") ImmutableMap.Builder<ResourceKey<Recipe<?>>, RecipeHolder<?>> byKey
    ) {
        HolderLookup.RegistryLookup<Item> items = RecipeMapItemLookupHolder.CURRENT.get();
        if (items == null) {
            items = BuiltInRegistries.ITEM;
        }
        List<RecipeHolder<?>> recipeHolders = new ArrayList<>();
        Set<ResourceKey<Recipe<?>>> existingKeys = recipes.listElements().map(Holder.Reference::key).collect(Collectors.toSet());
        HolderLookup.RegistryLookup<Item> finalItems = items;

        ModBlocks.SAPLINGS.forEach(handler -> {
            ResourcesSaplingBlock block = handler.get();
            ResourceKey<Recipe<?>> craftingKey = ResourceKey.create(Registries.RECIPE, Constants.id("saplings/" + handler.id().getPath()));
            ResourceKey<Recipe<?>> treeSimKey = ResourceKey.create(Registries.RECIPE, Constants.id("tree_simulator/" + handler.id().getPath()));

            if (!existingKeys.contains(craftingKey)){
                ShapedRecipePattern pattern = ShapedRecipePattern.of(java.util.Map.of(
                        'M', block.getResourcesType().ingredient(finalItems),
                        'S', Ingredient.of(finalItems.getOrThrow(ResourceKey.create(Registries.ITEM, Identifier.parse(block.getTreeType().originalSapling()))).value())
                ), " M ", "MSM", " M ");
                ShapedRecipe recipe = new ShapedRecipe(new Recipe.CommonInfo(false), new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.MISC, "resourcestrees:saplings"), pattern, new ItemStackTemplate(block.asItem()));
                recipeHolders.add(new RecipeHolder<>(craftingKey, recipe));
            }

            if (!existingKeys.contains(treeSimKey)){
                List<TreeSimulatorOutput> outputs = List.of(
                        new TreeSimulatorOutput(new ItemStackTemplate(BuiltInRegistries.ITEM.getValue(Identifier.parse(block.getTreeType().log()))), 0.3F, 1, 1),
                        new TreeSimulatorOutput(new ItemStackTemplate(block.getResourcesType().leafFragmentItem().get()), 1, 1, 1),
                        new TreeSimulatorOutput(new ItemStackTemplate(block.getResourcesType().leafFragmentItem().get()), block.getResourcesType().leafDropChance() * 0.5f, 1, 4),
                        new TreeSimulatorOutput(new ItemStackTemplate(block.getResourcesType().saplingBlock(block.getTreeType().name()).get().asItem()), block.getResourcesType().saplingDropChance(), 1, 1)
                );
                TreeSimulatorRecipe treeSimulatorRecipe = new TreeSimulatorRecipe(new ItemStackTemplate(block.asItem()), outputs, block.getResourcesType().treeSimulatorTicks());
                recipeHolders.add(new RecipeHolder<>(treeSimKey, treeSimulatorRecipe));
            }

            if (Services.PLATFORM.isModLoaded("agritechevolved")){
                Services.COMPAT.injectAgritechEvolvedRecipe(handler, recipeHolders, existingKeys, finalItems);
            }

            if (Services.PLATFORM.isModLoaded("agritechtwo") || Services.PLATFORM.isModLoaded("agritech")){
                Services.COMPAT.injectAgritechRecipe(handler, recipeHolders, existingKeys, finalItems);
            }
        });

        for (RecipeHolder<?> recipe : recipeHolders) {
            byType.put(recipe.value().getType(), recipe);
            byKey.put(recipe.id(), recipe);
        }
    }
}

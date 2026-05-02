package com.coolerpromc.resourcestrees.compat.jei.recipe;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.recipe.custom.ResourcesSaplingRecipe;
import com.coolerpromc.resourcestrees.registry.ModRegistries;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.ArrayList;
import java.util.List;

public record ResourcesSaplingCrafting(List<RecipeHolder<ResourcesSaplingRecipe>> recipeHolders) {
    public List<RecipeHolder<CraftingRecipe>> recipes(IVanillaRecipeFactory factory){
        List<RecipeHolder<CraftingRecipe>> recipes = new ArrayList<>();

        recipeHolders().stream().map(RecipeHolder::value).forEach(holder -> {
            Minecraft.getInstance().level.registryAccess().lookupOrThrow(ModRegistries.RESOURCES_TYPES_KEY).listElements().forEach(reference -> {
                Ingredient ingredient = reference.value().ingredient();
                Ingredient baseSapling = Ingredient.of(holder.getBaseSapling().item().value());
                ItemStackTemplate output = new ItemStackTemplate(holder.getResourcesSapling().item(), 1, DataComponentPatch.builder().set(ModDataComponents.TYPE.get(), reference).build());
                ResourceKey<Recipe<?>> key = key(reference.key().identifier().getPath() + "_" + holder.getBaseSapling().typeHolder().unwrapKey().get().identifier().getPath());
                SlotDisplay slotDisplay = new SlotDisplay.ItemStackSlotDisplay(output);
                CraftingRecipe recipe = factory.createShapedRecipeBuilder(CraftingBookCategory.MISC, slotDisplay)
                        .define('I', ingredient)
                        .define('B', baseSapling)
                        .pattern(" I ")
                        .pattern("IBI")
                        .pattern(" I ")
                        .build();
                recipes.add(new RecipeHolder<>(key, recipe));
            });
        });

        return recipes;
    }

    private ResourceKey<Recipe<?>> key(String name){
        return ResourceKey.create(Registries.RECIPE, Constants.id("jei/saplings/" + name));
    }
}

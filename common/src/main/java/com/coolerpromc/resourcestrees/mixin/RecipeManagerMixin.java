package com.coolerpromc.resourcestrees.mixin;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.platform.Services;
import com.coolerpromc.resourcestrees.recipe.custom.TreeSimulatorRecipe;
import com.coolerpromc.resourcestrees.recipe.output.TreeSimulatorOutput;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Shadow
    @Final
    private HolderLookup.Provider registries;

    @Inject(
            at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Ljava/util/SortedMap;forEach(Ljava/util/function/BiConsumer;)V"),
            method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Lnet/minecraft/world/item/crafting/RecipeMap;"
    )
    public void resourcestrees$prepare(ResourceManager manager, ProfilerFiller profiler, CallbackInfoReturnable<RecipeMap> cir, @Local(name = "recipeHolders") List<RecipeHolder<?>> recipeHolders) {
        HolderLookup.RegistryLookup<Item> items = registries.lookupOrThrow(Registries.ITEM);
        Set<ResourceKey<Recipe<?>>> existingKeys = recipeHolders.stream().map(RecipeHolder::id).collect(Collectors.toSet());

        ModBlocks.SAPLINGS.forEach(handler -> {
            ResourcesSaplingBlock block = handler.get();
            ResourceKey<Recipe<?>> craftingKey = ResourceKey.create(Registries.RECIPE, Constants.id("saplings/" + handler.id().getPath()));
            ResourceKey<Recipe<?>> treeSimKey = ResourceKey.create(Registries.RECIPE, Constants.id("tree_simulator/" + handler.id().getPath()));

            if (!existingKeys.contains(craftingKey)){
                ShapedRecipePattern pattern = ShapedRecipePattern.of(Map.of(
                        'M', block.getResourcesType().ingredient(items),
                        'S', Ingredient.of(items.getOrThrow(ResourceKey.create(Registries.ITEM, Identifier.parse(block.getTreeType().originalSapling()))).value())
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
                Services.COMPAT.injectAgritechEvolvedRecipe(handler, recipeHolders, existingKeys, items);
            }

            if (Services.PLATFORM.isModLoaded("agritechtwo") || Services.PLATFORM.isModLoaded("agritech")){
                Services.COMPAT.injectAgritechRecipe(handler, recipeHolders, existingKeys, items);
            }
        });
    }
}

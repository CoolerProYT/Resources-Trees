package com.coolerpromc.resourcestrees.util;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.block.entity.custom.TreeSimulatorBlockEntity;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.event.ModRecipeReceived;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.recipe.custom.TreeSimulatorRecipe;
import com.coolerpromc.resourcestrees.recipe.input.TreeSimulatorRecipeInput;
import com.coolerpromc.resourcestrees.recipe.output.TreeSimulatorOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class RecipeViewerFiller {
    public static List<RecipeHolder<TreeSimulatorRecipe>> addUndefinedRecipes(HolderLookup.Provider registryAccess, List<ResourceKey<Recipe<?>>> keys){
        List<RecipeHolder<TreeSimulatorRecipe>> treeSimulatorRecipe = new ArrayList<>();

        ResourcesTypes.getAllResourcesTypes(registryAccess).forEach((type, value) -> {
            Field[] fields = ModBlocks.class.getDeclaredFields();

            for (Field field : fields){
                try {
                    Object obj = field.get(null);
                    if (obj instanceof Supplier<?> supplier){
                        if (supplier.get() instanceof ResourcesSaplingBlock block){
                            ItemStackTemplate leaf = new ItemStackTemplate(ModItems.LEAF_FRAGMENT.get().builtInRegistryHolder(), 1, DataComponentPatch.builder().set(ModDataComponents.TYPE.get(), value).build());
                            ItemStackTemplate sapling = new ItemStackTemplate(block.asItem().builtInRegistryHolder(), 1, DataComponentPatch.builder().set(ModDataComponents.TYPE.get(), value).build());
                            Optional<RecipeHolder<TreeSimulatorRecipe>> exisingRecipe = ModRecipeReceived.recipeMap.getRecipesFor(ModRecipes.TREE_SIMULATOR_TYPE.get(), new TreeSimulatorRecipeInput(sapling.create()), null).findFirst();
                            if (exisingRecipe.isEmpty()){
                                List<TreeSimulatorOutput> drops = new ArrayList<>();
                                drops.add(TreeSimulatorOutput.of(TreeSimulatorBlockEntity.LOG_BY_SAPLINGS.get(block), 1, 2, 4));
                                drops.add(TreeSimulatorOutput.of(leaf, 1, 1, 1));
                                drops.add(TreeSimulatorOutput.of(leaf, value.value().leafDropChance(), 1, 1));
                                drops.add(TreeSimulatorOutput.of(sapling, value.value().saplingDropChance(), 1, 1));
                                drops.add(TreeSimulatorOutput.of(Items.STICK, 0.1f, 1, 2));
                                drops.add(TreeSimulatorOutput.of(Items.APPLE, 0.05f, 1, 1));
                                TreeSimulatorRecipe newRecipe = new TreeSimulatorRecipe(sapling, drops, value.value().treeSimulatorTicks());
                                ResourceKey<Recipe<?>> key = ResourceKey.create(Registries.RECIPE, type.withSuffix(BuiltInRegistries.BLOCK.getKey(block).getPath().substring(9)).withPrefix("tree_simulator/"));
                                if (!keys.contains(key)){
                                    treeSimulatorRecipe.add(new RecipeHolder<>(key, newRecipe));
                                }
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return treeSimulatorRecipe;
    }
}

package com.coolerpromc.resourcestrees.util;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.block.entity.custom.TreeSimulatorBlockEntity;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.datagen.ModRecipeProvider;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.recipe.custom.TreeSimulatorRecipe;
import com.coolerpromc.resourcestrees.recipe.input.TreeSimulatorRecipeInput;
import com.coolerpromc.resourcestrees.recipe.output.TreeSimulatorOutput;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class RecipeViewerFiller {
    public static List<RecipeEntry<TreeSimulatorRecipe>> addUndefinedRecipes(RegistryWrapper.WrapperLookup registryAccess, List<Identifier> keys){
        List<RecipeEntry<TreeSimulatorRecipe>> treeSimulatorRecipe = new ArrayList<>();

        ResourcesTypes.getAllResourcesTypes(registryAccess).forEach((type, value) -> {
            Field[] fields = ModBlocks.class.getDeclaredFields();

            for (Field field : fields){
                try {
                    Object obj = field.get(null);
                    if (obj instanceof Supplier<?> supplier){
                        if (supplier.get() instanceof ResourcesSaplingBlock block){
                            ItemStack leaf = ModItems.LEAF_FRAGMENT.getDefaultStack();
                            leaf.set(ModDataComponents.TYPE, value);
                            ItemStack sapling = block.asItem().getDefaultStack();
                            sapling.set(ModDataComponents.TYPE, value);
                            Optional<RecipeEntry<TreeSimulatorRecipe>> exisingRecipe = MinecraftClient.getInstance().world.getRecipeManager().getFirstMatch(ModRecipes.TREE_SIMULATOR_TYPE, new TreeSimulatorRecipeInput(sapling), null);
                            if (value != null && type != null && exisingRecipe.isEmpty()){
                                List<TreeSimulatorOutput> drops = new ArrayList<>();
                                drops.add(TreeSimulatorOutput.of(TreeSimulatorBlockEntity.LOG_BY_SAPLINGS.get(block).getDefaultStack(), 1, 2, 4));
                                drops.add(TreeSimulatorOutput.of(leaf, 1, 1, 1));
                                drops.add(TreeSimulatorOutput.of(leaf, value.value().leafDropChance(), 1, 1));
                                drops.add(TreeSimulatorOutput.of(sapling, value.value().saplingDropChance(), 1, 1));
                                drops.add(TreeSimulatorOutput.of(Items.STICK.getDefaultStack(), 0.1f, 1, 2));
                                drops.add(TreeSimulatorOutput.of(Items.APPLE.getDefaultStack(), 0.05f, 1, 1));
                                drops.add(TreeSimulatorOutput.of(ModRecipeProvider.SAPLINGS_BY_SAPLINGS.get(block).getDefaultStack(), 0.1f, 1, 1));
                                TreeSimulatorRecipe newRecipe = new TreeSimulatorRecipe(sapling, drops, 1200);
                                RegistryKey<Recipe<?>> key = RegistryKey.of(RegistryKeys.RECIPE, type.withSuffixedPath(Registries.BLOCK.getId(block).getPath().substring(9)).withPrefixedPath("tree_simulator/"));
                                if (!keys.contains(key)){
                                    treeSimulatorRecipe.add(new RecipeEntry<>(key.getValue(), newRecipe));
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

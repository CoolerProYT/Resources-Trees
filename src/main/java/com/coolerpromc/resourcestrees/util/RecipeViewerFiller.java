package com.coolerpromc.resourcestrees.util;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.block.entity.custom.TreeSimulatorBlockEntity;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datagen.ModRecipeProvider;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.recipe.custom.TreeSimulatorRecipe;
import com.coolerpromc.resourcestrees.recipe.input.TreeSimulatorRecipeInput;
import com.coolerpromc.resourcestrees.recipe.output.TreeSimulatorOutput;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class RecipeViewerFiller {
    public static List<TreeSimulatorRecipe> addUndefinedRecipes(HolderLookup.Provider registryAccess, List<ResourceLocation> keys){
        List<TreeSimulatorRecipe> treeSimulatorRecipe = new ArrayList<>();

        ResourcesTypes.getAllResourcesTypes(registryAccess).forEach((type, value) -> {
            Field[] fields = ModBlocks.class.getDeclaredFields();

            for (Field field : fields){
                try {
                    Object obj = field.get(null);
                    if (obj instanceof Supplier<?> supplier){
                        if (supplier.get() instanceof ResourcesSaplingBlock block){
                            ItemStack leaf = ModItems.LEAF_FRAGMENT.get().getDefaultInstance();
                            leaf.getOrCreateTag().putString("type", type.toString());
                            ItemStack sapling = block.asItem().getDefaultInstance();
                            sapling.getOrCreateTag().putString("type", type.toString());
                            Optional<TreeSimulatorRecipe> exisingRecipe = Minecraft.getInstance().level.getRecipeManager().getRecipesFor(ModRecipes.TREE_SIMULATOR_TYPE.get(), new TreeSimulatorRecipeInput(sapling), null).stream().findFirst();
                            if (value.value() != ResourcesTypes.EMPTY && exisingRecipe.isEmpty()){
                                List<TreeSimulatorOutput> drops = new ArrayList<>();
                                drops.add(TreeSimulatorOutput.of(TreeSimulatorBlockEntity.LOG_BY_SAPLINGS.get(block).getDefaultInstance(), 1, 2, 4));
                                drops.add(TreeSimulatorOutput.of(leaf, 1, 1, 1));
                                drops.add(TreeSimulatorOutput.of(leaf, value.value().secondaryDropChance(), 1, 1));
                                drops.add(TreeSimulatorOutput.of(sapling, value.value().saplingChance(), 1, 1));
                                drops.add(TreeSimulatorOutput.of(Items.STICK.getDefaultInstance(), 0.1f, 1, 2));
                                drops.add(TreeSimulatorOutput.of(Items.APPLE.getDefaultInstance(), 0.05f, 1, 1));
                                drops.add(TreeSimulatorOutput.of(ModRecipeProvider.SAPLINGS_BY_SAPLINGS.get(block).getDefaultInstance(), 0.1f, 1, 1));
                                TreeSimulatorRecipe newRecipe = new TreeSimulatorRecipe(sapling, drops, 1200, type.withSuffix(BuiltInRegistries.BLOCK.getKey(block).getPath().substring(9)).withPrefix("tree_simulator/"));
                                if (!keys.contains(newRecipe.id())){
                                    treeSimulatorRecipe.add(newRecipe);
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

package com.coolerpromc.resourcestrees.recipe;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.recipe.custom.StrictShapedRecipe;
import com.coolerpromc.resourcestrees.recipe.custom.TreeSimulatorRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModRecipes {

    public static final RecipeType<TreeSimulatorRecipe> TREE_SIMULATOR_TYPE = registerType("tree_simulator");
    public static final RecipeSerializer<TreeSimulatorRecipe> TREE_SIMULATOR_SERIALIZER = registerSerializer("tree_simulator", TreeSimulatorRecipe.Serializer.INSTANCE);

    public static final RecipeSerializer<StrictShapedRecipe> STRICT_SHAPED = registerSerializer("strict_shaped", StrictShapedRecipe.Serializer.INSTANCE);

    public static <T extends Recipe<?>> RecipeType<T> registerType(final String id) {
        return Registry.register(Registries.RECIPE_TYPE, ResourcesTrees.id(id), new RecipeType<T>() {
            public String toString() {
                return id;
            }
        });
    }

    public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S registerSerializer(String id, S serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, ResourcesTrees.id(id), serializer);
    }

    public static void register(){
        ResourcesTrees.LOGGER.info("Registering recipes.");
    }
}

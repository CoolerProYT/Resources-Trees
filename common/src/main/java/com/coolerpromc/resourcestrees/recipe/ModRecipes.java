package com.coolerpromc.resourcestrees.recipe;

import com.coolerpromc.resourcestrees.platform.Services;
import com.coolerpromc.resourcestrees.platform.util.RegistryHandler;
import com.coolerpromc.resourcestrees.recipe.custom.ResourcesSaplingRecipe;
import com.coolerpromc.resourcestrees.recipe.custom.StrictShapedRecipe;
import com.coolerpromc.resourcestrees.recipe.custom.TreeSimulatorRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRecipes {
    public static final RegistryHandler<RecipeType<TreeSimulatorRecipe>> TREE_SIMULATOR_TYPE = Services.REGISTRY.registerRecipeType("tree_simulator");
    public static final RegistryHandler<RecipeSerializer<TreeSimulatorRecipe>> TREE_SIMULATOR_SERIALIZER = Services.REGISTRY.registerRecipeSerializer("tree_simulator", TreeSimulatorRecipe.SERIALIZER);
    public static final RegistryHandler<RecipeSerializer<StrictShapedRecipe>> STRICT_SHAPED = Services.REGISTRY.registerRecipeSerializer("strict_shaped", StrictShapedRecipe.SERIALIZER);
    public static final RegistryHandler<RecipeSerializer<ResourcesSaplingRecipe>> RESOURCES_SAPLING = Services.REGISTRY.registerRecipeSerializer("resources_sapling", ResourcesSaplingRecipe.SERIALIZER);

    public static void init() {
        // Force class loading to trigger static initializers
    }
}

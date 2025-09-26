package com.coolerpromc.resourcestrees.recipe;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.recipe.custom.TreeSimulatorRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, ResourcesTrees.MODID);
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, ResourcesTrees.MODID);

    public static final RegistryObject<RecipeType<TreeSimulatorRecipe>> TREE_SIMULATOR_TYPE = registerType("tree_simulator");
    public static final RegistryObject<RecipeSerializer<TreeSimulatorRecipe>> TREE_SIMULATOR_SERIALIZER = registerSerializer("tree_simulator", TreeSimulatorRecipe.Serializer.INSTANCE);

    public static <T extends Recipe<?>> RegistryObject<RecipeType<T>> registerType(String name){
        return TYPES.register(name, () -> RecipeType.simple(ResourcesTrees.id(name)));
    }

    public static <T extends Recipe<?>, S extends RecipeSerializer<T>> RegistryObject<RecipeSerializer<T>> registerSerializer(String name, S serializer){
        return SERIALIZERS.register(name, () -> serializer);
    }

    public static void register(IEventBus eventBus){
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}

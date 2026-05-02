package com.coolerpromc.resourcestrees.recipe.custom;

import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.recipe.ingredient.ResourcesTypeIngredient;
import com.coolerpromc.resourcestrees.recipe.input.TreeSimulatorRecipeInput;
import com.coolerpromc.resourcestrees.recipe.output.TreeSimulatorOutput;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;

public record TreeSimulatorRecipe(ItemStackTemplate tree, List<TreeSimulatorOutput> drops, int ticksToGrow) implements Recipe<TreeSimulatorRecipeInput> {
    public static final MapCodec<TreeSimulatorRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStackTemplate.CODEC.fieldOf("tree").forGetter(TreeSimulatorRecipe::tree),
            Codec.list(TreeSimulatorOutput.CODEC).fieldOf("drops").forGetter(TreeSimulatorRecipe::drops),
            Codec.INT.fieldOf("ticksToGrow").forGetter(TreeSimulatorRecipe::ticksToGrow)
    ).apply(instance, TreeSimulatorRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, TreeSimulatorRecipe> STREAM_CODEC = StreamCodec.composite(
            ItemStackTemplate.STREAM_CODEC,
            TreeSimulatorRecipe::tree,
            TreeSimulatorOutput.STREAM_CODEC.apply(ByteBufCodecs.list()),
            TreeSimulatorRecipe::drops,
            ByteBufCodecs.INT,
            TreeSimulatorRecipe::ticksToGrow,
            TreeSimulatorRecipe::new
    );

    public static final RecipeSerializer<TreeSimulatorRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    @Override
    public boolean matches(TreeSimulatorRecipeInput input, Level world) {
        return ResourcesTypes.isSameItemSameType(tree.create(), input.tree());
    }

    @Override
    public ItemStack assemble(TreeSimulatorRecipeInput input) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public RecipeSerializer<? extends Recipe<TreeSimulatorRecipeInput>> getSerializer() {
        return ModRecipes.TREE_SIMULATOR_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<TreeSimulatorRecipeInput>> getType() {
        return ModRecipes.TREE_SIMULATOR_TYPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(ResourcesTypeIngredient.of(true, tree.create()));
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }
}
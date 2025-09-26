package com.coolerpromc.resourcestrees.recipe.custom;

import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.recipe.input.TreeSimulatorRecipeInput;
import com.coolerpromc.resourcestrees.recipe.output.TreeSimulatorOutput;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

import java.util.List;

public record TreeSimulatorRecipe(ItemStack tree, List<TreeSimulatorOutput> drops, int ticksToGrow) implements Recipe<TreeSimulatorRecipeInput> {
    @Override
    public boolean matches(TreeSimulatorRecipeInput input, World world) {
        return ResourcesTypes.isSameItemSameType(tree, input.tree());
    }

    @Override
    public ItemStack craft(TreeSimulatorRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<? extends Recipe<TreeSimulatorRecipeInput>> getSerializer() {
        return ModRecipes.TREE_SIMULATOR_SERIALIZER;
    }

    @Override
    public RecipeType<? extends Recipe<TreeSimulatorRecipeInput>> getType() {
        return ModRecipes.TREE_SIMULATOR_TYPE;
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        return IngredientPlacement.forSingleSlot(Ingredient.ofItems(tree.getItem()));
    }

    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return null;
    }

    public static class Serializer implements RecipeSerializer<TreeSimulatorRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public MapCodec<TreeSimulatorRecipe> codec() {
            return RecordCodecBuilder.mapCodec(instance -> instance.group(
                    ItemStack.CODEC.fieldOf("tree").forGetter(TreeSimulatorRecipe::tree),
                    Codec.list(TreeSimulatorOutput.CODEC).fieldOf("drops").forGetter(TreeSimulatorRecipe::drops),
                    Codec.INT.fieldOf("ticksToGrow").forGetter(TreeSimulatorRecipe::ticksToGrow)
            ).apply(instance, TreeSimulatorRecipe::new));
        }

        @Override
        public PacketCodec<RegistryByteBuf, TreeSimulatorRecipe> packetCodec() {
            return PacketCodec.tuple(
                    ItemStack.PACKET_CODEC,
                    TreeSimulatorRecipe::tree,
                    TreeSimulatorOutput.STREAM_CODEC.collect(PacketCodecs.toList()),
                    TreeSimulatorRecipe::drops,
                    PacketCodecs.INTEGER,
                    TreeSimulatorRecipe::ticksToGrow,
                    TreeSimulatorRecipe::new
            );
        }
    }
}

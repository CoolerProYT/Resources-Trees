package com.coolerpromc.resourcestrees.recipe.custom;

import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.recipe.input.TreeSimulatorRecipeInput;
import com.coolerpromc.resourcestrees.recipe.output.TreeSimulatorOutput;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;

public record TreeSimulatorRecipe(ItemStack tree, List<TreeSimulatorOutput> drops, int ticksToGrow) implements Recipe<TreeSimulatorRecipeInput> {
    @Override
    public boolean matches(TreeSimulatorRecipeInput treeSimulatorRecipeInput, Level level) {
        return ResourcesTypes.isSameItemSameType(tree, treeSimulatorRecipeInput.tree());
    }

    @Override
    public ItemStack assemble(TreeSimulatorRecipeInput treeSimulatorRecipeInput, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<? extends Recipe<TreeSimulatorRecipeInput>> getSerializer() {
        return ModRecipes.TREE_SIMULATOR_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<TreeSimulatorRecipeInput>> getType() {
        return ModRecipes.TREE_SIMULATOR_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<TreeSimulatorRecipe>{
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
        public StreamCodec<RegistryFriendlyByteBuf, TreeSimulatorRecipe> streamCodec() {
            return StreamCodec.composite(
                    ItemStack.STREAM_CODEC,
                    TreeSimulatorRecipe::tree,
                    TreeSimulatorOutput.STREAM_CODEC.apply(ByteBufCodecs.list()),
                    TreeSimulatorRecipe::drops,
                    ByteBufCodecs.INT,
                    TreeSimulatorRecipe::ticksToGrow,
                    TreeSimulatorRecipe::new
            );
        }
    }
}

package com.coolerpromc.resourcestrees.recipe.custom;

import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.recipe.input.TreeSimulatorRecipeInput;
import com.coolerpromc.resourcestrees.recipe.output.TreeSimulatorOutput;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record TreeSimulatorRecipe(ItemStack tree, List<TreeSimulatorOutput> drops, int ticksToGrow, ResourceLocation id) implements Recipe<TreeSimulatorRecipeInput> {
    @Override
    public boolean matches(TreeSimulatorRecipeInput treeSimulatorRecipeInput, Level level) {
        return ResourcesTypes.isSameItemSameType(tree, treeSimulatorRecipeInput.tree());
    }

    @Override
    public ItemStack assemble(TreeSimulatorRecipeInput treeSimulatorRecipeInput, RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
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
        public TreeSimulatorRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            ItemStack tree = ShapedRecipe.itemStackFromJson(jsonObject.getAsJsonObject("tree"));
            JsonArray dropsJson = jsonObject.getAsJsonArray("drops");

            List<TreeSimulatorOutput> drops = new ArrayList<>();
            for (JsonElement dropJson : dropsJson){
                drops.add(TreeSimulatorOutput.fromJson(dropJson.getAsJsonObject()));
            }

            int ticksToGrow = GsonHelper.getAsInt(jsonObject, "ticksToGrow");

            return new TreeSimulatorRecipe(tree, drops, ticksToGrow, resourceLocation);
        }

        @Override
        public @Nullable TreeSimulatorRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buf) {
            ItemStack tree = buf.readItem();
            List<TreeSimulatorOutput> drops = buf.readList(TreeSimulatorOutput::readOutput);
            int ticksToGrow = buf.readInt();
            ResourceLocation id = buf.readResourceLocation();
            return new TreeSimulatorRecipe(tree, drops, ticksToGrow, id);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, TreeSimulatorRecipe recipe) {
            buf.writeItem(recipe.tree);
            buf.writeCollection(recipe.drops, TreeSimulatorOutput::writeOutput);
            buf.writeInt(recipe.ticksToGrow);
            buf.writeResourceLocation(recipe.id);
        }
    }
}

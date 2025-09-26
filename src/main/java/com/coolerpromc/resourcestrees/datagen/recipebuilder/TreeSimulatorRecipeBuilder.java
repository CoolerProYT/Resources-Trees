package com.coolerpromc.resourcestrees.datagen.recipebuilder;

import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.recipe.output.TreeSimulatorOutput;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class TreeSimulatorRecipeBuilder implements RecipeBuilder {
    private ItemStack tree;
    private final List<TreeSimulatorOutput> drops = new ArrayList<>();
    private int ticksToGrow;
    private final Map<String, CriterionTriggerInstance> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;

    public static TreeSimulatorRecipeBuilder builder(){
        return new TreeSimulatorRecipeBuilder();
    }

    private TreeSimulatorRecipeBuilder(){}

    public TreeSimulatorRecipeBuilder setTree(ItemStack itemStack){
        this.tree = itemStack;
        return this;
    }

    public TreeSimulatorRecipeBuilder addDrops(TreeSimulatorOutput drop){
        this.drops.add(drop);
        return this;
    }

    public TreeSimulatorRecipeBuilder setTicksToGrow(int ticksToGrow){
        this.ticksToGrow = ticksToGrow;
        return this;
    }

    @Override
    public RecipeBuilder unlockedBy(String name, CriterionTriggerInstance criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public TreeSimulatorRecipeBuilder group(@Nullable String s) {
        this.group = s;
        return this;
    }
    @Override
    public Item getResult() {
        return Items.AIR;
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation resourceLocation) {
        consumer.accept(new Result(tree, drops, ticksToGrow, resourceLocation));
    }

    public static JsonElement itemToJson(ItemStack stack){
        JsonObject json = new JsonObject();
        json.addProperty("item", ForgeRegistries.ITEMS.getKey(stack.getItem()).toString());
        json.addProperty("count", stack.getCount());
        if (stack.hasTag()) {
            json.addProperty("nbt", stack.getTag().toString());
        }

        return json;
    }

    public record Result(ItemStack tree, List<TreeSimulatorOutput> drops, int ticksToGrow, ResourceLocation id) implements FinishedRecipe{
        @Override
        public void serializeRecipeData(JsonObject jsonObject) {
            jsonObject.addProperty("type", "resourcestrees:tree_simulator");

            jsonObject.add("tree", itemToJson(tree));

            JsonArray drops = new JsonArray();
            for (TreeSimulatorOutput output : this.drops){
                drops.add(output.toJson());
            }
            jsonObject.add("drops", drops);

            jsonObject.addProperty("ticksToGrow", ticksToGrow);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ModRecipes.TREE_SIMULATOR_SERIALIZER.get();
        }

        @Override
        public @Nullable JsonObject serializeAdvancement() {
            return null;
        }

        @Override
        public @Nullable ResourceLocation getAdvancementId() {
            return null;
        }
    }
}

package com.coolerpromc.resourcestrees.recipe.custom;

import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;

import java.util.List;

public class StrictShapedRecipe extends NormalCraftingRecipe {
    private static final MapCodec<StrictShapedRecipe> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    CommonInfo.MAP_CODEC.forGetter((o) -> o.commonInfo),
                    CraftingBookInfo.MAP_CODEC.forGetter((o) -> o.bookInfo),
                    ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.pattern),
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
            ).apply(instance, StrictShapedRecipe::new)
    );

    private static final StreamCodec<RegistryFriendlyByteBuf, StrictShapedRecipe> STREAM_CODEC = StreamCodec.composite(
            CommonInfo.STREAM_CODEC,
            r -> r.commonInfo,
            CraftingBookInfo.STREAM_CODEC,
            r -> r.bookInfo,
            ShapedRecipePattern.STREAM_CODEC,
            recipe -> recipe.pattern,
            ItemStackTemplate.STREAM_CODEC,
            recipe -> recipe.result,
            StrictShapedRecipe::new
    );

    public static final RecipeSerializer<StrictShapedRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    public final ShapedRecipePattern pattern;
    public final ItemStackTemplate result;

    public StrictShapedRecipe(final CommonInfo commonInfo, final CraftingBookInfo bookInfo, final ShapedRecipePattern pattern, final ItemStackTemplate result) {
        super(commonInfo, bookInfo);
        this.pattern = pattern;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        int recipeWidth = this.getWidth();
        int recipeHeight = this.getHeight();

        // Try all possible positions in the crafting grid
        for (int startX = 0; startX <= input.width() - recipeWidth; startX++) {
            for (int startY = 0; startY <= input.height() - recipeHeight; startY++) {
                if (matchesAtPosition(input, startX, startY)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        return this.result.create();
    }

    private boolean matchesAtPosition(CraftingInput input, int startX, int startY) {
        PlacementInfo placementInfo = this.placementInfo();
        List<Ingredient> ingredients = placementInfo.ingredients();
        IntList slotsToIngredientIndex = placementInfo.slotsToIngredientIndex();

        int recipeWidth = this.getWidth();
        int recipeHeight = this.getHeight();

        for (int inputY = 0; inputY < input.height(); inputY++) {
            for (int inputX = 0; inputX < input.width(); inputX++) {
                int recipeX = inputX - startX;
                int recipeY = inputY - startY;

                ItemStack inputStack = input.getItem(inputX, inputY);

                if (recipeX >= 0 && recipeX < recipeWidth && recipeY >= 0 && recipeY < recipeHeight) {
                    int recipeSlot = recipeY * recipeWidth + recipeX;

                    if (recipeSlot < slotsToIngredientIndex.size()) {
                        int ingredientIndex = slotsToIngredientIndex.getInt(recipeSlot);

                        if (ingredientIndex >= 0 && ingredientIndex < ingredients.size()) {
                            Ingredient ingredient = ingredients.get(ingredientIndex);

                            if (!ingredient.test(inputStack)) {
                                return false;
                            }
                        } else {
                            if (!inputStack.isEmpty()) {
                                return false;
                            }
                        }
                    }
                } else {
                    if (!inputStack.isEmpty()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public int getWidth() {
        return this.pattern.width();
    }

    public int getHeight() {
        return this.pattern.height();
    }

    public List<RecipeDisplay> display() {
        return List.of(new ShapedCraftingRecipeDisplay(this.pattern.width(), this.pattern.height(), this.pattern.ingredients().stream().map((e) -> (SlotDisplay)e.map(Ingredient::display).orElse(SlotDisplay.Empty.INSTANCE)).toList(), new SlotDisplay.ItemStackSlotDisplay(this.result), new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
    }

    @Override
    public RecipeSerializer<StrictShapedRecipe> getSerializer() {
        return ModRecipes.STRICT_SHAPED.get();
    }

    @Override
    protected PlacementInfo createPlacementInfo() {
        return PlacementInfo.createFromOptionals(this.pattern.ingredients());
    }
}
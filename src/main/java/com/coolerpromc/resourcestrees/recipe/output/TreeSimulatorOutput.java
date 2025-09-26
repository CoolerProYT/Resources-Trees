package com.coolerpromc.resourcestrees.recipe.output;

import com.coolerpromc.resourcestrees.datagen.recipebuilder.TreeSimulatorRecipeBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;

public record TreeSimulatorOutput(ItemStack output, float chance, int minRolls, int maxRolls) {
    public static TreeSimulatorOutput of(ItemStack output, float chance, int minRolls, int maxRolls){
        return new TreeSimulatorOutput(output, chance, minRolls, maxRolls);
    }

    public JsonElement toJson(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("output", TreeSimulatorRecipeBuilder.itemToJson(output));
        jsonObject.addProperty("chance", chance);
        jsonObject.addProperty("minRolls", minRolls);
        jsonObject.addProperty("maxRolls", maxRolls);

        return jsonObject;
    }

    public static TreeSimulatorOutput fromJson(JsonObject jsonObject){
        return new TreeSimulatorOutput(
                CraftingHelper.getItemStack(jsonObject.getAsJsonObject("output"), true),
                GsonHelper.getAsFloat(jsonObject, "chance"),
                GsonHelper.getAsInt(jsonObject, "minRolls"),
                GsonHelper.getAsInt(jsonObject, "maxRolls")
        );
    }

    public int getRolls(RandomSource random){
        if (minRolls > maxRolls) {
            throw new IllegalArgumentException("minRolls cannot be greater than maxRolls");
        }
        if (minRolls == maxRolls) {
            return minRolls;
        }
        return random.nextInt(maxRolls - minRolls + 1) + minRolls;
    }

    public static void writeOutput(FriendlyByteBuf buf, TreeSimulatorOutput output) {
        buf.writeItem(output.output);
        buf.writeFloat(output.chance);
        buf.writeInt(output.minRolls);
        buf.writeInt(output.maxRolls);
    }

    public static TreeSimulatorOutput readOutput(FriendlyByteBuf buf){
        ItemStack output = buf.readItem();
        float chance = buf.readFloat();
        int minRolls = buf.readInt();
        int maxRolls = buf.readInt();

        return new TreeSimulatorOutput(output, chance, minRolls, maxRolls);
    }
}

package com.coolerpromc.resourcestrees.compat.jei.subtype;

import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ResourcesTypeSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {
    public static final ResourcesTypeSubtypeInterpreter INSTANCE = new ResourcesTypeSubtypeInterpreter();


    public String getStringName(ItemStack itemStack) {
        if (!itemStack.hasTag()) {
            return "";
        }
        if (!itemStack.getTag().contains("type")) {
            return "";
        }
        ResourceLocation type = new ResourceLocation(itemStack.getOrCreateTag().getString("type"));
        return type.getPath();
    }

    @Override
    public String apply(ItemStack ingredient, UidContext context) {
        return getStringName(ingredient);
    }
}
package com.coolerpromc.resourcestrees.compat.jei.subtype;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class ResourcesTypeSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {
    public static final ResourcesTypeSubtypeInterpreter INSTANCE = new ResourcesTypeSubtypeInterpreter();

    @Override
    public @Nullable Object getSubtypeData(ItemStack ingredient, UidContext context) {
        return ingredient.get(ModDataComponents.TYPE);
    }

    @Override
    public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
        return getStringName(ingredient);
    }

    public String getStringName(ItemStack itemStack) {
        if (itemStack.getComponentsPatch().isEmpty()) {
            return "";
        }
        ResourceLocation type = itemStack.getOrDefault(ModDataComponents.TYPE, ResourcesTrees.id("empty"));
        return type.getPath();
    }
}
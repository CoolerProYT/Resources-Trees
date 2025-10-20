package com.coolerpromc.resourcestrees.compat.jei.subtype;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

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
        if (itemStack.contains(ModDataComponents.TYPE)) {
            return "";
        }
        RegistryEntry<ResourcesTypes> type = itemStack.get(ModDataComponents.TYPE);
        if (type != null && type.getKey().isPresent()){
            return type.getKey().get().getValue().getPath();
        }
        return "";
    }
}
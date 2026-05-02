package com.coolerpromc.resourcestrees.recipe.custom;

import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.registry.ModRegistries;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class ResourcesSaplingRecipe extends CustomRecipe {
    public static final MapCodec<ResourcesSaplingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStackTemplate.CODEC.fieldOf("baseSapling").forGetter(ResourcesSaplingRecipe::getBaseSapling),
            ItemStackTemplate.CODEC.fieldOf("resourcesSapling").forGetter(ResourcesSaplingRecipe::getResourcesSapling)
    ).apply(instance, ResourcesSaplingRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ResourcesSaplingRecipe> STREAM_CODEC = StreamCodec.composite(
            ItemStackTemplate.STREAM_CODEC,
            ResourcesSaplingRecipe::getBaseSapling,
            ItemStackTemplate.STREAM_CODEC,
            ResourcesSaplingRecipe::getResourcesSapling,
            ResourcesSaplingRecipe::new
    );
    public static final RecipeSerializer<ResourcesSaplingRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    private final ItemStackTemplate baseSapling;
    private final ItemStackTemplate resourcesSapling;
    private Level levelCache = null;

    public ResourcesSaplingRecipe(ItemStackTemplate baseSapling, ItemStackTemplate resourcesSapling){
        this.baseSapling = baseSapling;
        this.resourcesSapling = resourcesSapling;
    }

    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        levelCache = level;
        if (craftingInput.width() == 3 && craftingInput.height() == 3){
            if (craftingInput.getItem(1, 1).is(baseSapling.item()) && isCorrectPattern(craftingInput)){
                Optional<ResourcesTypes> optional = level.registryAccess().lookupOrThrow(ModRegistries.RESOURCES_TYPES_KEY).stream().filter(type -> type.isCorrectMaterial(getMaterial(craftingInput))).findFirst();
                return optional.isPresent();
            }
        }
        return false;
    }

    private ItemStack getMaterial(CraftingInput craftingInput){
        return craftingInput.getItem(0, 1);
    }

    private boolean isCorrectPattern(CraftingInput craftingInput) {
        if (!craftingInput.getItem(0, 0).isEmpty()) return false;
        if (!craftingInput.getItem(2, 0).isEmpty()) return false;
        if (!craftingInput.getItem(0, 2).isEmpty()) return false;
        if (!craftingInput.getItem(2, 2).isEmpty()) return false;

        ItemStack top = craftingInput.getItem(1, 0);
        ItemStack left = craftingInput.getItem(0, 1);
        ItemStack right = craftingInput.getItem(2, 1);
        ItemStack bottom = craftingInput.getItem(1, 2);

        if (top.isEmpty()) return false;

        return top.is(left.getItem()) && top.is(right.getItem()) && top.is(bottom.getItem());
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput) {
        if (levelCache != null){
            Optional<Holder<ResourcesTypes>> optional = ResourcesTypes.getAllResourcesTypes(levelCache.registryAccess()).values().stream().filter(type -> type.value().isCorrectMaterial(getMaterial(craftingInput))).findFirst();
            if (optional.isPresent()){
                return resourcesSapling.apply(1, DataComponentPatch.builder().set(ModDataComponents.TYPE.get(), optional.get()).build());
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return SERIALIZER;
    }

    public ItemStackTemplate getBaseSapling() {
        return baseSapling;
    }

    public ItemStackTemplate getResourcesSapling() {
        return resourcesSapling;
    }
}

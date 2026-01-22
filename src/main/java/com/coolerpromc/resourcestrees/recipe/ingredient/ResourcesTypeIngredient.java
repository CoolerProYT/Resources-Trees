package com.coolerpromc.resourcestrees.recipe.ingredient;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.*;

public record ResourcesTypeIngredient(Ingredient base, ComponentChanges components) implements CustomIngredient {
    public static final CustomIngredientSerializer<ResourcesTypeIngredient> SERIALIZER = new Serializer();

    public ResourcesTypeIngredient {
        if (components.get(ModDataComponents.TYPE).isEmpty()){
            throw new IllegalArgumentException("ResourcesTypeIngredient must have at least one defined Resources Type component");
        }
    }

    @Override
    public boolean test(ItemStack stack) {
        if (!base.test(stack)) return false;

        if (!stack.contains(ModDataComponents.TYPE)) return false;

        RegistryEntry<ResourcesTypes> stackType = stack.get(ModDataComponents.TYPE);
        RegistryEntry<ResourcesTypes> ingType = this.components.get(ModDataComponents.TYPE).get();

        return testFallback(stack) || stackType.value().equals(ingType.value());
    }

    @Override
    public List<ItemStack> getMatchingStacks() {
        List<ItemStack> stacks = new ArrayList<>(List.of(base.getMatchingStacks()));
        stacks.replaceAll(stack -> {
            ItemStack copy = stack.copy();

            copy.applyChanges(components);

            return copy;
        });
        stacks.removeIf(stack -> !base.test(stack));
        return stacks;
    }

    public boolean testFallback(ItemStack stack) {
        for (Map.Entry<ComponentType<?>, Optional<?>> entry : components.entrySet()) {
            final ComponentType<?> type = entry.getKey();
            final Optional<?> value = entry.getValue();

            if (value.isPresent()) {
                if (!stack.contains(type)) {
                    return false;
                }

                if (!Objects.equals(value.get(), stack.get(type))) {
                    return false;
                }
            } else {
                if (stack.contains(type)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean requiresTesting() {
        return true;
    }

    @Override
    public CustomIngredientSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    private static class Serializer implements CustomIngredientSerializer<ResourcesTypeIngredient> {
        private static final Identifier ID = ResourcesTrees.id("resources_type");
        private static final MapCodec<ResourcesTypeIngredient> ALLOW_EMPTY_CODEC = createCodec(Ingredient.ALLOW_EMPTY_CODEC);
        private static final MapCodec<ResourcesTypeIngredient> DISALLOW_EMPTY_CODEC = createCodec(Ingredient.DISALLOW_EMPTY_CODEC);
        private static final PacketCodec<RegistryByteBuf, ResourcesTypeIngredient> PACKET_CODEC = PacketCodec.tuple(
                Ingredient.PACKET_CODEC, ResourcesTypeIngredient::base,
                ComponentChanges.PACKET_CODEC, ResourcesTypeIngredient::components,
                ResourcesTypeIngredient::new
        );

        private static MapCodec<ResourcesTypeIngredient> createCodec(Codec<Ingredient> ingredientCodec) {
            return RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            ingredientCodec.fieldOf("base").forGetter(ResourcesTypeIngredient::base),
                            ComponentChanges.CODEC.fieldOf("components").forGetter(ResourcesTypeIngredient::components)
                    ).apply(instance, ResourcesTypeIngredient::new)
            );
        }
        
        @Override
        public Identifier getIdentifier() {
            return ID;
        }

        @Override
        public MapCodec<ResourcesTypeIngredient> getCodec(boolean allowEmpty) {
            return allowEmpty ? ALLOW_EMPTY_CODEC : DISALLOW_EMPTY_CODEC;
        }
        
        @Override
        public PacketCodec<RegistryByteBuf, ResourcesTypeIngredient> getPacketCodec() {
            return PACKET_CODEC;
        }
    }
}

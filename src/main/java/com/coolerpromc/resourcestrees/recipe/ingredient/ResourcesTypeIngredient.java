package com.coolerpromc.resourcestrees.recipe.ingredient;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.ComponentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

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
    public Stream<RegistryEntry<Item>> getMatchingItems() {
        return base.getMatchingItems();
    }

    @Override
    public SlotDisplay toDisplay() {
        return new SlotDisplay.CompositeSlotDisplay(
                base.getMatchingItems().map(this::createEntryDisplay).toList()
        );
    }

    private SlotDisplay createEntryDisplay(RegistryEntry<Item> entry) {
        ItemStack stack = entry.value().getDefaultStack();
        stack.applyChanges(components);
        return new SlotDisplay.StackSlotDisplay(stack);
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
        private static final MapCodec<ResourcesTypeIngredient> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Ingredient.CODEC.fieldOf("base").forGetter(ResourcesTypeIngredient::base),
                        ComponentChanges.CODEC.fieldOf("components").forGetter(ResourcesTypeIngredient::components)
                ).apply(instance, ResourcesTypeIngredient::new)
        );
        private static final PacketCodec<RegistryByteBuf, ResourcesTypeIngredient> PACKET_CODEC = PacketCodec.tuple(
                Ingredient.PACKET_CODEC, ResourcesTypeIngredient::base,
                ComponentChanges.PACKET_CODEC, ResourcesTypeIngredient::components,
                ResourcesTypeIngredient::new
        );

        @Override
        public Identifier getIdentifier() {
            return ID;
        }

        @Override
        public MapCodec<ResourcesTypeIngredient> getCodec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ResourcesTypeIngredient> getPacketCodec() {
            return PACKET_CODEC;
        }
    }
}

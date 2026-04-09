package com.coolerpromc.resourcestrees.recipe.ingredient;

import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.platform.Services;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

public record ResourcesTypeIngredient(HolderSet<Item> base, DataComponentPatch components, boolean exhaustive) {
    public static final MapCodec<ResourcesTypeIngredient> CODEC = RecordCodecBuilder.mapCodec(
            builder -> builder
                    .group(
                            HolderSetCodec.create(Registries.ITEM, BuiltInRegistries.ITEM.holderByNameCodec(), false).fieldOf("base").forGetter(ResourcesTypeIngredient::itemSet),
                            DataComponentPatch.CODEC.fieldOf("components").forGetter(ResourcesTypeIngredient::components),
                            Codec.BOOL.optionalFieldOf("strict", false).forGetter(ResourcesTypeIngredient::componentsExhaustive))
                    .apply(builder, ResourcesTypeIngredient::new));

    public ResourcesTypeIngredient {
        if (get(ModDataComponents.TYPE.get(), components.entrySet()).isEmpty()){
            throw new IllegalArgumentException("ResourcesTypeIngredient must have at least one defined Resources Type component");
        }
    }

    @Nullable
    public <T> Optional<? extends T> get(DataComponentType<T> type, Set<Map.Entry<DataComponentType<?>, Optional<?>>> entrySet) {
        return (Optional<? extends T>)entrySet.stream().filter(entry -> entry.getKey().equals(type)).map(Map.Entry::getValue).findFirst().orElse(null);
    }

    public boolean test(ItemStack stack) {
        if (!base.contains(stack.typeHolder())) return false;

        if (!stack.has(ModDataComponents.TYPE.get())) return false;

        Holder<ResourcesTypes> stackType = stack.get(ModDataComponents.TYPE.get());
        Holder<ResourcesTypes> ingType = get(ModDataComponents.TYPE.get(), components.entrySet()).get();

        return testFallback(stack) || stackType.value().equals(ingType.value());
    }

    public boolean testFallback(ItemStack stack) {
        for (Map.Entry<DataComponentType<?>, Optional<?>> entry : components.entrySet()) {
            final DataComponentType<?> type = entry.getKey();
            final Optional<?> value = entry.getValue();

            if (value.isPresent()) {
                if (!stack.has(type)) {
                    return false;
                }

                if (!Objects.equals(value.get(), stack.get(type))) {
                    return false;
                }
            } else {
                if (stack.has(type)) {
                    return false;
                }
            }
        }

        return true;
    }

    public Stream<Holder<Item>> items() {
        return base.stream();
    }

    public boolean isSimple() {
        return false;
    }

    public SlotDisplay display() {
        return new SlotDisplay.Composite(base.stream()
                .<SlotDisplay>map(item -> {
                    var template = new ItemStackTemplate(item, 1, components);
                    var display = new SlotDisplay.ItemStackSlotDisplay(template);
                    var remainderItem = item.value().getCraftingRemainder();
                    if (remainderItem != null) {
                        SlotDisplay remainderDisplay = new SlotDisplay.ItemStackSlotDisplay(remainderItem);
                        return new SlotDisplay.WithRemainder(display, remainderDisplay);
                    } else {
                        return display;
                    }
                })
                .toList());
    }

    public HolderSet<Item> itemSet() {
        return base;
    }

    public DataComponentPatch components() {
        return components;
    }

    public boolean componentsExhaustive() {
        return exhaustive;
    }

    @Deprecated(forRemoval = true)
    public boolean isStrict() {
        return exhaustive;
    }

    public static Ingredient of(boolean exhaustive, ItemStack stack) {
        return of(exhaustive, stack.getComponents(), stack.getItem());
    }

    public static Ingredient of(boolean exhaustive, ItemStackTemplate stack) {
        return of(exhaustive, stack.components(), stack.item());
    }

    public static <T> Ingredient of(DataComponentType<? super T> type, T value, ItemLike... items) {
        return of(false, DataComponentPatch.builder().set(type, value).build(), items);
    }

    public static <T> Ingredient of(boolean exhaustive, DataComponentType<? super T> type, T value, ItemLike... items) {
        return of(exhaustive, DataComponentPatch.builder().set(type, value).build(), items);
    }

    public static <T> Ingredient of(boolean exhaustive, Supplier<? extends DataComponentType<? super T>> type, T value, ItemLike... items) {
        return of(exhaustive, type.get(), value, items);
    }

    public static Ingredient of(boolean exhaustive, DataComponentMap map, ItemLike... items) {
        return of(exhaustive, asPatch(map), items);
    }

    @SafeVarargs
    public static Ingredient of(boolean exhaustive, DataComponentMap map, Holder<Item>... items) {
        return of(exhaustive, asPatch(map), items);
    }

    public static Ingredient of(boolean exhaustive, DataComponentMap map, HolderSet<Item> items) {
        return of(exhaustive, asPatch(map), items);
    }

    private static DataComponentPatch asPatch(DataComponentMap map) {
        var builder = DataComponentPatch.builder();
        for (var type : map) {
            builder.set(type);
        }
        return builder.build();
    }

    @SafeVarargs
    public static Ingredient of(boolean exhaustive, DataComponentPatch predicate, Holder<Item>... items) {
        return of(exhaustive, predicate, HolderSet.direct(items));
    }

    public static Ingredient of(DataComponentPatch predicate, ItemLike... items) {
        return of(false, predicate, HolderSet.direct(Arrays.stream(items).map(ItemLike::asItem).map(Item::builtInRegistryHolder).toList()));
    }

    public static Ingredient of(boolean exhaustive, DataComponentPatch predicate, ItemLike... items) {
        return of(exhaustive, predicate, HolderSet.direct(Arrays.stream(items).map(ItemLike::asItem).map(Item::builtInRegistryHolder).toList()));
    }

    public static Ingredient of(boolean exhaustive, DataComponentPatch predicate, HolderSet<Item> items) {
        return Services.REGISTRY.createCustomIngredient(items, predicate, exhaustive);
    }
}

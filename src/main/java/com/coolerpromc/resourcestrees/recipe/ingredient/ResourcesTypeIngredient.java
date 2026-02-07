package com.coolerpromc.resourcestrees.recipe.ingredient;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
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
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.ingredients.AbstractIngredient;
import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ResourcesTypeIngredient extends AbstractIngredient {
    public static final MapCodec<ResourcesTypeIngredient> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
            HolderSetCodec.create(Registries.ITEM, BuiltInRegistries.ITEM.holderByNameCodec(), false).fieldOf("items").forGetter(ResourcesTypeIngredient::itemSet),
            DataComponentPatch.CODEC.fieldOf("components").forGetter(ResourcesTypeIngredient::components),
            Codec.BOOL.optionalFieldOf("strict", false).forGetter(ResourcesTypeIngredient::isStrict)
    ).apply(builder, ResourcesTypeIngredient::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ResourcesTypeIngredient> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderSet(Registries.ITEM),
            ResourcesTypeIngredient::itemSet,
            DataComponentPatch.STREAM_CODEC,
            ResourcesTypeIngredient::components,
            ByteBufCodecs.BOOL,
            ResourcesTypeIngredient::isStrict,
            ResourcesTypeIngredient::new
    );
    
    private final HolderSet<Item> base;
    private final DataComponentPatch components;
    private final boolean exhaustive;
    private final ItemStack[] stacks;

    public ResourcesTypeIngredient(HolderSet<Item> base, DataComponentPatch components, boolean exhaustive) {
        if (components.get(ModDataComponents.TYPE.get()).isEmpty()){
            throw new IllegalArgumentException("ResourcesTypeIngredient must have at least one defined Resources Type component");
        }
        this.base = base;
        this.components = components;
        this.exhaustive = exhaustive;
        this.stacks = base.stream().map((i) -> new ItemStack(i, 1, components)).toArray(ItemStack[]::new);
    }
    
    @Override
    public boolean test(ItemStack stack) {
        if (!base.contains(stack.getItemHolder())) return false;

        if (!stack.has(ModDataComponents.TYPE.get())) return false;

        Holder<ResourcesTypes> stackType = stack.get(ModDataComponents.TYPE.get());
        Holder<ResourcesTypes> ingType = components.get(ModDataComponents.TYPE.get()).get();

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

    @Override
    public Stream<Holder<Item>> items() {
        return base.stream();
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> serializer() {
        return ResourcesTrees.RESOURCES_TYPE_INGREDIENT.get();
    }

    @Override
    public SlotDisplay display() {
        return new SlotDisplay.Composite(Stream.of(this.stacks).map((stack) -> {
            SlotDisplay display = new SlotDisplay.ItemStackSlotDisplay(stack);
            ItemStack remainder = stack.getCraftingRemainder();
            if (!remainder.isEmpty()) {
                SlotDisplay remainderDisplay = new SlotDisplay.ItemStackSlotDisplay(remainder);
                return new SlotDisplay.WithRemainder(display, remainderDisplay);
            } else {
                return display;
            }
        }).toList());
    }

    public HolderSet<Item> itemSet() {
        return base;
    }

    public DataComponentPatch components() {
        return components;
    }

    /**
     * {@return true if item stacks that have any component not listed in the components of this ingredient will fail to match}
     */
    public boolean componentsExhaustive() {
        return exhaustive;
    }

    @Deprecated(forRemoval = true)
    public boolean isStrict() {
        return exhaustive;
    }

    /**
     * Creates a new ingredient matching the given item, containing the given components
     */
    public static Ingredient of(boolean exhaustive, ItemStack stack) {
        return of(exhaustive, stack.getComponents(), stack.getItem());
    }

    /**
     * Creates a new ingredient matching any item from the list, containing the given components
     */
    public static <T> Ingredient of(DataComponentType<? super T> type, T value, ItemLike... items) {
        return of(false, DataComponentPatch.builder().set(type, value).build(), items);
    }

    /**
     * Creates a new ingredient matching any item from the list, containing the given components
     */
    public static <T> Ingredient of(boolean exhaustive, DataComponentType<? super T> type, T value, ItemLike... items) {
        return of(exhaustive, DataComponentPatch.builder().set(type, value).build(), items);
    }

    /**
     * Creates a new ingredient matching any item from the list, containing the given components
     */
    public static <T> Ingredient of(boolean exhaustive, Supplier<? extends DataComponentType<? super T>> type, T value, ItemLike... items) {
        return of(exhaustive, type.get(), value, items);
    }

    /**
     * Creates a new ingredient matching any item from the list, containing the given components
     */
    public static Ingredient of(boolean exhaustive, DataComponentMap map, ItemLike... items) {
        return of(exhaustive, asPatch(map), items);
    }

    /**
     * Creates a new ingredient matching any item from the list, containing the given components
     */
    @SafeVarargs
    public static Ingredient of(boolean exhaustive, DataComponentMap map, Holder<Item>... items) {
        return of(exhaustive, asPatch(map), items);
    }

    /**
     * Creates a new ingredient matching any item from the list, containing the given components
     */
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

    /**
     * Creates a new ingredient matching any item from the list, containing the given components
     */
    @SafeVarargs
    public static Ingredient of(boolean exhaustive, DataComponentPatch predicate, Holder<Item>... items) {
        return of(exhaustive, predicate, HolderSet.direct(items));
    }

    /**
     * Creates a new ingredient matching any item from the list, that contains the components set on the given patch
     * and that does <strong>not</strong> contain the components removed by the given patch.
     */
    public static Ingredient of(DataComponentPatch predicate, ItemLike... items) {
        return of(false, predicate, HolderSet.direct(Arrays.stream(items).map(ItemLike::asItem).map(Item::builtInRegistryHolder).toList()));
    }

    /**
     * Creates a new ingredient matching any item from the list, that contains the components set on the given patch
     * and that does <strong>not</strong> contain the components removed by the given patch.
     *
     * @param exhaustive If true, no other components besides the components set on the patch are allowed on an item to match.
     */
    public static Ingredient of(boolean exhaustive, DataComponentPatch predicate, ItemLike... items) {
        return of(exhaustive, predicate, HolderSet.direct(Arrays.stream(items).map(ItemLike::asItem).map(Item::builtInRegistryHolder).toList()));
    }

    /**
     * Creates a new ingredient matching any item from the list, containing the given components
     */
    public static Ingredient of(boolean exhaustive, DataComponentPatch predicate, HolderSet<Item> items) {
        return new ResourcesTypeIngredient(items, predicate, exhaustive);
    }

    public static class Serializer implements IIngredientSerializer<ResourcesTypeIngredient>{
        public static final ResourcesTypeIngredient.Serializer INSTANCE = new ResourcesTypeIngredient.Serializer();
        @Override
        public MapCodec<ResourcesTypeIngredient> codec() {
            return ResourcesTypeIngredient.CODEC;
        }

        @Override
        public void write(RegistryFriendlyByteBuf buffer, ResourcesTypeIngredient value) {
            STREAM_CODEC.encode(buffer, value);
        }

        @Override
        public ResourcesTypeIngredient read(RegistryFriendlyByteBuf buffer) {
            return STREAM_CODEC.decode(buffer);
        }
    }
}

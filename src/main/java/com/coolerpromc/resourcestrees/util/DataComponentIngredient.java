package com.coolerpromc.resourcestrees.util;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.DataComponentMap;
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
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DataComponentIngredient extends AbstractIngredient {
    public static final MapCodec<DataComponentIngredient> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
            HolderSetCodec.create(Registries.ITEM, BuiltInRegistries.ITEM.holderByNameCodec(), false).fieldOf("items").forGetter(DataComponentIngredient::itemSet),
            DataComponentExactPredicate.CODEC.fieldOf("components").forGetter(DataComponentIngredient::components),
            Codec.BOOL.optionalFieldOf("strict", false).forGetter(DataComponentIngredient::isStrict)
    ).apply(builder, DataComponentIngredient::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, DataComponentIngredient> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderSet(Registries.ITEM),
            DataComponentIngredient::itemSet,
            DataComponentExactPredicate.STREAM_CODEC,
            DataComponentIngredient::components,
            ByteBufCodecs.BOOL,
            DataComponentIngredient::isStrict,
            DataComponentIngredient::new
    );

    private final DataComponentExactPredicate components;
    private final boolean strict;
    private final ItemStack[] stacks;

    protected DataComponentIngredient(HolderSet<Item> p_368516_, DataComponentExactPredicate components, boolean strict) {
        super(p_368516_);
        this.components = components;
        this.strict = strict;
        this.stacks = values.stream().map((i) -> new ItemStack(i, 1, components.asPatch())).toArray(ItemStack[]::new);
    }

    @Override
    public boolean test(ItemStack stack) {
        if (this.strict) {
            for(ItemStack stack2 : this.stacks) {
                if (ItemStack.isSameItemSameComponents(stack, stack2)) {
                    return true;
                }
            }

            return false;
        } else {
            return this.values.contains(stack.getItemHolder()) && this.components.test(stack);
        }
    }

    @Override
    public Stream<Holder<Item>> items() {
        return this.values.stream();
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> serializer() {
        return ResourcesTrees.DATA_COMPONENT_INGREDIENT.get();
    }

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
        return this.values;
    }

    public DataComponentExactPredicate components() {
        return this.components;
    }

    public boolean isStrict() {
        return this.strict;
    }

    public static DataComponentIngredient of(boolean strict, ItemStack stack) {
        return of(strict, stack.getComponents(), stack.getItem());
    }

    public static <T> DataComponentIngredient of(boolean strict, DataComponentType<? super T> type, T value, ItemLike... items) {
        return of(strict, DataComponentExactPredicate.builder().expect(type, value).build(), items);
    }

    public static <T> DataComponentIngredient of(boolean strict, Supplier<? extends DataComponentType<? super T>> type, T value, ItemLike... items) {
        return of(strict, type.get(), value, items);
    }

    public static DataComponentIngredient of(boolean strict, DataComponentMap map, ItemLike... items) {
        return of(strict, DataComponentExactPredicate.allOf(map), items);
    }

    @SafeVarargs
    public static DataComponentIngredient of(boolean strict, DataComponentMap map, Holder<Item>... items) {
        return of(strict, DataComponentExactPredicate.allOf(map), items);
    }

    public static DataComponentIngredient of(boolean strict, DataComponentMap map, HolderSet<Item> items) {
        return of(strict, DataComponentExactPredicate.allOf(map), items);
    }

    @SafeVarargs
    public static DataComponentIngredient of(boolean strict, DataComponentExactPredicate predicate, Holder<Item>... items) {
        return of(strict, predicate, HolderSet.direct(items));
    }

    public static DataComponentIngredient of(boolean strict, DataComponentExactPredicate predicate, ItemLike... items) {
        return of(strict, predicate, HolderSet.direct(Arrays.stream(items).map(ItemLike::asItem).map(Item::builtInRegistryHolder).toList()));
    }

    public static DataComponentIngredient of(boolean strict, DataComponentExactPredicate predicate, HolderSet<Item> items) {
        return (new DataComponentIngredient(items, predicate, strict));
    }

    public static class Serializer implements IIngredientSerializer<DataComponentIngredient>{
        public static final Serializer INSTANCE = new Serializer();
        @Override
        public MapCodec<DataComponentIngredient> codec() {
            return DataComponentIngredient.CODEC;
        }

        @Override
        public void write(RegistryFriendlyByteBuf buffer, DataComponentIngredient value) {
            STREAM_CODEC.encode(buffer, value);
        }

        @Override
        public DataComponentIngredient read(RegistryFriendlyByteBuf buffer) {
            return STREAM_CODEC.decode(buffer);
        }
    }
}

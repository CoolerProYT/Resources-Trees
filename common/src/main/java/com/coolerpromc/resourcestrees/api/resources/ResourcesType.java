package com.coolerpromc.resourcestrees.api.resources;

import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.item.custom.LeafFragmentItem;
import com.coolerpromc.resourcestrees.platform.util.BlockRegistryHandler;
import com.coolerpromc.resourcestrees.platform.util.RegistryHandler;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Represents a resource type that a resource tree can produce.
 * <p>
 * A {@code ResourcesType} defines the material (item or item tag) dropped by the tree's leaves,
 * its color tint, drop chances, spawn weight, and growth simulation speed. Instances are
 * associated with one or more {@link com.coolerpromc.resourcestrees.api.tree.TreeType}s at registration time, producing unique
 * sapling and leaves block pairs for each combination.
 * </p>
 *
 * <p>
 * Use {@link Builder} to construct instances.
 * </p>
 */
public final class ResourcesType {
    public static final Codec<Either<Identifier, TagKey<Item>>> MATERIAL_CODEC = Codec.either(
            Identifier.CODEC,
            TagKey.hashedCodec(Registries.ITEM)
    );

    public static final Codec<ResourcesType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(ResourcesType::name),
            MATERIAL_CODEC.fieldOf("material").forGetter(ResourcesType::material),
            Codec.INT.fieldOf("color").forGetter(ResourcesType::color),
            Codec.INT.fieldOf("weight").forGetter(ResourcesType::weight),
            Codec.FLOAT.fieldOf("saplingDropChance").forGetter(ResourcesType::saplingDropChance),
            Codec.FLOAT.fieldOf("leafDropChance").forGetter(ResourcesType::leafDropChance),
            Codec.INT.fieldOf("treeSimulatorTicks").forGetter(ResourcesType::treeSimulatorTicks)
    ).apply(instance, ResourcesType::new));

    @Deprecated(forRemoval = true)
    public static final Codec<ResourcesType> LEGACY_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MATERIAL_CODEC.fieldOf("material").forGetter(ResourcesType::material),
            Codec.INT.fieldOf("color").forGetter(ResourcesType::color),
            Codec.INT.fieldOf("weight").forGetter(ResourcesType::weight),
            Codec.FLOAT.fieldOf("saplingDropChance").forGetter(ResourcesType::saplingDropChance),
            Codec.FLOAT.fieldOf("leafDropChance").forGetter(ResourcesType::leafDropChance),
            Codec.INT.fieldOf("treeSimulatorTicks").forGetter(ResourcesType::treeSimulatorTicks)
    ).apply(instance, ResourcesType::new));

    @Deprecated(forRemoval = true)
    public static final StreamCodec<RegistryFriendlyByteBuf, ResourcesType> LEGACY_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.either(Identifier.STREAM_CODEC, TagKey.streamCodec(Registries.ITEM)),
            ResourcesType::material,
            ByteBufCodecs.INT,
            ResourcesType::color,
            ByteBufCodecs.INT,
            ResourcesType::weight,
            ByteBufCodecs.FLOAT,
            ResourcesType::saplingDropChance,
            ByteBufCodecs.FLOAT,
            ResourcesType::leafDropChance,
            ByteBufCodecs.INT,
            ResourcesType::treeSimulatorTicks,
            ResourcesType::new
    );

    private final String name;
    private final Either<Identifier, TagKey<Item>> material;
    private final int color;
    private final int weight;
    private final float saplingDropChance;
    private final float leafDropChance;
    private final int treeSimulatorTicks;
    private Supplier<LeafFragmentItem> leafFragmentItem;

    private final Map<String, BlockRegistryHandler<ResourcesSaplingBlock>> saplingBlocks = new HashMap<>();
    private final Map<String, BlockRegistryHandler<ResourcesLeavesBlock>> leavesBlocks = new HashMap<>();

    public ResourcesType(String name, Either<Identifier, TagKey<Item>> material, int color, int weight, float saplingDropChance, float leafDropChance, int treeSimulatorTicks) {
        this.name = name;
        this.material = material;
        this.color = color;
        this.weight = weight;
        this.saplingDropChance = saplingDropChance;
        this.leafDropChance = leafDropChance;
        this.treeSimulatorTicks = treeSimulatorTicks;
    }

    @Deprecated(forRemoval = true)
    public ResourcesType(Either<Identifier, TagKey<Item>> material, int color, int weight, float saplingDropChance, float leafDropChance, int treeSimulatorTicks) {
        this("", material, color, weight, saplingDropChance, leafDropChance, treeSimulatorTicks);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ResourcesType that = (ResourcesType) o;
        return color() == that.color() && weight() == that.weight() && Float.compare(saplingDropChance(), that.saplingDropChance()) == 0 && Float.compare(leafDropChance(), that.leafDropChance()) == 0 && Objects.equals(treeSimulatorTicks(), that.treeSimulatorTicks()) && Objects.equals(material(), that.material());
    }

    @Override
    public int hashCode() {
        return Objects.hash(material(), color(), treeSimulatorTicks(), weight(), saplingDropChance(), leafDropChance());
    }

    public Ingredient ingredient(HolderGetter<Item> holderGetter) {
        if (material.left().isPresent()) {
            return Ingredient.of(BuiltInRegistries.ITEM.getValue(material.left().get()));
        }
        return Ingredient.of(holderGetter.getOrThrow(material.right().get()));
    }

    public String name() {
        return name;
    }

    public Either<Identifier, TagKey<Item>> material() {
        return material;
    }

    public int color() {
        return color;
    }

    public int weight() {
        return weight;
    }

    public float saplingDropChance() {
        return saplingDropChance;
    }

    public float leafDropChance() {
        return leafDropChance;
    }

    public int treeSimulatorTicks() {
        return treeSimulatorTicks;
    }

    @ApiStatus.Internal
    public void setLeavesBlock(String treeTypeName, BlockRegistryHandler<ResourcesLeavesBlock> block) {
        leavesBlocks.put(treeTypeName, block);
    }

    @ApiStatus.Internal
    public Supplier<ResourcesLeavesBlock> leavesBlock(String treeTypeName) {
        return leavesBlocks.get(treeTypeName);
    }

    @ApiStatus.Internal
    public void setSaplingBlock(String treeTypeName, BlockRegistryHandler<ResourcesSaplingBlock> block) {
        saplingBlocks.put(treeTypeName, block);
    }

    @ApiStatus.Internal
    public Supplier<ResourcesSaplingBlock> saplingBlock(String treeTypeName) {
        return saplingBlocks.get(treeTypeName);
    }

    @ApiStatus.Internal
    public void setLeafFragmentItem(Supplier<LeafFragmentItem> leafFragmentItem) {
        this.leafFragmentItem = leafFragmentItem;
    }

    @ApiStatus.Internal
    public Supplier<LeafFragmentItem> leafFragmentItem() {
        return leafFragmentItem;
    }

    @Override
    public String toString() {
        return "ResourcesType[" +
                "name=" + name + ", " +
                "material=" + material + ", " +
                "color=" + color + ", " +
                "weight=" + weight + ", " +
                "saplingDropChance=" + saplingDropChance + ", " +
                "leafDropChance=" + leafDropChance + ", " +
                "treeSimulatorTicks=" + treeSimulatorTicks + ']';
    }

    /**
     * Fluent builder for constructing {@link ResourcesType} instances.
     * <p>
     * The {@code name}, {@code material}, and {@code color} are required.
     * All other properties have sensible defaults:
     * </p>
     * <ul>
     *   <li>{@code weight} — {@code 5}</li>
     *   <li>{@code saplingDropChance} — {@code 0.125}</li>
     *   <li>{@code leafDropChance} — {@code 0.25}</li>
     *   <li>{@code treeSimulatorTicks} — {@code 1200}</li>
     * </ul>
     */
    public static class Builder {
        private final String name;
        private final Either<Identifier, TagKey<Item>> material;
        private final int color;
        private int weight;
        private float saplingDropChance;
        private float leafDropChance;
        private int treeSimulatorTicks;

        public Builder(String name, ItemLike material, int color) {
            this.name = name;
            this.material = Either.left(BuiltInRegistries.ITEM.getKey(material.asItem()));
            this.color = color;
            this.weight = 5;
            this.saplingDropChance = 0.125f;
            this.leafDropChance = 0.25f;
            this.treeSimulatorTicks = 1200;
        }

        public Builder(String name, RegistryHandler<Item> material, int color) {
            this.name = name;
            this.material = Either.left(material.id());
            this.color = color;
            this.weight = 5;
            this.saplingDropChance = 0.125f;
            this.leafDropChance = 0.25f;
            this.treeSimulatorTicks = 1200;
        }

        public Builder(String name, TagKey<Item> material, int color) {
            this.name = name;
            this.material = Either.right(material);
            this.color = color;
            this.weight = 5;
            this.saplingDropChance = 0.125f;
            this.leafDropChance = 0.25f;
            this.treeSimulatorTicks = 1200;
        }

        /**
         * Sets the spawn weight of this resource type relative to others.
         * Higher values make this type appear more frequently.
         *
         * @param weight the relative spawn weight; must be positive
         * @return this builder, for chaining
         *
         * @deprecated This field is no longer in use anywhere, will be removed once migration code is added
         */
        @Deprecated(forRemoval = true)
        public Builder weight(int weight) {
            this.weight = weight;
            return this;
        }

        /**
         * Sets the probability that a sapling drops when a leaf block decays or is broken.
         *
         * @param saplingDropChance a value between {@code 0.0} and {@code 1.0}
         * @return this builder, for chaining
         */
        public Builder saplingDropChance(float saplingDropChance) {
            this.saplingDropChance = saplingDropChance;
            return this;
        }

        /**
         * Sets the probability that a leaf fragment drops when a leaf block decays or is broken.
         *
         * @param leafDropChance a value between {@code 0.0} and {@code 1.0}
         * @return this builder, for chaining
         */
        public Builder leafDropChance(float leafDropChance) {
            this.leafDropChance = leafDropChance;
            return this;
        }

        /**
         * Sets the number of ticks for it to grow in a TreeSimulator block.
         * Lower values cause faster simulated growth.
         *
         * @param treeSimulatorTicks the tick interval; must be positive
         * @return this builder, for chaining
         */
        public Builder treeSimulatorTicks(int treeSimulatorTicks) {
            this.treeSimulatorTicks = treeSimulatorTicks;
            return this;
        }

        @ApiStatus.Internal
        public ResourcesType build() {
            return new ResourcesType(name, material, color, weight, saplingDropChance, leafDropChance, treeSimulatorTicks);
        }
    }
}
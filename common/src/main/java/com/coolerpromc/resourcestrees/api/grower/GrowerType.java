package com.coolerpromc.resourcestrees.api.grower;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.Weighted;
import net.minecraft.world.level.levelgen.feature.Feature;

import java.util.List;
import java.util.Optional;

/**
 * Defines a tree grower — the set of configured features used to generate a tree's structure when a resource
 * sapling grows.
 * <p>
 * A {@code GrowerType} is referenced by a {@link com.coolerpromc.resourcestrees.api.tree.TreeType} through its
 * {@link com.coolerpromc.resourcestrees.api.tree.TreeType#treeGrowerName() treeGrowerName}, and is used to build a
 * vanilla {@link net.minecraft.world.level.block.grower.TreeGrower}. Each list contains
 * {@link Weighted weighted} {@link ResourceKey configured feature keys}; when a sapling grows, one entry is chosen
 * at random according to its weight.
 * </p>
 *
 * <p>
 * Grower types can be supplied programmatically through
 * {@link com.coolerpromc.resourcestrees.api.IResourcesTreesPlugin#registerGrowerType(IGrowerTypeRegistry)}, or
 * loaded from JSON files placed in {@code config/resourcestrees/grower_type/} using {@link #CODEC}.
 * </p>
 *
 * @param name             the unique identifier name of this grower, matched against a
 *                         {@link com.coolerpromc.resourcestrees.api.tree.TreeType#treeGrowerName()}
 * @param trees            the weighted configured features used to generate normal (single-sapling) trees;
 *                         must not be empty
 * @param megaTrees        the weighted configured features used to generate mega (2x2) trees; may be empty
 * @param flowerTrees      the weighted configured features used to generate flowering tree variants
 *                         (e.g. flowering azalea); may be empty
 * @param shortestTreeType an optional configured feature used as the fallback/shortest tree variant when there is
 *                         insufficient space for the larger variants; {@link Optional#empty()} if none
 * @see net.minecraft.world.level.block.grower.TreeGrower
 */
public record GrowerType(String name, List<Weighted<ResourceKey<Feature>>> trees, List<Weighted<ResourceKey<Feature>>> megaTrees, List<Weighted<ResourceKey<Feature>>> flowerTrees, Optional<ResourceKey<Feature>> shortestTreeType) {
    /**
     * Codec used to (de)serialize a {@code GrowerType} to and from JSON, enabling grower types to be defined
     * via data files in {@code config/resourcestrees/grower_type/}. The {@code shortestTreeType} field is optional.
     */
    public static final Codec<GrowerType> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.STRING.fieldOf("name").forGetter(GrowerType::name),
            Codec.list(Weighted.codec(ResourceKey.codec(Registries.FEATURE))).fieldOf("trees").forGetter(GrowerType::trees),
            Codec.list(Weighted.codec(ResourceKey.codec(Registries.FEATURE))).fieldOf("megaTrees").forGetter(GrowerType::megaTrees),
            Codec.list(Weighted.codec(ResourceKey.codec(Registries.FEATURE))).fieldOf("flowerTrees").forGetter(GrowerType::flowerTrees),
            ResourceKey.codec(Registries.FEATURE).optionalFieldOf("shortestTreeType").forGetter(GrowerType::shortestTreeType)
    ).apply(i, GrowerType::new));
}

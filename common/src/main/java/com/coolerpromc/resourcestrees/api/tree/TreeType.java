package com.coolerpromc.resourcestrees.api.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

/**
 * Defines the visual appearance and structural properties of a tree shape used by resource trees.
 * <p>
 * A {@code TreeType} describes how a resource sapling should look and grow — including the
 * textures applied to its sapling and leaves blocks, the vanilla blocks those are derived from,
 * the grower responsible for generating the tree structure, and the log block forming its trunk.
 * </p>
 *
 * <p>
 * Each registered {@link com.coolerpromc.resourcestrees.api.resources.ResourcesType} can be combined with any registered {@code TreeType}
 * to produce a unique sapling and leaves block pair in the world.
 * </p>
 *
 * @param name             the unique identifier name of this tree type (e.g. {@code "oak"}, {@code "birch"})
 * @param treeGrowerName   the name of the tree grower responsible for generating this tree's structure
 *                         when the sapling grows (must match a registered grower)
 * @param saplingTexture   the {@link Identifier} of the texture applied to the resource sapling block
 *                         (e.g. {@code minecraft:block/oak_sapling})
 * @param leavesTexture    the {@link Identifier} of the texture applied to the resource leaves block
 *                         (e.g. {@code minecraft:block/oak_leaves})
 * @param originalSapling  the registry name of the vanilla sapling block this tree type is based on
 *                         (e.g. {@code "minecraft:oak_sapling"}); used for sound/behavior references and crafting recipe registration
 * @param originalLeaves   the registry name of the vanilla leaves block this tree type is based on
 *                         (e.g. {@code "minecraft:oak_leaves"}); used for sound/behavior references
 * @param log              the registry name of the log block used as the trunk for this tree type and tree simulator recipe registration
 *                         (e.g. {@code "minecraft:oak_log"})
 */
public record TreeType(String name, String treeGrowerName, Identifier saplingTexture, Identifier leavesTexture, String originalSapling, String originalLeaves, String log) {
    public static final Codec<TreeType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(TreeType::name),
            Codec.STRING.fieldOf("treeGrowerName").forGetter(TreeType::treeGrowerName),
            Identifier.CODEC.fieldOf("saplingTexture").forGetter(TreeType::saplingTexture),
            Identifier.CODEC.fieldOf("leavesTexture").forGetter(TreeType::leavesTexture),
            Codec.STRING.fieldOf("originalSapling").forGetter(TreeType::originalSapling),
            Codec.STRING.fieldOf("originalLeaves").forGetter(TreeType::originalLeaves),
            Codec.STRING.fieldOf("log").forGetter(TreeType::log)
    ).apply(instance, TreeType::new));
}

package com.coolerpromc.resourcestrees.api.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.sounds.AmbientLeavesBlockSoundPlayer;

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
 * @param particle         the per-tick chance (0.0–1.0) for a leaves block of this tree type to spawn a falling
 *                         tinted leaf particle. A value greater than {@code 0} causes the leaves to be created as a
 *                         particle-emitting block; a value of {@code 0} disables the particles entirely.
 *                         Serialized from the optional JSON field {@code "particle"} and defaults to {@code 0.01}.
 * @param leavesBlockSoundPlayer the {@link AmbientLeavesBlockSoundPlayer} controlling the ambient (rustling) sounds
 *                         emitted by this tree type's leaves blocks. Serialized from the optional JSON field
 *                         {@code "ambient_leaves_block_sound_player"} and defaults to
 *                         {@link AmbientLeavesBlockSoundPlayer#noAmbientSound()} (no ambient sound).
 */
public record TreeType(String name, String treeGrowerName, Identifier saplingTexture, Identifier leavesTexture, String originalSapling, String originalLeaves, String log, float particle, AmbientLeavesBlockSoundPlayer leavesBlockSoundPlayer) {
    public static final Codec<TreeType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(TreeType::name),
            Codec.STRING.fieldOf("treeGrowerName").forGetter(TreeType::treeGrowerName),
            Identifier.CODEC.fieldOf("saplingTexture").forGetter(TreeType::saplingTexture),
            Identifier.CODEC.fieldOf("leavesTexture").forGetter(TreeType::leavesTexture),
            Codec.STRING.fieldOf("originalSapling").forGetter(TreeType::originalSapling),
            Codec.STRING.fieldOf("originalLeaves").forGetter(TreeType::originalLeaves),
            Codec.STRING.fieldOf("log").forGetter(TreeType::log),
            Codec.FLOAT.optionalFieldOf("particle", 0.01f).forGetter(TreeType::particle),
            AmbientLeavesBlockSoundPlayer.CODEC.optionalFieldOf("ambient_leaves_block_sound_player", AmbientLeavesBlockSoundPlayer.noAmbientSound()).forGetter(TreeType::leavesBlockSoundPlayer)
    ).apply(instance, TreeType::new));

    public static class Builder {
        private final String name;
        private final String treeGrowerName;
        private final Identifier saplingTexture;
        private final Identifier leavesTexture;
        private final String originalSapling;
        private final String originalLeaves;
        private final String log;
        private float particle = 0.01f;
        private AmbientLeavesBlockSoundPlayer leavesBlockSoundPlayer = AmbientLeavesBlockSoundPlayer.noAmbientSound();

        public Builder(String name, String treeGrowerName, Identifier saplingTexture, Identifier leavesTexture, String originalSapling, String originalLeaves, String log) {
            this.name = name;
            this.treeGrowerName = treeGrowerName;
            this.saplingTexture = saplingTexture;
            this.leavesTexture = leavesTexture;
            this.originalSapling = originalSapling;
            this.originalLeaves = originalLeaves;
            this.log = log;
        }

        /**
         * Sets the per-tick chance (0.0–1.0) for this tree type's leaves blocks to spawn a falling tinted leaf
         * particle. Any value greater than {@code 0} enables the particle-emitting leaves block; {@code 0} disables
         * the particles. Defaults to {@code 0.01} if not set.
         *
         * @param particle the per-tick leaf particle chance
         * @return this builder for chaining
         */
        public Builder particle(float particle) {
            this.particle = particle;
            return this;
        }

        /**
         * Sets the {@link AmbientLeavesBlockSoundPlayer} controlling the ambient (rustling) sounds emitted by this
         * tree type's leaves blocks. Defaults to {@link AmbientLeavesBlockSoundPlayer#noAmbientSound()} if not set.
         *
         * @param leavesBlockSoundPlayer the ambient leaves sound player
         * @return this builder for chaining
         */
        public Builder leavesBlockSoundPlayer(AmbientLeavesBlockSoundPlayer leavesBlockSoundPlayer) {
            this.leavesBlockSoundPlayer = leavesBlockSoundPlayer;
            return this;
        }

        public TreeType build() {
            return new TreeType(name, treeGrowerName, saplingTexture, leavesTexture, originalSapling, originalLeaves, log, particle, leavesBlockSoundPlayer);
        }
    }
}

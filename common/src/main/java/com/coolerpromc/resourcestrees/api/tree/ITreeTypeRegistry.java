package com.coolerpromc.resourcestrees.api.tree;

/**
 * Registry interface for submitting {@link TreeType} definitions to the Resources Trees mod.
 * <p>
 * This registry is provided to {@link com.coolerpromc.resourcestrees.api.IResourcesTreesPlugin#registerTreeType(ITreeTypeRegistry)}
 * during initialization. Use it to register the tree shapes and visuals that resource
 * trees can grow into.
 * </p>
 */
public interface ITreeTypeRegistry {
    /**
     * Registers a new {@link TreeType}.
     * <p>
     * Each tree type defines a distinct tree shape, its associated grower logic, the textures
     * used for its sapling and leaves blocks, the vanilla sapling and leaves blocks it
     * derives from, and the log block it uses for its trunk.
     * </p>
     *
     * <p>Example:</p>
     * <pre>{@code
     * registry.register(new TreeType(
     *     "oak",
     *     "oak",
     *     Identifier.fromNamespaceAndPath(ResourcesTrees.MODID, "block/resources_oak_sapling"),
     *     Identifier.fromNamespaceAndPath(ResourcesTrees.MODID, "block/resources_oak_leaves"),
     *     "minecraft:oak_sapling",
     *     "minecraft:oak_leaves",
     *     "minecraft:oak_log"
     * ));
     * }</pre>
     *
     * @param treeType the {@link TreeType} to register; must not be {@code null}
     *                 and must have a unique {@link TreeType#name()}
     */
    void register(TreeType treeType);
}

package com.coolerpromc.resourcestrees.api;

import com.coolerpromc.resourcestrees.api.grower.IGrowerTypeRegistry;
import com.coolerpromc.resourcestrees.api.resources.IResourcesTypeRegistry;
import com.coolerpromc.resourcestrees.api.tree.ITreeTypeRegistry;

/**
 * Entry point interface for the Resources Trees plugin API.
 * <p>
 * Implement this interface to register custom resource types, tree types and grower types
 * into the Resources Trees mod. How your implementation is discovered depends on the mod loader.
 * </p>
 *
 * <h2>Registration (NeoForge)</h2>
 * <p>
 * On NeoForge, annotate your implementation class with {@link ResourcesTreesPlugin}. The mod's
 * annotation scanner discovers the annotated class, instantiates it via its public no-argument
 * constructor, and invokes the registration methods automatically:
 * </p>
 * <pre>{@code
 * @ResourcesTreesPlugin
 * public class MyResourcesTreesPlugin implements IResourcesTreesPlugin {
 *     // ...
 * }
 * }</pre>
 *
 * <h2>Registration (Fabric)</h2>
 * <p>
 * On Fabric, declare your implementation under the {@code resources_trees_plugin} entrypoint in your
 * {@code fabric.mod.json}:
 * </p>
 * <pre>
 * "entrypoints": {
 *     "resources_trees_plugin": [
 *         "com.example.mymod.MyResourcesTreesPlugin"
 *     ]
 * }
 * </pre>
 *
 * <h2>Example Implementation</h2>
 * <pre>{@code
 * public class MyResourcesTreesPlugin implements IResourcesTreesPlugin {
 *
 *     @Override
 *     public void registerResourcesType(IResourcesTypeRegistry registry) {
 *         registry.register(new ResourcesType.Builder("copper", Items.COPPER_INGOT, 0xB87333));
 *     }
 *
 *     @Override
 *     public void registerTreeType(ITreeTypeRegistry registry) {
 *         registry.register(new TreeType("birch", ...));
 *     }
 * }
 * }</pre>
 *
 * @see ResourcesTreesPlugin
 * @see IResourcesTypeRegistry
 * @see ITreeTypeRegistry
 * @see IGrowerTypeRegistry
 */
public interface IResourcesTreesPlugin {
    /**
     * Called during initialization to register custom {@link com.coolerpromc.resourcestrees.api.resources.ResourcesType} entries.
     * <p>
     * Use the provided {@link IResourcesTypeRegistry} to register one or more
     * resource types that define what material a tree produces, its drop chances,
     * color tint, and other properties.
     * </p>
     *
     * @param registry the registry used to submit {@link com.coolerpromc.resourcestrees.api.resources.ResourcesType.Builder} instances
     */
    default void registerResourcesType(IResourcesTypeRegistry registry){}

    /**
     * Called during initialization to register custom {@link com.coolerpromc.resourcestrees.api.tree.TreeType} entries.
     * <p>
     * Use the provided {@link ITreeTypeRegistry} to register one or more tree types
     * that define the visual appearance and growth behavior of resource trees,
     * such as their sapling/leaves textures and associated log block.
     * </p>
     *
     * @param registry the registry used to submit {@link com.coolerpromc.resourcestrees.api.tree.TreeType} instances
     */
    default void registerTreeType(ITreeTypeRegistry registry){}

    /**
     * Called during initialization to register custom {@link com.coolerpromc.resourcestrees.api.grower.GrowerType} entries.
     * <p>
     * Use the provided {@link IGrowerTypeRegistry} to register one or more growers that define which tree
     * structures a resource sapling generates when it grows. A registered grower is referenced by a
     * {@link com.coolerpromc.resourcestrees.api.tree.TreeType} through its
     * {@link com.coolerpromc.resourcestrees.api.tree.TreeType#treeGrowerName() treeGrowerName}.
     * </p>
     *
     * @param registry the registry used to submit {@link com.coolerpromc.resourcestrees.api.grower.GrowerType} instances
     */
    default void registerGrowerType(IGrowerTypeRegistry registry){}
}

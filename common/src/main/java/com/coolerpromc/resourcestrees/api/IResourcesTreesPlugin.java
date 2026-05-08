package com.coolerpromc.resourcestrees.api;

import com.coolerpromc.resourcestrees.api.resources.IResourcesTypeRegistry;
import com.coolerpromc.resourcestrees.api.tree.ITreeTypeRegistry;

/**
 * Entry point interface for the Resources Trees plugin API.
 * <p>
 * Implement this interface to register custom resource types and tree types
 * into the Resources Trees mod. Your implementation is discovered automatically
 * via the Java {@link java.util.ServiceLoader} mechanism.
 * </p>
 *
 * <h2>Registration</h2>
 * <p>
 * To register your implementation, create the following file in your mod's resources:
 * </p>
 * <pre>
 * resources/META-INF/services/com.coolerpromc.resourcestrees.api.IResourcesTreesPlugin
 * </pre>
 * <p>
 * The file must contain the fully qualified class name of your implementation, one per line:
 * </p>
 * <pre>
 * com.example.mymod.MyResourcesTreesPlugin
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
 * @see java.util.ServiceLoader
 * @see IResourcesTypeRegistry
 * @see ITreeTypeRegistry
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
    void registerResourcesType(IResourcesTypeRegistry registry);

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
    void registerTreeType(ITreeTypeRegistry registry);
}

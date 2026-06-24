package com.coolerpromc.resourcestrees.api.grower;

/**
 * Registry interface for submitting {@link GrowerType} definitions to the Resources Trees mod.
 * <p>
 * This registry is provided to {@link com.coolerpromc.resourcestrees.api.IResourcesTreesPlugin#registerGrowerType(IGrowerTypeRegistry)}
 * during initialization. Use it to register the growers that determine which tree structures a resource
 * sapling generates when it grows. A {@link com.coolerpromc.resourcestrees.api.tree.TreeType} references a
 * registered grower by its {@link GrowerType#name() name}.
 * </p>
 */
public interface IGrowerTypeRegistry {
    /**
     * Registers a new {@link GrowerType}.
     * <p>
     * Each grower type defines the weighted configured features used to generate the normal, mega, and flowering
     * tree variants, along with an optional shortest/fallback variant.
     * </p>
     *
     * @param type the {@link GrowerType} to register; must not be {@code null} and must have a unique
     *             {@link GrowerType#name()}
     */
    void register(GrowerType type);
}
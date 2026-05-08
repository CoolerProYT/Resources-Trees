package com.coolerpromc.resourcestrees.api.resources;

/**
 * Registry interface for submitting {@link ResourcesType} definitions to the Resources Trees mod.
 * <p>
 * This registry is provided to {@link com.coolerpromc.resourcestrees.api.IResourcesTreesPlugin#registerResourcesType(IResourcesTypeRegistry)}
 * during initialization. Use it to register any number of custom resource types by supplying
 * a configured {@link ResourcesType.Builder}.
 * </p>
 */
public interface IResourcesTypeRegistry {
    /**
     * Registers a new {@link ResourcesType} using the provided builder.
     * <p>
     * The builder must have a unique name and a valid material (either an item identifier
     * or an item tag), along with a color tint. Optional properties such as weight,
     * drop chances, and tree simulator ticks can be configured on the builder
     * before passing it here.
     * </p>
     *
     * <p>Example:</p>
     * <pre>{@code
     * registry.register(
     *     new ResourcesType.Builder("iron", Items.IRON_INGOT, 0xD8D8D8)
     *         .weight(4)
     *         .saplingDropChance(0.1f)
     *         .leafDropChance(0.2f)
     *         .treeSimulatorTicks(1000)
     * );
     * }</pre>
     *
     * @param builder a configured {@link ResourcesType.Builder} describing the resource type to register;
     *                must not be {@code null} and must have a unique name
     */
    void register(ResourcesType.Builder builder);
}

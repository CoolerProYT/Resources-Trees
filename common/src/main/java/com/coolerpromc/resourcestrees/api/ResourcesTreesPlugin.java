package com.coolerpromc.resourcestrees.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as a Resources Trees plugin entry point on NeoForge.
 * <p>
 * The annotated class must implement {@link IResourcesTreesPlugin} and provide a public no-argument
 * constructor. During mod construction the annotation scanner discovers all classes bearing this
 * annotation, instantiates them, and invokes their registration methods.
 * </p>
 * <p>
 * This annotation is only used for discovery on NeoForge. On Fabric, plugins are instead declared
 * under the {@code resources_trees_plugin} entrypoint in {@code fabric.mod.json}.
 * </p>
 *
 * @see IResourcesTreesPlugin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ResourcesTreesPlugin {
}
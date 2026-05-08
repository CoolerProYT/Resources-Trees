package com.coolerpromc.resourcestrees.registry;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.api.resources.ResourcesType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;

@Deprecated(forRemoval = true)
public class ModRegistries {
    public static final ResourceKey<Registry<ResourcesType>> RESOURCES_TYPES_KEY = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(Constants.MODID, "resources_type"));
}
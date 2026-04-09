package com.coolerpromc.resourcestrees.registry;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;

public class ModRegistries {
    public static final ResourceKey<Registry<ResourcesTypes>> RESOURCES_TYPES_KEY = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(Constants.MODID, "resources_type"));
}
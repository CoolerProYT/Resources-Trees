package com.coolerpromc.resourcestrees.registry;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class ModRegistries {
    public static final ResourceKey<Registry<ResourcesTypes>> RESOURCES_TYPES_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ResourcesTrees.MODID, "resources_type"));
}
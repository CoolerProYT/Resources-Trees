package com.coolerpromc.resourcestrees.registry;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ModRegistries {
    public static final RegistryKey<Registry<ResourcesTypes>> RESOURCES_TYPES_KEY = RegistryKey.ofRegistry(Identifier.of(ResourcesTrees.MODID, "resources_type"));
}
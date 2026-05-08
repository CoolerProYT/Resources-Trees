package com.coolerpromc.resourcestrees.platform;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.platform.services.IClientHelper;
import com.coolerpromc.resourcestrees.platform.services.IPlatformHelper;
import com.coolerpromc.resourcestrees.platform.services.IRegistryHelper;

import java.util.ServiceLoader;

public class Services {
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final IRegistryHelper REGISTRY = load(IRegistryHelper.class);
    public static final IClientHelper CLIENT = load(IClientHelper.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz, Services.class.getClassLoader())
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
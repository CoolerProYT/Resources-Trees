package com.coolerpromc.resourcestrees.api.resources;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.api.IResourcesTreesPlugin;
import com.coolerpromc.resourcestrees.platform.Services;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.ApiStatus;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

@ApiStatus.Internal
public final class ResourcesTypeRegistryImpl implements IResourcesTypeRegistry{
    private static final List<ResourcesType.Builder> BUILDERS = new ArrayList<>();
    private static final ResourcesTypeRegistryImpl INSTANCE = new ResourcesTypeRegistryImpl();

    public static void registerResourcesTypes(){
        ServiceLoader.load(IResourcesTreesPlugin.class).forEach(plugin -> {
            try{
                plugin.registerResourcesType(INSTANCE);
            }
            catch (Exception e){
                Constants.LOG.error("Plugin failed: {}", plugin.getClass().getName(), e);
            }
        });

        registerFromConfig();

        BUILDERS.stream().map(ResourcesType.Builder::build).forEach(type -> {
            ResourcesTypes.TYPES.add(type);
            ResourcesTypes.TYPE_BY_NAME.put(type.name(), type);
        });
    }

    private static void registerFromConfig(){
        File dir = Services.PLATFORM.getConfigDir().resolve("resourcestrees/resources_type/").toFile();
        if (!dir.exists() && dir.mkdirs()) {
            Constants.LOG.info("Created /config/resourcestrees/resources_type/ directory");
        }

        try (var paths = Files.walk(dir.toPath())) {
            List<File> files = paths.filter(Files::isRegularFile).filter(path -> path.toString().toLowerCase().endsWith(".json")).map(Path::toFile).toList();

            for (File file : files) {
                InputStreamReader reader = null;
                String name = null;
                ResourcesType resourcesType = null;

                try {
                    reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
                    JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                    name = file.getName().replace(".json", "");

                    resourcesType = ResourcesType.CODEC.parse(JsonOps.INSTANCE, json).result().orElse(ResourcesType.LEGACY_CODEC.parse(JsonOps.INSTANCE, json).getOrThrow());

                    reader.close();
                } catch (Exception e) {
                    Constants.LOG.error("Something is wrong when registering {} resources type.", name, e);
                } finally {
                    IOUtils.closeQuietly(reader);
                }

                if (resourcesType != null){
                    ResourcesTypes.TYPES.add(resourcesType);
                    ResourcesTypes.TYPE_BY_NAME.put(resourcesType.name(), resourcesType);
                }
            }
        } catch (Exception e) {
            Constants.LOG.error("Something is wrong when registering resources type.", e);
        }
    }

    @Override
    public void register(ResourcesType.Builder builder) {
        BUILDERS.add(builder);
    }
}

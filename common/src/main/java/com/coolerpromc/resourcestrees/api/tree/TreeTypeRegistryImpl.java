package com.coolerpromc.resourcestrees.api.tree;

import com.coolerpromc.resourcestrees.Constants;
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
import java.util.List;

@ApiStatus.Internal
public final class TreeTypeRegistryImpl implements ITreeTypeRegistry{
    public static final ITreeTypeRegistry INSTANCE = new TreeTypeRegistryImpl();

    public static void registerTreeTypes(){
        Services.PLATFORM.getPlugins().forEach(plugin -> {
            try{
                plugin.registerTreeType(INSTANCE);
            }
            catch (Exception e){
                Constants.LOG.error("Plugin failed: {}", plugin.getClass().getName(), e);
            }
        });

        registerFromConfig();
    }

    private static void registerFromConfig(){
        File dir = Services.PLATFORM.getConfigDir().resolve("resourcestrees/tree_type/").toFile();
        if (!dir.exists() && dir.mkdirs()) {
            Constants.LOG.info("Created /config/resourcestrees/tree_type/ directory");
        }

        try (var paths = Files.walk(dir.toPath())) {
            List<File> files = paths.filter(Files::isRegularFile).filter(path -> path.toString().toLowerCase().endsWith(".json")).map(Path::toFile).toList();

            for (File file : files) {
                InputStreamReader reader = null;
                String name = null;
                TreeType treeType = null;

                try {
                    reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
                    JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                    name = file.getName().replace(".json", "");

                    treeType = TreeType.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();

                    reader.close();
                } catch (Exception e) {
                    Constants.LOG.error("Something is wrong when registering {} tree type.", name, e);
                } finally {
                    IOUtils.closeQuietly(reader);
                }

                if (treeType != null){
                    TreeTypes.TYPES.add(treeType);
                    TreeTypes.TYPE_BY_NAME.put(treeType.name(), treeType);
                }
            }
        } catch (Exception e) {
            Constants.LOG.error("Something is wrong when registering tree type.", e);
        }
    }

    @Override
    public void register(TreeType treeType) {
        TreeTypes.TYPES.add(treeType);
        TreeTypes.TYPE_BY_NAME.put(treeType.name(), treeType);
    }
}

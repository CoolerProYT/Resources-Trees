package com.coolerpromc.resourcestrees.api.grower;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.platform.Services;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.grower.TreeGrower;
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
public class GrowerTypeRegistryImpl implements IGrowerTypeRegistry{
    public static final GrowerTypeRegistryImpl INSTANCE = new GrowerTypeRegistryImpl();

    public static void registerGrowerTypes(){
        Services.PLATFORM.plugins().forEach(plugin -> {
            try{
                plugin.registerGrowerType(INSTANCE);
            }
            catch (Exception e){
                Constants.LOG.error("Plugin failed: {}", plugin.getClass().getName(), e);
            }
        });

        registerFromConfig();

        GrowerTypes.getTypes().forEach(type -> new TreeGrower(type.name(), WeightedList.of(type.trees()), WeightedList.of(type.megaTrees()), WeightedList.of(type.flowerTrees()), type.shortestTreeType().orElse(null)));
    }

    private static void registerFromConfig(){
        File dir = Services.PLATFORM.getConfigDir().resolve("resourcestrees/grower_type/").toFile();
        if (!dir.exists() && dir.mkdirs()) {
            Constants.LOG.info("Created /config/resourcestrees/grower_type/ directory");
        }

        try (var paths = Files.walk(dir.toPath())) {
            List<File> files = paths.filter(Files::isRegularFile).filter(path -> path.toString().toLowerCase().endsWith(".json")).map(Path::toFile).toList();

            for (File file : files) {
                InputStreamReader reader = null;
                String name = null;
                GrowerType growerType = null;

                try {
                    reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
                    JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                    name = file.getName().replace(".json", "");

                    growerType = GrowerType.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();

                    reader.close();
                } catch (Exception e) {
                    Constants.LOG.error("Something is wrong when registering {} tree type.", name, e);
                } finally {
                    IOUtils.closeQuietly(reader);
                }

                if (growerType != null){
                    GrowerTypes.TYPES.add(growerType);
                    GrowerTypes.TYPE_BY_NAME.put(growerType.name(), growerType);
                }
            }
        } catch (Exception e) {
            Constants.LOG.error("Something is wrong when registering tree type.", e);
        }
    }

    @Override
    public void register(GrowerType growerType) {
        GrowerTypes.TYPES.add(growerType);
        GrowerTypes.TYPE_BY_NAME.put(growerType.name(), growerType);
    }
}

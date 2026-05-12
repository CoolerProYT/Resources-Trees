package com.coolerpromc.resourcestrees.config;

import com.coolerpromc.coolerconfig.config.ConfigFormat;
import com.coolerpromc.coolerconfig.config.ConfigSpec;
import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class ResourcesTreesConfig {
    private static final Map<String, Number> DEFAULT_VALUES = Map.of(
            "minecraft:wooden_axe", 1.0,
            "minecraft:stone_axe", 2.0,
            "minecraft:copper_axe", 3.0,
            "minecraft:iron_axe", 4.0,
            "minecraft:golden_axe", 7.0,
            "minecraft:diamond_axe", 5.0,
            "minecraft:netherite_axe", 6.0
    );

    public static final ConfigSpec CONFIG = ConfigSpec.builder(Constants.MODID, ConfigFormat.HOCON)
            .comment("ResourcesTrees Common Config")
            .define("treeSimulator.axe", DEFAULT_VALUES, "The Tree Simulator growth speed scales with the type of axe placed in the axe slot. The key of each entry should be a valid axe item id.", ResourcesTreesConfig::validateAxe)
            .watchForChanges()
            .build();

    private static Map<String, Number> actualValues = DEFAULT_VALUES;

    private static boolean validateAxe(Object o){
        if (o instanceof Map<?,?> map){
            for (Map.Entry<?, ?> entry : map.entrySet()){
                if (entry.getKey() instanceof String s && entry.getValue() instanceof Number){
                    if (!isValidAxeItem(s)){
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static boolean isValidAxeItem(String key) {
        try {
            Identifier id = Identifier.tryParse(key);
            if (id == null) {
                return false;
            }

            Item item = BuiltInRegistries.ITEM.getValue(id);
            return item instanceof AxeItem;
        } catch (Exception e) {
            return false;
        }
    }

    public static double get(String key) {
        if (!isValidAxeItem(key)) {
            System.err.println("Warning: '" + key + "' is not a valid AxeItem");
            return 0.0;
        }
        Number value = actualValues.get(key);
        return value != null ? value.doubleValue() : 0.0;
    }

    @Deprecated(forRemoval = true)
    private static void deleteLegacyConfigFile(){
        Path path = Services.PLATFORM.getConfigDir().resolve("resourcestrees/axe.json");
        try {
            Files.deleteIfExists(path);
        } catch (IOException _) {
        }
    }

    public static void init(){
        deleteLegacyConfigFile();
        CONFIG.addReloadListener(() -> actualValues = CONFIG.getMap("treeSimulator.axe"));
    }
}
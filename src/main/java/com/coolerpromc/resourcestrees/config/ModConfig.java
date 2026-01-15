package com.coolerpromc.resourcestrees.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("resourcestrees/axe.json");

    private Map<String, Double> values = new HashMap<>();

    public ModConfig() {
        setDefaults();
    }

    private void setDefaults() {
        values.put("minecraft:wooden_axe", 1.0);
        values.put("minecraft:stone_axe", 2.0);
        values.put("minecraft:iron_axe", 3.0);
        values.put("minecraft:golden_axe", 6.0);
        values.put("minecraft:diamond_axe", 4.0);
        values.put("minecraft:netherite_axe", 5.0);
    }

    private boolean isValidAxeItem(String key) {
        try {
            ResourceLocation id = ResourceLocation.tryParse(key);
            if (id == null) {
                return false;
            }

            Item item = BuiltInRegistries.ITEM.get(id);
            return item instanceof AxeItem;
        } catch (Exception e) {
            return false;
        }
    }

    public double get(String key) {
        if (!isValidAxeItem(key)) {
            System.err.println("Warning: '" + key + "' is not a valid AxeItem");
            return 0.0;
        }
        return values.getOrDefault(key, 0.0);
    }

    public boolean set(String key, double value) {
        if (!isValidAxeItem(key)) {
            System.err.println("Error: Cannot set '" + key + "' - not a valid AxeItem");
            return false;
        }
        values.put(key, value);
        return true;
    }

    public Map<String, Double> getAll() {
        return new HashMap<>(values);
    }

    public void load() {
        if (!Files.exists(CONFIG_PATH)) {
            save();
            return;
        }

        try {
            String json = Files.readString(CONFIG_PATH);
            ConfigData data = GSON.fromJson(json, ConfigData.class);

            if (data != null && data.values != null) {
                for (Map.Entry<String, Double> entry : data.values.entrySet()) {
                    if (isValidAxeItem(entry.getKey())) {
                        values.put(entry.getKey(), entry.getValue());
                    } else {
                        System.err.println("Skipping invalid axe item in config: " + entry.getKey());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load config: " + e.getMessage());
        }
    }

    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());

            // Filter out any invalid entries before saving
            Map<String, Double> validValues = new HashMap<>();
            for (Map.Entry<String, Double> entry : values.entrySet()) {
                if (isValidAxeItem(entry.getKey())) {
                    validValues.put(entry.getKey(), entry.getValue());
                }
            }

            ConfigData data = new ConfigData();
            data.values = validValues;

            String json = GSON.toJson(data);
            Files.writeString(CONFIG_PATH, json);
        } catch (IOException e) {
            System.err.println("Failed to save config: " + e.getMessage());
        }
    }

    private static class ConfigData {
        Map<String, Double> values;
    }
}
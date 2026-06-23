package com.coolerpromc.resourcestrees.pack;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.api.resources.ResourcesType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.*;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class ResourcesTreesModelPack extends AbstractPackMetadataResources implements PackResources {
    private final Map<Identifier, byte[]> resources = new HashMap<>();
    private final Set<String> namespaces = new HashSet<>();

    public ResourcesTreesModelPack() {
        super(new PackLocationInfo(Constants.MODID + "_models", Component.literal("Resources Trees Pack"), PackSource.BUILT_IN, Optional.empty()));
    }

    public void addLeafFragmentModel(ResourcesType type){
        String modelName = type.name() + "_leaf_fragment";
        String itemsJson = "{\n" +
                "                  \"model\": {\n" +
                "                    \"type\": \"minecraft:model\",\n" +
                "                    \"model\": \"resourcestrees:item/" + modelName +"\",\n" +
                "                    \"tints\": [\n" +
                "                      {\n" +
                "                        \"type\": \"resourcestrees:resources_type_tint\",\n" +
                "                        \"default\": -1\n" +
                "                      }\n" +
                "                    ]\n" +
                "                  }\n" +
                "                }";
        String modelJson = """
                {
                  "parent": "minecraft:item/generated",
                  "textures": {
                    "layer0": "resourcestrees:item/leaf_fragment"
                  }
                }""";
        Identifier itemsFileKey = Constants.id("items/" + modelName + ".json");
        Identifier modelFileKey = Constants.id("models/item/" + modelName + ".json");

        resources.put(itemsFileKey, itemsJson.getBytes(StandardCharsets.UTF_8));
        resources.put(modelFileKey, modelJson.getBytes(StandardCharsets.UTF_8));
        namespaces.add(Constants.MODID);
    }

    public void addLeavesModel(Identifier textureId) {
        String json = "{\"parent\":\"minecraft:block/leaves\",\"textures\":{\"all\":\"" + textureId + "\"}}";
        Identifier fileKey = Identifier.fromNamespaceAndPath(
                textureId.getNamespace(),
                "models/" + textureId.getPath() + ".json"
        );
        resources.put(fileKey, json.getBytes(StandardCharsets.UTF_8));
        namespaces.add(textureId.getNamespace());
    }

    public void addSaplingBlockModel(Identifier textureId) {
        String json = "{\n" +
                "  \"parent\": \"resourcestrees:block/cross_tinted\",\n" +
                "  \"textures\": {\n" +
                "    \"cross\": \"" + textureId + "\",\n" +
                "    \"cross_tinted\": \"" + textureId + "_layer1\"\n" +
                "  }\n" +
                "}";
        Identifier fileKey = Identifier.fromNamespaceAndPath(
                textureId.getNamespace(),
                "models/" + textureId.getPath() + ".json"
        );
        resources.put(fileKey, json.getBytes(StandardCharsets.UTF_8));
        namespaces.add(textureId.getNamespace());
    }

    public void addSaplingItemModel(Identifier textureId) {
        String json = "{\n" +
                "  \"parent\": \"minecraft:item/generated\",\n" +
                "  \"textures\": {\n" +
                "    \"layer0\": \"" + textureId + "\",\n" +
                "    \"layer1\": \"" + textureId + "_layer1\"\n" +
                "  }\n" +
                "}";
        Identifier fileKey = Identifier.fromNamespaceAndPath(
                textureId.getNamespace(),
                "models/" + textureId.withPath(s -> s.replace("block", "item")).getPath() + ".json"
        );
        resources.put(fileKey, json.getBytes(StandardCharsets.UTF_8));
        namespaces.add(textureId.getNamespace());
    }

    @Override
    public IoSupplier<InputStream> getRootResource(String... elements) {
        return null;
    }

    @Override
    public IoSupplier<InputStream> getResource(PackType packType, Identifier location) {
        if (packType != PackType.CLIENT_RESOURCES) return null;
        byte[] data = resources.get(location);
        if (data == null) return null;
        return () -> new ByteArrayInputStream(data);
    }

    @Override
    public void listResources(PackType packType, String namespace, String prefix, PackResources.ResourceOutput resourceOutput) {
        if (packType != PackType.CLIENT_RESOURCES) return;
        String prefixSlash = prefix + "/";
        for (Map.Entry<Identifier, byte[]> entry : resources.entrySet()) {
            Identifier id = entry.getKey();
            if (id.getNamespace().equals(namespace) && id.getPath().startsWith(prefixSlash)) {
                final byte[] data = entry.getValue();
                resourceOutput.accept(id, () -> new ByteArrayInputStream(data));
            }
        }
    }

    @Override
    public Set<String> getNamespaces(PackType type) {
        return namespaces;
    }

    @Override
    public void close() {
    }

    public Component getDescription(){
        return Component.literal("Models for all leaves and saplings");
    }
}
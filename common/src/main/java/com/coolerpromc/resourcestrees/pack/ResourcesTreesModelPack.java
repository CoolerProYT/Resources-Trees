package com.coolerpromc.resourcestrees.pack;

import com.coolerpromc.resourcestrees.Constants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class ResourcesTreesModelPack extends AbstractPackResources {
    private final Map<Identifier, byte[]> resources = new HashMap<>();
    private final Set<String> namespaces = new HashSet<>();

    public ResourcesTreesModelPack() {
        super(new PackLocationInfo(Constants.MODID + "_models", Component.literal("Resources Trees Pack"), PackSource.BUILT_IN, Optional.empty()));
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

    public void addBlockstateEntry(Identifier blockId, Identifier modelId) {
        String json = "{\"variants\":{\"\":{\"model\":\"" + modelId + "\"}}}";
        Identifier fileKey = Identifier.fromNamespaceAndPath(
                blockId.getNamespace(),
                "blockstates/" + blockId.getPath() + ".json"
        );
        resources.put(fileKey, json.getBytes(StandardCharsets.UTF_8));
        namespaces.add(blockId.getNamespace());
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
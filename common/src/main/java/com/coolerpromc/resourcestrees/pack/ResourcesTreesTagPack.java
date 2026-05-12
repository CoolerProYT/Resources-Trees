package com.coolerpromc.resourcestrees.pack;

import com.coolerpromc.resourcestrees.Constants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public final class ResourcesTreesTagPack extends AbstractPackResources {
    private final Map<Identifier, byte[]> resources = new HashMap<>();
    private final Set<String> namespaces = new HashSet<>();

    public ResourcesTreesTagPack() {
        super(new PackLocationInfo(Constants.MODID + "_tags", Component.literal("Resources Trees Tags Pack"), PackSource.BUILT_IN, Optional.empty()));
    }

    public void addItemTag(Identifier tagId, List<Identifier> items) {
        var packPath = Identifier.fromNamespaceAndPath(
                tagId.getNamespace(),
                "tags/item/" + tagId.getPath() + ".json"
        );
        var json = buildTagJson(items);
        resources.put(packPath, json.getBytes(StandardCharsets.UTF_8));
        namespaces.add(tagId.getNamespace());
    }

    public void addBlockTag(Identifier tagId, List<Identifier> blocks) {
        var packPath = Identifier.fromNamespaceAndPath(
                tagId.getNamespace(),
                "tags/block/" + tagId.getPath() + ".json"
        );
        resources.put(packPath, buildTagJson(blocks).getBytes(StandardCharsets.UTF_8));
        namespaces.add(tagId.getNamespace());
    }

    private String buildTagJson(List<Identifier> entries) {
        var values = entries.stream()
                .map(id -> "\"" + id + "\"")
                .collect(Collectors.joining(", "));
        return "{\"replace\":false,\"values\":[" + values + "]}";
    }

    @Override
    public IoSupplier<InputStream> getRootResource(String... elements) {
        return null;
    }

    @Override
    public IoSupplier<InputStream> getResource(PackType packType, Identifier location) {
        if (packType != PackType.SERVER_DATA) return null;
        byte[] data = resources.get(location);
        if (data == null) return null;
        return () -> new ByteArrayInputStream(data);
    }

    @Override
    public void listResources(PackType packType, String namespace, String prefix, ResourceOutput resourceOutput) {
        if (packType != PackType.SERVER_DATA) return;
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
        return Component.literal("Tags for all spalings, leaves and leaf fragments");
    }
}
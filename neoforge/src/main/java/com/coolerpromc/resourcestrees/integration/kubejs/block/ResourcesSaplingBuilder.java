package com.coolerpromc.resourcestrees.integration.kubejs.block;

import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.util.ID;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.HashMap;
import java.util.Map;

public class ResourcesSaplingBuilder extends BlockBuilder {
    public static final Map<Identifier, String> SAPLINGS = new HashMap<>();

    private Identifier leaves;
    private TreeGrower grower;
    private String base;

    public ResourcesSaplingBuilder(Identifier id) {
        super(id);
    }

    @Override
    public Block createObject() {
        return new ResourcesSaplingBlock(grower, createProperties().instabreak(), leaves);
    }

    /**
     *
     * @param leavesId ResourcesLeaves block id
     * @return ResourcesSaplingBuilder
     */
    public ResourcesSaplingBuilder leaves(String leavesId) {
        this.leaves = Identifier.parse(leavesId);
        return this;
    }

    /**
     *
     * @param type Name of the TreeGrower
     * @return ResourcesSaplingBuilder
     */
    public ResourcesSaplingBuilder treeGrower(String type) {
        this.grower = TreeGrower.GROWERS.get(type);
        if (grower == null) {
            throw new IllegalArgumentException("Unknown tree grower: " + type);
        }
        return this;
    }

    /**
     *
     * @param base Base sapling item id
     * @return ResourcesSaplingBuilder
     */
    public ResourcesSaplingBuilder base(String base) {
        this.base = base;
        return this;
    }

    @Override
    protected ItemBuilder getOrCreateItemBuilder() {
        return new ModBlockItemBuilder(this, id);
    }

    @Override
    public void generateData(KubeDataGenerator generator) {
        super.generateData(generator);
        SAPLINGS.put(this.id, base);
    }

    @Override
    public void generateAssets(KubeAssetGenerator generator) {
        if (this.useMultipartBlockState()) {
            generator.multipartState(this.id, this::generateMultipartBlockState);
        } else {
            generator.blockState(this.id, this::generateBlockState);
        }

        this.generateBlockModels(generator);
        if (this.itemBuilder != null) {
            ModelGenerator gen = Util.make(new ModelGenerator(), this::generateItemModel);
            generator.json(id.withPath(ID.ITEM_MODEL), gen.toJson());
            JsonObject modelRef = new JsonObject();
            modelRef.addProperty("type", "minecraft:model");
            modelRef.addProperty("model", id.withPath(ID.ITEM).toString());
            JsonArray tints = new JsonArray();
            JsonObject layer1 = new JsonObject();
            layer1.addProperty("type", "minecraft:constant");
            layer1.addProperty("value", -1);
            tints.add(layer1);
            JsonObject layer2 = new JsonObject();
            layer2.addProperty("type", "resourcestrees:resources_type_tint");
            layer2.addProperty("default", -12012264);
            tints.add(layer2);
            modelRef.add("tints", tints);
            JsonObject def = new JsonObject();
            def.add("model", modelRef);
            generator.json(id.withPath(ID.ITEM_DEFINITION), def);
        }
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        generator.blockModel(id, m -> {
            m.parent(Identifier.parse("resourcestrees:block/cross_tinted"));
            var texture = Identifier.fromNamespaceAndPath(id.getNamespace(), "block/" + id.getPath()).toString();
            m.texture("cross", texture);
            m.texture("cross_tinted", texture + "_layer1");
        });
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        m.parent(Identifier.parse("minecraft:item/generated"));
        var texture = Identifier.fromNamespaceAndPath(id.getNamespace(), "block/" + id.getPath()).toString();
        m.texture("layer0", texture);
        m.texture("layer1", texture + "_layer1");
    }
}
package com.coolerpromc.resourcestrees.integration.kubejs.block;

import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.util.ID;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class ResourcesLeavesBuilder extends BlockBuilder {
    private Supplier<ResourcesSaplingBlock> sapling;

    public ResourcesLeavesBuilder(Identifier id) {
        super(id);
    }

    @Override
    public Block createObject() {
        return new ResourcesLeavesBlock(0.01f, createProperties().strength(0.2f), sapling);
    }

    /**
     *
     * @param saplingId ResourcesSapling block id
     * @return ResourcesSaplingBuilder
     */
    public ResourcesLeavesBuilder sapling(String saplingId) {
        this.sapling = () -> (ResourcesSaplingBlock) BuiltInRegistries.BLOCK.getValue(Identifier.parse(saplingId));
        return this;
    }

    @Override
    protected ItemBuilder getOrCreateItemBuilder() {
        return new ModBlockItemBuilder(this, id);
    }

    @Override
    public void generateData(KubeDataGenerator generator) {
        var json = new JsonObject();
        json.addProperty("type", "minecraft:block");

        var pool = new JsonObject();
        pool.addProperty("bonus_rolls", 0.0);
        pool.addProperty("rolls", 1.0);

        var anyOf = new JsonObject();
        anyOf.addProperty("condition", "minecraft:any_of");
        var terms = new JsonArray();

        var shearsTerm = new JsonObject();
        shearsTerm.addProperty("condition", "minecraft:match_tool");
        var shearsPredicate = new JsonObject();
        shearsPredicate.addProperty("items", "minecraft:shears");
        shearsTerm.add("predicate", shearsPredicate);
        terms.add(shearsTerm);

        var silkTerm = new JsonObject();
        silkTerm.addProperty("condition", "minecraft:match_tool");
        var silkPredicate = new JsonObject();
        var silkPredicates = new JsonObject();
        var enchantments = new JsonArray();
        var enchEntry = new JsonObject();
        enchEntry.addProperty("enchantments", "minecraft:silk_touch");
        var levels = new JsonObject();
        levels.addProperty("min", 1);
        enchEntry.add("levels", levels);
        enchantments.add(enchEntry);
        silkPredicates.add("minecraft:enchantments", enchantments);
        silkPredicate.add("predicates", silkPredicates);
        silkTerm.add("predicate", silkPredicate);
        terms.add(silkTerm);

        anyOf.add("terms", terms);
        var conditions = new JsonArray();
        conditions.add(anyOf);
        pool.add("conditions", conditions);

        // entry — current block (sapling itself)
        var entry = new JsonObject();
        entry.addProperty("type", "minecraft:item");
        entry.addProperty("name", id.toString());
        var entries = new JsonArray();
        entries.add(entry);
        pool.add("entries", entries);

        var pools = new JsonArray();
        pools.add(pool);
        json.add("pools", pools);

        json.addProperty("random_sequence", id.toString());

        generator.json(id.withPath(ID.BLOCK_LOOT_TABLE), json);
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
            layer1.addProperty("type", "resourcestrees:resources_type_tint");
            layer1.addProperty("default", -12012264);
            tints.add(layer1);
            modelRef.add("tints", tints);
            JsonObject def = new JsonObject();
            def.add("model", modelRef);
            generator.json(id.withPath(ID.ITEM_DEFINITION), def);
        }
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        generator.blockModel(id, m -> {
            m.parent(Identifier.parse("minecraft:block/leaves"));
            var texture = Identifier.fromNamespaceAndPath(id.getNamespace(), "block/" + id.getPath());
            m.texture("all", texture.toString());
        });
    }
}
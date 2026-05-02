package com.coolerpromc.resourcestrees.integration.kubejs;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.integration.kubejs.block.ResourcesLeavesBuilder;
import com.coolerpromc.resourcestrees.integration.kubejs.block.ResourcesSaplingBuilder;
import com.coolerpromc.resourcestrees.registry.ModRegistries;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.RecipesKubeEvent;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.ContextFactory;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;

import java.util.Map;

public class ModKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerBuilderTypes(BuilderTypeRegistry registry) {
        registry.of(Registries.BLOCK, callback -> callback.add(Constants.id("resources_sapling_block"), ResourcesSaplingBuilder.class, ResourcesSaplingBuilder::new));
        registry.of(Registries.BLOCK, callback -> callback.add(Constants.id("resources_leaves_block"), ResourcesLeavesBuilder.class, ResourcesLeavesBuilder::new));
    }

    @Override
    public void beforeRecipeLoading(RecipesKubeEvent event, Map<Identifier, JsonElement> recipeJsons) {
        ResourcesSaplingBuilder.SAPLINGS.forEach((saplingId, saplingBlock) -> {
            JsonObject json = new JsonObject();
            json.addProperty("type", "resourcestrees:resources_sapling");

            JsonObject baseSapling = new JsonObject();
            baseSapling.addProperty("id", saplingBlock);
            json.add("baseSapling", baseSapling);

            JsonObject resourcesSapling = new JsonObject();
            resourcesSapling.addProperty("id", saplingId.toString());
            json.add("resourcesSapling", resourcesSapling);

            KubeRecipe kubeRecipe = event.custom(new Context(new ContextFactory()), json);
            kubeRecipe.id = saplingId.withPath(s -> "recipes/saplings/" + s);
        });
    }
}

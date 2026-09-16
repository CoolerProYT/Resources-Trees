# Leaf Fragment Recipes

Fragment recipes turn leaf fragments into resources. The built-in ones use `resourcestrees:strict_shaped`, a shaped recipe that also checks each ingredient's resource type. Types added by a datapack have none until the pack adds them.

## Generator

Load a built-in recipe to change it, or clear the grid and draw your own. For a datapack resource type, fill in **Custom resource type** and use its fragment as an ingredient. Pick your Minecraft version and loader at the bottom of the form.

<CraftingRecipeBuilder mode="fragment" />

## File location

```
data/<namespace>/recipe/<fragment>_to_<result>.json
```

The built-in recipes live in `data/resourcestrees/recipe/fragment_crafting/`, named like `amethyst_fragment_to_amethyst_shard.json`; a file with the same name replaces one. 1.20.1 uses a `recipes` folder instead of `recipe`.

## Ingredient format

A fragment of a given type needs the mod's `resourcestrees:resources_type` ingredient, written differently per loader:

::: code-group

```json [1.21.2+ NeoForge]
{
  "neoforge:ingredient_type": "resourcestrees:resources_type",
  "base": "resourcestrees:leaf_fragment",
  "components": { "resourcestrees:resources_type": "resourcestrees:amethyst" }
}
```

```json [1.21.2+ Fabric]
{
  "fabric:type": "resourcestrees:resources_type",
  "base": "resourcestrees:leaf_fragment",
  "components": { "resourcestrees:resources_type": "resourcestrees:amethyst" }
}
```

```json [1.21.x Forge]
{
  "type": "resourcestrees:resources_type",
  "components": { "resourcestrees:resources_type": "resourcestrees:amethyst" },
  "items": "resourcestrees:leaf_fragment",
  "strict": true
}
```

```json [1.21.1 NeoForge]
{
  "type": "resourcestrees:resources_type",
  "base": "resourcestrees:leaf_fragment",
  "components": { "resourcestrees:resources_type": "resourcestrees:amethyst" }
}
```

```json [1.21.1 Fabric]
{
  "fabric:type": "resourcestrees:resources_type",
  "base": { "item": "resourcestrees:leaf_fragment" },
  "components": { "resourcestrees:resources_type": "resourcestrees:amethyst" }
}
```

```json [1.20.1 Forge]
{
  "type": "forge:partial_nbt",
  "item": "resourcestrees:leaf_fragment",
  "nbt": "{type:\"resourcestrees:amethyst\"}"
}
```

:::

Every built-in recipe is listed on [Leaf Fragments](../gameplay/leaf-fragments#all-recipes).

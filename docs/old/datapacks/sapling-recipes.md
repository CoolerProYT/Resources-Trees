# Sapling Recipes

The built-in resource types ship with a crafting recipe for every tree shape. Types added by a datapack get **none**, so the pack has to add them.

## The built-in recipe

The resource type's material (`A`) around the vanilla sapling (`B`), for one resource sapling:

<RecipeCard id="saplings/iron_birch_sapling" />

## Generator

Pick a sapling, then paint a shape. Choose an ingredient and click grid cells to place it; click a cell again to clear it. For a datapack resource type, fill in **Custom resource type**; the result then carries that type. Pick your Minecraft version and loader at the bottom of the form.

<CraftingRecipeBuilder mode="sapling" />

## File location

```
data/resourcestrees/recipe/saplings/<type>_<tree>_sapling.json
```

1.20.1 uses a `recipes` folder instead of `recipe`. A file with the same name as a built-in one replaces it; for your own types, any name works.

## Format

A normal shaped recipe whose result carries the resource type:

::: code-group

```json [1.21.2+]
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "key": {
    "A": "minecraft:diamond_block",
    "B": "minecraft:oak_sapling"
  },
  "pattern": [" A ", "ABA", " A "],
  "result": {
    "components": {
      "resourcestrees:resources_type": "resourcestrees:diamond"
    },
    "count": 1,
    "id": "resourcestrees:resources_oak_sapling"
  }
}
```

```json [1.21.1]
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "key": {
    "A": { "item": "minecraft:diamond_block" },
    "B": { "item": "minecraft:oak_sapling" }
  },
  "pattern": [" A ", "ABA", " A "],
  "result": {
    "components": {
      "resourcestrees:resources_type": "resourcestrees:diamond"
    },
    "count": 1,
    "id": "resourcestrees:resources_oak_sapling"
  }
}
```

```json [1.20.1]
{
  "type": "minecraft:crafting_shaped",
  "category": "building",
  "key": {
    "A": { "item": "minecraft:diamond_block" },
    "B": { "item": "minecraft:oak_sapling" }
  },
  "pattern": [" A ", "ABA", " A "],
  "result": {
    "count": 1,
    "item": "resourcestrees:resources_oak_sapling",
    "nbt": "{type:\"resourcestrees:diamond\"}"
  },
  "show_notification": true
}
```

:::

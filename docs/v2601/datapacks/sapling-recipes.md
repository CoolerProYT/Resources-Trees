# Sapling Recipes

The mod generates a crafting recipe for every sapling, so custom resource types and tree types get one automatically. A datapack can replace any of them.

## The generated recipe

The resource type's material (`M`) around the tree type's vanilla sapling (`S`), for one resource sapling:

```
 M
MSM
 M
```

<RecipeCard id="saplings/iron_birch_sapling" />

Generated sapling recipes show up in the **Misc** tab of the recipe book.

## Generator

Pick a sapling, then paint a new shape. Choose an ingredient on the left and click grid cells to place it; click a cell again to clear it.

<CraftingRecipeBuilder mode="sapling" />

## Replacing a recipe

Put a recipe at this path, named after the sapling:

```
data/resourcestrees/recipe/saplings/<sapling_name>.json
```

When a recipe with that id exists, the mod doesn't generate its own. For example, to make `diamond_oak_sapling` cost diamonds instead of diamond blocks:

`data/resourcestrees/recipe/saplings/diamond_oak_sapling.json`

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "misc",
  "pattern": [
    "DDD",
    "DSD",
    "DDD"
  ],
  "key": {
    "D": "minecraft:diamond",
    "S": "minecraft:oak_sapling"
  },
  "result": {
    "id": "resourcestrees:diamond_oak_sapling"
  }
}
```

<RecipeCard :recipe="{ id: 'example', type: 'minecraft:crafting_shaped', result: { id: 'resourcestrees:diamond_oak_sapling', count: 1 }, pattern: ['DDD', 'DSD', 'DDD'], key: { D: ['minecraft:diamond'], S: ['minecraft:oak_sapling'] } }" />

Any recipe type works, as long as the id matches, so the replacement can also be shapeless or come from another mod.

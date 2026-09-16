# Sapling Types

These versions have a fixed set of tree shapes. Each has one sapling and one leaves item, shared by every resource type; the type is stored on the item.

<TreeTypeTable />

::: info Version differences
**1.20.1** and **1.21.1** have no Pale Oak.
:::

## Setting the type on an item

Anywhere a recipe lists one of these items, give it the resource type:

| Version | How |
| --- | --- |
| 1.21.1 and later | Data component `resourcestrees:resources_type`, e.g. `"resourcestrees:resources_type": "resourcestrees:diamond"` |
| 1.20.1 | NBT tag `type`, e.g. `"nbt": "{type:\"resourcestrees:diamond\"}"` |

Recipe ingredients need the mod's own ingredient type to match on it; the exact JSON differs per loader. The generators on [Sapling Recipes](./sapling-recipes), [Tree Simulator Recipes](./tree-simulator-recipes) and [Leaf Fragment Recipes](./leaf-fragment-recipes) write it for you.

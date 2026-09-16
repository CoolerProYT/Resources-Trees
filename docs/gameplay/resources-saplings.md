# Resources Saplings

Resources saplings grow into resource trees. Every combination of a **resource type** and a **tree type** is its own sapling.

<p>
  <ItemSlot id="resourcestrees:diamond_oak_sapling" />
  <ItemSlot id="resourcestrees:diamond_spruce_sapling" />
  <ItemSlot id="resourcestrees:diamond_birch_sapling" />
  <ItemSlot id="resourcestrees:diamond_jungle_sapling" />
  <ItemSlot id="resourcestrees:diamond_acacia_sapling" />
  <ItemSlot id="resourcestrees:diamond_dark_oak_sapling" />
  <ItemSlot id="resourcestrees:diamond_cherry_sapling" />
  <ItemSlot id="resourcestrees:diamond_pale_oak_sapling" />
  <ItemSlot id="resourcestrees:diamond_poplar_sapling" />
</p>

## Naming

Sapling ids follow the pattern:

```
resourcestrees:<resource_type>_<tree_type>_sapling
```

For example `resourcestrees:iron_oak_sapling`, `resourcestrees:diamond_birch_sapling` or `resourcestrees:netherite_dark_oak_sapling`.

## Crafting

Surround the vanilla sapling of the tree type with the resource type's material in a `+` shape. The recipe is generated for every sapling.

<RecipeCard id="saplings/diamond_oak_sapling" />
<RecipeCard id="saplings/fire_cherry_sapling" />

Resource types whose material is an item tag accept any item in it. Wood trees take any log:

<RecipeCard id="saplings/wood_spruce_sapling" />

::: tip Changing a recipe
A datapack can replace the recipe of any single sapling. See [Sapling Recipes](../datapacks/sapling-recipes).
:::

## Growing

Plant a resources sapling on grass or dirt like a vanilla sapling. It grows with the same tree shapes as its tree type, including big 2×2 trees for tree types that have them, and bone meal works as usual. The leaves it grows are [resources leaves](./resources-leaves) of the same resource type.

## Getting more

Breaking or decaying the leaves of a resource tree can drop its sapling. The chance is set by the resource type:

| Resource type | Sapling drop chance |
| --- | --- |
| Most types | 12.5% |
| <ItemSlot id="resourcestrees:diamond_oak_sapling" /> Diamond | 10% |
| <ItemSlot id="resourcestrees:netherite_oak_sapling" /> Netherite | 7.5% |

The [Tree Simulator](./tree-simulator) can drop the sapling too, with the same chance per harvest.

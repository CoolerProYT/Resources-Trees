# Resources Saplings

Resources saplings grow into resource trees. Each tree shape has one sapling item, `resourcestrees:resources_<tree>_sapling`, and the resource type is stored on it.

<p>
  <ItemSlot id="resourcestrees:diamond_oak_sapling" />
  <ItemSlot id="resourcestrees:diamond_spruce_sapling" />
  <ItemSlot id="resourcestrees:diamond_birch_sapling" />
  <ItemSlot id="resourcestrees:diamond_jungle_sapling" />
  <ItemSlot id="resourcestrees:diamond_acacia_sapling" />
  <ItemSlot id="resourcestrees:diamond_dark_oak_sapling" />
  <ItemSlot id="resourcestrees:diamond_cherry_sapling" />
  <ItemSlot id="resourcestrees:diamond_pale_oak_sapling" />
</p>

## Crafting

Every built-in resource type has a recipe for every tree shape: the vanilla sapling surrounded by the resource's material.

<RecipeCard id="saplings/diamond_oak_sapling" />
<RecipeCard id="saplings/fire_cherry_sapling" />

Wood trees take any log:

<RecipeCard id="saplings/wood_spruce_sapling" />

::: warning Custom resource types
Recipes are **not** generated for resource types added by a datapack. The pack has to add them. See [Sapling Recipes](../datapacks/sapling-recipes).
:::

## Growing

Plant a resources sapling on grass or dirt like a vanilla sapling. It grows with the same tree shapes as its vanilla sapling, including 2×2 trees, and the leaves it grows carry the same resource type.

## Getting more

Breaking or decaying the leaves of a resource tree can drop its sapling. See [Resources Leaves](./resources-leaves#drops). The [Tree Simulator](./tree-simulator) can drop it too.

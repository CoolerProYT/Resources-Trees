# Leaf Fragments

Leaf fragments are what resource trees are for. Break resources leaves or run a [Tree Simulator](./tree-simulator) to collect them, then craft them into the resource.

<p>
  <ItemSlot id="resourcestrees:stone_leaf_fragment" />
  <ItemSlot id="resourcestrees:coal_leaf_fragment" />
  <ItemSlot id="resourcestrees:iron_leaf_fragment" />
  <ItemSlot id="resourcestrees:copper_leaf_fragment" />
  <ItemSlot id="resourcestrees:gold_leaf_fragment" />
  <ItemSlot id="resourcestrees:lapis_leaf_fragment" />
  <ItemSlot id="resourcestrees:redstone_leaf_fragment" />
  <ItemSlot id="resourcestrees:emerald_leaf_fragment" />
  <ItemSlot id="resourcestrees:diamond_leaf_fragment" />
  <ItemSlot id="resourcestrees:netherite_leaf_fragment" />
</p>

Every resource type has one fragment, `resourcestrees:<resource_type>_leaf_fragment`, shared by all of its tree shapes.

## Getting fragments

- **Resources leaves** roll a fragment at `leafDropChance` (25% by default) and a bonus fragment at half that. See [Resources Leaves](./resources-leaves#drops).
- **The Tree Simulator** always gives one fragment per harvest, plus up to four bonus rolls.

## Crafting

Fragment recipes need the **exact shape** shown: every gap in the pattern must stay empty, and unlike vanilla shaped recipes they can't be mirrored. Smaller patterns can still go anywhere in the grid. Some recipes mix in a second fragment, such as coal for stone and deepslate.

<RecipeCard id="fragment_crafting/stone_leaf_fragment_to_stone" />
<RecipeCard id="fragment_crafting/diamond_leaf_fragment_to_diamond" />

## All recipes

Click a fragment to see what it makes. Search by fragment or by what you want to craft.

<FragmentRecipes />

::: tip
With JEI installed, you can also look up any of these in game. A datapack can add more fragment recipes. See [Leaf Fragment Recipes](../datapacks/leaf-fragment-recipes).
:::

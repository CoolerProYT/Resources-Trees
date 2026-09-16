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

There is one fragment item, `resourcestrees:leaf_fragment`, and the resource type is stored on it.

## Getting fragments

- **Resources leaves** roll a fragment at `leafDropChance` (25% by default) and a bonus fragment at half that. See [Resources Leaves](./resources-leaves#drops).
- **The Tree Simulator** always gives one fragment per harvest, plus bonus rolls.

## Crafting

Fragment recipes use `resourcestrees:strict_shaped`, so the fragment's resource type has to match exactly. Some recipes mix in a second fragment, such as coal for stone and deepslate.

<RecipeCard id="fragment_crafting/stone_fragment_to_stone" />
<RecipeCard id="fragment_crafting/diamond_fragment_to_diamond" />

## All recipes

Click a fragment to see what it makes. Search by fragment or by what you want to craft.

<FragmentRecipes />

::: info Version differences
1.20.1 has no Breeze fragment recipes. Fragments of resource types added by a datapack have no recipes until the pack adds them. See [Leaf Fragment Recipes](../datapacks/leaf-fragment-recipes).
:::

# Getting Started

::: info Minecraft 26.1 – 26.2
These pages are for Resources Trees 26.1.2.100 and later on Minecraft 26.1 and 26.2. Playing on 26.3? See the [current docs](/gameplay/getting-started), which add custom grower types, leaf particle and sound settings, and the Poplar tree. Coming from an older version? See [Updating Old Versions](./updating).
:::

Resources Trees adds trees that grow resources. Each tree combines a **resource type** (what it drops) with a **tree type** (what shape it grows as), so an *Iron Oak Sapling* grows into an oak whose leaves drop iron.

<p>
  <ItemSlot id="resourcestrees:iron_oak_sapling" />
  <ItemSlot id="resourcestrees:diamond_birch_sapling" />
  <ItemSlot id="resourcestrees:gold_cherry_sapling" />
  <ItemSlot id="resourcestrees:emerald_jungle_sapling" />
  <ItemSlot id="resourcestrees:redstone_dark_oak_sapling" />
  <ItemSlot id="resourcestrees:lapis_spruce_sapling" />
  <ItemSlot id="resourcestrees:amethyst_pale_oak_sapling" />
  <ItemSlot id="resourcestrees:copper_acacia_sapling" />
</p>

## Install

1. Install [Fabric](https://fabricmc.net/) with Fabric API, or [NeoForge](https://neoforged.net/), for Minecraft **26.1** or **26.2**.
2. Put the Resources Trees jar for your loader in your `mods` folder. Its config library is bundled inside.
3. Optional: add [JEI](https://modrinth.com/mod/jei) to browse every sapling and Tree Simulator recipe in game.

## Your first resource tree

**1. Craft a resource sapling.** Surround a vanilla sapling with the resource's material. Every combination has a recipe.

<RecipeCard id="saplings/iron_oak_sapling" />

**2. Plant it** on grass or dirt like any sapling. It grows into the same shape as the vanilla tree, with tinted leaves.

**3. Break the leaves.** They drop [leaf fragments](./leaf-fragments), and sometimes another sapling.

<p><ItemSlot id="resourcestrees:iron_oak_leaves" label /> ➜ <ItemSlot id="resourcestrees:iron_leaf_fragment" label /></p>

**4. Craft the fragments** back into the resource.

<RecipeCard id="fragment_crafting/iron_leaf_fragment_to_iron_ingot" />

## Automate it

Once you have a sapling to spare, put it in a **[Tree Simulator](./tree-simulator)** with an axe. It "grows" and harvests the tree inside the block, over and over, without touching the world.

<RecipeCard id="tree_simulator" />

<TreeSimulatorGui resource="iron" tree="oak" axe="minecraft:iron_axe" />

## Where to go next

- [Sapling explorer](./explorer): pick any sapling and see its recipe, drops and simulator output
- [Resources types](./resources-types-list): every resource type and tree type in the mod
- [Essence items](./essence-items): the crafted materials for mob and elemental trees
- [Custom resources types](../datapacks/resources-types): add your own trees with a JSON file

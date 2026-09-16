# Getting Started

::: info Older versions
These pages cover Resources Trees before 26.1.2.100: Minecraft **1.20.1** (Forge) and **1.21.1 – 1.21.11** (NeoForge, Fabric and Forge). The data shown comes from the last of these releases, for 1.21.11. Where older releases differ, the page says so.

On Minecraft 26.1 or later, see the [26.3 docs](/gameplay/getting-started) or the [26.1 – 26.2 docs](/v2601/gameplay/getting-started).
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

In these versions every tree shape has **one** sapling, one leaves block and one leaf fragment item. The resource type is stored on the item as a data component (NBT on 1.20.1), so an Iron Oak Sapling is `resourcestrees:resources_oak_sapling` with the type `resourcestrees:iron`.

## Your first resource tree

**1. Craft a resource sapling.** Surround a vanilla sapling with the resource's material in a `+` shape. Every built-in type has this recipe.

<RecipeCard id="saplings/iron_oak_sapling" />

**2. Plant it** on grass or dirt like any sapling. It grows into the same shape as the vanilla tree, with tinted leaves.

**3. Break the leaves.** They drop [leaf fragments](./leaf-fragments), and sometimes another sapling.

<p><ItemSlot id="resourcestrees:iron_oak_leaves" label /> ➜ <ItemSlot id="resourcestrees:iron_leaf_fragment" label /></p>

**4. Craft the fragments** back into the resource.

<RecipeCard id="fragment_crafting/iron_fragment_to_iron_ingot" />

## Automate it

Put a sapling in a **[Tree Simulator](./tree-simulator)** with an axe, and it harvests the tree inside the block over and over.

<RecipeCard id="tree_simulator" />

<TreeSimulatorGui resource="iron" tree="oak" axe="minecraft:iron_axe" />

## Where to go next

- [Sapling explorer](./explorer): pick any sapling and see its recipe, drops and simulator output
- [Resources types](./resources-types-list): every resource type and tree shape
- [Custom resources types](../datapacks/resources-types): add your own with a datapack

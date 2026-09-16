# Tree Simulator

The Tree Simulator grows and harvests a resource tree inside a single block. Put a sapling and an axe in it, and it produces logs, leaf fragments, saplings and a little extra on a timer.

## Crafting

<RecipeCard id="tree_simulator" />

## The menu

<TreeSimulatorGui />

## How it works

1. Put a resources sapling in the sapling slot. It is never used up.
2. Put an axe in the axe slot. **Nothing happens without a usable axe.**
3. The progress arrow fills over the sapling's grow time, divided by the axe's multiplier.
4. When it is full, the simulator rolls the sapling's drops into the output slots and the axe takes 1 damage.
5. The cycle starts again.

If the output slots can't fit every drop of the next harvest, the simulator waits until you empty them. A broken axe is removed.

## Built-in drops per harvest

The built-in saplings ship with these drops:

| Drop | Chance per roll | Rolls |
| --- | --- | --- |
| <ItemSlot id="minecraft:oak_log" /> Log of the tree type | 50% | 1–4 |
| <ItemSlot id="resourcestrees:iron_leaf_fragment" /> Leaf fragment | 100% | 1 |
| <ItemSlot id="resourcestrees:iron_leaf_fragment" /> Bonus leaf fragment | `leafDropChance` | 1–4 |
| <ItemSlot id="resourcestrees:iron_oak_sapling" /> The same sapling | `saplingDropChance` | 1 |
| <ItemSlot id="minecraft:stick" /> Stick | 10% | 1–2 |
| <ItemSlot id="minecraft:apple" /> Apple | 5% | 1 |

::: warning Custom resource types
JEI and REI show these drops for every resource type, but a type added by a datapack has **no** real Tree Simulator recipe until the pack adds one. See [Tree Simulator Recipes](../datapacks/tree-simulator-recipes).
:::

The [Sapling Explorer](./explorer) works these out for any sapling and axe.

## Axes

Each harvest takes the sapling's grow time divided by the axe's multiplier.

<AxeTable />

Any axe item works, including axes from other mods. An axe that isn't in the [config](../config/axe-config) works at the base grow time. Axes with the `unbreakable` component never wear out.

::: info Version differences
- **1.21.1 NeoForge 1.8.0** adds Fortune (one extra roll per level on every drop), makes Unbreaking count, and adds the `useAxeDurability` option.
- Other releases ignore enchantments on the axe.
:::

## Automation

| Side | Slot |
| --- | --- |
| Top and sides | Sapling |
| Bottom | Output |

The axe slot can't be filled or emptied by hoppers.

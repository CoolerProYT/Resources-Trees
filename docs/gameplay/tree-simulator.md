# Tree Simulator

The Tree Simulator grows and harvests a resource tree inside a single block. Put a sapling and an axe in it, and it produces logs, leaf fragments and saplings on a timer, without growing a tree in the world.

## Crafting

<RecipeCard id="tree_simulator" />

## The menu

<TreeSimulatorGui />

## How it works

1. Put a resources sapling in the sapling slot. It is never used up.
2. Put an axe in the axe slot. **Nothing happens without an axe.**
3. The progress arrow fills over the sapling's grow time, shortened by the axe.
4. When it is full, the simulator rolls the sapling's drops into the output slots and the axe loses 1 durability.
5. The cycle starts again.

If the output slots can't fit every drop of the next harvest, the simulator waits until you empty them.

## Default drops per harvest

Every sapling gets these drops unless a datapack [replaces its recipe](../datapacks/tree-simulator-recipes):

| Drop | Chance per roll | Rolls |
| --- | --- | --- |
| <ItemSlot id="minecraft:oak_log" /> Log of the tree type | 30% | 1 |
| <ItemSlot id="resourcestrees:iron_leaf_fragment" /> Leaf fragment | 100% | 1 |
| <ItemSlot id="resourcestrees:iron_leaf_fragment" /> Bonus leaf fragment | `leafDropChance × 0.5` | 1–4 |
| <ItemSlot id="resourcestrees:iron_oak_sapling" /> The same sapling | `saplingDropChance` | 1 |

The [Sapling Explorer](./explorer) works these out for any sapling and axe, including averages per hour.

## Axes

The axe sets the speed. Each harvest takes the sapling's grow time divided by the axe's multiplier.

<AxeTable />

Any item that can strip logs counts as an axe, including axes from other mods. An axe that isn't listed in the [config](../config/axe-config) works at the base grow time.

### Enchantments

| Enchantment | Effect |
| --- | --- |
| **Efficiency** | Each level shortens a harvest: the time is divided by 1 + 0.2 × level. Efficiency V halves it. |
| **Fortune** | Each level adds one roll to **every** drop. |
| **Unbreaking** | Makes the axe last longer, as usual. |

Axes with the `unbreakable` component never lose durability. You can also turn axe damage off completely with `useAxeDurability` in the [config](../config/axe-config).

## Automation

| Side | Slot |
| --- | --- |
| Top | Sapling |
| Sides | Axe (only accepts axes) |
| Bottom | Output |

A hopper above can insert a sapling, a hopper on the side can keep it stocked with axes, and a hopper below collects the output.

## Breaking the simulator

Breaking the block drops the simulator along with its sapling, axe and everything in the output slots.

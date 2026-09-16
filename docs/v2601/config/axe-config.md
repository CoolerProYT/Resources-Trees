# Common Config

The common config controls the [Tree Simulator](../gameplay/tree-simulator): how fast each axe makes it run, and whether it wears axes down.

## File location

```
config/resourcestrees-common.conf
```

The file is created with the default values on first launch. It is watched for changes, so edits apply without restarting.

## Generator

Change multipliers, add modded axes, and copy the whole file.

<AxeConfigBuilder />

## Format (HOCON)

```txt
# ResourcesTrees Common Config
treeSimulator {
  # The Tree Simulator growth speed scales with the type of axe placed in the axe slot.
  # The key of each entry should be a valid axe item id.
  axe {
    "minecraft:wooden_axe": 1
    "minecraft:stone_axe": 2
    "minecraft:copper_axe": 3
    "minecraft:iron_axe": 4
    "minecraft:diamond_axe": 5
    "minecraft:netherite_axe": 6
    "minecraft:golden_axe": 7
  }
  # Whether the Tree Simulator damages the axe placed in the axe slot on every harvest.
  useAxeDurability = true
}
```

## `treeSimulator.axe`

Maps an axe's item id to its speed multiplier. Each harvest takes:

```
harvest time = grow time ÷ multiplier
```

Default multipliers and harvest times:

<AxeTable />

- An axe that is **not listed** still works, at the base grow time (as if its multiplier were 1).
- Decimal multipliers such as `2.5` are allowed.
- Efficiency on the axe shortens the time further. See [Enchantments](../gameplay/tree-simulator#enchantments).

### Adding modded axes

Add any axe from another mod by its item id:

```txt
treeSimulator {
  axe {
    "minecraft:wooden_axe": 1
    "minecraft:iron_axe": 4
    "mymod:titanium_axe": 8
  }
}
```

::: warning
Every key must be an axe item (an `AxeItem`), and every value a number. A single bad entry makes the whole `axe` value fail validation.
:::

## `treeSimulator.useAxeDurability`

| Value | Effect |
| --- | --- |
| `true` (default) | The axe loses 1 durability on every harvest. |
| `false` | Axes never wear out in the Tree Simulator. |

Axes with the `unbreakable` component never wear out either way.

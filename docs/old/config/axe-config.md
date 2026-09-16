# Axe Speed Config

This file sets how fast each axe makes the [Tree Simulator](../gameplay/tree-simulator) run.

## File location

```
config/resourcestrees/axe.json
```

The file is created with the default values on first launch and read when the game starts.

## Generator

Change multipliers, add modded axes, and copy the whole file.

<AxeConfigBuilder />

## Format

```json
{
  "values": {
    "minecraft:wooden_axe": 1.0,
    "minecraft:stone_axe": 2.0,
    "minecraft:iron_axe": 3.0,
    "minecraft:diamond_axe": 4.0,
    "minecraft:netherite_axe": 5.0,
    "minecraft:golden_axe": 6.0
  }
}
```

## `values`

Maps an axe's item id to its speed multiplier. Each harvest takes:

```
harvest time = grow time ÷ multiplier
```

<AxeTable />

- Axes you leave out of the file keep their default multiplier.
- An axe with no multiplier at all, such as a modded axe you haven't listed, works at the base grow time.
- Entries that aren't axe items (`AxeItem`) are skipped, with a warning in the log.

## `useAxeDurability`

Only in **1.21.1 NeoForge 1.8.0** and later. Set it to `false` to stop the Tree Simulator from damaging axes. The file is rewritten at start-up, so older files gain the option automatically.

```json
{
  "useAxeDurability": true,
  "values": {
    "minecraft:wooden_axe": 1.0
  }
}
```

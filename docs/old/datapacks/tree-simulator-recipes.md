# Tree Simulator Recipes

The Tree Simulator uses `resourcestrees:tree_simulator` recipes to decide what a sapling produces and how long each harvest takes.

The built-in resource types ship with a recipe for every tree shape. A type added by a datapack has **none**, so the simulator won't grow its saplings until you add one. JEI and REI show a recipe for every type anyway, which only exists in the viewer.

## Generator

Pick a sapling to load the built-in drops, then change them. For a datapack resource type, type its id (`namespace:name`) under the tree picker. Pick your Minecraft version and loader at the bottom of the form.

<SimulatorRecipeBuilder />

## File location

```
data/resourcestrees/recipe/tree_simulator/<type>_<tree>_sapling.json
```

1.20.1 uses a `recipes` folder instead of `recipe`. A file with the same name as a built-in one replaces it.

## Format

::: code-group

```json [1.21+]
{
  "type": "resourcestrees:tree_simulator",
  "drops": [
    {
      "chance": 0.5,
      "maxRolls": 4,
      "minRolls": 1,
      "output": { "count": 1, "id": "minecraft:acacia_log" }
    },
    {
      "chance": 1.0,
      "maxRolls": 1,
      "minRolls": 1,
      "output": {
        "components": { "resourcestrees:resources_type": "resourcestrees:amethyst" },
        "count": 1,
        "id": "resourcestrees:leaf_fragment"
      }
    }
  ],
  "ticksToGrow": 800,
  "tree": {
    "components": { "resourcestrees:resources_type": "resourcestrees:amethyst" },
    "count": 1,
    "id": "resourcestrees:resources_acacia_sapling"
  }
}
```

```json [1.20.1]
{
  "type": "resourcestrees:tree_simulator",
  "drops": [
    {
      "chance": 0.5,
      "maxRolls": 4,
      "minRolls": 1,
      "output": { "count": 1, "item": "minecraft:acacia_log" }
    },
    {
      "chance": 1.0,
      "maxRolls": 1,
      "minRolls": 1,
      "output": {
        "count": 1,
        "item": "resourcestrees:leaf_fragment",
        "nbt": "{type:\"resourcestrees:amethyst\"}"
      }
    }
  ],
  "ticksToGrow": 800,
  "tree": {
    "count": 1,
    "item": "resourcestrees:resources_acacia_sapling",
    "nbt": "{type:\"resourcestrees:amethyst\"}"
  }
}
```

:::

### Fields

| Field | Description |
| --- | --- |
| `type` | Always `resourcestrees:tree_simulator` |
| `tree` | The sapling this recipe is for, including its resource type |
| `ticksToGrow` | Ticks per harvest before the [axe multiplier](../config/axe-config). Lower is faster. |
| `drops` | What each harvest can produce |

### Drop fields

| Field | Description |
| --- | --- |
| `output` | The item stack to produce |
| `chance` | Chance (0.0–1.0) of producing it on each roll |
| `minRolls`, `maxRolls` | Each harvest rolls a random number of times between these, inclusive |

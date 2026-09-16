# Tree Simulator Recipes

The Tree Simulator uses custom `tree_simulator` recipes to determine what a sapling produces and how long it takes to grow.

## Auto-Generation

By default, a Tree Simulator recipe is **automatically generated** for every registered resource sapling. You do not need to create these files unless you want to customize the outputs.

The auto-generated recipe produces:

| Output | Chance | Min Rolls | Max Rolls |
|---|---|---|---|
| Log (matching tree type) | 30% | 1 | 1 |
| Leaf Fragment | 100% | 1 | 1 |
| Leaf Fragment (bonus) | `leafDropChance × 0.5` | 1 | 4 |
| Sapling (same type) | `saplingDropChance` | 1 | 1 |

## Generator

Pick a sapling to load the drops the mod generates for it, then change them.

<SimulatorRecipeBuilder />

## Custom Recipe Format

To override the recipe for a specific sapling, place a JSON file at:

```
data/resourcestrees/recipe/tree_simulator/<sapling_name>.json
```

For example, to customize the `iron_oak_sapling` recipe:

```
data/resourcestrees/recipe/tree_simulator/iron_oak_sapling.json
```

### JSON Format

```json
{
  "type": "resourcestrees:tree_simulator",
  "tree": {
    "id": "resourcestrees:iron_oak_sapling",
    "count": 1
  },
  "ticksToGrow": 1200,
  "drops": [
    {
      "output": {
        "id": "minecraft:iron_ingot",
        "count": 1
      },
      "chance": 0.5,
      "minRolls": 1,
      "maxRolls": 3
    },
    {
      "output": {
        "id": "resourcestrees:iron_leaf_fragment",
        "count": 1
      },
      "chance": 1.0,
      "minRolls": 1,
      "maxRolls": 1
    }
  ]
}
```

### Fields

| Field | Description |
|---|---|
| `type` | Always `"resourcestrees:tree_simulator"` |
| `tree` | The sapling this recipe applies to (matched by item and components) |
| `ticksToGrow` | Base ticks per harvest, before the [axe multiplier](../config/axe-config). Lower is faster. The generated recipe uses the resource type's `treeSimulatorTicks`. |
| `drops` | List of output entries (see below) |

### Drop Entry Fields

| Field | Description |
|---|---|
| `output` | The item to produce (uses `ItemStackTemplate` format: `id` + optional `count` and components) |
| `chance` | Probability (0.0–1.0) this drop occurs per roll |
| `minRolls` | Minimum number of times to attempt this drop per harvest |
| `maxRolls` | Maximum number of times to attempt this drop per harvest |

Each harvest rolls a random number of times between `minRolls` and `maxRolls` (inclusive), plus one extra roll per level of Fortune on the axe. Every roll succeeds on its own `chance`.

## Viewing Recipes In-Game

With JEI installed, Tree Simulator recipes have their own **Tree Simulator** category. Look up the Tree Simulator block's uses to browse them all, or try the [Sapling Explorer](../gameplay/explorer) for the generated ones.

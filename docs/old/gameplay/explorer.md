---
aside: false
---

# Sapling Explorer

Pick a resource type and a tree shape to see how to craft the sapling, what its leaves drop, what the [Tree Simulator](./tree-simulator) produces with your axe, and what its leaf fragments craft into.

<SaplingExplorer />

::: tip How the averages are worked out
Each Tree Simulator drop rolls a number of times between its minimum and maximum, and every roll succeeds on its own chance. The axe's [multiplier](../config/axe-config) divides the grow time. These versions have no Efficiency bonus; 1.21.1 NeoForge 1.8.0 adds one roll per level of Fortune, which isn't counted here.
:::

The leaf drops use the 1.21.x chances; 1.20.1 drops saplings twice as often. These numbers use the built-in recipes. A datapack can [replace the Tree Simulator recipe](../datapacks/tree-simulator-recipes) or [the sapling recipe](../datapacks/sapling-recipes) of any sapling.

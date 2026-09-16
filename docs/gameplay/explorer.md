---
aside: false
---

# Sapling Explorer

Pick a resource type and a tree shape to see everything about that sapling: how to craft it, what its leaves drop, what the [Tree Simulator](./tree-simulator) produces with your axe, and what its leaf fragments craft into.

<SaplingExplorer />

::: tip How the averages are worked out
Each Tree Simulator drop rolls a number of times between its minimum and maximum, and every roll succeeds on its own chance. Each level of **Fortune** on the axe adds one roll to every drop. The axe's [multiplier](../config/axe-config) divides the grow time, then **Efficiency** divides it again by 1 + 0.2 per level.
:::

These numbers use the mod's built-in values. A datapack can [override the Tree Simulator recipe](../datapacks/tree-simulator-recipes) or [the sapling recipe](../datapacks/sapling-recipes) for any sapling.

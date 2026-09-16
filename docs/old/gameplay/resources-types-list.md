# Resources Types

Every resource type sets what material crafts its sapling, the tint of its leaves and fragments, how often its leaves drop saplings and fragments, and how long it takes in the Tree Simulator.

In these versions resource types are a datapack registry. Their ids are `resourcestrees:<name>`, the value recipes put in the `resourcestrees:resources_type` component.

## Built-in resources types

Use the tree buttons to preview the sapling and leaves in each shape.

<ResourceTypeTable />

::: info Version differences
- **1.20.1** has 45 types: there is no Breeze.
- `weight` is required in the files but has no effect.
:::

## Built-in tree types

Tree types are fixed in these versions; you can't add new ones. Each has one sapling and one leaves item that every resource type shares.

<TreeTypeTable />

::: info Version differences
**1.20.1** and **1.21.1** have no Pale Oak.
:::

## Adding your own

See [Custom Resources Types](../datapacks/resources-types) to add one with a datapack.

# Tags

The mod adds five tags. They are filled in automatically with every sapling, leaves block and leaf fragment, including ones from [custom resource types](./resources-types) and [tree types](./tree-types).

| Tag | Registry | Contains |
| --- | --- | --- |
| `#resourcestrees:resources_saplings` | Block and item | <ItemSlot id="resourcestrees:iron_oak_sapling" /> Every resources sapling |
| `#resourcestrees:resources_leaves` | Block and item | <ItemSlot id="resourcestrees:iron_oak_leaves" /> Every resources leaves block |
| `#resourcestrees:leaf_fragments` | Item | <ItemSlot id="resourcestrees:iron_leaf_fragment" /> Every leaf fragment |

The tags come from a built-in datapack that the mod creates at startup, so they are always up to date with your config.

## Vanilla tags

The built-in saplings and leaves are also in the vanilla `#minecraft:saplings` and `#minecraft:leaves` tags, so they behave like vanilla ones with other mods. The Tree Simulator is in `#minecraft:mineable/pickaxe`.

::: tip
Saplings and leaves from custom resource types are only in the `resourcestrees` tags. Add them to vanilla tags with your own datapack if you need to, for example by including `#resourcestrees:resources_leaves` in `data/minecraft/tags/block/leaves.json`.
:::

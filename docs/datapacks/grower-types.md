# Custom Grower Types

A **grower type** decides which world-generation tree features a sapling can grow into. Each [tree type](/datapacks/tree-types) references a grower type through its `treeGrowerName` field.

You can add your own grower types by dropping JSON files into the config directory. No coding required (for the Java API, see [Plugin API](/developer/plugin-api#growertype-parameters)).

::: tip Client & Server
Grower types only control sapling growth (server-side world generation), so they don't register any blocks or items and are only required on the server. Even so, the simplest approach is to keep the entire `config/resourcestrees` folder identical on the client and server — your [resource types](/datapacks/resources-types) and [tree types](/datapacks/tree-types) **must** match on both sides.
:::

## Generator

Start from a vanilla grower and change the trees it picks from. The percentages show how often each feature is chosen.

<GrowerTypeBuilder />

## File location

Place JSON files in:

```
config/resourcestrees/grower_type/<name>.json
```

### JSON Format

```json
{
  "name": "oak",
  "trees": [
    { "data": "minecraft:oak", "weight": 9 },
    { "data": "minecraft:fancy_oak", "weight": 1 }
  ],
  "megaTrees": [],
  "flowerTrees": [
    { "data": "minecraft:oak_bees_005", "weight": 1 }
  ],
  "shortestTreeType": "minecraft:oak"
}
```

### Fields

| Field | Required | Default | Description |
|---|---|---|---|
| `name` | ✅ | — | Unique identifier. A [tree type](/datapacks/tree-types) references this through its `treeGrowerName`. |
| `trees` | ✅ | — | Weighted list of tree features used for a normal, single-sapling tree. May be empty (`[]`), like vanilla dark oak, which only grows from four saplings. |
| `megaTrees` | ✅ | — | Weighted list of features used when 4 saplings are placed in a 2×2 pattern. May be empty (`[]`). |
| `flowerTrees` | ✅ | — | Weighted list of flowering variants (e.g. bee-nest trees) that may be chosen instead of a normal tree. May be empty (`[]`). |
| `shortestTreeType` | ❌ | _none_ | A single feature used as a fallback when there is not enough space above the sapling for a full tree. |

### Weighted entries

Each entry in `trees`, `megaTrees`, and `flowerTrees` is a weighted object:

| Field | Description                                                                   |
|---|-------------------------------------------------------------------------------|
| `data` | Resource key of the tree feature to grow.                                     |
| `weight` | Relative weight (non-negative integer). Higher weights are picked more often. |

In the example above, an oak grows into `minecraft:oak` 90% of the time and `minecraft:fancy_oak` 10% of the time.

The vanilla growers (`oak`, `spruce`, `birch`, `jungle`, `acacia`, `dark_oak`, `cherry`, `pale_oak`, `poplar`, `mangrove` and `azalea`) can be used as a `treeGrowerName` without a file. The generator above can load any of them as a starting point.

::: tip
The feature ids (e.g. `minecraft:oak`, `minecraft:fancy_oak`) must match registered world-gen tree features. You can use vanilla feature ids or ones added by other mods/datapacks.
:::

## Writing your own tree feature

You aren't limited to vanilla trees — you can define your own [feature](https://minecraft.wiki/w/Configured_feature) of type `minecraft:tree` in a datapack and reference its id in the `data` field above.

A tree configured feature controls the trunk, foliage, roots, decorators and more. See the Minecraft Wiki for the full JSON format:

- [Tree → Config](https://minecraft.wiki/w/Tree#Config) — all options for a `minecraft:tree` feature
- [Feature](https://minecraft.wiki/w/Configured_feature) — general feature format and where to place the files

Once your feature is loaded (e.g. `yourpack:my_custom_tree`), just use that id as the `data` of a weighted entry.

::: warning Outdated
At the time of writing this page, Minecraft wiki is not updated yet, `Configured Feature` has been changed in `26.3-snapshot-1`
:::
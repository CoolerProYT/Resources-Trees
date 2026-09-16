# Custom Tree Types

Add your own tree shapes by dropping JSON files into the config folder. No coding needed. Every [resource type](./resources-types) grows as every tree type, so one new tree type adds a sapling and leaves block for each resource type.

The built-in tree types are listed on [Resources Types](../gameplay/resources-types-list#built-in-tree-types).

::: warning Client & Server must match
This config registers actual blocks and items, so the **client and server must use the exact same config**. If they differ, the client will fail to connect (registry mismatch) and textures/models won't load correctly. Distribute the same `config/resourcestrees` files to every player and the server.
:::

## Generator

Pick a built-in tree to copy its vanilla blocks, then name your tree. The file list under the JSON shows the textures you need to draw.

<TreeTypeBuilder />

## File location

Place JSON files in:

```
config/resourcestrees/tree_type/<name>.json
```

### JSON Format

```json
{
  "name": "oak",
  "treeGrowerName": "oak",
  "saplingTexture": "resourcestrees:block/oak_sapling",
  "leavesTexture": "resourcestrees:block/oak_leaves",
  "originalSapling": "minecraft:oak_sapling",
  "originalLeaves": "minecraft:oak_leaves",
  "log": "minecraft:oak_log",
  "particle": 0.01,
  "ambient_leaves_block_sound_player": {
    "ambient_sound": "minecraft:block.poplar_leaves.ambient",
    "satisfying_blocks": "minecraft:required_for_poplar_leaf_ambience"
  }
}
```

### Fields

| Field | Required | Default | Description                                                                                                                                                    |
|---|---|---|----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `name` | ✅ | — | Unique identifier (e.g. `"oak"`, `"birch"`)                                                                                                                    |
| `treeGrowerName` | ✅ | — | Name of the [grower type](/datapacks/grower-types) used to grow this tree (e.g. `"oak"`, `"spruce"`, `"birch"`, `"jungle"`, `"acacia"`, `"dark_oak"`, `"cherry"`, `"pale_oak"`) |
| `saplingTexture` | ✅ | — | Resource location of the model/texture to use for the sapling block/item                                                                                       |
| `leavesTexture` | ✅ | — | Resource location of the model/texture to use for the leaves block/item                                                                                        |
| `originalSapling` | ✅ | — | Registry name of the vanilla sapling to copy block properties from                                                                                             |
| `originalLeaves` | ✅ | — | Registry name of the vanilla leaves to copy block properties from                                                                                              |
| `log` | ✅ | — | Registry name of the log block used as the trunk and for Tree Simulator recipe generation                                                                      |
| `particle` | ❌ | `0.01` | Per-tick chance (`0.0`–`1.0`) for the leaves block to spawn falling tinted leaf particles. Set to `0` to disable.                                              |
| `ambient_leaves_block_sound_player` | ❌ | _no sound_ | Controls the ambient rustling sound emitted by the leaves block. Omit for silent leaves. See [below](#ambient-leaves-sound).                                   |

### Ambient leaves sound

The optional `ambient_leaves_block_sound_player` object uses Minecraft's vanilla `AmbientLeavesBlockSoundPlayer` format. In 26.3 the only vanilla leaves ambience is `minecraft:block.poplar_leaves.ambient`, which poplar leaves play near the blocks in `minecraft:required_for_poplar_leaf_ambience`. All of its fields are optional:

| Field | Default | Description |
|---|---|---|
| `ambient_sound` | _none_ | Sound event id played as the ambient rustle. If omitted, no sound plays. |
| `chance` | `300` | Average number of ticks between sound attempts (higher = rarer). |
| `satisfying_blocks` | _none_ | A block tag id, written without `#`. The sound only plays when enough of these blocks are next to the leaves. |
| `nearby_satisfying_blocks_required` | `1` | How many `satisfying_blocks` must be next to it for the sound to play. |
| `nearby_same_leaves_required` | `3` | How many of the same leaves block must be next to it for the sound to play. |

"Next to" means the four blocks around it on the same level.

::: warning
Without `satisfying_blocks`, set `nearby_satisfying_blocks_required` to `0`. Otherwise the default of `1` can never be met and the sound never plays.
:::

## Textures

A new tree type needs its own textures, in a resource pack. The paths below match the ids in the example above: `saplingTexture` and `leavesTexture` point at them, so use whatever ids you set there.

### Leaves texture

```
assets/resourcestrees/textures/block/<tree_type_name>_leaves.png
```

The leaves texture needs to be grayscale (black & white) because it is tinted with the resource type's colour.

### Sapling texture

A sapling needs two textures:

```
assets/resourcestrees/textures/block/<tree_type_name>_sapling.png
assets/resourcestrees/textures/block/<tree_type_name>_sapling_layer1.png
```

The first texture is the stem of the sapling and is drawn as is.

The `_layer1` texture is the leafy part of the sapling. It needs to be grayscale (black & white) because it is tinted with the resource type's colour.

For example, these are the built-in oak textures, and how they come out for a few resource types:

![Oak sapling stem](/textures/block/resources_oak_sapling.png){.pixelated width=48}
![Oak sapling leaves layer](/textures/block/resources_oak_sapling_layer1.png){.pixelated width=48}
![Oak leaves](/textures/block/resources_oak_leaves.png){.pixelated width=48}
➜
<ItemSlot id="resourcestrees:gold_oak_sapling" />
<ItemSlot id="resourcestrees:lapis_oak_sapling" />
<ItemSlot id="resourcestrees:gold_oak_leaves" />
<ItemSlot id="resourcestrees:lapis_oak_leaves" />
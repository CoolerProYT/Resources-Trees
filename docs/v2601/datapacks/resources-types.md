# Custom Resources Types

Add your own resource types by dropping JSON files into the config folder. No coding needed.

::: warning Client and server must match
Resource types register real blocks and items, so the **client and server need the exact same files**. If they differ, players can't join (registry mismatch) and textures won't load properly. Ship the same `config/resourcestrees` folder to every player and the server.
:::

## File location

```
config/resourcestrees/resources_type/<name>.json
```

The folder is created on first launch. Files in subfolders are loaded too.

## Generator

Fill in the form to get the file and its translation. The previews use your colour on every built-in tree shape.

<ResourceTypeBuilder />

## JSON format

```json
{
  "name": "ruby",
  "material": "minecraft:redstone",
  "color": -3407872,
  "saplingDropChance": 0.1,
  "leafDropChance": 0.2,
  "treeSimulatorTicks": 1400
}
```

### Fields

| Field | Required | Description |
| --- | --- | --- |
| `name` | ❌ | Unique id, used in block and item ids. Defaults to the file name without `.json`. |
| `material` | ✅ | Item id used to craft the sapling. Start with `#` to use an item tag. |
| `color` | ✅ | Tint for the leaves, sapling and leaf fragment, as a signed ARGB int. See [Colour](#colour). |
| `saplingDropChance` | ✅ | Chance (0.0–1.0) that leaves drop a sapling. The built-in default is `0.125`. |
| `leafDropChance` | ✅ | Chance (0.0–1.0) that leaves drop a leaf fragment. The built-in default is `0.25`. |
| `treeSimulatorTicks` | ✅ | Ticks per [Tree Simulator](../gameplay/tree-simulator) harvest before the axe speeds it up. Lower is faster. The built-in default is `1200`. |

The old `weight` field is no longer used and is ignored.

## Using an item tag

Start `material` with `#` to accept any item in a tag:

```json
{
  "name": "wood",
  "material": "#minecraft:logs",
  "color": -7508381,
  "saplingDropChance": 0.125,
  "leafDropChance": 0.25,
  "treeSimulatorTicks": 800
}
```

## Colour

`color` is an ARGB colour stored as a **signed 32-bit int**, with alpha `FF`. For example, `#CC0000` is `0xFFCC0000`, which is `-3407872`. The [generator](#generator) converts it for you, both ways.

The leaves, sapling and fragment textures are grayscale, so the colour is multiplied into them the same way the previews on this site are drawn.

## Example: Ruby

`config/resourcestrees/resources_type/ruby.json` with the JSON above creates:

- `resourcestrees:ruby_oak_sapling`, `resourcestrees:ruby_birch_sapling`, and one for every other [tree type](./tree-types)
- `resourcestrees:ruby_oak_leaves`, `resourcestrees:ruby_birch_leaves`, and so on
- `resourcestrees:ruby_leaf_fragment`

It also gets, without any extra files:

- a [sapling recipe](./sapling-recipes) and a [Tree Simulator recipe](./tree-simulator-recipes) for every sapling
- block and item models, and loot tables
- entries in the mod's [tags](./tags)

What you still need to add is a way to turn the fragments into something useful. See [Leaf Fragment Recipes](./leaf-fragment-recipes). The name shows as `ruby` until you add a translation:

```json
{
  "resources_type.resourcestrees.ruby": "Ruby"
}
```

Put it in a resource pack at `assets/resourcestrees/lang/en_us.json`.

# Custom Resources Types

Add your own resource types with a datapack. In these versions resource types are a datapack registry, so only the server needs the files; players receive them when they join.

## File location

```
data/<namespace>/resourcestrees/resources_type/<name>.json
```

`resourcestrees/resources_type` is the registry folder. The file name is the type's name, and the type's id is `<namespace>:<name>`. Recipes refer to it by that id.

## Generator

Fill in the form to get the file and its translation. The previews use your colour on every tree shape.

<ResourceTypeBuilder />

## JSON format

```json
{
  "material": "minecraft:redstone",
  "color": -3407872,
  "weight": 4,
  "saplingDropChance": 0.1,
  "leafDropChance": 0.2,
  "treeSimulatorTicks": 1400
}
```

### Fields

All fields are required.

| Field | Description |
| --- | --- |
| `material` | Item id, or `#tag`. Required, but has no effect in these versions: sapling recipes are separate files. |
| `color` | Tint for the leaves, sapling and leaf fragment, as a signed ARGB int. `#CC0000` is `0xFFCC0000`, which is `-3407872`. The generator converts it for you. |
| `weight` | A whole number. Required, but has no effect. |
| `saplingDropChance` | Chance (0.0–1.0) used for sapling drops. Leaves roll half of it on 1.21.x. |
| `leafDropChance` | Chance (0.0–1.0) that leaves drop a leaf fragment. |
| `treeSimulatorTicks` | Shown in recipe viewers. The real grow time is set by each [Tree Simulator recipe](./tree-simulator-recipes). |

## What a new type needs

A type on its own already works with every [sapling and leaves block](./sapling-types): its trees grow and its leaves drop fragments. What it doesn't get is recipes. Add these to the same datapack:

1. **[Sapling recipes](./sapling-recipes)**, one per tree shape you want to be craftable.
2. **[Tree Simulator recipes](./tree-simulator-recipes)**, one per tree shape, or the simulator won't accept the sapling.
3. **[Leaf fragment recipes](./leaf-fragment-recipes)**, so the fragments turn into something.

## Display name

Items show the type as `type.resourcestrees.<name>` followed by the item name, whatever your datapack's namespace. Add the translation in a resource pack at `assets/resourcestrees/lang/en_us.json`:

```json
{
  "type.resourcestrees.ruby": "Ruby"
}
```

## Built-in type ids

The built-in types use the `resourcestrees` namespace, such as `resourcestrees:iron` or `resourcestrees:nether_star`. All of them are listed on [Resources Types](../gameplay/resources-types-list).

# Resources Leaves

Resources leaves grow on resource trees. When they are broken or decay, they drop [leaf fragments](./leaf-fragments) and sometimes a new sapling.

<p>
  <ItemSlot id="resourcestrees:coal_oak_leaves" />
  <ItemSlot id="resourcestrees:iron_spruce_leaves" />
  <ItemSlot id="resourcestrees:gold_birch_leaves" />
  <ItemSlot id="resourcestrees:emerald_jungle_leaves" />
  <ItemSlot id="resourcestrees:redstone_acacia_leaves" />
  <ItemSlot id="resourcestrees:lapis_dark_oak_leaves" />
  <ItemSlot id="resourcestrees:amethyst_cherry_leaves" />
  <ItemSlot id="resourcestrees:diamond_pale_oak_leaves" />
  <ItemSlot id="resourcestrees:copper_poplar_leaves" />
</p>

## Naming

Leaves ids follow the pattern:

```
resourcestrees:<resource_type>_<tree_type>_leaves
```

For example `resourcestrees:iron_oak_leaves` or `resourcestrees:diamond_birch_leaves`.

## Drops

Breaking or decaying resources leaves (without shears or Silk Touch) rolls three separate chances:

| Drop | Chance | Default |
| --- | --- | --- |
| <ItemSlot id="resourcestrees:iron_oak_sapling" /> Sapling | `saplingDropChance` | 12.5% |
| <ItemSlot id="resourcestrees:iron_leaf_fragment" /> Leaf fragment | `leafDropChance` | 25% |
| <ItemSlot id="resourcestrees:iron_leaf_fragment" /> Bonus leaf fragment | `leafDropChance × 0.5` | 12.5% |

Diamond and Netherite trees have lower chances. The [Sapling Explorer](./explorer) shows the numbers for any resource type.

### Shears and Silk Touch

Breaking resources leaves with shears or a Silk Touch tool drops the leaves block itself instead.

## Looks and sounds

- **Tint.** The leaves are coloured with the resource type's colour, so every resource type looks different without its own texture.
- **Falling leaves.** Most tree types spawn falling leaf particles in the same colour. Cherry leaves drop the most, and spruce leaves drop none.
- **Ambient sound.** Poplar leaves rustle like vanilla poplar leaves.

[Tree types](./resources-types-list#built-in-tree-types) set the particle chance and sound.

## Flammability

Resources leaves burn like vanilla leaves: flammability 60, fire spread speed 30.

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
</p>

Each tree shape has one leaves block, `resourcestrees:resources_<tree>_leaves`, which stores its resource type.

## Drops

Breaking or decaying resources leaves (without shears or Silk Touch) rolls three separate chances:

| Drop | 1.21.x | 1.20.1 | Default on 1.21.x |
| --- | --- | --- | --- |
| <ItemSlot id="resourcestrees:iron_oak_sapling" /> Sapling | `saplingDropChance × 0.5` | `saplingDropChance` | 6.25% |
| <ItemSlot id="resourcestrees:iron_leaf_fragment" /> Leaf fragment | `leafDropChance` | `leafDropChance` | 25% |
| <ItemSlot id="resourcestrees:iron_leaf_fragment" /> Bonus leaf fragment | `leafDropChance × 0.5` | `leafDropChance × 0.5` | 12.5% |

Diamond and Netherite trees have lower chances. The [Sapling Explorer](./explorer) shows the numbers for any resource type.

### Shears and Silk Touch

Breaking resources leaves with shears or a Silk Touch tool drops the leaves block itself, still carrying its resource type.

## Looks

The leaves are tinted with the resource type's colour, and drop falling leaf particles in the same colour.

// Vanilla world-gen data the builders offer as presets, copied from the Minecraft 26.3 sources
// (TreeGrower, TreeFeatures). Mojang's code is not in this repository, so update these by hand after a port.

export interface WeightedFeature {
  data: string
  weight: number
}

export interface GrowerPreset {
  name: string
  trees: WeightedFeature[]
  megaTrees: WeightedFeature[]
  flowerTrees: WeightedFeature[]
  shortestTreeType: string | null
}

const w = (data: string, weight = 1): WeightedFeature => ({ data: `minecraft:${data}`, weight })

/** Vanilla sapling growers. Tree types can use these names directly as `treeGrowerName`. */
export const VANILLA_GROWERS: GrowerPreset[] = [
  { name: 'oak', trees: [w('oak', 9), w('fancy_oak', 1)], megaTrees: [], flowerTrees: [w('oak_bees_005', 9), w('fancy_oak_bees_005', 1)], shortestTreeType: 'minecraft:oak' },
  { name: 'spruce', trees: [w('spruce')], megaTrees: [w('mega_spruce'), w('mega_pine')], flowerTrees: [], shortestTreeType: 'minecraft:spruce' },
  { name: 'birch', trees: [w('birch')], megaTrees: [], flowerTrees: [w('birch_bees_005')], shortestTreeType: 'minecraft:birch' },
  { name: 'jungle', trees: [w('jungle_tree_no_vine')], megaTrees: [w('mega_jungle_tree')], flowerTrees: [], shortestTreeType: 'minecraft:jungle_tree_no_vine' },
  { name: 'acacia', trees: [w('acacia')], megaTrees: [], flowerTrees: [], shortestTreeType: 'minecraft:acacia' },
  { name: 'dark_oak', trees: [], megaTrees: [w('dark_oak')], flowerTrees: [], shortestTreeType: null },
  { name: 'cherry', trees: [w('cherry')], megaTrees: [], flowerTrees: [w('cherry_bees_005')], shortestTreeType: 'minecraft:cherry' },
  { name: 'pale_oak', trees: [], megaTrees: [w('pale_oak_bonemeal')], flowerTrees: [], shortestTreeType: null },
  { name: 'poplar', trees: [w('red_poplar'), w('orange_poplar'), w('yellow_poplar')], megaTrees: [], flowerTrees: [], shortestTreeType: 'minecraft:red_poplar' },
  { name: 'mangrove', trees: [w('mangrove', 15), w('tall_mangrove', 85)], megaTrees: [], flowerTrees: [], shortestTreeType: 'minecraft:mangrove' },
  { name: 'azalea', trees: [w('azalea_tree')], megaTrees: [], flowerTrees: [], shortestTreeType: 'minecraft:azalea_tree' },
]

/** Vanilla `minecraft:tree` features, for suggestions. Fungi, mushrooms and fallen trees are left out. */
export const VANILLA_TREE_FEATURES = [
  'oak', 'fancy_oak', 'oak_bees_0002_leaf_litter', 'oak_bees_002', 'oak_bees_005', 'oak_leaf_litter',
  'fancy_oak_bees', 'fancy_oak_bees_0002_leaf_litter', 'fancy_oak_bees_002', 'fancy_oak_bees_005', 'fancy_oak_leaf_litter',
  'swamp_oak', 'dark_oak', 'dark_oak_leaf_litter', 'pale_oak', 'pale_oak_bonemeal', 'pale_oak_creaking',
  'birch', 'birch_bees_0002', 'birch_bees_0002_leaf_litter', 'birch_bees_002', 'birch_bees_005', 'birch_leaf_litter',
  'super_birch_bees', 'super_birch_bees_0002',
  'spruce', 'pine', 'mega_spruce', 'mega_pine',
  'jungle_tree', 'jungle_tree_no_vine', 'mega_jungle_tree', 'jungle_bush',
  'acacia', 'cherry', 'cherry_bees_005', 'azalea_tree', 'mangrove', 'tall_mangrove',
  'red_poplar', 'orange_poplar', 'yellow_poplar', 'red_poplar_leaf_litter', 'orange_poplar_leaf_litter', 'yellow_poplar_leaf_litter',
].map((path) => `minecraft:${path}`)

/** The only vanilla leaves ambience in 26.3, used by poplar leaves. The tag is written without `#` in this codec. */
export const POPLAR_AMBIENCE = {
  ambient_sound: 'minecraft:block.poplar_leaves.ambient',
  satisfying_blocks: 'minecraft:required_for_poplar_leaf_ambience',
}

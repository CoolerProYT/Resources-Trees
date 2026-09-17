// Everything the wiki knows about one version of the mod, and the helpers the components use to show it.
// Components get the version of the page they are on through `useRt()` in ./version.ts.

export interface ResourceType {
  name: string
  label: string
  /** Item id, or #tag. */
  material: string
  /** #rrggbb tint for leaves, sapling leaves and leaf fragments. */
  color: string
  /** Legacy versions only: a required field with no effect. */
  weight?: number
  saplingDropChance: number
  leafDropChance: number
  treeSimulatorTicks: number
}

export interface TreeType {
  name: string
  label: string
  grower: string
  originalSapling: string
  originalLeaves: string
  log: string
  /** Null in versions without the option. */
  particle: number | null
  ambientSound: string | null
}

export interface Recipe {
  id: string
  type: string
  result: { id: string; count: number }
  pattern?: string[]
  /** Each ingredient is the list of items that satisfy it. */
  key?: Record<string, string[]>
  ingredients?: string[][]
  fragmentOf?: string | null
}

export interface SimulatorDrop {
  item: string
  /** The extra fragment entry, shown separately from the guaranteed one. */
  bonus?: boolean
  chance: number
  minRolls: number
  maxRolls: number
}

/** What a version supports, read from its source by the sync and snapshot scripts. */
export interface Features {
  /** Pre-26.1.2.100: one item per tree shape with the resource type as a data component, types from datapacks. */
  legacy: boolean
  growerTypes: boolean
  /** Tree type `particle` and `ambient_leaves_block_sound_player`. */
  treeTypeOptions: boolean
  configFormat: 'hocon' | 'json'
  axeDurabilityOption: boolean
  efficiency: boolean
  fortune: boolean
  /** Leaves roll a sapling at saplingDropChance times this. */
  leafSaplingFactor: number
  /** Whether sapling and Tree Simulator recipes are generated for every type or only shipped for built-in ones. */
  generatedRecipes: 'all' | 'builtin'
}

export interface Dataset {
  names: Record<string, string>
  defaults: { saplingDropChance: number; leafDropChance: number; treeSimulatorTicks: number }
  resourceTypes: ResourceType[]
  treeTypes: TreeType[]
  axes: { item: string; multiplier: number }[]
  essences: Record<string, string>
  recipes: Recipe[]
  features: Features
}

const MOD = 'resourcestrees'

export const saplingId = (resource: string, tree: string) => `${MOD}:${resource}_${tree}_sapling`
export const leavesId = (resource: string, tree: string) => `${MOD}:${resource}_${tree}_leaves`
export const fragmentId = (resource: string) => `${MOD}:${resource}_leaf_fragment`

const TAG_NAMES: Record<string, string> = {
  '#minecraft:logs': 'Any Log',
}

/** Item shown for a tag ingredient. */
const TAG_ICONS: Record<string, string> = {
  '#minecraft:logs': 'minecraft:oak_log',
}

function prettify(id: string): string {
  const path = id.replace(/^#/, '').split(':').pop() ?? id
  return path
    .split('_') // @ts-ignore
    .map((word) => (['of', 'the', 'and', 'on', 'a'].includes(word) ? word : word.charAt(0).toUpperCase() + word.slice(1)))
    .join(' ')
}

/**
 * Hosted item renders, one 1024px PNG per item at <namespace>/<name>.png. The mod's own icons are rendered
 * and uploaded by scripts/render-icons.py; Mojang's textures are not bundled here.
 */
const ICONS = 'https://storage.googleapis.com/coolerpromc/textures'
const hosted = (namespace: string, name: string): Icon => ({ kind: 'image', src: `${ICONS}/${namespace}/${name}.png`, local: false })

export type Icon =
  /** A ready-made picture. Local paths still need the site base. */
  | { kind: 'image'; src: string; local: boolean }
  /** Flat item layers, each multiplied by its tint, like an item model with tints. */
  | { kind: 'layers'; layers: { src: string; tint?: string }[] }
  /** A full block drawn in the inventory's isometric view. */
  | { kind: 'cube'; src: string; tint: string }

// Tinted in the browser, for builder previews of colours that have no uploaded icon.
// Every documented version uses the same textures, so the icons don't depend on the version.
export const saplingIcon = (tree: string, tint: string): Icon => ({
  kind: 'layers',
  layers: [{ src: `/textures/block/resources_${tree}_sapling.png` }, { src: `/textures/block/resources_${tree}_sapling_layer1.png`, tint }],
})
export const leavesIcon = (tree: string, tint: string): Icon => ({ kind: 'cube', src: `/textures/block/resources_${tree}_leaves.png`, tint })
export const fragmentIcon = (tint: string): Icon => ({ kind: 'layers', layers: [{ src: '/textures/item/leaf_fragment.png', tint }] })

type Parsed =
  | { kind: 'sapling' | 'leaves'; resource: ResourceType; tree: TreeType }
  | { kind: 'fragment'; resource: ResourceType }

export type Api = ReturnType<typeof createApi>

export function createApi(data: Dataset) {
  const features = data.features
  const resourceType = (name: string) => data.resourceTypes.find((type) => type.name === name)
  const treeType = (name: string) => data.treeTypes.find((type) => type.name === name)

  /** Splits a per-type item id into its resource type and tree type. Type names can contain underscores. */
  function parseId(id: string): Parsed | null {
    if (!id.startsWith(`${MOD}:`)) return null
    const path = id.slice(MOD.length + 1)
    const fragment = path.match(/^(.+)_leaf_fragment$/)
    if (fragment) {
      const resource = resourceType(fragment[1])
      return resource ? { kind: 'fragment', resource } : null
    }
    const block = path.match(/^(.+)_(sapling|leaves)$/)
    if (!block) return null
    for (const tree of data.treeTypes) {
      if (!block[1].endsWith(`_${tree.name}`)) continue
      const resource = resourceType(block[1].slice(0, -tree.name.length - 1))
      if (resource) return { kind: block[2] as 'sapling' | 'leaves', resource, tree }
    }
    return null
  }

  /** In-game name. Saplings, leaves and fragments follow the mod's "<type> <tree> Sapling" pattern. */
  function itemName(id: string): string {
    if (data.names[id]) return data.names[id]
    if (TAG_NAMES[id]) return TAG_NAMES[id]
    const parsed = parseId(id)
    if (parsed?.kind === 'fragment') return `${parsed.resource.label} Leaf Fragment`
    if (parsed) return `${parsed.resource.label} ${parsed.tree.label} ${parsed.kind === 'sapling' ? 'Sapling' : 'Leaves'}`
    return prettify(id)
  }

  /**
   * How to draw an item's icon: the hosted render for vanilla items and the mod's known saplings, leaves,
   * fragments, essences and Tree Simulator. Other mod ids (a builder's custom type) have none.
   */
  function itemIcon(id: string): Icon | null {
    const itemId = TAG_ICONS[id] ?? id
    // @ts-ignore
    const [namespace, path] = itemId.includes(':') ? itemId.split(':') : ['minecraft', itemId]
    if (namespace === 'minecraft') return hosted(namespace, path)
    if (parseId(itemId) || data.essences[itemId] || itemId === `${MOD}:tree_simulator`) return hosted(namespace, path)
    return null
  }

  /** The sapling recipe: generated for every sapling, or in legacy versions shipped for built-in ones. */
  function saplingRecipe(resource: ResourceType, tree: TreeType): Recipe {
    return {
      id: `${MOD}:saplings/${resource.name}_${tree.name}_sapling`,
      type: 'minecraft:crafting_shaped',
      result: { id: saplingId(resource.name, tree.name), count: 1 },
      pattern: features.legacy ? [' A ', 'ABA', ' A '] : [' M ', 'MSM', ' M '],
      key: features.legacy ? { A: [resource.material], B: [tree.originalSapling] } : { M: [resource.material], S: [tree.originalSapling] },
    }
  }

  /** A recipe by id. `saplings/<sapling>` ids resolve to the sapling recipe. */
  function recipe(id: string): Recipe | undefined {
    const found = data.recipes.find((r) => r.id === id || r.id === `${MOD}:${id}`)
    if (found) return found
    const sapling = id.match(/saplings\/([a-z0-9_]+)$/)
    const parsed = sapling ? parseId(`${MOD}:${sapling[1]}`) : null
    return parsed?.kind === 'sapling' ? saplingRecipe(parsed.resource, parsed.tree) : undefined
  }

  /** The Tree Simulator drops the mod gives a sapling unless a datapack overrides them. */
  function simulatorDrops(resource: ResourceType, tree: TreeType): SimulatorDrop[] {
    const fragment = fragmentId(resource.name)
    const sapling = saplingId(resource.name, tree.name)
    if (features.legacy) {
      return [
        { item: tree.log, chance: 0.5, minRolls: 1, maxRolls: 4 },
        { item: fragment, chance: 1, minRolls: 1, maxRolls: 1 },
        { item: fragment, chance: resource.leafDropChance, minRolls: 1, maxRolls: 4, bonus: true },
        { item: sapling, chance: resource.saplingDropChance, minRolls: 1, maxRolls: 1 },
        { item: 'minecraft:stick', chance: 0.1, minRolls: 1, maxRolls: 2 },
        { item: 'minecraft:apple', chance: 0.05, minRolls: 1, maxRolls: 1 },
      ]
    }
    return [
      { item: tree.log, chance: 0.3, minRolls: 1, maxRolls: 1 },
      { item: fragment, chance: 1, minRolls: 1, maxRolls: 1 },
      { item: fragment, chance: resource.leafDropChance * 0.5, minRolls: 1, maxRolls: 4, bonus: true },
      { item: sapling, chance: resource.saplingDropChance, minRolls: 1, maxRolls: 1 },
    ]
  }

  /** Every recipe that uses the given item, by any of its ingredients. */
  function recipesUsing(item: string): Recipe[] {
    return data.recipes.filter((r) => [...Object.values(r.key ?? {}), ...(r.ingredients ?? [])].some((options) => options.includes(item)))
  }

  /** Item ids the builders suggest: every vanilla item the version's data mentions, plus the mod's own items. */
  function knownItems(): string[] {
    const ids = new Set<string>(Object.keys(data.names))
    for (const type of data.resourceTypes) {
      if (!type.material.startsWith('#')) ids.add(type.material)
      ids.add(fragmentId(type.name))
    }
    for (const tree of data.treeTypes) {
      ids.add(tree.log)
      ids.add(tree.originalSapling)
    }
    for (const r of data.recipes) {
      ids.add(r.result.id)
      for (const options of [...Object.values(r.key ?? {}), ...(r.ingredients ?? [])]) options.forEach((id) => ids.add(id))
    }
    return [...ids].filter((id) => ID.test(id)).sort()
  }

  const fastestAxe = Math.max(...data.axes.map((axe) => axe.multiplier))

  return {
    data,
    features,
    fastestAxe,
    resourceType,
    treeType,
    parseId,
    itemName,
    itemIcon,
    recipe,
    saplingRecipe,
    simulatorDrops,
    recipesUsing,
    knownItems,
  }
}

/** Average items per harvest: every roll is a separate chance, and each Fortune level adds one roll. */
export function expectedCount(drop: SimulatorDrop, fortune = 0): number {
  return ((drop.minRolls + drop.maxRolls) / 2 + fortune) * drop.chance
}

/** Namespaced id, as the game's `Identifier` accepts it. */
export const ID = /^[a-z0-9_.-]+:[a-z0-9_./-]+$/

/** Signed 32-bit ARGB int for an opaque #rrggbb colour, as resource type JSON stores it. */
export const signedColor = (hex: string) => (0xff000000 | parseInt(hex.slice(1), 16)) | 0

export function seconds(ticks: number): string {
  const value = Math.round((ticks / 20) * 10) / 10
  // @ts-ignore
  return Number.isInteger(value) ? `${value}` : value.toFixed(1)
}

/**
 * Cycle length in the Tree Simulator: the grow time divided by the axe's multiplier (an axe missing from the
 * config keeps the base time), then by 1 + 0.2 per Efficiency level where the version supports it.
 */
export function cycleTicks(ticks: number, multiplier: number, efficiency = 0): number {
  let result = multiplier !== 0 ? Math.trunc(ticks / multiplier) : ticks
  if (efficiency > 0) result = Math.trunc(result / (1 + efficiency * 0.2))
  return result
}

/** Up to two decimals, so 6.25% stays exact. */
export function percent(fraction: number): string {
  return `${Math.round(fraction * 10000) / 100}%`
}

export function decimal(value: number, digits = 2): string {
  return `${Math.round(value * 10 ** digits) / 10 ** digits}`
}

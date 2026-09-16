// JSON writers for the pre-26.1.2.100 versions, where saplings, leaves and fragments are one item per tree
// shape carrying the resource type as a data component (NBT on 1.20.1). Formats copied from each branch's datagen.

export interface LegacyFormat {
  id: string
  label: string
  /** Recipe folder name: `recipes` before 1.21. */
  folder: 'recipe' | 'recipes'
}

export const LEGACY_FORMATS: LegacyFormat[] = [
  { id: '1.21.2-neoforge', label: '1.21.2+ NeoForge', folder: 'recipe' },
  { id: '1.21.2-fabric', label: '1.21.2+ Fabric', folder: 'recipe' },
  { id: '1.21-forge', label: '1.21.x Forge', folder: 'recipe' },
  { id: '1.21.1-neoforge', label: '1.21.1 NeoForge', folder: 'recipe' },
  { id: '1.21.1-fabric', label: '1.21.1 Fabric', folder: 'recipe' },
  { id: '1.20.1-forge', label: '1.20.1 Forge', folder: 'recipes' },
]

const COMPONENT = 'resourcestrees:resources_type'

/** A shared legacy item plus the resource type it carries, e.g. resources_oak_sapling + resourcestrees:diamond. */
export interface LegacyItem {
  base: string
  /** Namespaced resource type id, or null for a plain item. */
  type: string | null
}

/**
 * Turns the per-type ids the wiki uses back into the legacy item and type. `types` lists the resource type
 * names to recognise, with the namespace to write for each.
 */
export function legacyItem(id: string, treeNames: string[], types: Record<string, string>): LegacyItem {
  const fragment = id.match(/^resourcestrees:(.+)_leaf_fragment$/)
  if (fragment && types[fragment[1]]) return { base: 'resourcestrees:leaf_fragment', type: types[fragment[1]] }
  const block = id.match(/^resourcestrees:(.+)_(sapling|leaves)$/)
  if (block) {
    for (const tree of treeNames) {
      const name = block[1].endsWith(`_${tree}`) ? block[1].slice(0, -tree.length - 1) : null
      if (name && types[name]) return { base: `resourcestrees:resources_${tree}_${block[2]}`, type: types[name] }
    }
  }
  return { base: id, type: null }
}

const oldStyle = (format: string) => format.startsWith('1.20') || format.startsWith('1.21.1')

/** An item stack: a recipe result, simulator output or simulator input. */
export function legacyStack(item: LegacyItem, count: number, format: string): Record<string, unknown> {
  if (format.startsWith('1.20')) {
    const stack: Record<string, unknown> = { count, item: item.base }
    if (item.type) stack.nbt = `{type:"${item.type}"}`
    return stack
  }
  const stack: Record<string, unknown> = {}
  if (item.type) stack.components = { [COMPONENT]: item.type }
  return Object.assign(stack, { count, id: item.base })
}

/** A recipe ingredient. Typed items need the mod's custom ingredient, whose shape differs per loader. */
export function legacyIngredient(item: LegacyItem, format: string): unknown {
  if (!item.type) {
    if (item.base.startsWith('#')) return oldStyle(format) ? { tag: item.base.slice(1) } : item.base
    return oldStyle(format) ? { item: item.base } : item.base
  }
  const components = { [COMPONENT]: item.type }
  switch (format) {
    case '1.20.1-forge':
      return { type: 'forge:partial_nbt', item: item.base, nbt: `{type:"${item.type}"}` }
    case '1.21.1-neoforge':
      return { type: COMPONENT, base: item.base, components }
    case '1.21.1-fabric':
      return { 'fabric:type': COMPONENT, base: { item: item.base }, components }
    case '1.21-forge':
      return { type: COMPONENT, components, items: item.base, strict: true }
    case '1.21.2-fabric':
      return { 'fabric:type': COMPONENT, base: item.base, components }
    default:
      return { 'neoforge:ingredient_type': COMPONENT, base: item.base, components }
  }
}

/** Extra top-level fields 1.20.1 recipes carry. */
export const legacyRecipeExtras = (format: string): Record<string, unknown> =>
  format.startsWith('1.20') ? { show_notification: true } : {}

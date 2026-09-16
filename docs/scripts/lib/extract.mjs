// Reads a version of the mod into the dataset the wiki renders. A "source" is a working tree or a git ref, so
// older versions can be read from their branches without checking them out.
import { execFileSync } from 'node:child_process'
import { existsSync, readdirSync, readFileSync } from 'node:fs'
import { basename, join } from 'node:path'

export function fsSource(root) {
  return {
    name: root,
    exists: (path) => existsSync(join(root, path)),
    read: (path) => readFileSync(join(root, path), 'utf8'),
    list: (dir) => (existsSync(join(root, dir)) ? readdirSync(join(root, dir)).sort() : []),
  }
}

export function gitSource(ref, cwd) {
  const git = (...args) => execFileSync('git', args, { cwd, encoding: 'utf8', maxBuffer: 64 * 1024 * 1024 })
  const files = new Set(git('ls-tree', '-r', '--name-only', ref).split('\n').filter(Boolean))
  return {
    name: ref,
    exists: (path) => files.has(path) || [...files].some((file) => file.startsWith(`${path}/`)),
    read: (path) => git('show', `${ref}:${path}`),
    list: (dir) =>
      [...new Set([...files].filter((file) => file.startsWith(`${dir}/`)).map((file) => file.slice(dir.length + 1).split('/')[0]))].sort(),
  }
}

/** Fails loudly when a source file no longer has the shape these readers expect. */
export function expect(value, message) {
  if (value === undefined || value === null || (Array.isArray(value) && value.length === 0)) {
    console.error(`extract: ${message}`)
    process.exit(1)
  }
  return value
}

/** Signed ARGB int to #rrggbb. */
const hex = (argb) => `#${(Number(argb) >>> 0).toString(16).padStart(8, '0').slice(2)}`
const json = (src, path) => JSON.parse(src.read(path))
const jsonFiles = (src, dir) => src.list(dir).filter((file) => file.endsWith('.json'))
const mostCommon = (values) =>
  [...values.reduce((counts, v) => counts.set(v, (counts.get(v) ?? 0) + 1), new Map())].sort((a, b) => b[1] - a[1])[0]?.[0]

function essenceTints(src, dir) {
  const essences = {}
  for (const file of jsonFiles(src, dir).filter((f) => f.endsWith('_essence.json'))) {
    const tint = json(src, `${dir}/${file}`).model?.tints?.[0]?.value
    essences[`resourcestrees:${basename(file, '.json')}`] = hex(expect(tint, `no tint in ${file}`))
  }
  return essences
}

/** Reads recipes, turning every item into a plain id with `toId`. */
function readRecipes(src, dir, toId, fragmentPattern) {
  const ingredient = (value) => {
    if (typeof value === 'string') return [value]
    if (Array.isArray(value)) return value.flatMap(ingredient)
    if (value?.tag) return [`#${value.tag}`]
    return [toId(value)]
  }
  const recipe = (id, data) => {
    const result = { id, type: data.type, result: { id: toId(data.result), count: data.result?.count ?? 1 } }
    if (data.pattern) {
      result.pattern = data.pattern
      result.key = Object.fromEntries(Object.entries(data.key).map(([symbol, value]) => [symbol, ingredient(value)]))
    } else if (data.ingredients) {
      result.ingredients = data.ingredients.map(ingredient)
    }
    return result
  }
  return [
    ...jsonFiles(src, dir).map((file) => recipe(`resourcestrees:${basename(file, '.json')}`, json(src, `${dir}/${file}`))),
    ...jsonFiles(src, `${dir}/fragment_crafting`).map((file) => {
      const id = basename(file, '.json')
      return {
        ...recipe(`resourcestrees:fragment_crafting/${id}`, json(src, `${dir}/fragment_crafting/${file}`)),
        fragmentOf: id.match(fragmentPattern)?.[1] ?? null,
      }
    }),
  ]
}

/** Multiloader versions (26.1 and later): types are registered in code and every item has its own id. */
export function extractModern(src) {
  const java = 'common/src/main/java/com/coolerpromc/resourcestrees'
  const generated = 'common/src/generated/resources'
  const lang = json(src, `${generated}/assets/resourcestrees/lang/en_us.json`)

  const names = {}
  for (const [key, value] of Object.entries(lang)) {
    const match = key.match(/^(item|block)\.resourcestrees\.([a-z0-9_]+)$/)
    if (match && !value.includes('%')) names[`resourcestrees:${match[2]}`] = value
  }

  const builderSource = src.read(`${java}/api/resources/ResourcesType.java`)
  const builderDefault = (field) =>
    Number(expect(builderSource.match(new RegExp(`this\\.${field} = ([\\d.]+)f?;`))?.[1], `no default for ${field} in ResourcesType.java`))
  const defaults = {
    saplingDropChance: builderDefault('saplingDropChance'),
    leafDropChance: builderDefault('leafDropChance'),
    treeSimulatorTicks: builderDefault('treeSimulatorTicks'),
  }

  const material = (expression) => {
    let match
    if ((match = expression.match(/^Items\.([A-Z0-9_]+)/))) return `minecraft:${match[1].toLowerCase()}`
    if ((match = expression.match(/^ModItems\.([A-Z0-9_]+)/))) return `resourcestrees:${match[1].toLowerCase()}`
    if ((match = expression.match(/^ItemTags\.([A-Z0-9_]+)/))) return `#minecraft:${match[1].toLowerCase()}`
    return expect(undefined, `unknown material expression ${expression}`)
  }

  const plugin = src.read(`${java}/api/internal/InternalResourcesTreesPlugin.java`)
  const resourceTypes = [
    ...plugin.matchAll(/new ResourcesType\.Builder\("([a-z0-9_]+)",\s*([\w.()]+?),\s*0x([0-9A-Fa-f]{8})\)((?:\.\w+\([^)]*\))*)\)/g),
  ].map(([, name, materialExpression, color, chain]) => {
    const option = (method) => chain.match(new RegExp(`\\.${method}\\(([\\d.]+)f?\\)`))?.[1]
    return {
      name,
      label: lang[`resources_type.resourcestrees.${name}`] ?? name,
      material: material(materialExpression),
      color: hex(parseInt(color, 16)),
      saplingDropChance: Number(option('saplingDropChance') ?? defaults.saplingDropChance),
      leafDropChance: Number(option('leafDropChance') ?? defaults.leafDropChance),
      treeSimulatorTicks: Number(option('treeSimulatorTicks') ?? defaults.treeSimulatorTicks),
    }
  })
  expect(resourceTypes, 'no resource types found in InternalResourcesTreesPlugin.java')

  // 26.3 lists (name, sound, particle) triples; 26.1 and 26.2 list plain names and have neither option.
  const treeTypeOptions = /\bparticle\b/.test(src.read(`${java}/api/tree/TreeType.java`))
  const triples = [...plugin.matchAll(/Triple\.of\("([a-z0-9_]+)",\s*(null|AmbientLeavesBlockSoundPlayer\.of\(SoundEvents\.(\w+)[^)]*\)),\s*([\d.]+)f\)/g)]
  const plain = plugin.match(/String\[\] trees = \{([^}]*)\}/)?.[1]
  const trees = triples.length
    ? triples.map(([, name, , sound, particle]) => ({ name, sound, particle: Number(particle) }))
    : [...(plain ?? '').matchAll(/"([a-z0-9_]+)"/g)].map(([, name]) => ({ name, sound: undefined, particle: undefined }))
  const treeTypes = trees.map(({ name, sound, particle }) => ({
    name,
    label: lang[`tree_type.resourcestrees.${name}`] ?? name,
    grower: name,
    originalSapling: `minecraft:${name}_sapling`,
    // Mirrors the plugin: poplar copies its leaf properties from orange poplar leaves.
    originalLeaves: `minecraft:${name === 'poplar' ? 'orange_poplar' : name}_leaves`,
    log: `minecraft:${name}_log`,
    particle: treeTypeOptions ? particle : null,
    // The SoundEvents constant, e.g. "Poplar leaves ambient"; its registry id is not in the source.
    ambientSound: treeTypeOptions && sound ? sound.charAt(0) + sound.slice(1).toLowerCase().replaceAll('_', ' ') : null,
  }))
  expect(treeTypes, 'no tree types found in InternalResourcesTreesPlugin.java')

  const config = src.read(`${java}/config/ResourcesTreesConfig.java`)
  const axes = [...config.matchAll(/"(minecraft:[a-z_]+_axe)",\s*([\d.]+)/g)]
    .map(([, item, multiplier]) => ({ item, multiplier: Number(multiplier) }))
    .sort((a, b) => a.multiplier - b.multiplier)
  expect(axes, 'no axe multipliers found in ResourcesTreesConfig.java')

  const entity = src.read(`${java}/block/entity/custom/TreeSimulatorBlockEntity.java`)

  return {
    names,
    defaults,
    resourceTypes,
    treeTypes,
    axes,
    essences: essenceTints(src, `${generated}/assets/resourcestrees/items`),
    recipes: readRecipes(src, `${generated}/data/resourcestrees/recipe`, (stack) => stack?.id ?? stack?.item ?? '?', /^(.+?)_leaf_fragment_to_/),
    features: {
      legacy: false,
      growerTypes: src.exists(`${java}/api/grower`),
      treeTypeOptions,
      configFormat: 'hocon',
      axeDurabilityOption: /useAxeDurability/.test(config),
      efficiency: /Enchantments\.EFFICIENCY/.test(entity),
      fortune: /Enchantments\.FORTUNE/.test(entity),
      leafSaplingFactor: 1,
      generatedRecipes: 'all',
    },
  }
}

/**
 * Pre-26.1.2.100 versions: resource types are a datapack registry, and one sapling, leaves and fragment item per
 * tree shape carries the type as a data component. Items are turned into the same per-type ids the modern
 * versions use, so the wiki can draw them the same way; the builders write the component form back out.
 */
export function extractLegacy(src) {
  const java = 'src/main/java/com/coolerpromc/resourcestrees'
  const generated = 'src/generated/resources'
  const lang = json(src, 'src/main/resources/assets/resourcestrees/lang/en_us.json')

  const names = {}
  for (const [key, value] of Object.entries(lang)) {
    const match = key.match(/^(item|block)\.resourcestrees\.([a-z0-9_]+)$/)
    if (match && !match[2].startsWith('resources_') && match[2] !== 'leaf_fragment') names[`resourcestrees:${match[2]}`] = value
  }

  // Lang lists the types in registration order.
  const order = Object.keys(lang)
    .map((key) => key.match(/^type\.resourcestrees\.([a-z0-9_]+)$/)?.[1])
    .filter((name) => name && name !== 'empty')
  const typeDir = `${generated}/data/resourcestrees/resourcestrees/resources_type`
  const resourceTypes = jsonFiles(src, typeDir)
    .map((file) => {
      const name = basename(file, '.json')
      const data = json(src, `${typeDir}/${file}`)
      return {
        name,
        label: lang[`type.resourcestrees.${name}`] ?? name,
        material: data.material,
        color: hex(data.color),
        weight: data.weight,
        saplingDropChance: data.saplingDropChance,
        leafDropChance: data.leafDropChance,
        treeSimulatorTicks: data.treeSimulatorTicks,
      }
    })
    .sort((a, b) => order.indexOf(a.name) - order.indexOf(b.name))
  expect(resourceTypes, `no resource types in ${typeDir}`)

  const treeTypes = Object.entries(lang)
    .map(([key, value]) => [key.match(/^block\.resourcestrees\.resources_([a-z0-9_]+)_sapling$/)?.[1], value])
    .filter(([name]) => name)
    .map(([name, value]) => ({
      name,
      label: value.replace(/ Sapling$/, ''),
      grower: name,
      originalSapling: `minecraft:${name}_sapling`,
      originalLeaves: `minecraft:${name}_leaves`,
      log: `minecraft:${name}_log`,
      particle: null,
      ambientSound: null,
    }))
  expect(treeTypes, 'no tree types in the lang file')

  const config = src.read(`${java}/config/ModConfig.java`)
  const axes = [...config.matchAll(/values\.put\("(minecraft:[a-z_]+_axe)",\s*([\d.]+)\)/g)]
    .map(([, item, multiplier]) => ({ item, multiplier: Number(multiplier) }))
    .sort((a, b) => a.multiplier - b.multiplier)
  expect(axes, 'no axe multipliers found in ModConfig.java')

  /** `{id, components: {resources_type}}` to the per-type id, e.g. resourcestrees:diamond_oak_sapling. */
  const toId = (stack) => {
    const id = stack?.id ?? stack?.item ?? stack?.base?.item ?? stack?.base ?? stack?.items ?? '?'
    const type = stack?.components?.['resourcestrees:resources_type']?.split(':')[1]
    if (!type) return id
    if (id === 'resourcestrees:leaf_fragment') return `resourcestrees:${type}_leaf_fragment`
    const block = id.match(/^resourcestrees:resources_([a-z0-9_]+)_(sapling|leaves)$/)
    return block ? `resourcestrees:${type}_${block[1]}_${block[2]}` : id
  }

  const entity = src.read(`${java}/block/entity/custom/TreeSimulatorBlockEntity.java`)

  return {
    names,
    defaults: {
      saplingDropChance: mostCommon(resourceTypes.map((t) => t.saplingDropChance)),
      leafDropChance: mostCommon(resourceTypes.map((t) => t.leafDropChance)),
      treeSimulatorTicks: 1200,
    },
    resourceTypes,
    treeTypes,
    axes,
    essences: essenceTints(src, `${generated}/assets/resourcestrees/items`),
    recipes: readRecipes(src, `${generated}/data/resourcestrees/recipe`, toId, /^(.+?)_fragment_to_/),
    features: {
      legacy: true,
      growerTypes: false,
      treeTypeOptions: false,
      configFormat: 'json',
      axeDurabilityOption: /useAxeDurability/.test(config),
      efficiency: /Enchantments\.EFFICIENCY/.test(entity),
      fortune: /Enchantments\.FORTUNE/.test(entity),
      leafSaplingFactor: /saplingDropChance\(\) \/ 2/.test(src.read(`${java}/block/custom/ResourcesLeavesBlock.java`)) ? 0.5 : 1,
      generatedRecipes: 'builtin',
    },
  }
}

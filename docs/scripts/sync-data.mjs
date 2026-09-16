// Pulls the current version's wiki data straight from the mod so the docs never drift from the game:
// built-in resource and tree types and axe speeds from the Java sources, recipes and names from datagen output,
// and the mod's own textures. Run `./gradlew :neoforge:runData` first when recipes or lang change.
// Older versions are frozen snapshots; see snapshot-versions.mjs.
import { copyFileSync, existsSync, mkdirSync, readdirSync, writeFileSync } from 'node:fs'
import { dirname, join } from 'node:path'
import { fileURLToPath } from 'node:url'
import { expect, extractModern, fsSource } from './lib/extract.mjs'

const docs = join(dirname(fileURLToPath(import.meta.url)), '..')
const root = join(docs, '..')
const assets = join(root, 'common/src/main/resources/assets/resourcestrees')

if (!existsSync(join(root, 'common/src/generated/resources'))) {
  console.error('No datagen output in common/src/generated/resources. Run ./gradlew :neoforge:runData first.')
  process.exit(1)
}

const data = extractModern(fsSource(root))

// Textures are shared by every documented version, so only the current ones are copied.
const pngFiles = (dir) => (existsSync(dir) ? readdirSync(dir).filter((f) => f.endsWith('.png')) : [])
const copied = []
for (const kind of ['item', 'block']) {
  const from = join(assets, 'textures', kind)
  mkdirSync(join(docs, 'public/textures', kind), { recursive: true })
  for (const file of pngFiles(from).filter((f) => kind === 'item' || f.startsWith('resources_'))) {
    copyFileSync(join(from, file), join(docs, 'public/textures', kind, file))
    copied.push(file)
  }
}
mkdirSync(join(docs, 'public/gui'), { recursive: true })
for (const file of ['tree_simulator.png', 'progress.png']) {
  copyFileSync(join(assets, 'textures/gui', file), join(docs, 'public/gui', file))
}
for (const tree of data.treeTypes) {
  for (const texture of [`resources_${tree.name}_sapling`, `resources_${tree.name}_sapling_layer1`, `resources_${tree.name}_leaves`]) {
    expect(copied.includes(`${texture}.png`) || undefined, `missing texture ${texture}.png`)
  }
}

mkdirSync(join(docs, '.vitepress/data'), { recursive: true })
writeFileSync(join(docs, '.vitepress/data/data.json'), JSON.stringify(data, null, 2))
console.log(
  `Synced ${data.resourceTypes.length} resource types, ${data.treeTypes.length} tree types, ${data.axes.length} axes, ${Object.keys(data.essences).length} essences, ${data.recipes.length} recipes, ${copied.length} textures.`,
)

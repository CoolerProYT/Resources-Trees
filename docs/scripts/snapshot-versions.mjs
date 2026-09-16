// Writes the data for the older documented versions into committed snapshots, read straight from their git
// branches. Those versions only get occasional fixes, so rerun this by hand after releasing one:
//
//   npm run snapshot                                  # default branches below
//   npm run snapshot -- v2601=origin/26.2             # pick a different branch for one version
//
// The CI build uses the committed files and never needs these branches.
import { mkdirSync, writeFileSync } from 'node:fs'
import { dirname, join } from 'node:path'
import { fileURLToPath } from 'node:url'
import { extractLegacy, extractModern, gitSource } from './lib/extract.mjs'

const docs = join(dirname(fileURLToPath(import.meta.url)), '..')
const root = join(docs, '..')

const versions = {
  v2601: { ref: 'origin/26.1', extract: extractModern },
  // The newest legacy release, in the 1.21.2+ format. The pages note where 1.20.1 and 1.21.1 differ.
  old: { ref: 'origin/1.21.11-NeoForge', extract: extractLegacy },
}
for (const arg of process.argv.slice(2)) {
  const [name, ref] = arg.split('=')
  if (!versions[name] || !ref) {
    console.error(`Unknown argument ${arg}. Use <version>=<git ref> with version ${Object.keys(versions).join(' or ')}.`)
    process.exit(1)
  }
  versions[name].ref = ref
}

mkdirSync(join(docs, '.vitepress/snapshots'), { recursive: true })
for (const [name, { ref, extract }] of Object.entries(versions)) {
  const data = { source: ref, ...extract(gitSource(ref, root)) }
  writeFileSync(join(docs, `.vitepress/snapshots/${name}.json`), `${JSON.stringify(data, null, 2)}\n`)
  console.log(`${name} from ${ref}: ${data.resourceTypes.length} resource types, ${data.treeTypes.length} tree types, ${data.recipes.length} recipes.`)
}

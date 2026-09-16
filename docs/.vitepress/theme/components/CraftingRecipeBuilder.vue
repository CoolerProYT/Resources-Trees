<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { ID, fragmentId, saplingId, type Recipe } from '../resourcestrees'
import { LEGACY_FORMATS, legacyIngredient, legacyItem, legacyRecipeExtras, legacyStack } from '../legacy'
import { useRt } from '../version'
import BuilderOutput from './BuilderOutput.vue'
import ItemSlot from './ItemSlot.vue'

const { data, features, itemName, knownItems, saplingRecipe } = useRt()
const legacy = features.legacy

/** `sapling` replaces a sapling's generated recipe; `fragment` makes a leaf fragment recipe. */
const props = withDefaults(defineProps<{ mode?: 'sapling' | 'fragment' }>(), { mode: 'fragment' })

const items = knownItems()
const fragmentRecipes = data.recipes.filter((r) => r.fragmentOf)
// Diamond fragments to diamonds is the starting example; file names differ between versions.
const initial = Math.max(
  fragmentRecipes.findIndex((r) => r.fragmentOf === 'diamond' && r.result.id === 'minecraft:diamond'),
  0,
)

const form = reactive({
  resource: 'diamond',
  tree: 'oak',
  type: props.mode === 'fragment' ? 'resourcestrees:strict_shaped' : 'minecraft:crafting_shaped',
  result: 'minecraft:diamond',
  count: 1,
  namespace: 'resourcestrees',
  /** Ingredients that can be painted onto the grid. */
  palette: [] as string[],
  /** Palette index per cell, left to right, top to bottom. */
  cells: Array<number | null>(9).fill(null),
  /** Legacy only: output format, and a custom datapack resource type (namespace:name) to use in the recipe. */
  format: LEGACY_FORMATS[0].id,
  customType: '',
})
const brush = ref(0)

/** Puts a recipe's shape into the grid, in the top-left corner like the recipe book. */
function load(r: Recipe) {
  const palette: string[] = []
  const cells = Array<number | null>(9).fill(null)
  const indexOf = (id: string) => {
    if (!palette.includes(id)) palette.push(id)
    return palette.indexOf(id)
  }
  if (r.pattern && r.key) {
    r.pattern.forEach((row, y) =>
      [...row].forEach((symbol, x) => {
        if (symbol !== ' ' && r.key![symbol]) cells[y * 3 + x] = indexOf(r.key![symbol][0])
      }),
    )
  }
  form.palette = palette.length ? palette : ['minecraft:stick']
  form.cells = cells
  form.result = r.result.id
  form.count = r.result.count
  brush.value = 0
}

function loadSapling() {
  const resource = data.resourceTypes.find((t) => t.name === form.resource)
  const tree = data.treeTypes.find((t) => t.name === form.tree)
  if (!resource || !tree) return
  const generated = saplingRecipe(resource, tree)
  load(generated)
}

if (props.mode === 'sapling') loadSapling()
else load(fragmentRecipes[initial])

function paint(i: number) {
  form.cells[i] = brush.value < 0 || form.cells[i] === brush.value ? null : brush.value
}

function addIngredient() {
  form.palette.push(props.mode === 'fragment' ? fragmentId(form.resource) : 'minecraft:diamond')
  brush.value = form.palette.length - 1
}

function removeIngredient(index: number) {
  form.palette.splice(index, 1)
  form.cells = form.cells.map((cell) => (cell === null || cell === index ? null : cell > index ? cell - 1 : cell))
  brush.value = Math.min(brush.value, form.palette.length - 1)
}

/** One letter per ingredient: its first letter when free, otherwise the next unused letter. */
const letters = computed(() => {
  const used = new Set<string>()
  return form.palette.map((id) => {
    const first = (id.split(':').pop() ?? 'x')[0].toUpperCase()
    const letter = /[A-Z]/.test(first) && !used.has(first) ? first : [...'ABCDEFGHIJKLMNOPQRSTUVWXYZ'].find((l) => !used.has(l))!
    used.add(letter)
    return letter
  })
})

/** The painted shape with empty outer rows and columns trimmed, as the game reads it. */
const pattern = computed(() => {
  const rows = [0, 1, 2].map((y) => [0, 1, 2].map((x) => form.cells[y * 3 + x]))
  const usedRows = [0, 1, 2].filter((y) => rows[y].some((c) => c !== null))
  const usedCols = [0, 1, 2].filter((x) => rows.some((row) => row[x] !== null))
  if (!usedRows.length) return []
  const cols = [usedCols[0], usedCols[usedCols.length - 1]]
  return rows
    .slice(usedRows[0], usedRows[usedRows.length - 1] + 1)
    .map((row) => row.slice(cols[0], cols[1] + 1).map((c) => (c === null ? ' ' : letters.value[c])).join(''))
})

const usedIngredients = computed(() => [...new Set(form.cells.filter((c): c is number => c !== null))].sort((a, b) => a - b))

// A legacy custom type replaces the chosen built-in one as the sapling's type.
const customName = computed(() => (legacy ? form.customType.split(':')[1] ?? '' : ''))
const resultId = computed(() => (props.mode === 'sapling' ? saplingId(customName.value || form.resource, form.tree) : form.result))

/** Legacy items carry the resource type as a component. Built-in types live in the resourcestrees namespace. */
const legacyTypes = computed(() => {
  const types = Object.fromEntries(data.resourceTypes.map((type) => [type.name, `resourcestrees:${type.name}`]))
  const custom = form.customType.match(/^([a-z0-9_.-]+):([a-z0-9_]+)$/)
  if (custom) types[custom[2]] = form.customType
  return types
})
const toLegacy = (id: string) => legacyItem(id, data.treeTypes.map((tree) => tree.name), legacyTypes.value)

function legacyJson() {
  const key: Record<string, unknown> = {}
  for (const index of usedIngredients.value) key[letters.value[index]] = legacyIngredient(toLegacy(form.palette[index]), form.format)
  return JSON.stringify(
    {
      type: form.type,
      category: 'building',
      key,
      pattern: pattern.value,
      result: legacyStack(toLegacy(resultId.value), form.count, form.format),
      ...legacyRecipeExtras(form.format),
    },
    null,
    2,
  )
}

const json = computed(() => {
  if (legacy) return legacyJson()
  const key: Record<string, string> = {}
  for (const index of usedIngredients.value) key[letters.value[index]] = form.palette[index]
  const result: Record<string, unknown> = { id: resultId.value }
  if (form.count > 1) result.count = form.count
  return JSON.stringify(
    { type: form.type, category: props.mode === 'sapling' ? 'misc' : 'building', key, pattern: pattern.value, result },
    null,
    2,
  )
})

const file = computed(() => {
  const recipes = legacy ? LEGACY_FORMATS.find((f) => f.id === form.format)!.folder : 'recipe'
  const path = resultId.value.split(':').pop()
  if (props.mode === 'sapling') return `data/resourcestrees/${recipes}/saplings/${path}.json`
  // Legacy built-in files are named <type>_fragment_to_<result>, newer ones <type>_leaf_fragment_to_<result>.
  const first = form.palette[usedIngredients.value[0] ?? 0]?.split(':').pop() ?? 'fragment'
  const fragment = legacy ? first.replace(/_leaf_fragment$/, '_fragment') : first
  const folder = form.namespace === 'resourcestrees' ? 'fragment_crafting/' : ''
  return `data/${form.namespace}/${recipes}/${folder}${fragment}_to_${path}.json`
})

const problems = computed(() => {
  const list: string[] = []
  if (!usedIngredients.value.length) list.push('Paint at least one ingredient onto the grid.')
  form.palette.forEach((id, i) => {
    if (usedIngredients.value.includes(i) && !ID.test(id.replace(/^#/, ''))) {
      list.push(`"${id}" should be an item id like minecraft:diamond, or a #tag.`)
    }
  })
  if (!ID.test(resultId.value)) list.push('The result should be an item id like minecraft:diamond.')
  if (!Number.isInteger(form.count) || form.count < 1 || form.count > 99) list.push('The count should be 1 to 99.')
  if (props.mode === 'fragment' && !/^[a-z0-9_.-]+$/.test(form.namespace)) list.push('The datapack namespace should be lowercase.')
  if (legacy && form.customType && !/^[a-z0-9_.-]+:[a-z0-9_]+$/.test(form.customType)) {
    list.push('The custom resource type should look like mypack:ruby.')
  }
  return list
})
</script>

<template>
  <div class="rt-builder">
    <div class="rt-grid">
      <div class="rt-fields">
        <div v-if="mode === 'sapling'" class="row">
          <label>
            <span>Resource type</span>
            <select v-model="form.resource" @change="loadSapling">
              <option v-for="type in data.resourceTypes" :key="type.name" :value="type.name">{{ type.label }}</option>
            </select>
          </label>
          <label>
            <span>Tree type</span>
            <select v-model="form.tree" @change="loadSapling">
              <option v-for="tree in data.treeTypes" :key="tree.name" :value="tree.name">{{ tree.label }}</option>
            </select>
          </label>
        </div>
        <template v-else>
          <label>
            <span>Start from a built-in recipe</span>
            <select @change="load(fragmentRecipes[Number(($event.target as HTMLSelectElement).value)])">
              <option v-for="(r, i) in fragmentRecipes" :key="r.id" :value="i" :selected="i === initial">
                {{ itemName(fragmentId(r.fragmentOf!)) }} → {{ itemName(r.result.id) }}
              </option>
            </select>
          </label>
          <div class="row">
            <label>
              <span>Result (<code>result.id</code>)</span>
              <input v-model.trim="form.result" list="rt-craft-items" spellcheck="false" />
            </label>
            <label>
              <span>Count</span>
              <input v-model.number="form.count" type="number" min="1" max="99" />
            </label>
          </div>
        </template>
        <label>
          <span>Recipe type (<code>type</code>)</span>
          <select v-model="form.type">
            <option value="resourcestrees:strict_shaped">resourcestrees:strict_shaped (exact shape, no mirroring)</option>
            <option value="minecraft:crafting_shaped">minecraft:crafting_shaped (vanilla shaped)</option>
          </select>
        </label>

        <datalist id="rt-craft-items">
          <option v-for="item in items" :key="item" :value="item" />
        </datalist>
        <fieldset>
          <legend>Ingredients <span class="hint">pick one, then click grid cells</span></legend>
          <div v-for="(id, i) in form.palette" :key="i" class="ingredient" :class="{ active: brush === i }" @click="brush = i">
            <ItemSlot :id="id" />
            <code class="letter">{{ letters[i] }}</code>
            <input v-model.trim="form.palette[i]" list="rt-craft-items" spellcheck="false" aria-label="Ingredient id" @focus="brush = i" />
            <button type="button" class="remove" :aria-label="`Remove ${id}`" @click.stop="removeIngredient(i)">✕</button>
          </div>
          <div class="buttons">
            <button type="button" class="small-button" @click="addIngredient">+ Add ingredient</button>
            <button type="button" class="small-button" :class="{ on: brush < 0 }" @click="brush = -1">Eraser</button>
            <button type="button" class="small-button" @click="form.cells = Array(9).fill(null)">Clear grid</button>
          </div>
        </fieldset>

        <div class="craft">
          <div class="grid" role="grid" aria-label="Crafting grid">
            <button v-for="(cell, i) in form.cells" :key="i" type="button" class="cell" :aria-label="`Cell ${i + 1}`" @click="paint(i)">
              <ItemSlot :id="cell === null ? null : form.palette[cell]" />
            </button>
          </div>
          <span class="arrow">➜</span>
          <ItemSlot :id="resultId" :count="form.count" label />
        </div>
        <p v-if="mode === 'fragment' && form.type === 'resourcestrees:strict_shaped'" class="hint">
          Strict: the empty cells inside the shape must stay empty, and the shape can't be mirrored.
        </p>
        <template v-if="legacy">
          <label>
            <span>Custom resource type <span class="hint">optional, namespace:name from your datapack</span></span>
            <input v-model.trim="form.customType" placeholder="mypack:ruby" spellcheck="false" />
          </label>
          <p v-if="form.customType" class="hint">
            Use <code>resourcestrees:{{ form.customType.split(':')[1] }}_leaf_fragment</code> or
            <code>resourcestrees:{{ form.customType.split(':')[1] }}_oak_sapling</code> as an item above to get this type's
            component in the output.
          </p>
          <label>
            <span>Minecraft version and loader</span>
            <select v-model="form.format">
              <option v-for="f in LEGACY_FORMATS" :key="f.id" :value="f.id">{{ f.label }}</option>
            </select>
          </label>
        </template>
        <label v-if="mode === 'fragment'">
          <span>Datapack namespace <span class="hint">resourcestrees replaces a built-in recipe with the same file name</span></span>
          <input v-model.trim="form.namespace" spellcheck="false" />
        </label>
      </div>

      <BuilderOutput :file="file" :text="json" :problems="problems">
        <template v-if="mode === 'sapling' && legacy">
          Put it in a datapack. A file with the same name replaces a built-in sapling recipe; custom resource types need one
          per tree shape.
        </template>
        <template v-else-if="mode === 'sapling'">Put it in a datapack. The mod then skips generating this sapling's recipe.</template>
        <template v-else>Put it in a datapack and run <code>/reload</code>.</template>
      </BuilderOutput>
    </div>
  </div>
</template>

<style scoped>
.ingredient {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px;
  border: 1px solid transparent;
  border-radius: 8px;
  cursor: pointer;
}

.ingredient.active {
  border-color: var(--vp-c-brand-1);
  background: var(--vp-c-brand-soft);
}

.ingredient input {
  flex: 1;
  min-width: 0;
}

.letter {
  flex: none;
  width: 22px;
  text-align: center;
}

.remove {
  flex: none;
  width: 26px;
  height: 26px;
  border-radius: 6px;
  color: var(--vp-c-text-3);
}

.remove:hover {
  background: var(--vp-c-danger-soft);
  color: var(--vp-c-danger-1);
}

.buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.small-button.on {
  border-color: var(--vp-c-danger-1);
  color: var(--vp-c-danger-1);
}

.craft {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 14px;
}

.grid {
  display: grid;
  grid-template-columns: repeat(3, 36px);
}

.cell {
  display: block;
  width: 36px;
  height: 36px;
  padding: 0;
  cursor: crosshair;
}

.cell:hover :deep(.rt-slot) {
  background: #a8a8a8;
}

.arrow {
  font-size: 22px;
  color: var(--vp-c-text-2);
}

p.hint {
  margin: 0;
}
</style>

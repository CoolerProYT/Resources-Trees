<script setup lang="ts">
import { computed, reactive } from 'vue'
import { ID, decimal, expectedCount, percent, saplingId, seconds } from '../resourcestrees'
import { LEGACY_FORMATS, legacyItem, legacyStack } from '../legacy'
import { useRt } from '../version'
import BuilderOutput from './BuilderOutput.vue'
import ItemSlot from './ItemSlot.vue'

const { data, fastestAxe, features, knownItems, resourceType, simulatorDrops, treeType } = useRt()
const legacy = features.legacy

const items = knownItems()

interface Drop {
  id: string
  count: number
  chance: number
  minRolls: number
  maxRolls: number
}

const form = reactive({
  resource: 'iron',
  tree: 'oak',
  ticksToGrow: 1200,
  drops: [] as Drop[],
  /** Legacy only: output format, and the datapack namespace of a custom resource type. */
  format: LEGACY_FORMATS[0].id,
  typeNamespace: 'resourcestrees',
})

/** Loads what the mod would generate for the chosen sapling. */
function useDefaults() {
  const resource = resourceType(form.resource)
  const tree = treeType(form.tree)
  if (!resource || !tree) return
  form.ticksToGrow = resource.treeSimulatorTicks
  form.drops = simulatorDrops(resource, tree).map((drop) => ({
    id: drop.item,
    count: 1,
    chance: Math.round(drop.chance * 10000) / 10000,
    minRolls: drop.minRolls,
    maxRolls: drop.maxRolls,
  }))
}
useDefaults()

/** Accepts any `resourcestrees:<resource>_<tree>_sapling` id, including custom types. */
function setSapling(value: string) {
  const match = value.trim().match(/^resourcestrees:([a-z0-9_]+)_sapling$/)
  if (!match) return
  const tree = data.treeTypes.find((t) => match[1].endsWith(`_${t.name}`))?.name ?? match[1].split('_').pop()!
  const resource = match[1].slice(0, -(tree.length + 1))
  if (!resource) return
  form.tree = tree
  form.resource = resource
  useDefaults()
}

/** Legacy: a custom datapack resource type, as namespace:name. */
function setLegacyType(value: string) {
  const match = value.trim().match(/^([a-z0-9_.-]+):([a-z0-9_]+)$/)
  if (!match) return
  form.typeNamespace = match[1]
  form.resource = match[2]
  useDefaults()
}

const sapling = computed(() => saplingId(form.resource, form.tree))
const folder = computed(() => (legacy ? LEGACY_FORMATS.find((f) => f.id === form.format)!.folder : 'recipe'))
const file = computed(() => `data/resourcestrees/${folder.value}/tree_simulator/${sapling.value.split(':')[1]}.json`)

/** Legacy items carry the resource type as a component; built-in types live in the resourcestrees namespace. */
const legacyTypes = computed(() => {
  const types = Object.fromEntries(data.resourceTypes.map((type) => [type.name, `resourcestrees:${type.name}`]))
  if (!resourceType(form.resource)) types[form.resource] = `${form.typeNamespace}:${form.resource}`
  return types
})
const toLegacy = (id: string) => legacyItem(id, data.treeTypes.map((tree) => tree.name), legacyTypes.value)

function legacyJson() {
  return JSON.stringify(
    {
      type: 'resourcestrees:tree_simulator',
      drops: form.drops.map((drop) => ({
        chance: drop.chance,
        maxRolls: drop.maxRolls,
        minRolls: drop.minRolls,
        output: legacyStack(toLegacy(drop.id), drop.count, form.format),
      })),
      ticksToGrow: form.ticksToGrow,
      tree: legacyStack(toLegacy(sapling.value), 1, form.format),
    },
    null,
    2,
  )
}

const add = () => form.drops.push({ id: 'minecraft:iron_ingot', count: 1, chance: 0.5, minRolls: 1, maxRolls: 1 })

const json = computed(() =>
  legacy ? legacyJson() : JSON.stringify(
    {
      type: 'resourcestrees:tree_simulator',
      tree: { id: sapling.value, count: 1 },
      ticksToGrow: form.ticksToGrow,
      drops: form.drops.map((drop) => ({
        output: drop.count === 1 ? { id: drop.id } : { id: drop.id, count: drop.count },
        chance: drop.chance,
        minRolls: drop.minRolls,
        maxRolls: drop.maxRolls,
      })),
    },
    null,
    2,
  ),
)

const problems = computed(() => {
  const list: string[] = []
  if (!Number.isInteger(form.ticksToGrow) || form.ticksToGrow < 1) list.push('Grow time should be a whole number of ticks, at least 1.')
  if (!form.drops.length) list.push('Add at least one drop.')
  if (legacy && !/^[a-z0-9_.-]+$/.test(form.typeNamespace)) list.push('The resource type namespace should be lowercase.')
  form.drops.forEach((drop, i) => {
    const n = `Drop ${i + 1}`
    if (!ID.test(drop.id)) list.push(`${n}: the item should be an id like minecraft:iron_ingot.`)
    if (!Number.isInteger(drop.count) || drop.count < 1 || drop.count > 99) list.push(`${n}: count should be 1 to 99.`)
    if (!(drop.chance >= 0 && drop.chance <= 1)) list.push(`${n}: chance should be between 0 and 1.`)
    if (!Number.isInteger(drop.minRolls) || !Number.isInteger(drop.maxRolls) || drop.minRolls < 0) {
      list.push(`${n}: rolls should be whole numbers, 0 or more.`)
    } else if (drop.minRolls > drop.maxRolls) list.push(`${n}: min rolls can't be more than max rolls.`)
  })
  return list
})

const expected = (drop: Drop) =>
  expectedCount({ item: drop.id, chance: drop.chance, minRolls: drop.minRolls, maxRolls: drop.maxRolls }) * drop.count
</script>

<template>
  <div class="rt-builder">
    <div class="rt-grid">
      <div class="rt-fields">
        <fieldset>
          <legend><ItemSlot :id="sapling" /> Sapling (<code>tree</code>)</legend>
          <div class="row">
            <label>
              <span>Resource type</span>
              <select v-model="form.resource" @change="useDefaults">
                <option v-for="type in data.resourceTypes" :key="type.name" :value="type.name">{{ type.label }}</option>
                <option v-if="!resourceType(form.resource)" :value="form.resource">{{ form.resource }} (custom)</option>
              </select>
            </label>
            <label>
              <span>Tree type</span>
              <select v-model="form.tree" @change="useDefaults">
                <option v-for="tree in data.treeTypes" :key="tree.name" :value="tree.name">{{ tree.label }}</option>
                <option v-if="!treeType(form.tree)" :value="form.tree">{{ form.tree }} (custom)</option>
              </select>
            </label>
          </div>
          <label v-if="legacy">
            <span>Or a custom resource type <span class="hint">namespace:name from your datapack</span></span>
            <input
              :value="`${resourceType(form.resource) ? 'resourcestrees' : form.typeNamespace}:${form.resource}`"
              spellcheck="false"
              @change="setLegacyType(($event.target as HTMLInputElement).value)"
            />
          </label>
          <label v-else>
            <span>Or type any sapling id <span class="hint">for custom resource or tree types</span></span>
            <input
              :value="sapling"
              spellcheck="false"
              @change="setSapling(($event.target as HTMLInputElement).value)"
            />
          </label>
        </fieldset>
        <label>
          <span>
            Grow time (<code>ticksToGrow</code>)
            <span class="hint">{{ seconds(form.ticksToGrow || 0) }} s, {{ seconds(Math.trunc((form.ticksToGrow || 0) / fastestAxe)) }} s with the fastest axe</span>
          </span>
          <input v-model.number="form.ticksToGrow" type="number" min="1" step="100" />
        </label>
        <datalist id="rt-sim-items">
          <option v-for="item in items" :key="item" :value="item" />
        </datalist>
        <fieldset>
          <legend>Drops (<code>drops</code>)</legend>
          <div v-for="(drop, i) in form.drops" :key="i" class="drop">
            <ItemSlot :id="drop.id" :count="drop.count" />
            <div class="drop-fields">
              <div class="line">
                <input v-model.trim="drop.id" list="rt-sim-items" spellcheck="false" aria-label="Item id" />
                <button type="button" class="remove" :aria-label="`Remove ${drop.id}`" @click="form.drops.splice(i, 1)">✕</button>
              </div>
              <div class="nums">
                <label><span>Count</span><input v-model.number="drop.count" type="number" min="1" max="99" /></label>
                <label><span>Chance</span><input v-model.number="drop.chance" type="number" min="0" max="1" step="0.05" /></label>
                <label><span>Min rolls</span><input v-model.number="drop.minRolls" type="number" min="0" /></label>
                <label><span>Max rolls</span><input v-model.number="drop.maxRolls" type="number" min="0" /></label>
              </div>
              <span class="hint">{{ percent(drop.chance || 0) }} per roll · about {{ decimal(expected(drop) || 0) }} per harvest</span>
            </div>
          </div>
          <div class="buttons">
            <button type="button" class="small-button" @click="add">+ Add drop</button>
            <button type="button" class="small-button" @click="useDefaults">Reset to {{ legacy ? 'built-in' : 'generated' }} drops</button>
          </div>
        </fieldset>
        <label v-if="legacy">
          <span>Minecraft version and loader</span>
          <select v-model="form.format">
            <option v-for="f in LEGACY_FORMATS" :key="f.id" :value="f.id">{{ f.label }}</option>
          </select>
        </label>
      </div>

      <BuilderOutput :file="file" :text="json" :problems="problems">
        <template v-if="legacy">
          Put it in a datapack. Built-in saplings already have a recipe with these drops, and a file with the same name replaces
          it. Custom resource types have none until you add one per tree shape.
        </template>
        <template v-else>
          Put it in a datapack. It replaces the recipe the mod generates for this sapling.
          <template v-if="features.fortune">Fortune on the axe adds a roll to every drop.</template>
        </template>
      </BuilderOutput>
    </div>
  </div>
</template>

<style scoped>
.drop {
  display: flex;
  gap: 10px;
  padding-bottom: 10px;
  border-bottom: 1px dashed var(--vp-c-divider);
}

.drop-fields {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.line {
  display: flex;
  gap: 6px;
}

.nums {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 6px;
}

.nums label span {
  font-size: 11px;
  font-weight: 400;
  color: var(--vp-c-text-2);
}

.remove {
  flex: none;
  width: 30px;
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
</style>

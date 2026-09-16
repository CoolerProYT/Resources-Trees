<script setup lang="ts">
import { computed, reactive } from 'vue'
import { ID, fragmentIcon, leavesIcon, percent, saplingIcon, seconds, signedColor } from '../resourcestrees'
import { useRt } from '../version'
import BuilderOutput from './BuilderOutput.vue'
import ItemSlot from './ItemSlot.vue'

const { data, features, knownItems, link, resourceType } = useRt()
const legacy = features.legacy

const items = knownItems()
const tags = ['#minecraft:logs', '#minecraft:planks', '#minecraft:wool', '#minecraft:saplings', '#minecraft:leaves', '#minecraft:flowers']

const EXAMPLE = {
  name: 'ruby',
  label: 'Ruby',
  material: 'minecraft:redstone',
  color: '#cc0000',
  colorInt: '',
  saplingDropChance: 0.1,
  leafDropChance: 0.2,
  treeSimulatorTicks: 1400,
  weight: 4,
}

/** `namespace` is the legacy datapack namespace; the resource type id becomes namespace:name. */
const form = reactive({ ...EXAMPLE, writeName: true, namespace: 'mypack' })

/** Starts the form from a built-in type. */
function copyFrom(name: string) {
  const type = resourceType(name)
  if (!type) return Object.assign(form, EXAMPLE)
  Object.assign(form, {
    name: `my_${type.name}`,
    label: `My ${type.label}`,
    material: type.material,
    color: type.color,
    colorInt: '',
    saplingDropChance: type.saplingDropChance,
    leafDropChance: type.leafDropChance,
    treeSimulatorTicks: type.treeSimulatorTicks,
    weight: type.weight ?? EXAMPLE.weight,
  })
}

function fromInt() {
  const value = Number(form.colorInt.trim())
  if (form.colorInt.trim() && Number.isInteger(value)) form.color = `#${((value >>> 0) & 0xffffff).toString(16).padStart(6, '0')}`
}

const json = computed(() => {
  const entry: Record<string, unknown> = {}
  if (form.writeName && !legacy) entry.name = form.name
  Object.assign(entry, {
    material: form.material,
    color: signedColor(form.color),
    ...(legacy ? { weight: form.weight } : {}),
    saplingDropChance: form.saplingDropChance,
    leafDropChance: form.leafDropChance,
    treeSimulatorTicks: form.treeSimulatorTicks,
  })
  return JSON.stringify(entry, null, 2)
})

// Legacy versions translate `type.resourcestrees.<name>` whatever the datapack namespace.
const langKey = computed(() => (legacy ? `type.resourcestrees.${form.name}` : `resources_type.resourcestrees.${form.name}`))
const lang = computed(() => JSON.stringify({ [langKey.value]: form.label }, null, 2))
const file = computed(() =>
  legacy
    ? `data/${form.namespace}/resourcestrees/resources_type/${form.name}.json`
    : `config/resourcestrees/resources_type/${form.name}.json`,
)

const problems = computed(() => {
  const list: string[] = []
  if (!/^[a-z0-9_]+$/.test(form.name)) list.push('The name should be lowercase letters, digits and underscores.')
  else if (resourceType(form.name)) list.push(`"${form.name}" is already a built-in resource type.`)
  const material = form.material.replace(/^#/, '')
  if (!ID.test(material)) list.push('The material should be an item id like minecraft:redstone, or a tag like #minecraft:logs.')
  for (const [key, label] of [
    ['saplingDropChance', 'Sapling drop chance'],
    ['leafDropChance', 'Fragment drop chance'],
  ] as const) {
    const value = form[key]
    if (typeof value !== 'number' || value < 0 || value > 1) list.push(`${label} should be between 0 and 1.`)
  }
  if (!Number.isInteger(form.treeSimulatorTicks) || form.treeSimulatorTicks < 1) list.push('Grow time should be a whole number of ticks, at least 1.')
  if (legacy && !/^[a-z0-9_.-]+$/.test(form.namespace)) list.push('The datapack namespace should be lowercase letters, digits and underscores.')
  if (legacy && !Number.isInteger(form.weight)) list.push('Weight should be a whole number. It is required but has no effect.')
  return list
})

const bestAxe = Math.max(...data.axes.map((axe) => axe.multiplier))
</script>

<template>
  <div class="rt-builder">
    <div class="rt-grid">
      <div class="rt-fields">
        <label>
          <span>Start from a built-in type</span>
          <select @change="copyFrom(($event.target as HTMLSelectElement).value)">
            <option value="">Ruby (example)</option>
            <option v-for="type in data.resourceTypes" :key="type.name" :value="type.name">{{ type.label }}</option>
          </select>
        </label>
        <div class="row">
          <label>
            <span>Name (<code>name</code>)</span>
            <input v-model.trim="form.name" spellcheck="false" />
          </label>
          <label>
            <span>Display name</span>
            <input v-model="form.label" spellcheck="false" />
          </label>
        </div>
        <label v-if="legacy">
          <span>Datapack namespace <span class="hint">the type's id is {{ form.namespace }}:{{ form.name }}</span></span>
          <input v-model.trim="form.namespace" spellcheck="false" />
        </label>
        <label v-else class="check">
          <input v-model="form.writeName" type="checkbox" />
          Write <code>name</code> into the file (otherwise the file name is used)
        </label>
        <label>
          <span>Material (<code>material</code>) <span class="hint">item id, or #tag</span></span>
          <input v-model.trim="form.material" list="rt-resource-materials" spellcheck="false" />
          <datalist id="rt-resource-materials">
            <option v-for="tag in tags" :key="tag" :value="tag" />
            <option v-for="item in items" :key="item" :value="item" />
          </datalist>
        </label>
        <div class="rt-field">
          <span>Colour (<code>color</code>)</span>
          <div class="color">
            <input v-model="form.color" type="color" aria-label="Colour" />
            <code>{{ form.color }}</code>
            <code class="int">{{ signedColor(form.color) }}</code>
            <input v-model="form.colorInt" placeholder="or paste an int" aria-label="Colour as int" @input="fromInt" />
          </div>
        </div>
        <div class="row">
          <label>
            <span>Sapling drop <span class="hint">{{ percent(form.saplingDropChance || 0) }}</span></span>
            <input v-model.number="form.saplingDropChance" type="number" min="0" max="1" step="0.005" />
          </label>
          <label>
            <span>Fragment drop <span class="hint">{{ percent(form.leafDropChance || 0) }}</span></span>
            <input v-model.number="form.leafDropChance" type="number" min="0" max="1" step="0.005" />
          </label>
          <label v-if="legacy">
            <span>Weight <span class="hint">required, unused</span></span>
            <input v-model.number="form.weight" type="number" min="0" step="1" />
          </label>
          <label>
            <span>Grow time <span class="hint">{{ seconds(form.treeSimulatorTicks || 0) }} s</span></span>
            <input v-model.number="form.treeSimulatorTicks" type="number" min="1" step="100" />
          </label>
        </div>
        <div class="rt-field">
          <span>Preview</span>
          <div class="rt-preview">
            <ItemSlot
              v-for="tree in data.treeTypes"
              :id="`resourcestrees:${form.name}_${tree.name}_sapling`"
              :key="`s-${tree.name}`"
              :icon="saplingIcon(tree.name, form.color)"
              :name="`${form.label} ${tree.label} Sapling`"
            />
          </div>
          <div class="rt-preview">
            <ItemSlot
              v-for="tree in data.treeTypes"
              :id="`resourcestrees:${form.name}_${tree.name}_leaves`"
              :key="`l-${tree.name}`"
              :icon="leavesIcon(tree.name, form.color)"
              :name="`${form.label} ${tree.label} Leaves`"
            />
          </div>
          <div class="rt-preview">
            <ItemSlot :id="`resourcestrees:${form.name}_leaf_fragment`" :icon="fragmentIcon(form.color)" :name="`${form.label} Leaf Fragment`" label />
            <span class="hint">· {{ seconds(Math.trunc((form.treeSimulatorTicks || 0) / bestAxe)) }} s per harvest with the fastest axe</span>
          </div>
        </div>
      </div>

      <div class="stack">
        <BuilderOutput :file="file" :text="json" :problems="problems">
          <template v-if="legacy">
            Put it in a datapack on the server; players receive it when they join. The type works with every built-in sapling
            and leaves, but has no recipes yet: add a <a :href="link('datapacks/sapling-recipes')">sapling recipe</a> and a
            <a :href="link('datapacks/tree-simulator-recipes')">Tree Simulator recipe</a> for each tree shape you want, and
            <a :href="link('datapacks/leaf-fragment-recipes')">fragment recipes</a>.
          </template>
          <template v-else>
            Put the same file on the server and every client. It adds {{ data.treeTypes.length }} saplings,
            {{ data.treeTypes.length }} leaves and a leaf fragment, with their recipes.
          </template>
        </BuilderOutput>
        <BuilderOutput label="Display name, in a resource pack at" file="assets/resourcestrees/lang/en_us.json" :text="lang">
          Without it, the name shows as <code>{{ legacy ? langKey : form.name }}</code>.
        </BuilderOutput>
      </div>
    </div>
  </div>
</template>

<style scoped>
.color {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px;
}

.color input[type='color'] {
  width: 44px;
  height: 32px;
  padding: 0;
  border: 1px solid var(--vp-c-divider);
  border-radius: 6px;
  background: none;
  cursor: pointer;
}

.color input:not([type='color']) {
  flex: 1 1 120px;
  width: auto !important;
}

.int {
  color: var(--vp-c-brand-1);
}

.stack {
  display: flex;
  flex-direction: column;
  gap: 16px;
  min-width: 0;
}
</style>

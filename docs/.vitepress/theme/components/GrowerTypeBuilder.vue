<script setup lang="ts">
import { computed, reactive } from 'vue'
import { ID, percent } from '../resourcestrees'
import { VANILLA_GROWERS, VANILLA_TREE_FEATURES, type GrowerPreset, type WeightedFeature } from '../vanilla'
import { useRt } from '../version'
import BuilderOutput from './BuilderOutput.vue'

const { link } = useRt()

type ListKey = 'trees' | 'megaTrees' | 'flowerTrees'

const LISTS: { key: ListKey; label: string; hint: string }[] = [
  { key: 'trees', label: 'Normal trees', hint: 'one sapling' },
  { key: 'megaTrees', label: 'Mega trees', hint: 'four saplings in a 2×2 square' },
  { key: 'flowerTrees', label: 'Flower trees', hint: 'chosen near flowers, e.g. trees with bee nests' },
]

const clone = (preset: GrowerPreset) => ({
  trees: preset.trees.map((entry) => ({ ...entry })),
  megaTrees: preset.megaTrees.map((entry) => ({ ...entry })),
  flowerTrees: preset.flowerTrees.map((entry) => ({ ...entry })),
  shortestTreeType: preset.shortestTreeType ?? '',
})

const form = reactive({ name: 'maple', ...clone(VANILLA_GROWERS[0]) })

function copyFrom(name: string) {
  const preset = VANILLA_GROWERS.find((grower) => grower.name === name)
  if (preset) Object.assign(form, clone(preset))
}

const add = (key: ListKey) => form[key].push({ data: 'minecraft:oak', weight: 1 })
const remove = (key: ListKey, index: number) => form[key].splice(index, 1)

function share(list: WeightedFeature[], entry: WeightedFeature) {
  const total = list.reduce((sum, e) => sum + (Number(e.weight) || 0), 0)
  return total > 0 ? percent((Number(entry.weight) || 0) / total) : '—'
}

const json = computed(() => {
  const entry: Record<string, unknown> = { name: form.name }
  for (const { key } of LISTS) entry[key] = form[key].map((e) => ({ data: e.data, weight: e.weight }))
  if (form.shortestTreeType) entry.shortestTreeType = form.shortestTreeType
  return JSON.stringify(entry, null, 2)
})

const problems = computed(() => {
  const list: string[] = []
  if (!/^[a-z0-9_]+$/.test(form.name)) list.push('The name should be lowercase letters, digits and underscores.')
  for (const { key, label } of LISTS) {
    for (const entry of form[key]) {
      if (!ID.test(entry.data)) list.push(`${label}: "${entry.data}" should be a feature id like minecraft:oak.`)
      if (!Number.isInteger(entry.weight) || entry.weight < 0) list.push(`${label}: weights should be whole numbers, 0 or more.`)
    }
  }
  if (form.shortestTreeType && !ID.test(form.shortestTreeType)) list.push('The fallback tree should be a feature id, or empty.')
  if (!form.trees.length && !form.megaTrees.length) list.push('Add at least one normal or mega tree, or the sapling can never grow.')
  return [...new Set(list)]
})
</script>

<template>
  <div class="rt-builder">
    <div class="rt-grid">
      <div class="rt-fields">
        <label>
          <span>Start from a vanilla grower</span>
          <select @change="copyFrom(($event.target as HTMLSelectElement).value)">
            <option v-for="grower in VANILLA_GROWERS" :key="grower.name" :value="grower.name">{{ grower.name }}</option>
          </select>
        </label>
        <label>
          <span>Name (<code>name</code>) <span class="hint">what a tree type's treeGrowerName points to</span></span>
          <input v-model.trim="form.name" spellcheck="false" />
        </label>
        <datalist id="rt-tree-features">
          <option v-for="feature in VANILLA_TREE_FEATURES" :key="feature" :value="feature" />
        </datalist>
        <fieldset v-for="list in LISTS" :key="list.key">
          <legend><code>{{ list.key }}</code> <span class="hint">{{ list.hint }}</span></legend>
          <div v-for="(entry, i) in form[list.key]" :key="i" class="entry">
            <input v-model.trim="entry.data" list="rt-tree-features" spellcheck="false" aria-label="Feature id" />
            <input v-model.number="entry.weight" type="number" min="0" aria-label="Weight" class="weight" />
            <span class="share">{{ share(form[list.key], entry) }}</span>
            <button type="button" class="remove" :aria-label="`Remove ${entry.data}`" @click="remove(list.key, i)">✕</button>
          </div>
          <p v-if="!form[list.key].length" class="hint">Empty ({{ list.label.toLowerCase() }} never grow).</p>
          <button type="button" class="small-button" @click="add(list.key)">+ Add feature</button>
        </fieldset>
        <label>
          <span>Fallback when there's no room (<code>shortestTreeType</code>) <span class="hint">empty for none</span></span>
          <input v-model.trim="form.shortestTreeType" list="rt-tree-features" spellcheck="false" />
        </label>
      </div>

      <BuilderOutput :file="`config/resourcestrees/grower_type/${form.name}.json`" :text="json" :problems="problems">
        Only the server needs this file. Point a <a :href="link('datapacks/tree-types')">tree type</a>'s <code>treeGrowerName</code> at
        <code>{{ form.name }}</code> to use it.
      </BuilderOutput>
    </div>
  </div>
</template>

<style scoped>
.entry {
  display: flex;
  align-items: center;
  gap: 6px;
}

.entry input:first-child {
  flex: 1;
  min-width: 0;
}

.entry .weight {
  width: 70px !important;
  flex: none;
}

.share {
  width: 48px;
  flex: none;
  text-align: right;
  font: 12px var(--vp-font-family-mono);
  color: var(--vp-c-text-2);
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

p.hint {
  margin: 0;
}
</style>

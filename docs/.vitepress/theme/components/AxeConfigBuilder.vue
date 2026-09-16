<script setup lang="ts">
import { computed, reactive } from 'vue'
import { ID, cycleTicks, seconds } from '../resourcestrees'
import { useRt } from '../version'
import BuilderOutput from './BuilderOutput.vue'
import ItemSlot from './ItemSlot.vue'

const { data, features } = useRt()

const defaults = () => data.axes.map((axe) => ({ item: axe.item, multiplier: axe.multiplier }))

const form = reactive({
  axes: defaults(),
  useAxeDurability: true,
  /** Legacy JSON only: the option exists from 1.21.1 NeoForge 1.8.0. */
  writeDurability: false,
  ticks: 1200,
})

const file = features.configFormat === 'json' ? 'config/resourcestrees/axe.json' : 'config/resourcestrees-common.conf'
const axeKey = features.configFormat === 'json' ? 'values' : 'treeSimulator.axe'
const durabilityKey = features.configFormat === 'json' ? 'useAxeDurability' : 'treeSimulator.useAxeDurability'
const showDurability = computed(() => features.configFormat === 'hocon' ? features.axeDurabilityOption : form.writeDurability)

const add = () => form.axes.push({ item: 'mymod:titanium_axe', multiplier: 8 })

/** The legacy file is written by Gson: the durability flag first, then the map, with decimal numbers. */
function legacyJson() {
  const values = Object.fromEntries(form.axes.map((axe) => [axe.item, axe.multiplier]))
  const body = JSON.stringify(form.writeDurability ? { useAxeDurability: form.useAxeDurability, values } : { values }, null, 2)
  return body.replace(/": (\d+)(,?)$/gm, '": $1.0$2')
}

const text = computed(() => {
  if (features.configFormat === 'json') return legacyJson()
  const lines = form.axes.map((axe) => `    "${axe.item}": ${axe.multiplier}`)
  return [
    '# ResourcesTrees Common Config',
    'treeSimulator {',
    '  # The Tree Simulator growth speed scales with the type of axe placed in the axe slot.',
    '  # The key of each entry should be a valid axe item id.',
    '  axe {',
    ...lines,
    '  }',
    ...(features.axeDurabilityOption
      ? ['  # Whether the Tree Simulator damages the axe placed in the axe slot on every harvest.', `  useAxeDurability = ${form.useAxeDurability}`]
      : []),
    '}',
  ].join('\n')
})

const problems = computed(() => {
  const list: string[] = []
  const seen = new Set<string>()
  for (const axe of form.axes) {
    if (!ID.test(axe.item)) list.push(`"${axe.item}" should be an item id like minecraft:iron_axe.`)
    if (seen.has(axe.item)) list.push(`${axe.item} is listed twice.`)
    seen.add(axe.item)
    if (typeof axe.multiplier !== 'number' || !(axe.multiplier > 0)) list.push(`${axe.item}: the multiplier should be a number above 0.`)
  }
  return list
})
</script>

<template>
  <div class="rt-builder">
    <div class="rt-grid">
      <div class="rt-fields">
        <fieldset>
          <legend>Axes (<code>{{ axeKey }}</code>)</legend>
          <div class="head">
            <span>Axe</span>
            <span>Multiplier</span>
            <span>Harvest</span>
          </div>
          <div v-for="(axe, i) in form.axes" :key="i" class="axe">
            <ItemSlot :id="axe.item" />
            <input v-model.trim="axe.item" spellcheck="false" aria-label="Axe item id" />
            <input v-model.number="axe.multiplier" type="number" min="0.1" step="0.5" aria-label="Multiplier" />
            <span class="time">{{ axe.multiplier > 0 ? `${seconds(cycleTicks(form.ticks, axe.multiplier))} s` : '—' }}</span>
            <button type="button" class="remove" :aria-label="`Remove ${axe.item}`" @click="form.axes.splice(i, 1)">✕</button>
          </div>
          <div class="buttons">
            <button type="button" class="small-button" @click="add">+ Add axe</button>
            <button type="button" class="small-button" @click="form.axes = defaults()">Reset to defaults</button>
          </div>
          <label>
            <span>Harvest times for a grow time of</span>
            <select v-model.number="form.ticks">
              <option v-for="t in [...new Set(data.resourceTypes.map((type) => type.treeSimulatorTicks))].sort((a, b) => a - b)" :key="t" :value="t">
                {{ t }} ticks
              </option>
            </select>
          </label>
        </fieldset>
        <label v-if="features.configFormat === 'json'" class="check">
          <input v-model="form.writeDurability" type="checkbox" />
          Include <code>useAxeDurability</code> (1.21.1 NeoForge 1.8.0 and later only)
        </label>
        <label v-if="showDurability" class="check">
          <input v-model="form.useAxeDurability" type="checkbox" />
          Damage the axe on every harvest (<code>{{ durabilityKey }}</code>)
        </label>
      </div>

      <BuilderOutput :file="file" :text="text" :problems="problems">
        <template v-if="features.configFormat === 'json'">
          Replace the file's contents and restart the game. Axes you leave out keep their default multiplier.
        </template>
        <template v-else>
          Replace the file's contents. The mod picks up changes while the game is running. An axe you leave out still works,
          at the base grow time.
        </template>
      </BuilderOutput>
    </div>
  </div>
</template>

<style scoped>
.head,
.axe {
  display: grid;
  grid-template-columns: 36px minmax(0, 1fr) 64px 44px 26px;
  align-items: center;
  gap: 4px;
}

.head {
  font-size: 12px;
  color: var(--vp-c-text-2);
}

.head span:first-child {
  grid-column: 1 / 3;
}

.time {
  font: 12px var(--vp-font-family-mono);
  color: var(--vp-c-text-2);
  text-align: right;
}

.remove {
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
</style>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { fragmentId, leavesId, percent, saplingId, seconds } from '../resourcestrees'
import { useRt } from '../version'
import ItemSlot from './ItemSlot.vue'
import TreePicker from './TreePicker.vue'

const { data, features, itemName } = useRt()

type Sort = 'default' | 'name' | 'ticks'

const tree = ref(data.treeTypes[0]?.name ?? '')
const query = ref('')
const sort = ref<Sort>('default')

const rows = computed(() => {
  const q = query.value.trim().toLowerCase()
  const list = data.resourceTypes.filter(
    (type) => !q || type.name.includes(q) || type.label.toLowerCase().includes(q) || itemName(type.material).toLowerCase().includes(q),
  )
  if (sort.value === 'name') return [...list].sort((a, b) => a.label.localeCompare(b.label))
  if (sort.value === 'ticks') return [...list].sort((a, b) => a.treeSimulatorTicks - b.treeSimulatorTicks)
  return list
})

const isDefault = (key: 'saplingDropChance' | 'leafDropChance', value: number) => value === data.defaults[key]
</script>

<template>
  <div class="rt-panel">
    <div class="controls">
      <input v-model="query" type="search" placeholder="Filter by name or material…" aria-label="Filter resource types" />
      <label>
        Sort
        <select v-model="sort">
          <option value="default">Registration order</option>
          <option value="name">Name</option>
          <option value="ticks">Grow time</option>
        </select>
      </label>
      <span class="count">{{ rows.length }} of {{ data.resourceTypes.length }}</span>
    </div>
    <TreePicker v-model="tree" label="Show as" />

    <div class="table-wrap">
      <table>
        <thead>
          <tr>
            <th>Type</th>
            <th>Sapling · Leaves · Fragment</th>
            <th>Material</th>
            <th>Grow time</th>
            <th>Sapling drop</th>
            <th>Fragment drop</th>
            <th v-if="features.legacy">Weight</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="type in rows" :key="type.name">
            <td>
              <div class="type">
                <span class="swatch" :style="{ background: type.color }" :title="type.color" />
                <div>
                  <strong>{{ type.label }}</strong>
                  <code>{{ features.legacy ? `resourcestrees:${type.name}` : type.name }}</code>
                </div>
              </div>
            </td>
            <td class="icons">
              <ItemSlot :id="saplingId(type.name, tree)" />
              <ItemSlot :id="leavesId(type.name, tree)" />
              <ItemSlot :id="fragmentId(type.name)" />
            </td>
            <td><ItemSlot :id="type.material" label /></td>
            <td class="num">
              {{ type.treeSimulatorTicks }} <span class="unit">ticks</span>
              <div class="sub">{{ seconds(type.treeSimulatorTicks) }} s</div>
            </td>
            <td class="num" :class="{ changed: !isDefault('saplingDropChance', type.saplingDropChance) }">
              {{ percent(type.saplingDropChance) }}
            </td>
            <td class="num" :class="{ changed: !isDefault('leafDropChance', type.leafDropChance) }">
              {{ percent(type.leafDropChance) }}
            </td>
            <td v-if="features.legacy" class="num">{{ type.weight }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <p class="legend">Grow time is before the axe speeds it up. Highlighted chances differ from the defaults.</p>
  </div>
</template>

<style scoped>
.controls {
  display: flex;
  flex-wrap: wrap;
  gap: 10px 18px;
  align-items: center;
  margin-bottom: 10px;
  font-size: 14px;
}

.controls input {
  flex: 1 1 220px;
  padding: 6px 10px;
  border: 1px solid var(--vp-c-divider);
  border-radius: 8px;
  background: var(--vp-c-bg);
}

.controls label {
  display: flex;
  align-items: center;
  gap: 8px;
}

.count {
  color: var(--vp-c-text-2);
}

.table-wrap {
  margin-top: 12px;
  max-height: 640px;
  overflow: auto;
  border: 1px solid var(--vp-c-divider);
  border-radius: 8px;
}

.table-wrap table {
  display: table;
  width: 100%;
  margin: 0;
  border: none;
}

.table-wrap thead th {
  position: sticky;
  top: 0;
  z-index: 1;
  background: var(--vp-c-bg-soft);
  white-space: nowrap;
}

.table-wrap td {
  vertical-align: middle;
}

.type {
  display: flex;
  align-items: center;
  gap: 10px;
  white-space: nowrap;
}

.type strong,
.type code {
  display: block;
}

.type code {
  margin-top: 2px;
  font-size: 12px;
}

.swatch {
  width: 14px;
  height: 36px;
  flex: none;
  border-radius: 3px;
  border: 1px solid var(--vp-c-divider);
}

.icons {
  white-space: nowrap;
}

.icons > * + * {
  margin-left: 4px;
}

.num {
  font-family: var(--vp-font-family-mono);
  white-space: nowrap;
}

.unit,
.sub {
  color: var(--vp-c-text-3);
  font-size: 12px;
}

.changed {
  color: var(--vp-c-brand-1);
  font-weight: 700;
}

.legend {
  margin: 10px 0 0;
  font-size: 13px;
  color: var(--vp-c-text-2);
}
</style>

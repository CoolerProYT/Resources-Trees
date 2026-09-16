<script setup lang="ts">
import { computed, ref } from 'vue'
import { cycleTicks, seconds } from '../resourcestrees'
import { useRt } from '../version'
import ItemSlot from './ItemSlot.vue'

const { data, features } = useRt()

const presets = [...new Set(data.resourceTypes.map((type) => type.treeSimulatorTicks))].sort((a, b) => a - b)
const ticks = ref(presets.includes(1200) ? 1200 : presets[0])
const efficiency = ref(0)

const typesWith = computed(() =>
  data.resourceTypes
    .filter((type) => type.treeSimulatorTicks === ticks.value)
    .map((type) => type.label)
    .join(', '),
)
const fastest = Math.max(...data.axes.map((axe) => axe.multiplier))
</script>

<template>
  <div class="rt-panel">
    <div class="controls">
      <label>
        Grow time
        <select v-model.number="ticks">
          <option v-for="value in presets" :key="value" :value="value">{{ value }} ticks</option>
        </select>
      </label>
      <label v-if="features.efficiency">
        Efficiency
        <select v-model.number="efficiency">
          <option v-for="level in 6" :key="level" :value="level - 1">{{ level - 1 || 'None' }}</option>
        </select>
      </label>
    </div>
    <p class="types">Used by {{ typesWith }}.</p>
    <table>
      <thead>
        <tr>
          <th>Axe</th>
          <th>Multiplier</th>
          <th>One harvest</th>
          <th class="wide" />
        </tr>
      </thead>
      <tbody>
        <tr v-for="axe in data.axes" :key="axe.item">
          <td><ItemSlot :id="axe.item" label /></td>
          <td class="num">×{{ axe.multiplier }}</td>
          <td class="num">
            {{ seconds(cycleTicks(ticks, axe.multiplier, efficiency)) }} s
            <span class="sub">{{ cycleTicks(ticks, axe.multiplier, efficiency) }} ticks</span>
          </td>
          <td class="wide">
            <div class="bar"><span :style="{ width: `${(axe.multiplier / fastest) * 100}%` }" /></div>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<style scoped>
.controls {
  display: flex;
  flex-wrap: wrap;
  gap: 10px 18px;
  font-size: 14px;
}

.controls label {
  display: flex;
  align-items: center;
  gap: 8px;
}

.types {
  margin: 8px 0 0 !important;
  font-size: 13px;
  color: var(--vp-c-text-2);
}

@media (min-width: 640px) {
  table {
    display: table;
    width: 100%;
  }
}

td {
  vertical-align: middle;
}

.num {
  font-family: var(--vp-font-family-mono);
  white-space: nowrap;
}

.sub {
  display: block;
  font-size: 12px;
  color: var(--vp-c-text-3);
}

.wide {
  width: 35%;
}

.bar {
  height: 8px;
  border-radius: 4px;
  background: var(--vp-c-divider);
  overflow: hidden;
}

.bar span {
  display: block;
  height: 100%;
  background: var(--vp-c-brand-1);
}
</style>

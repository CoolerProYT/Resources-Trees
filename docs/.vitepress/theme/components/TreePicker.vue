<script setup lang="ts">
import { saplingId } from '../resourcestrees'
import { useRt } from '../version'
import ItemSlot from './ItemSlot.vue'

const { data } = useRt()

/** Pills for choosing a tree type, each with its sapling. */
withDefaults(defineProps<{ label?: string }>(), { label: 'Tree' })
const model = defineModel<string>({ required: true })

// The mod's own sapling in a green tint: vanilla renders are missing for the newest saplings.
const tint = data.resourceTypes.find((type) => type.name === 'emerald')?.name ?? data.resourceTypes[0].name
</script>

<template>
  <div class="rt-picker" role="radiogroup" :aria-label="label">
    <span class="caption">{{ label }}</span>
    <button
      v-for="tree in data.treeTypes"
      :key="tree.name"
      role="radio"
      :aria-checked="tree.name === model"
      :class="{ active: tree.name === model }"
      @click="model = tree.name"
    >
      <ItemSlot :id="saplingId(tint, tree.name)" />
      {{ tree.label }}
    </button>
  </div>
</template>

<style scoped>
.rt-picker {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 6px;
}

.caption {
  margin-right: 4px;
  font-size: 14px;
  color: var(--vp-c-text-2);
}

button {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 2px 12px 2px 3px;
  border: 1px solid var(--vp-c-divider);
  border-radius: 999px;
  font-size: 13px;
  color: var(--vp-c-text-2);
  transition: border-color 0.2s, color 0.2s;
}

button:hover {
  border-color: var(--vp-c-brand-2);
}

button.active {
  border-color: var(--vp-c-brand-1);
  background: var(--vp-c-brand-soft);
  color: var(--vp-c-brand-1);
  font-weight: 600;
}

button :deep(.rt-slot) {
  --size: 26px;
  --icon: 20px;
  border-radius: 999px;
  border-width: 1px;
}
</style>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { fragmentId } from '../resourcestrees'
import { useRt } from '../version'
import ItemSlot from './ItemSlot.vue'
import RecipeCard from './RecipeCard.vue'

const { data, itemName } = useRt()

const query = ref('')

const groups = computed(() => {
  const q = query.value.trim().toLowerCase()
  return data.resourceTypes
    .map((type) => ({
      type,
      recipes: data.recipes.filter(
        (r) =>
          r.fragmentOf === type.name &&
          (!q || type.label.toLowerCase().includes(q) || itemName(r.result.id).toLowerCase().includes(q)),
      ),
    }))
    .filter((group) => group.recipes.length > 0)
})
const shown = computed(() => groups.value.reduce((sum, group) => sum + group.recipes.length, 0))
const total = data.recipes.filter((r) => r.fragmentOf).length
</script>

<template>
  <div class="rt-panel">
    <div class="controls">
      <input v-model="query" type="search" placeholder="Filter by fragment or result, e.g. “honey” or “nether”…" aria-label="Filter recipes" />
      <span class="count">{{ shown }} of {{ total }} recipes</span>
    </div>
    <details v-for="group in groups" :key="group.type.name" class="group" :open="query.trim() !== '' || undefined">
      <summary :style="{ '--tint': group.type.color }">
        <ItemSlot :id="fragmentId(group.type.name)" />
        <strong>{{ itemName(fragmentId(group.type.name)) }}</strong>
        <span class="n">{{ group.recipes.length }}</span>
        <span class="results">
          <ItemSlot v-for="r in group.recipes.slice(0, 8)" :key="r.id" :id="r.result.id" />
          <span v-if="group.recipes.length > 8" class="more">+{{ group.recipes.length - 8 }}</span>
        </span>
      </summary>
      <div class="recipes">
        <RecipeCard v-for="r in group.recipes" :key="r.id" :recipe="r" compact />
      </div>
    </details>
    <p v-if="!groups.length" class="rt-muted">Nothing matches “{{ query }}”.</p>
  </div>
</template>

<style scoped>
.controls {
  display: flex;
  flex-wrap: wrap;
  gap: 10px 18px;
  align-items: center;
  margin-bottom: 12px;
  font-size: 14px;
}

.controls input {
  flex: 1 1 260px;
  padding: 6px 10px;
  border: 1px solid var(--vp-c-divider);
  border-radius: 8px;
  background: var(--vp-c-bg);
}

.count {
  color: var(--vp-c-text-2);
}

.group {
  border: 1px solid var(--vp-c-divider);
  border-radius: 10px;
  background: var(--vp-c-bg);
  margin-top: 8px;
}

summary {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 6px 12px 6px 8px;
  cursor: pointer;
  border-left: 5px solid var(--tint);
  border-radius: 10px;
  list-style: none;
}

summary::-webkit-details-marker {
  display: none;
}

summary::after {
  content: '▸';
  margin-left: 4px;
  color: var(--vp-c-text-3);
  transition: transform 0.2s;
}

.group[open] summary::after {
  transform: rotate(90deg);
}

.n {
  padding: 0 8px;
  border-radius: 999px;
  background: var(--vp-c-default-soft);
  font-size: 12px;
  color: var(--vp-c-text-2);
}

.results {
  display: flex;
  align-items: center;
  gap: 2px;
  margin-left: auto;
  overflow: hidden;
}

.results :deep(.rt-slot) {
  --size: 26px;
  --icon: 22px;
  border-width: 1px;
}

.more {
  margin-left: 4px;
  font-size: 12px;
  color: var(--vp-c-text-3);
}

@media (max-width: 640px) {
  .results {
    display: none;
  }
}

.recipes {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  padding: 4px 12px 12px;
}
</style>

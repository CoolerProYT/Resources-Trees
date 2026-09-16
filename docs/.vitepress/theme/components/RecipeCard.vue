<script setup lang="ts">
import { computed } from 'vue'
import type { Recipe } from '../resourcestrees'
import { useRt } from '../version'
import ItemSlot from './ItemSlot.vue'

const { recipe: findRecipe } = useRt()

/** Looks a recipe up by `id`, or shows the `recipe` passed in (used for generated sapling recipes). */
const props = withDefaults(defineProps<{ id?: string; recipe?: Recipe | null; compact?: boolean }>(), {
  id: '',
  recipe: null,
  compact: false,
})

const shown = computed(() => props.recipe ?? findRecipe(props.id))

const STATION: Record<string, string> = {
  'minecraft:crafting_shaped': 'Shaped',
  'minecraft:crafting_shapeless': 'Shapeless',
  'resourcestrees:strict_shaped': 'Exact shape',
}

/** Nine cells for the crafting grid, left to right, top to bottom. */
const grid = computed<(string[] | null)[]>(() => {
  const r = shown.value
  if (!r) return []
  if (r.pattern && r.key) {
    // Shaped patterns can be smaller than 3×3; they sit in the top-left corner like in the recipe book.
    const cells: (string[] | null)[] = []
    for (let row = 0; row < 3; row++) {
      for (let col = 0; col < 3; col++) {
        const symbol = r.pattern[row]?.[col] ?? ' '
        cells.push(symbol === ' ' ? null : r.key[symbol] ?? null)
      }
    }
    return cells
  }
  if (r.ingredients) {
    return Array.from({ length: 9 }, (_, i) => r.ingredients![i] ?? null)
  }
  return []
})
</script>

<template>
  <div v-if="shown" class="rt-recipe" :class="{ compact }">
    <div class="grid">
      <ItemSlot v-for="(cell, i) in grid" :id="cell" :key="i" />
    </div>
    <span class="arrow">
      <span class="station">{{ STATION[shown.type] ?? 'Crafting' }}</span>
      ➜
    </span>
    <ItemSlot :id="shown.result.id" :count="shown.result.count" :label="!compact" />
  </div>
  <p v-else class="rt-muted">Recipe {{ id }} not found.</p>
</template>

<style scoped>
.rt-recipe {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 14px;
  margin: 12px 0;
  padding: 12px 16px;
  border: 1px solid var(--vp-c-divider);
  border-radius: 10px;
  background: var(--vp-c-bg-soft);
}

.rt-recipe.compact {
  display: inline-flex;
  flex-wrap: nowrap;
  gap: 8px;
  margin: 0;
  padding: 8px 10px;
}

.grid {
  display: grid;
  grid-template-columns: repeat(3, 36px);
}

.arrow {
  display: flex;
  flex-direction: column;
  align-items: center;
  font-size: 22px;
  line-height: 1;
  color: var(--vp-c-text-2);
}

.station {
  font-size: 11px;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  white-space: nowrap;
}
</style>

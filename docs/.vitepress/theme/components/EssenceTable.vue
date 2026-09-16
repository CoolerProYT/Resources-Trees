<script setup lang="ts">
import { useRt } from '../version'
import ItemSlot from './ItemSlot.vue'
import RecipeCard from './RecipeCard.vue'

const { data, itemName, recipe } = useRt()

const essences = Object.keys(data.essences)
  .map((id) => ({
    id,
    recipe: recipe(id.split(':')[1]),
    // Resource types that use the essence as their sapling material.
    types: data.resourceTypes.filter((type) => type.material === id),
  }))
  .sort((a, b) => itemName(a.id).localeCompare(itemName(b.id)))
</script>

<template>
  <div class="cards">
    <article v-for="essence in essences" :key="essence.id" class="card" :style="{ '--tint': data.essences[essence.id] }">
      <header>
        <ItemSlot :id="essence.id" />
        <div>
          <strong>{{ itemName(essence.id) }}</strong>
          <span v-if="essence.types.length" class="for">
            Material for {{ essence.types.map((type) => type.label).join(', ') }} trees
          </span>
        </div>
      </header>
      <RecipeCard v-if="essence.recipe" :recipe="essence.recipe" compact />
    </article>
  </div>
</template>

<style scoped>
.cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 12px;
  margin: 16px 0;
}

.card {
  padding: 12px;
  border: 1px solid var(--vp-c-divider);
  border-top: 4px solid var(--tint);
  border-radius: 10px;
  background: var(--vp-c-bg-soft);
}

header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}

strong {
  display: block;
}

.for {
  font-size: 12px;
  color: var(--vp-c-text-2);
}

.card :deep(.rt-recipe) {
  background: var(--vp-c-bg);
}
</style>

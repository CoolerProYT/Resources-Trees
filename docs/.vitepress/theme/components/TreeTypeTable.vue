<script setup lang="ts">
import { ref } from 'vue'
import { leavesId, percent, saplingId } from '../resourcestrees'
import { useRt } from '../version'
import ItemSlot from './ItemSlot.vue'

const { data, features } = useRt()

const tint = ref(data.resourceTypes.find((type) => type.name === 'emerald')?.name ?? data.resourceTypes[0].name)
</script>

<template>
  <div class="rt-panel">
    <label class="picker">
      Preview as
      <select v-model="tint">
        <option v-for="type in data.resourceTypes" :key="type.name" :value="type.name">{{ type.label }}</option>
      </select>
    </label>
    <div class="cards">
      <article v-for="tree in data.treeTypes" :key="tree.name" class="card">
        <header>
          <ItemSlot :id="saplingId(tint, tree.name)" large />
          <ItemSlot :id="leavesId(tint, tree.name)" large />
          <div>
            <strong>{{ tree.label }}</strong>
            <code>{{ tree.name }}</code>
          </div>
        </header>
        <dl>
          <dt>Crafted from</dt>
          <dd><ItemSlot :id="tree.originalSapling" label /></dd>
          <dt>Trunk</dt>
          <dd><ItemSlot :id="tree.log" label /></dd>
          <template v-if="features.treeTypeOptions">
            <dt>Leaf particles</dt>
            <dd>{{ tree.particle ? `${percent(tree.particle)} per tick` : 'None' }}</dd>
            <dt>Ambient sound</dt>
            <dd>{{ tree.ambientSound ?? 'None' }}</dd>
          </template>
          <template v-if="features.legacy">
            <dt>Sapling id</dt>
            <dd><code>resourcestrees:resources_{{ tree.name }}_sapling</code></dd>
            <dt>Leaves id</dt>
            <dd><code>resourcestrees:resources_{{ tree.name }}_leaves</code></dd>
          </template>
        </dl>
      </article>
    </div>
  </div>
</template>

<style scoped>
.picker {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  margin-bottom: 12px;
}

.cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 12px;
}

.card {
  padding: 12px;
  border: 1px solid var(--vp-c-divider);
  border-radius: 10px;
  background: var(--vp-c-bg);
}

header {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 10px;
}

header div {
  margin-left: 6px;
}

header strong,
header code {
  display: block;
}

header code {
  font-size: 12px;
}

dl {
  display: grid;
  grid-template-columns: auto 1fr;
  gap: 6px 12px;
  align-items: center;
  margin: 0;
  font-size: 13px;
}

dt {
  color: var(--vp-c-text-2);
}

dd {
  margin: 0;
}

dd :deep(.rt-slot) {
  --size: 28px;
  --icon: 24px;
}
</style>

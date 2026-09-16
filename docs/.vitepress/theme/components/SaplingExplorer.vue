<script setup lang="ts">
import { computed, ref } from 'vue'
import { cycleTicks, decimal, expectedCount, fragmentId, leavesId, percent, saplingId, seconds } from '../resourcestrees'
import { useRt } from '../version'
import ItemSlot from './ItemSlot.vue'
import RecipeCard from './RecipeCard.vue'
import TreePicker from './TreePicker.vue'

const { data, features, itemName, recipesUsing, resourceType, saplingRecipe, simulatorDrops, treeType } = useRt()

const props = withDefaults(defineProps<{ resource?: string; tree?: string }>(), { resource: 'iron', tree: 'oak' })

const HOUR = 72000

const selectedResource = ref(resourceType(props.resource)?.name ?? data.resourceTypes[0].name)
const selectedTree = ref(treeType(props.tree)?.name ?? data.treeTypes[0].name)
const selectedAxe = ref(data.axes.find((axe) => axe.item === 'minecraft:iron_axe')?.item ?? data.axes[0].item)
const efficiency = ref(0)
const fortune = ref(0)

const resource = computed(() => resourceType(selectedResource.value)!)
const tree = computed(() => treeType(selectedTree.value)!)
const axe = computed(() => data.axes.find((a) => a.item === selectedAxe.value)!)

const sapling = computed(() => saplingId(resource.value.name, tree.value.name))
const leaves = computed(() => leavesId(resource.value.name, tree.value.name))
const fragment = computed(() => fragmentId(resource.value.name))

const cycle = computed(() => cycleTicks(resource.value.treeSimulatorTicks, axe.value.multiplier, efficiency.value))
const harvestsPerHour = computed(() => HOUR / cycle.value)

const drops = computed(() =>
  simulatorDrops(resource.value, tree.value).map((drop, i) => ({
    ...drop,
    key: `${drop.item}-${i}`,
    expected: expectedCount(drop, fortune.value),
  })),
)
const maxExpected = computed(() => Math.max(...drops.value.map((drop) => drop.expected), 0.0001))

/** Totals per item, since the fragment appears in two drop entries. */
const perHour = computed(() => {
  const totals = new Map<string, number>()
  for (const drop of drops.value) totals.set(drop.item, (totals.get(drop.item) ?? 0) + drop.expected * harvestsPerHour.value)
  return [...totals.entries()]
})

const fragmentUses = computed(() => recipesUsing(fragment.value))

const rolls = (drop: { minRolls: number; maxRolls: number }) => {
  const min = drop.minRolls + fortune.value
  const max = drop.maxRolls + fortune.value
  return min === max ? `${min}` : `${min}–${max}`
}
</script>

<template>
  <div class="rt-panel explorer">
    <div class="controls">
      <label>
        Resource
        <select v-model="selectedResource">
          <option v-for="type in data.resourceTypes" :key="type.name" :value="type.name">{{ type.label }}</option>
        </select>
      </label>
      <label>
        Axe
        <select v-model="selectedAxe">
          <option v-for="a in data.axes" :key="a.item" :value="a.item">{{ itemName(a.item) }} (×{{ a.multiplier }})</option>
        </select>
      </label>
      <label v-if="features.efficiency">
        Efficiency
        <select v-model.number="efficiency">
          <option v-for="level in 6" :key="level" :value="level - 1">{{ level - 1 || 'None' }}</option>
        </select>
      </label>
      <label v-if="features.fortune">
        Fortune
        <select v-model.number="fortune">
          <option v-for="level in 4" :key="level" :value="level - 1">{{ level - 1 || 'None' }}</option>
        </select>
      </label>
    </div>
    <TreePicker v-model="selectedTree" />

    <div class="hero" :style="{ '--tint': resource.color }">
      <div class="items">
        <ItemSlot :id="sapling" large />
        <ItemSlot :id="leaves" large />
        <ItemSlot :id="fragment" large />
      </div>
      <div>
        <div class="title">{{ itemName(sapling) }}</div>
        <div class="meta">
          <code v-if="features.legacy">resourcestrees:resources_{{ tree.name }}_sapling</code>
          <code v-if="features.legacy">type resourcestrees:{{ resource.name }}</code>
          <code v-else>{{ sapling }}</code>
          <span class="chip"><span class="dot" />{{ resource.color }}</span>
          <span class="chip">Grows as {{ tree.label }}</span>
        </div>
      </div>
    </div>

    <div class="cols">
      <section>
        <h4>Crafting</h4>
        <RecipeCard :recipe="saplingRecipe(resource, tree)" compact />
        <p v-if="features.generatedRecipes === 'builtin'" class="note">
          Built-in types ship with this recipe. Custom types need one from a datapack.
        </p>
      </section>
      <section>
        <h4>Breaking the leaves</h4>
        <ul class="facts">
          <li><ItemSlot :id="sapling" /> <span>Sapling</span> <b>{{ percent(resource.saplingDropChance * features.leafSaplingFactor) }}</b></li>
          <li><ItemSlot :id="fragment" /> <span>Fragment</span> <b>{{ percent(resource.leafDropChance) }}</b></li>
          <li><ItemSlot :id="fragment" /> <span>Bonus fragment</span> <b>{{ percent(resource.leafDropChance * 0.5) }}</b></li>
        </ul>
        <p class="note">Shears or Silk Touch drop the leaves block instead.</p>
      </section>
    </div>

    <section>
      <h4>
        Tree Simulator
        <span class="cycle">
          one harvest every <b>{{ seconds(cycle) }} s</b> ({{ cycle }} ticks) · {{ decimal(harvestsPerHour, 1) }} per hour
        </span>
      </h4>
      <ul class="rows">
        <li v-for="drop in drops" :key="drop.key">
          <ItemSlot :id="drop.item" />
          <div class="info">
            <div class="line">
              <span class="name">{{ drop.bonus ? `Bonus ${itemName(drop.item)}` : itemName(drop.item) }}</span>
              <span class="pct">{{ decimal(drop.expected) }}</span>
            </div>
            <div class="bar" aria-hidden="true">
              <span :style="{ width: `${(drop.expected / maxExpected) * 100}%` }" />
            </div>
            <div class="sub">{{ percent(drop.chance) }} × {{ rolls(drop) }} roll{{ rolls(drop) === '1' ? '' : 's' }} · average per harvest</div>
          </div>
        </li>
      </ul>
      <div class="hourly">
        <span class="caption">Average per hour</span>
        <span v-for="[item, amount] in perHour" :key="item" class="total">
          <ItemSlot :id="item" /> <b>{{ decimal(amount, 1) }}</b>
        </span>
      </div>
    </section>

    <section v-if="fragmentUses.length">
      <h4>What {{ itemName(fragment) }} crafts into</h4>
      <div class="recipes">
        <RecipeCard v-for="r in fragmentUses" :key="r.id" :recipe="r" compact />
      </div>
    </section>
    <section v-else>
      <h4>What {{ itemName(fragment) }} crafts into</h4>
      <p class="note">No built-in recipes use this fragment.</p>
    </section>
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

.controls label {
  display: flex;
  align-items: center;
  gap: 8px;
}

.hero {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 16px;
  margin: 16px 0 4px;
  padding: 14px 16px;
  border-radius: 10px;
  border: 1px solid var(--vp-c-divider);
  border-left: 6px solid var(--tint);
  background: linear-gradient(90deg, color-mix(in srgb, var(--tint) 18%, transparent), transparent 70%);
}

.items {
  display: flex;
  gap: 6px;
}

.title {
  font-size: 20px;
  font-weight: 700;
}

.meta {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px;
  margin-top: 4px;
  font-size: 13px;
}

.chip {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 1px 10px;
  border-radius: 999px;
  border: 1px solid var(--vp-c-divider);
  color: var(--vp-c-text-2);
}

.dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: var(--tint);
  border: 1px solid var(--vp-c-divider);
}

section {
  margin-top: 18px;
}

h4 {
  display: flex;
  flex-wrap: wrap;
  align-items: baseline;
  gap: 4px 12px;
  margin: 0 0 10px !important;
  font-size: 15px;
}

.cycle {
  font-weight: 400;
  font-size: 13px;
  color: var(--vp-c-text-2);
}

.cols {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 0 24px;
}

.facts {
  list-style: none;
  padding: 0 !important;
  margin: 0;
}

.facts li {
  display: flex;
  align-items: center;
  gap: 10px;
  margin: 0 0 4px !important;
  font-size: 14px;
}

.facts b {
  margin-left: auto;
  font-family: var(--vp-font-family-mono);
}

.note {
  margin: 6px 0 0 !important;
  font-size: 13px;
  color: var(--vp-c-text-2);
}

.rows {
  list-style: none;
  padding: 0 !important;
  margin: 0;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 10px 20px;
}

.rows li {
  display: flex;
  gap: 10px;
  align-items: center;
  margin: 0 !important;
}

.info {
  flex: 1;
  min-width: 0;
}

.line {
  display: flex;
  justify-content: space-between;
  gap: 8px;
  font-size: 14px;
}

.name {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.pct {
  font-family: var(--vp-font-family-mono);
  font-weight: 600;
}

.bar {
  height: 6px;
  margin: 3px 0;
  border-radius: 3px;
  background: var(--vp-c-divider);
  overflow: hidden;
}

.bar span {
  display: block;
  height: 100%;
  background: var(--vp-c-brand-1);
  transition: width 0.25s;
}

.sub {
  font-size: 12px;
  color: var(--vp-c-text-3);
}

.hourly {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px 16px;
  margin-top: 12px;
  padding-top: 10px;
  border-top: 1px dashed var(--vp-c-divider);
  font-size: 14px;
}

.caption {
  color: var(--vp-c-text-2);
}

.total {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-family: var(--vp-font-family-mono);
}

.recipes {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
</style>

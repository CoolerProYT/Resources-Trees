<script setup lang="ts">
import { computed } from 'vue'
import { withBase } from 'vitepress'
import { fragmentId, saplingId } from '../resourcestrees'
import { useRt } from '../version'
import ItemSlot from './ItemSlot.vue'

const { itemName, resourceType, treeType } = useRt()

/** The Tree Simulator menu, drawn from the mod's own GUI texture with a working example inside. */
const props = withDefaults(defineProps<{ resource?: string; tree?: string; axe?: string }>(), {
  resource: 'diamond',
  tree: 'oak',
  axe: 'minecraft:diamond_axe',
})

const resource = computed(() => resourceType(props.resource)!)
const tree = computed(() => treeType(props.tree)!)

/** Slot positions from TreeSimulatorMenu, in GUI pixels. */
const slots = computed(() => [
  { x: 26, y: 35, id: saplingId(resource.value.name, tree.value.name), count: 1 },
  { x: 62, y: 59, id: props.axe, count: 1 },
  { x: 98, y: 17, id: tree.value.log, count: 4 },
  { x: 116, y: 17, id: fragmentId(resource.value.name), count: 13 },
  { x: 134, y: 17, id: saplingId(resource.value.name, tree.value.name), count: 2 },
])

const style = computed(() => ({
  '--gui': `url(${withBase('/gui/tree_simulator.png')})`,
  '--progress': `url(${withBase('/gui/progress.png')})`,
}))
</script>

<template>
  <figure class="rt-gui" :style="style">
    <div class="frame" role="img" :aria-label="`Tree Simulator growing a ${itemName(slots[0].id)}`">
      <span class="title">Tree Simulator</span>
      <span class="progress" />
      <span v-for="slot in slots" :key="`${slot.x}-${slot.y}`" class="slot" :style="{ left: `${slot.x * 2}px`, top: `${slot.y * 2}px` }">
        <ItemSlot :id="slot.id" :count="slot.count" bare />
      </span>
      <span class="callout" style="left: 52px; top: 106px">1</span>
      <span class="callout" style="left: 124px; top: 154px">2</span>
      <span class="callout" style="left: 116px; top: 62px">3</span>
      <span class="callout" style="left: 306px; top: 26px">4</span>
    </div>
    <figcaption>
      <ol>
        <li><strong>Sapling</strong>: any resource sapling with a Tree Simulator recipe.</li>
        <li><strong>Axe</strong>: required. Better axes finish each harvest sooner.</li>
        <li><strong>Progress</strong>: fills once per harvest.</li>
        <li><strong>Output</strong>: nine slots. The simulator pauses when a drop has nowhere to go.</li>
      </ol>
    </figcaption>
  </figure>
</template>

<style scoped>
.rt-gui {
  margin: 16px 0;
}

.frame {
  position: relative;
  width: 352px;
  max-width: 100%;
  height: 172px;
  overflow: hidden;
  background: var(--gui) 0 0 / 512px 512px no-repeat;
  image-rendering: pixelated;
  border-bottom: 4px solid #555;
  border-radius: 0 0 6px 6px;
}

.title {
  position: absolute;
  left: 16px;
  top: 10px;
  font: 600 15px/1 var(--vp-font-family-mono);
  color: #404040;
}

.progress {
  position: absolute;
  left: 118px;
  top: 70px;
  width: 44px;
  height: 32px;
  background: var(--progress) 0 0 / 44px 32px no-repeat;
  animation: grow 3s steps(22) infinite;
}

@keyframes grow {
  from {
    width: 0;
  }
  to {
    width: 44px;
  }
}

@media (prefers-reduced-motion: reduce) {
  .progress {
    animation: none;
    width: 30px;
  }
}

.slot {
  position: absolute;
  width: 32px;
  height: 32px;
  display: flex;
}

.slot :deep(.rt-count) {
  font-size: 14px;
  right: -2px;
  bottom: -3px;
}

.callout {
  position: absolute;
  display: grid;
  place-items: center;
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: var(--vp-c-brand-1);
  color: var(--vp-c-white);
  font: 700 11px/1 var(--vp-font-family-base);
  box-shadow: 0 0 0 2px var(--vp-c-bg);
}

figcaption ol {
  margin: 10px 0 0;
  font-size: 14px;
}
</style>

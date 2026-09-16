<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { withBase } from 'vitepress'
import type { Icon } from '../resourcestrees'
import { useRt } from '../version'
import { renderIcon } from '../render'

const { itemIcon, itemName } = useRt()

/**
 * `id` may list several items (an ingredient with alternatives); the slot cycles through them like JEI.
 * `bare` drops the slot frame and fills the parent, for items drawn on top of a GUI texture.
 * `icon` and `name` override the looked-up ones, for items that only exist in a builder's form.
 */
const props = withDefaults(
  defineProps<{
    id?: string | string[] | null
    count?: number
    label?: boolean
    large?: boolean
    bare?: boolean
    icon?: Icon | null
    name?: string
  }>(),
  { id: null, count: 1, label: false, large: false, bare: false, icon: null, name: '' },
)

const options = computed(() => (props.id == null ? [] : Array.isArray(props.id) ? props.id : [props.id]))
const index = ref(0)
let timer: ReturnType<typeof setInterval> | undefined
onMounted(() => {
  timer = setInterval(() => {
    if (options.value.length > 1) index.value = (index.value + 1) % options.value.length
  }, 1500)
})
onBeforeUnmount(() => clearInterval(timer))
watch(options, () => (index.value = 0))

const current = computed(() => options.value[index.value] ?? null)
const name = computed(() => props.name || (current.value ? itemName(current.value) : ''))
const icon = computed(() => props.icon ?? (current.value ? itemIcon(current.value) : null))

// Tinted icons need a canvas, so they are drawn after mount; the server render leaves those slots empty.
const drawn = ref<string | null>(null)
const failed = ref(false)
// Keyed by content, so a parent that rebuilds an equal icon object on every render doesn't cause a redraw.
const iconKey = computed(() => JSON.stringify(icon.value))
function draw() {
  const value = icon.value
  const key = iconKey.value
  drawn.value = null
  failed.value = false
  if (!value || value.kind === 'image') return
  renderIcon(value, withBase).then(
    (url) => iconKey.value === key && (drawn.value = url),
    () => iconKey.value === key && (failed.value = true),
  )
}
onMounted(draw)
watch(iconKey, draw)
const src = computed(() => {
  const value = icon.value
  if (value?.kind === 'image') return value.local ? withBase(value.src) : value.src
  return drawn.value
})

// Falls back to initials when an item has no icon or the icon fails to load.
const initials = computed(() =>
  name.value
    .split(' ')
    .filter((word) => /^[A-Z]/.test(word))
    .slice(0, 2)
    .map((word) => word[0])
    .join(''),
)
</script>

<template>
  <span class="rt-item" :class="{ 'with-label': label, large, bare }">
    <span class="rt-slot" :title="name" :aria-label="name" role="img">
      <img v-if="src && !failed" class="pixelated" :src="src" alt="" loading="lazy" @error="failed = true" />
      <span v-else-if="current && (failed || !icon)" class="rt-initials">{{ initials }}</span>
      <span v-if="count > 1" class="rt-count">{{ count }}</span>
    </span>
    <span v-if="label && current" class="rt-label">{{ name }}</span>
  </span>
</template>

<style scoped>
.rt-item {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  vertical-align: middle;
  --size: 36px;
  --icon: 32px;
}

.rt-item.large {
  --size: 52px;
  --icon: 48px;
}

.rt-item.bare {
  width: 100%;
  height: 100%;
  --size: 100%;
  --icon: 100%;
}

.bare .rt-slot {
  background: none;
  border: none;
}

.rt-slot {
  position: relative;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: var(--size);
  height: var(--size);
  flex: none;
  background: var(--rt-slot-bg);
  border: 2px solid;
  border-color: var(--rt-slot-dark) var(--rt-slot-light) var(--rt-slot-light) var(--rt-slot-dark);
}

.rt-slot img {
  width: var(--icon);
  height: var(--icon);
}

.rt-initials {
  font: 600 12px/1 var(--vp-font-family-mono);
  color: #fff;
  text-shadow: 1px 1px 0 #3f3f3f;
}

.rt-count {
  position: absolute;
  right: 1px;
  bottom: -1px;
  font: 700 12px/1 var(--vp-font-family-mono);
  color: #fff;
  text-shadow: 1px 1px 0 #3f3f3f;
}

.rt-label {
  font-weight: 500;
}
</style>

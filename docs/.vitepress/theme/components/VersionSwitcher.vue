<script setup lang="ts">
import { computed, ref } from 'vue'
import { useData, useRouter, withBase } from 'vitepress'
import { VERSIONS, versionOf, type Version } from '../version'

const router = useRouter()
const { page, theme } = useData()

const isOpen = ref(false)
const current = computed(() => versionOf(page.value.relativePath))

function links(version: Version): string[] {
  const groups = theme.value.sidebar?.[version.path] ?? []
  return groups.flatMap((group: { items?: { link: string }[] }) => group.items?.map((item) => item.link) ?? [])
}

/** Opens the same page in the chosen version, or its first page when that version has no such page. */
function switchVersion(version: Version) {
  isOpen.value = false
  const path = `/${page.value.relativePath}`.slice(current.value.path.length).replace(/(index)?\.md$/, '')
  const available = links(version)
  const target = `${version.path}${path}`
  router.go(withBase(available.includes(target) ? target : available[0] ?? version.path))
}
</script>

<template>
  <div class="version-switcher" @mouseenter="isOpen = true" @mouseleave="isOpen = false">
    <button class="button" type="button" :aria-expanded="isOpen" @click="isOpen = !isOpen">
      <span class="text">{{ current.label }}</span>
      <svg class="icon" width="14" height="14" viewBox="0 0 24 24" fill="none" aria-hidden="true">
        <path d="M6 9l6 6 6-6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
      </svg>
    </button>

    <div v-if="isOpen" class="menu">
      <button
        v-for="v in VERSIONS"
        :key="v.label"
        type="button"
        class="item"
        :class="{ active: v.label === current.label }"
        @click="switchVersion(v)"
      >
        {{ v.label }}
      </button>
    </div>
  </div>
</template>

<style scoped>
.version-switcher {
  position: relative;
  display: flex;
  align-items: center;
}

.button {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 0 12px;
  line-height: var(--vp-nav-height);
  font-size: 14px;
  font-weight: 500;
  color: var(--vp-c-text-1);
  transition: color 0.25s;
}

.button:hover {
  color: var(--vp-c-brand-1);
}

.icon {
  transition: transform 0.25s;
}

.button[aria-expanded='true'] .icon {
  transform: rotate(180deg);
}

.menu {
  position: absolute;
  top: calc(100% - 12px);
  right: 0;
  display: flex;
  flex-direction: column;
  gap: 2px;
  min-width: 150px;
  padding: 8px;
  border: 1px solid var(--vp-c-divider);
  border-radius: 12px;
  background: var(--vp-c-bg-elv);
  box-shadow: var(--vp-shadow-3);
  z-index: 100;
}

.item {
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 14px;
  text-align: left;
  white-space: nowrap;
  color: var(--vp-c-text-1);
  transition: background-color 0.25s, color 0.25s;
}

.item:hover {
  background-color: var(--vp-c-default-soft);
  color: var(--vp-c-brand-1);
}

.item.active {
  color: var(--vp-c-brand-1);
  font-weight: 600;
}
</style>

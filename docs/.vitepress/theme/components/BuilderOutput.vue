<script setup lang="ts">
import { ref } from 'vue'

/** The right-hand side of a builder: where the file goes, the generated text, and what is wrong with it. */
const props = withDefaults(defineProps<{ file: string; text: string; problems?: string[]; label?: string }>(), {
  problems: () => [],
  label: 'Save as',
})

const copied = ref(false)
async function copy() {
  try {
    await navigator.clipboard.writeText(props.text)
    copied.value = true
    setTimeout(() => (copied.value = false), 1500)
  } catch {
    copied.value = false
  }
}
</script>

<template>
  <div class="rt-output">
    <div class="head">
      <span class="file">
        <span class="caption">{{ label }}</span>
        <code>{{ file }}</code>
      </span>
      <button type="button" :disabled="problems.length > 0" @click="copy">{{ copied ? 'Copied' : 'Copy' }}</button>
    </div>
    <pre><code>{{ text }}</code></pre>
    <ul v-if="problems.length" class="problems">
      <li v-for="problem in problems" :key="problem">{{ problem }}</li>
    </ul>
    <div v-else class="note"><slot /></div>
  </div>
</template>

<style scoped>
.rt-output {
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.head {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 8px;
  font-size: 0.875em;
}

.file {
  min-width: 0;
}

.caption {
  display: block;
  font-weight: 500;
  color: var(--vp-c-text-2);
}

.file code {
  word-break: break-all;
}

button {
  flex: none;
  padding: 4px 14px;
  border-radius: 6px;
  background: var(--vp-c-brand-1);
  color: var(--vp-c-neutral-inverse);
  font-size: 13px;
  font-weight: 600;
}

button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

pre {
  flex: 1;
  margin: 8px 0;
  padding: 12px;
  max-height: 520px;
  overflow: auto;
  border-radius: 8px;
  background: var(--vp-code-block-bg);
  font-size: 13px;
  line-height: 1.5;
}

pre code {
  padding: 0;
  background: none;
  font-size: inherit;
}

.problems {
  margin: 0;
  padding-left: 18px;
  color: var(--vp-c-danger-1);
  font-size: 0.875em;
}

.problems li {
  margin: 2px 0;
}

.note {
  color: var(--vp-c-text-2);
  font-size: 0.875em;
}

.note :deep(p) {
  margin: 0;
}
</style>

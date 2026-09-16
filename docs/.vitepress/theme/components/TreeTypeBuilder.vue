<script setup lang="ts">
import { computed, reactive } from 'vue'
import { ID, leavesIcon, percent, saplingIcon } from '../resourcestrees'
import { useRt } from '../version'
import { POPLAR_AMBIENCE, VANILLA_GROWERS } from '../vanilla'
import BuilderOutput from './BuilderOutput.vue'
import ItemSlot from './ItemSlot.vue'

const { data, features, link, treeType, version } = useRt()

// Poplar is new in 26.3.
const vanilla = VANILLA_GROWERS.map((grower) => grower.name).filter((name) => name !== 'poplar' || version.key === 'current')

const form = reactive({
  name: 'maple',
  treeGrowerName: 'oak',
  namespace: 'resourcestrees',
  originalSapling: 'minecraft:oak_sapling',
  originalLeaves: 'minecraft:oak_leaves',
  log: 'minecraft:oak_log',
  particle: 0.01,
  sound: false,
  ambientSound: POPLAR_AMBIENCE.ambient_sound,
  chance: 300,
  satisfyingBlocks: POPLAR_AMBIENCE.satisfying_blocks,
  nearbySatisfying: 1,
  nearbySameLeaves: 3,
  /** Built-in shape used for the preview, since the new textures aren't on this site. */
  previewAs: 'oak',
})

/** Fills the vanilla blocks from a built-in tree type. */
function basedOn(name: string) {
  const tree = treeType(name)
  if (!tree) return
  Object.assign(form, {
    treeGrowerName: tree.grower,
    originalSapling: tree.originalSapling,
    originalLeaves: tree.originalLeaves,
    log: tree.log,
    particle: tree.particle ?? 0.01,
    sound: tree.ambientSound !== null,
    previewAs: tree.name,
  })
}

const saplingTexture = computed(() => `${form.namespace}:block/${form.name}_sapling`)
const leavesTexture = computed(() => `${form.namespace}:block/${form.name}_leaves`)
const texturePath = (id: string) => {
  const [namespace, path] = id.split(':')
  return `assets/${namespace}/textures/${path}.png`
}
const textures = computed(() => [
  texturePath(saplingTexture.value),
  texturePath(`${saplingTexture.value}_layer1`),
  texturePath(leavesTexture.value),
])

const json = computed(() => {
  const entry: Record<string, unknown> = {
    name: form.name,
    treeGrowerName: form.treeGrowerName,
    saplingTexture: saplingTexture.value,
    leavesTexture: leavesTexture.value,
    originalSapling: form.originalSapling,
    originalLeaves: form.originalLeaves,
    log: form.log,
  }
  if (features.treeTypeOptions && form.particle !== 0.01) entry.particle = form.particle
  if (features.treeTypeOptions && form.sound) {
    const sound: Record<string, unknown> = { ambient_sound: form.ambientSound }
    if (form.chance !== 300) sound.chance = form.chance
    if (form.satisfyingBlocks) sound.satisfying_blocks = form.satisfyingBlocks.replace(/^#/, '')
    if (form.nearbySatisfying !== 1) sound.nearby_satisfying_blocks_required = form.nearbySatisfying
    if (form.nearbySameLeaves !== 3) sound.nearby_same_leaves_required = form.nearbySameLeaves
    entry.ambient_leaves_block_sound_player = sound
  }
  return JSON.stringify(entry, null, 2)
})

const problems = computed(() => {
  const list: string[] = []
  if (!/^[a-z0-9_]+$/.test(form.name)) list.push('The name should be lowercase letters, digits and underscores.')
  else if (treeType(form.name)) list.push(`"${form.name}" is already a built-in tree type.`)
  if (!/^[a-z0-9_]+$/.test(form.treeGrowerName)) list.push('The grower name should be lowercase letters, digits and underscores.')
  if (!/^[a-z0-9_.-]+$/.test(form.namespace)) list.push('The texture namespace should be lowercase, like resourcestrees or mymod.')
  for (const [label, value] of [
    ['Original sapling', form.originalSapling],
    ['Original leaves', form.originalLeaves],
    ['Log', form.log],
  ]) {
    if (!ID.test(value)) list.push(`${label} should be a block id like minecraft:oak_log.`)
  }
  if (!features.growerTypes && !vanilla.includes(form.treeGrowerName)) {
    list.push(`This version has no custom grower types, so the grower must be one of: ${vanilla.join(', ')}.`)
  }
  if (!(form.particle >= 0 && form.particle <= 1)) list.push('Leaf particle chance should be between 0 and 1.')
  if (features.treeTypeOptions && form.sound) {
    if (!ID.test(form.ambientSound)) list.push('The ambient sound should be a sound event id.')
    if (form.satisfyingBlocks && !ID.test(form.satisfyingBlocks.replace(/^#/, ''))) list.push('Satisfying blocks should be a block tag id.')
    for (const value of [form.chance, form.nearbySatisfying, form.nearbySameLeaves]) {
      if (!Number.isInteger(value) || value < 0) list.push('The sound numbers should be whole numbers, 0 or more.')
    }
    if (form.chance < 1) list.push('Chance should be at least 1.')
    if (!form.satisfyingBlocks && form.nearbySatisfying > 0) {
      list.push('Without a block tag, set "Tag blocks around" to 0, or the sound never plays.')
    }
    if (form.nearbySatisfying + form.nearbySameLeaves > 4) list.push('Only four blocks are checked around the leaves, so the two counts can add up to 4 at most.')
  }
  return [...new Set(list)]
})

const sampleColors = ['emerald', 'gold', 'redstone', 'lapis', 'amethyst']
  .map((name) => data.resourceTypes.find((type) => type.name === name)?.color)
  .filter((color): color is string => !!color)
const customGrower = computed(() => !vanilla.includes(form.treeGrowerName))
</script>

<template>
  <div class="rt-builder">
    <div class="rt-grid">
      <div class="rt-fields">
        <label>
          <span>Base it on a built-in tree</span>
          <select :value="form.previewAs" @change="basedOn(($event.target as HTMLSelectElement).value)">
            <option v-for="tree in data.treeTypes" :key="tree.name" :value="tree.name">{{ tree.label }}</option>
          </select>
        </label>
        <div class="row">
          <label>
            <span>Name (<code>name</code>)</span>
            <input v-model.trim="form.name" spellcheck="false" />
          </label>
          <label>
            <span>Grower (<code>treeGrowerName</code>)</span>
            <input v-model.trim="form.treeGrowerName" list="rt-growers" spellcheck="false" />
            <datalist id="rt-growers">
              <option v-for="grower in vanilla" :key="grower" :value="grower" />
            </datalist>
          </label>
        </div>
        <p v-if="customGrower && features.growerTypes" class="hint">
          "{{ form.treeGrowerName }}" isn't a vanilla grower. Define it as a <a :href="link('datapacks/grower-types')">grower type</a>.
        </p>
        <label>
          <span>Texture namespace <span class="hint">the resource pack folder under assets/</span></span>
          <input v-model.trim="form.namespace" spellcheck="false" />
        </label>
        <fieldset>
          <legend>
            <ItemSlot :id="form.originalSapling" />
            <ItemSlot :id="form.originalLeaves" />
            <ItemSlot :id="form.log" />
            Vanilla blocks
          </legend>
          <label>
            <span>Crafted from and copies properties of (<code>originalSapling</code>)</span>
            <input v-model.trim="form.originalSapling" spellcheck="false" />
          </label>
          <label>
            <span>Leaves copy properties of (<code>originalLeaves</code>)</span>
            <input v-model.trim="form.originalLeaves" spellcheck="false" />
          </label>
          <label>
            <span>Trunk and simulator log (<code>log</code>)</span>
            <input v-model.trim="form.log" spellcheck="false" />
          </label>
        </fieldset>
        <label v-if="features.treeTypeOptions">
          <span>Leaf particles per tick (<code>particle</code>) <span class="hint">{{ form.particle > 0 ? percent(form.particle) : 'off' }}</span></span>
          <input v-model.number="form.particle" type="number" min="0" max="1" step="0.01" />
        </label>
        <label v-if="features.treeTypeOptions" class="check">
          <input v-model="form.sound" type="checkbox" />
          Ambient leaves sound
        </label>
        <fieldset v-if="features.treeTypeOptions && form.sound">
          <legend>Sound (<code>ambient_leaves_block_sound_player</code>)</legend>
          <label>
            <span>Sound event (<code>ambient_sound</code>)</span>
            <input v-model.trim="form.ambientSound" spellcheck="false" />
          </label>
          <label>
            <span>Only near blocks in tag (<code>satisfying_blocks</code>) <span class="hint">empty for none</span></span>
            <input v-model.trim="form.satisfyingBlocks" spellcheck="false" />
          </label>
          <div class="row">
            <label>
              <span><code>chance</code> <span class="hint">1 in N ticks</span></span>
              <input v-model.number="form.chance" type="number" min="1" />
            </label>
            <label>
              <span>Tag blocks around</span>
              <input v-model.number="form.nearbySatisfying" type="number" min="0" max="4" />
            </label>
            <label>
              <span>Same leaves around</span>
              <input v-model.number="form.nearbySameLeaves" type="number" min="0" max="4" />
            </label>
          </div>
        </fieldset>
        <div class="rt-field">
          <span>Preview <span class="hint">with the built-in {{ treeType(form.previewAs)?.label }} textures in place of yours</span></span>
          <div class="rt-preview">
            <ItemSlot v-for="color in sampleColors" :key="`s-${color}`" :id="form.name" :icon="saplingIcon(form.previewAs, color)" :name="`${form.name} sapling`" />
            <ItemSlot v-for="color in sampleColors" :key="`l-${color}`" :id="form.name" :icon="leavesIcon(form.previewAs, color)" :name="`${form.name} leaves`" />
          </div>
        </div>
      </div>

      <BuilderOutput :file="`config/resourcestrees/tree_type/${form.name}.json`" :text="json" :problems="problems">
        <p>Put the same file on the server and every client. Then add these grayscale textures to a resource pack:</p>
        <ul class="files">
          <li v-for="file in textures" :key="file"><code>{{ file }}</code></li>
        </ul>
      </BuilderOutput>
    </div>
  </div>
</template>

<style scoped>
.files {
  margin: 6px 0 0;
  padding-left: 18px;
}

.files li {
  margin: 2px 0;
}

p.hint {
  margin: -4px 0 0;
}
</style>

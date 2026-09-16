import DefaultTheme from 'vitepress/theme'
import type { Theme } from 'vitepress'
import AxeConfigBuilder from './components/AxeConfigBuilder.vue'
import AxeTable from './components/AxeTable.vue'
import CraftingRecipeBuilder from './components/CraftingRecipeBuilder.vue'
import EssenceTable from './components/EssenceTable.vue'
import FragmentRecipes from './components/FragmentRecipes.vue'
import GrowerTypeBuilder from './components/GrowerTypeBuilder.vue'
import ItemSlot from './components/ItemSlot.vue'
import RecipeCard from './components/RecipeCard.vue'
import ResourceTypeBuilder from './components/ResourceTypeBuilder.vue'
import ResourceTypeTable from './components/ResourceTypeTable.vue'
import SaplingExplorer from './components/SaplingExplorer.vue'
import SimulatorRecipeBuilder from './components/SimulatorRecipeBuilder.vue'
import TreeSimulatorGui from './components/TreeSimulatorGui.vue'
import TreeTypeBuilder from './components/TreeTypeBuilder.vue'
import TreeTypeTable from './components/TreeTypeTable.vue'
import VersionSwitcher from './components/VersionSwitcher.vue'
import './style.css'

export default {
  extends: DefaultTheme,
  enhanceApp({ app }) {
    app.component('AxeConfigBuilder', AxeConfigBuilder)
    app.component('AxeTable', AxeTable)
    app.component('CraftingRecipeBuilder', CraftingRecipeBuilder)
    app.component('EssenceTable', EssenceTable)
    app.component('FragmentRecipes', FragmentRecipes)
    app.component('GrowerTypeBuilder', GrowerTypeBuilder)
    app.component('ItemSlot', ItemSlot)
    app.component('RecipeCard', RecipeCard)
    app.component('ResourceTypeBuilder', ResourceTypeBuilder)
    app.component('ResourceTypeTable', ResourceTypeTable)
    app.component('SaplingExplorer', SaplingExplorer)
    app.component('SimulatorRecipeBuilder', SimulatorRecipeBuilder)
    app.component('TreeSimulatorGui', TreeSimulatorGui)
    app.component('TreeTypeBuilder', TreeTypeBuilder)
    app.component('TreeTypeTable', TreeTypeTable)
    app.component('VersionSwitcher', VersionSwitcher)
  },
} satisfies Theme

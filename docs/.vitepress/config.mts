import { defineConfig } from 'vitepress'

// GitHub Pages serves a project site from /<repository>/. For a custom domain or a user site, build with DOCS_BASE=/.
const base = process.env.DOCS_BASE ?? '/Resources-Trees/'

export default defineConfig({
  title: 'Resources Trees',
  description: 'Resource-producing trees for Minecraft 26.3: saplings, leaf fragments, the Tree Simulator and custom tree configs.',
  base,
  cleanUrls: true,
  lastUpdated: true,
  srcExclude: ['README.md', 'scripts/**'],
  // `head` entries are not rewritten for the base path, unlike links, images and the theme logo.
  head: [['link', { rel: 'icon', type: 'image/png', href: `${base}logo.png` }]],
  themeConfig: {
    logo: { src: '/logo.png', alt: '' },
    nav: [
      { text: 'Guide', link: '/gameplay/getting-started', activeMatch: '^/gameplay/' },
      { text: 'Explorer', link: '/gameplay/explorer' },
      { text: 'Pack developers', link: '/datapacks/resources-types', activeMatch: '^/datapacks/' },
      { component: 'VersionSwitcher' },
    ],
    sidebar: {
      '/v2601/': [
        {
          text: 'Guide',
          items: [
            { text: 'Getting Started', link: '/v2601/gameplay/getting-started' },
            { text: 'Updating Old Versions', link: '/v2601/gameplay/updating' },
            { text: 'Sapling Explorer', link: '/v2601/gameplay/explorer' },
            { text: 'Resources Types', link: '/v2601/gameplay/resources-types-list' },
            { text: 'Resources Saplings', link: '/v2601/gameplay/resources-saplings' },
            { text: 'Resources Leaves', link: '/v2601/gameplay/resources-leaves' },
            { text: 'Leaf Fragments', link: '/v2601/gameplay/leaf-fragments' },
            { text: 'Tree Simulator', link: '/v2601/gameplay/tree-simulator' },
            { text: 'Essence Items', link: '/v2601/gameplay/essence-items' },
          ],
        },
        {
          text: 'Configuration',
          items: [{ text: 'Common Config', link: '/v2601/config/axe-config' }],
        },
        {
          text: 'For Pack Developers',
          items: [
            { text: 'Custom Resources Types', link: '/v2601/datapacks/resources-types' },
            { text: 'Custom Tree Types', link: '/v2601/datapacks/tree-types' },
            { text: 'Tree Simulator Recipes', link: '/v2601/datapacks/tree-simulator-recipes' },
            { text: 'Sapling Recipes', link: '/v2601/datapacks/sapling-recipes' },
            { text: 'Leaf Fragment Recipes', link: '/v2601/datapacks/leaf-fragment-recipes' },
            { text: 'Tags', link: '/v2601/datapacks/tags' },
          ],
        },
        {
          text: 'For Mod Developers',
          items: [{ text: 'Plugin API', link: '/v2601/developer/plugin-api' }],
        },
      ],
      '/old/': [
        {
          text: 'Guide',
          items: [
            { text: 'Getting Started', link: '/old/gameplay/getting-started' },
            { text: 'Sapling Explorer', link: '/old/gameplay/explorer' },
            { text: 'Resources Types', link: '/old/gameplay/resources-types-list' },
            { text: 'Resources Saplings', link: '/old/gameplay/resources-saplings' },
            { text: 'Resources Leaves', link: '/old/gameplay/resources-leaves' },
            { text: 'Leaf Fragments', link: '/old/gameplay/leaf-fragments' },
            { text: 'Tree Simulator', link: '/old/gameplay/tree-simulator' },
            { text: 'Essence Items', link: '/old/gameplay/essence-items' },
          ],
        },
        {
          text: 'Configuration',
          items: [{ text: 'Axe Speed Config', link: '/old/config/axe-config' }],
        },
        {
          text: 'For Pack Developers',
          items: [
            { text: 'Custom Resources Types', link: '/old/datapacks/resources-types' },
            { text: 'Sapling Types', link: '/old/datapacks/sapling-types' },
            { text: 'Tree Simulator Recipes', link: '/old/datapacks/tree-simulator-recipes' },
            { text: 'Sapling Recipes', link: '/old/datapacks/sapling-recipes' },
            { text: 'Leaf Fragment Recipes', link: '/old/datapacks/leaf-fragment-recipes' },
          ],
        },
      ],
      // The current version lives at the root. VitePress picks the deepest matching key, so the folders above win.
      '/': [
        {
          text: 'Guide',
          items: [
            { text: 'Getting Started', link: '/gameplay/getting-started' },
            { text: 'Sapling Explorer', link: '/gameplay/explorer' },
            { text: 'Resources Types', link: '/gameplay/resources-types-list' },
            { text: 'Resources Saplings', link: '/gameplay/resources-saplings' },
            { text: 'Resources Leaves', link: '/gameplay/resources-leaves' },
            { text: 'Leaf Fragments', link: '/gameplay/leaf-fragments' },
            { text: 'Tree Simulator', link: '/gameplay/tree-simulator' },
            { text: 'Essence Items', link: '/gameplay/essence-items' },
          ],
        },
        {
          text: 'Configuration',
          items: [{ text: 'Common Config', link: '/config/axe-config' }],
        },
        {
          text: 'For Pack Developers',
          items: [
            { text: 'Custom Resources Types', link: '/datapacks/resources-types' },
            { text: 'Custom Tree Types', link: '/datapacks/tree-types' },
            { text: 'Custom Grower Types', link: '/datapacks/grower-types' },
            { text: 'Tree Simulator Recipes', link: '/datapacks/tree-simulator-recipes' },
            { text: 'Sapling Recipes', link: '/datapacks/sapling-recipes' },
            { text: 'Leaf Fragment Recipes', link: '/datapacks/leaf-fragment-recipes' },
            { text: 'Tags', link: '/datapacks/tags' },
          ],
        },
        {
          text: 'For Mod Developers',
          items: [{ text: 'Plugin API', link: '/developer/plugin-api' }],
        },
        { text: 'Changelog', link: '/changelog' },
      ],
    },
    socialLinks: [
      { icon: 'github', link: 'https://github.com/CoolerProYT/Resources-Trees' },
      { icon: 'discord', link: 'https://discord.gg/hvFfqsqQm8' },
    ],
    editLink: {
      pattern: 'https://github.com/CoolerProYT/Resources-Trees/edit/26.3/docs/:path',
      text: 'Edit this page on GitHub',
    },
    search: { provider: 'local' },
    outline: { level: [2, 3] },
    footer: {
      message: 'All Rights Reserved.',
      copyright: 'Copyright © 2024-present CoolerProMC',
    },
  },
})

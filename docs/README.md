# Resources Trees wiki

VitePress site for the mod. Resource types, tree types, axe speeds, recipes and textures are read from the mod itself, so regenerate the mod's data before building when it changes.

```bash
./gradlew :neoforge:runData   # from the repository root, when recipes or lang changed
cd docs
npm install
npm run dev                   # syncs data, then serves http://localhost:5173
npm run build                 # syncs data, then builds to .vitepress/dist
```

`npm run sync` (run automatically by `dev` and `build`) writes `.vitepress/data/data.json` and copies the mod's textures to `public/textures/` and `public/gui/`. All of them are git-ignored.

Where the data comes from:

| Data | Source |
| --- | --- |
| Built-in resource types and tree types | `InternalResourcesTreesPlugin.java` (defaults from `ResourcesType.java`) |
| Axe multipliers and the durability option | `ResourcesTreesConfig.java` |
| Essence, Tree Simulator and leaf fragment recipes, names | datagen output in `common/src/generated/resources` |
| Essence tint colours | generated item model definitions |

Sapling, leaves, leaf fragment and essence icons are drawn in the browser from the mod's grayscale textures, tinted with each resource type's colour the same way the game does. Vanilla items load from the hosted renders at `https://storage.googleapis.com/coolerpromc/textures/`, set in `.vitepress/theme/resourcestrees.ts`. The Tree Simulator has no flat texture, so its icon is a committed render in `public/icons/`.

Pages under `v2601/` and `old/` document earlier mod versions. They are kept mostly as written and are not driven by the synced data.

Pages for pack developers and the config page have JSON/HOCON generators (`*Builder.vue`). Their vanilla presets (sapling growers, tree feature ids, the poplar leaves sound) are copied by hand into `.vitepress/theme/vanilla.ts` from the Minecraft sources, so check them after porting to a new Minecraft version.

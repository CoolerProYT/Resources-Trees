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

Item icons load from the texture bucket, `https://storage.googleapis.com/coolerpromc/textures/<namespace>/<name>.png` (set in `.vitepress/theme/resourcestrees.ts`). Vanilla renders are already there. The mod's saplings, leaves, leaf fragments, essences and Tree Simulator for every documented version are rendered by `scripts/render-icons.py` and uploaded under `resourcestrees/`. After adding a resource type, tree type or essence, render and upload the new icons (existing ones are skipped):

```bash
npm run sync && npm run snapshot   # so the script sees every version's types
python scripts/render-icons.py --upload
```

The script needs Pillow and an authenticated `gcloud`. The Tree Simulator has no flat texture, so its source is a committed render in `scripts/assets/`. The builders still tint the synced grayscale textures in the browser, to preview colours that have no uploaded icon.

Pages under `v2601/` (Minecraft 26.1 – 26.2) and `old/` (before 26.1.2.100) use the same components with their own data: committed snapshots in `.vitepress/snapshots/`, read from the `origin/26.1` and `origin/1.21.11-NeoForge` branches by `npm run snapshot`. Rerun it after releasing a fix on those branches. `.vitepress/theme/version.ts` maps each folder to its data, and `legacy.ts` writes the pre-26.1 recipe formats.

Pages for pack developers and the config page have JSON/HOCON generators (`*Builder.vue`). Their vanilla presets (sapling growers, tree feature ids, the poplar leaves sound) are copied by hand into `.vitepress/theme/vanilla.ts` from the Minecraft sources, so check them after porting to a new Minecraft version.

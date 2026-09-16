# Changelog

## 26.3.0.0

- Updated to Minecraft 26.3.
- Added the **Poplar** tree type, with poplar leaf ambience.
- The [Tree Simulator](/gameplay/tree-simulator) now works with the **Fortune** enchantment: each level adds a roll to every drop.
- Added the `useAxeDurability` [config option](/config/axe-config) to stop the Tree Simulator from damaging axes.

## 26.1.2.100 — Major Update

### What's New

- **Resources Types are no longer a datapack registry.** They are now loaded from `/config/resourcestrees/resources_type/` using simple JSON files. Add the `name` field to each JSON (the filename without `.json` also works as a fallback).
- **Tree Types are now configurable.** Drop JSON files into `/config/resourcestrees/tree_type/` to register new tree shapes.
- **Every resource+tree combination is its own block/item.** Saplings, leaves, and leaf fragments are individually registered instead of relying on data components or block entities to store the resource type.
- **Automatic migration.** Legacy saplings, leaves, and leaf fragments from older versions are automatically migrated to the new block/item on load.
- **Auto-generated client assets.** Block states and item models are generated at runtime — you only need to define block models and item models for brand-new tree types.
- **Auto-generated loot tables.** If no custom loot table is defined, one is generated automatically for saplings and leaves.
- **Auto-generated recipes.** Sapling crafting recipes and Tree Simulator recipes are generated automatically unless you override them (place overrides under `/saplings/<sapling_name>.json` and `/tree_simulator/<sapling_name>.json`).
- **Plugin API added (experimental).** Developers can register resource types and tree types in code via `IResourcesTreesPlugin`. Use config files for stability.
- **KubeJS integration removed.**

### Deprecations

- Legacy saplings, leaves, and leaf fragments are deprecated and will be **completely removed in version 26.2**. Any blocks/items that have not been migrated by then will disappear.

> **Version numbering:** patch updates increment by 1, minor updates by 10, major updates by 100.

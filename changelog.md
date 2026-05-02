## 26.1.2.2
- Added new custom recipe serializer `resources_sapling`
  - no longer need to add recipe JSON for each resources type
- Removed all legacy resource sapling recipe JSON files

### NeoForge
- Added KubeJS plugin support for creating custom Resources Saplings and Leaves
  - Recipe JSONs are generated automatically for custom saplings

KubeJS Example:
```js
StartupEvents.registry('block', event => {
    event.create('oak_sapling', 'resourcestrees:resources_sapling_block') // Custom resources sapling (identifier must not be changed)
        .treeGrower('oak') // Vanilla tree grower ID (modded growers may work, but trunk placers can be buggy)
        .leaves('kubejs:oak_leaves') // Leaves block used by this sapling
        .base('minecraft:oak_sapling') // Original sapling reference
        .copyPropertiesFrom(Blocks.OAK_SAPLING);

    event.create('oak_leaves', 'resourcestrees:resources_leaves_block') // Custom leaves (identifier must not be changed)
        .sapling("kubejs:oak_sapling") // Sapling dropped from these leaves
        .copyPropertiesFrom(Blocks.OAK_LEAVES);
});
```
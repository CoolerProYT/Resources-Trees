## 26.1.2.100 (Major Update)
### Changes
- Resources Type is no longer datapack registry
- Resources Type can be added in `/config/resourcestrees/resources_type` using the old datapack resources type JSON with one additional field `name` in the JSON
- Tree type can be added in `/config/resourcestrees/tree_type`
- Every resources type + tree type (sapling and leaves) are registered individually instead of using data component/block entity to store resources type
- Legacy saplings (block/item), leaves (block/item), and leaf fragment will be migrated to new block/item automatically
- Legacy sapling, leaves, and leaf fragment are now deprecated and will be removed completely in `26.2` (Once removed, block/item that is not migrated will disappear)
- Client Item and BlockState will be generated automatically, block model and item model need to be defined for new tree type
- LootTable for sapling and leaves will be generated automatically if no default one is defined
- Recipe for sapling and tree simulator will be generating automatically if not defined (it should be under `/saplings/sapling_name.json` and `/tree_simulator/sapling_name.json`)
- Added API to add resources type and tree type (experimental, use config folder for stability)
- Removed KubeJS integration for adding sapling and leaves

Note: patch update version number increase by 1, minor update increase by 10, major update increase by 100
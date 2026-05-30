## 26.1.2.124
### Changes
- Updated CoolerConfig version to fix conflict with other mod that use newer version of it

**Note: Legacy sapling, leaves, and leaf fragment are now deprecated and will be removed completely once this mod is marked as release (Once removed, block/item that is not migrated will disappear)**

### How to migrate?
1. Download any ResourcesTrees 26.1.2 BETA version
2. If you have any custom resources type in datapack, copy the JSON files to `/config/resourcestrees/resources_type/` (Both server and client side)
3. DO NOT DELETE JSON in datapack until migration complete
4. Load into the world
5. Ensure the chunk that contain resources tree block/item loaded
6. Migration done immediately the block/item is loaded
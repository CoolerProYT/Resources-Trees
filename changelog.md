## 26.1.2.111
### Changes
- Exposed tree simulator inventory to transfer api (Top - Input, Sides - Axe, Bottom - Output)
- Make BER update 5 tick slower to prevent ghost stack happening in tree simulator menu
- Added warning message on player joined to warn about system changes

**Note: Legacy sapling, leaves, and leaf fragment are now deprecated and will be removed completely once this mod is no longer marked as BETA in 26.1.2 (Once removed, block/item that is not migrated will disappear)**

### How to migrate?
1. Download any ResourcesTrees 26.1.2 BETA version
2. If you have any custom resources type in datapack, copy the JSON files to `/config/resourcestrees/resources_type/`
3. DO NOT DELETE JSON in datapack until migration complete
4. Load into the world
5. Ensure the chunk that contain resources tree block/item loaded
6. Migration done immediately the block/item is loaded
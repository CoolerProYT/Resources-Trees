- Limit Tree Simulator sapling input slot to hold at most 1 item only
- Fixed a rare case where ghost stack rendering in Tree Simulator
- Lowered leaf fragment amount drop from leaves
- Lowered log output chance for Tree Simulator fallback recipe
- Increased leaf fragment output chance for Tree Simulator fallback recipe
- Added new field `treeSimulatorTicks` to `ResourcesTypes`, tree simulator processing time no longer hardcoded
- Removed `translationKey` from `ResourcesTypes` it will be `type.resourcestrees.{path of the resources type}` by default
- Tree simulator no longer output vanilla sapling
- Changed Resources Saplings texture
- Changed `Tree Simulator` crafting recipe to use dirt instead of grass block
- Added recipe and resources types for most of the vanilla mob drops and natural block

**Breaking Changes to Resources Sapling and Resources Leaves Block (Item not affected), if you have any sapling/leaves placed in the world, please remove them before updating to this version.**
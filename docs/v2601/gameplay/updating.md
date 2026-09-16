# Updating Old Version

26.1.2.100 has changed entire resources type registry and Resources Trees block/item registration system. To ensure nothing is lost during the update, please follow the steps for migrating old version to the new system.

## Migrating Step
1. Download any ResourcesTrees 26.1.2.100+ BETA version
2. If you have any custom resources type in datapack, copy the JSON files to `/config/resourcestrees/resources_type/`
3. DO NOT DELETE JSON in datapack until migration complete
4. Load into the world
5. Ensure the chunk that contain resources tree block/item loaded
6. Migration done immediately the block/item is loaded

## FAQ
::: info Q: Why only 26.1.2.100+ BETA?
A: All legacy registration code and resources type datapack will be removed in 26.1.2 `release` version.
:::

::: info Q: Why change the system?
A: Data-driven make pack developers hard to add recipes for saplings/leaf fragment because they will need to define components for every ItemStack entry.
:::

::: info Q: What is different that caused old block/item unusable in new system?
A: In old version all resources type share 1 block/item and differentiate with data components. In the new system every resources type + saplings/leaves/leaf fragment are registered separately.
:::
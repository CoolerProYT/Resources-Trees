# Plugin API (Experimental)

ResourcesTrees provides a Java plugin API that allows other mods to register custom resource types and tree types programmatically.

::: warning Experimental
The Plugin API is experimental and may change in future versions. For stability, prefer using [config files](/datapacks/resources-types) instead.
:::

## Adding the Dependency

ResourcesTrees is published on a Maven repository.

### `build.gradle` (Groovy DSL)

```groovy
repositories {
    maven {
        name = "CoolerProMC Maven"
        url = "https://maven.coolerpromc.com/releases"
    }
}

dependencies {
    // Pick the artifact matching your loader:

    // Common (multiloader projects)
    compileOnly "com.coolerpromc.resourcestrees:resourcestrees-common-${minecraft_version}:${resourcestrees_version}"

    // Fabric
    compileOnly "com.coolerpromc.resourcestrees:resourcestrees-fabric-${minecraft_version}:${resourcestrees_version}"

    // NeoForge
    compileOnly "com.coolerpromc.resourcestrees:resourcestrees-neoforge-${minecraft_version}:${resourcestrees_version}"
}
```

### `gradle.properties`

```properties
minecraft_version=26.3
resourcestrees_version=26.3.0.0
```

Replace `26.3.0.0` with the actual latest version.

## Implementing the Plugin

### 1. Implement `IResourcesTreesPlugin`

```java
public class MyPlugin implements IResourcesTreesPlugin {

    @Override
    public void registerResourcesType(IResourcesTypeRegistry registry) {
        // Register using a specific item
        registry.register(new ResourcesType.Builder("ruby", Items.REDSTONE, 0xFFCC0000)
                .saplingDropChance(0.1f)
                .leafDropChance(0.2f)
                .treeSimulatorTicks(1400));

        // Register using an item tag
        registry.register(new ResourcesType.Builder("logs", ItemTags.LOGS, 0xFF8D6E63));
    }

    @Override
    public void registerTreeType(ITreeTypeRegistry registry) {
        registry.register(new TreeType.Builder(
                "maple",                                              // unique name
                "maple",                                              // treeGrowerName
                Identifier.fromNamespaceAndPath("mymod", "block/maple_sapling"),  // saplingTexture
                Identifier.fromNamespaceAndPath("mymod", "block/maple_leaves"),   // leavesTexture
                "minecraft:oak_sapling",                              // originalSapling
                "minecraft:oak_leaves",                               // originalLeaves
                "minecraft:oak_log"                                   // log
        )
                .particle(0.02f)                                      // optional, defaults to 0.01
                .leavesBlockSoundPlayer(AmbientLeavesBlockSoundPlayer.noAmbientSound())  // optional
                .build());
    }

    @Override
    public void registerGrowerType(IGrowerTypeRegistry registry) {
        // Reference vanilla tree features directly via TreeFeatures
        registry.register(new GrowerType(
                "maple",                                         // name (matches TreeType#treeGrowerName)
                List.of(                                         // trees
                        new Weighted<>(TreeFeatures.OAK, 9),
                        new Weighted<>(TreeFeatures.FANCY_OAK, 1)),
                List.of(),                                       // megaTrees (may be empty)
                List.of(new Weighted<>(TreeFeatures.OAK_BEES_005, 1)),  // flowerTrees (may be empty)
                Optional.of(TreeFeatures.OAK)                    // shortestTreeType
        ));
    }
}
```

::: tip
You can reference vanilla tree features directly through `net.minecraft.data.worldgen.features.TreeFeatures` (e.g. `TreeFeatures.OAK`, `TreeFeatures.FANCY_OAK`, `TreeFeatures.SPRUCE`) instead of building a `ResourceKey` by hand.
:::

### 2. Register to EntryPoint

#### NeoForge

Use `@ResourcesTreesPlugin` annotation.

```java
@ResourcesTreesPlugin
public class MyPlugin implements IResourcesTreesPlugin {
}
```

#### Fabric

Add your class to the `resources_trees_plugin` entrypoint in `fabric.mod.json`:

```json
"entrypoints": {
  "resources_trees_plugin": [
    "com.example.mymod.MyPlugin"
  ]
}
```

## `ResourcesType.Builder` Parameters

| Parameter | Required | Default | Description |
|---|---|---|---|
| `name` | ✅ | — | Unique identifier used in block/item registry names |
| `material` | ✅ | — | `ItemLike`, `TagKey<Item>`, or `RegistryHandler<Item>` |
| `color` | ✅ | — | ARGB tint color applied to leaves, sapling, and leaf fragment |
| `saplingDropChance` | ❌ | `0.125` | Chance a sapling drops when leaves decay or are broken |
| `leafDropChance` | ❌ | `0.25` | Chance a leaf fragment drops from a leaves block |
| `treeSimulatorTicks` | ❌ | `1200` | Ticks per Tree Simulator growth cycle |
| `weight` | ❌ | `5` | **Deprecated** — no longer used |

## `TreeType` Parameters

| Parameter | Required | Default | Description |
|---|---|---|---|
| `name` | ✅ | — | Unique identifier (e.g. `"oak"`, `"birch"`) |
| `treeGrowerName` | ✅ | — | Name of the `GrowerType` used to grow this tree (e.g. `"oak"`, `"spruce"`) |
| `saplingTexture` | ✅ | — | `Identifier` of the sapling texture. The mod also uses `<id>_layer1` for the tinted part. |
| `leavesTexture` | ✅ | — | `Identifier` of the leaves texture |
| `originalSapling` | ✅ | — | Registry name of the vanilla sapling to copy properties from |
| `originalLeaves` | ✅ | — | Registry name of the vanilla leaves to copy properties from |
| `log` | ✅ | — | Registry name of the log block used as the trunk |
| `particle` | ❌ | `0.01f` | Per-tick chance (`0.0`–`1.0`) for the leaves block to spawn falling tinted leaf particles |
| `leavesBlockSoundPlayer` | ❌ | `AmbientLeavesBlockSoundPlayer.noAmbientSound()` | Controls the ambient rustling sound emitted by the leaves block |

::: tip
Use `TreeType.Builder` for the required parameters and set `particle` and `leavesBlockSoundPlayer` with its methods only when you need them. The record's own constructor takes all nine parameters.
:::

## `GrowerType` Parameters

A `GrowerType` defines which world-gen tree features a sapling can grow into. Its `name` is referenced by `TreeType#treeGrowerName`.

| Parameter | Required | Default | Description |
|---|---|---|---|
| `name` | ✅ | — | Unique identifier, referenced by `TreeType#treeGrowerName` |
| `trees` | ✅ | — | `List<Weighted<ResourceKey<Feature>>>` of features used for normal single-sapling trees (must not be empty) |
| `megaTrees` | ✅ | — | `List<Weighted<ResourceKey<Feature>>>` of features used for 2×2 mega trees (may be empty) |
| `flowerTrees` | ✅ | — | `List<Weighted<ResourceKey<Feature>>>` of flowering variants, e.g. bee-nest trees (may be empty) |
| `shortestTreeType` | ✅ | — | `Optional<ResourceKey<Feature>>` fallback feature used when there is not enough space for a full tree |

`Weighted` is Minecraft's `net.minecraft.util.random.Weighted` — wrap each feature key with a weight (`new Weighted<>(featureKey, weight)`); higher weights are chosen more often.

The `GrowerType` for a `TreeType` can also be supplied from a [config file](/datapacks/grower-types) without writing code.

## Textures for a TreeType

See [tree type textures](/datapacks/tree-types#textures).
# Plugin API (Experimental)

ResourcesTrees provides a Java plugin API that allows other mods to register custom resource types and tree types programmatically.

::: warning Experimental
The Plugin API is experimental and may change in future versions. For stability, prefer using [config files](/v2601/datapacks/resources-types) instead.
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
minecraft_version=26.1.2
resourcestrees_version=26.1.2.111
```

Replace `26.1.2.100` with the actual latest version.

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
        registry.register(new TreeType(
                "oak",                                           // unique name
                "oak",                                           // treeGrowerName
                Identifier.withDefaultNamespace("block/oak_sapling"),  // saplingTexture
                Identifier.withDefaultNamespace("block/oak_leaves"),   // leavesTexture
                "minecraft:oak_sapling",                         // originalSapling
                "minecraft:oak_leaves",                          // originalLeaves
                "minecraft:oak_log"                              // log
        ));
    }
}
```

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

| Parameter | Description |
|---|---|
| `name` | Unique identifier (e.g. `"oak"`, `"birch"`) |
| `treeGrowerName` | Key in `TreeGrower.GROWERS` (e.g. `"oak"`, `"spruce"`) |
| `saplingTexture` | `Identifier` of the sapling texture |
| `leavesTexture` | `Identifier` of the leaves texture |
| `originalSapling` | Registry name of the vanilla sapling to copy properties from |
| `originalLeaves` | Registry name of the vanilla leaves to copy properties from |
| `log` | Registry name of the log block used as the trunk |

## Texture for TreeType
Please see [tree type textures](/v2601/datapacks/tree-types#textures) for more information.
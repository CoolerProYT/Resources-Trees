# Resources Trees

A multiloader Minecraft mod (Fabric & NeoForge) that adds resource-producing trees to the game. Each tree is a combination of a **Resources Type** (what material it produces) and a **Tree Type** (what shape/species it grows as), generating unique saplings, leaves, and leaf fragments for every combination.

---

## Customization

Resources Trees is designed to be fully customizable — both through a developer API and through config/data files for server admins and modpack makers.

---

## For Developers — Adding as a Dependency

Resources Trees is published to a Maven repository. Add it to your project to use the plugin API.

### Gradle (Groovy DSL)

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
minecraft_version = 26.1.2
resourcestrees_version=26.1.2.111   # replace with actual version
```

---

## For Developers — Plugin API (Experimental)
Use config for stability.

You can add your own resource types and tree types by implementing the plugin interface.

### 1. Implement `IResourcesTreesPlugin`

```java
public class MyPlugin implements IResourcesTreesPlugin {

    @Override
    public void registerResourcesType(IResourcesTypeRegistry registry) {
        // Register a type using a specific item
        registry.register(new ResourcesType.Builder("ruby", Items.REDSTONE, 0xFFCC0000)
                .weight(4)
                .saplingDropChance(0.1f)
                .leafDropChance(0.2f)
                .treeSimulatorTicks(1400));

        // Or using an item tag
        registry.register(new ResourcesType.Builder("logs", ItemTags.LOGS, 0xFF8D6E63));
    }

    @Override
    public void registerTreeType(ITreeTypeRegistry registry) {
        registry.register(new TreeType(
                "oak",                                          // unique name
                "oak",                                          // tree grower name
                ResourceLocation.withDefaultNamespace("block/oak_sapling"),
                ResourceLocation.withDefaultNamespace("block/oak_leaves"),
                "minecraft:oak_sapling",
                "minecraft:oak_leaves",
                "minecraft:oak_log"
        ));
    }
}
```

### 2. Register to Entrypoint
#### NeoForge

Use `@ResourcesTreesPlugin` annotation.

```java
@ResourcesTreesPlugin
public class MyPlugin implements IResourcesTreesPlugin {
}
```

#### Fabric

Add `resources_trees_plugin` entrypoint to `fabric.mod.json`
```json
"entrypoints": {
  "resources_trees_plugin": [
    "com.coolerpromc.resourcestrees.api.internal.InternalResourcesTreesPlugin"
  ]
}
```

### `ResourcesType.Builder` Parameters

| Parameter | Default | Description                                                   |
|---|---|---------------------------------------------------------------|
| `name` | *(required)* | Unique identifier used in block/item names                    |
| `material` | *(required)* | Item, or `TagKey<Item>` to craft the sapling                  |
| `color` | *(required)* | ARGB tint color applied to leaves, sapling, and leaf fragment |
| `weight` | `5` | No longer used                                                |
| `saplingDropChance` | `0.125` | Chance a sapling drops when a leaf decays or is broken        |
| `leafDropChance` | `0.25` | Chance a leaf fragment drops from a leaves block              |
| `treeSimulatorTicks` | `1200` | Ticks between each Tree Simulator growth cycle                |

### `TreeType` Parameters

| Parameter | Description                                                        |
|---|--------------------------------------------------------------------|
| `name` | Unique identifier (e.g. `"oak"`, `"birch"`)                        |
| `treeGrowerName` | Must match a key in `TreeGrower.GROWERS` (e.g. `"oak"`)            |
| `saplingTexture` | `Identifier` of the sapling texture                          |
| `leavesTexture` | `Identifier` of the leaves texture                                 |
| `originalSapling` | Registry name of the vanilla sapling to copy block properties from |
| `originalLeaves` | Registry name of the vanilla leaves to copy block properties from  |
| `log` | Registry name of the log block used as the trunk                   |

---

## Modpack Makers — Config Files

No code required. You can register new types by dropping JSON files into the config directory.

### Resources Types

Place JSON files in:
```
config/resourcestrees/resources_type/<name>.json
```

The filename (without `.json`) is used as the type name. Example — `config/resourcestrees/resources_type/ruby.json`:

```json
{
  "name": "ruby",
  "material": "minecraft:redstone",
  "color": -3342336,
  "weight": 4,
  "saplingDropChance": 0.1,
  "leafDropChance": 0.2,
  "treeSimulatorTicks": 1400
}
```

To use an item tag as the material, prefix with `#`:

```json
{
  "material": "#minecraft:logs"
}
```

### Tree Types

Place JSON files in:
```
config/resourcestrees/tree_type/<name>.json
```

Example — `config/resourcestrees/tree_type/oak.json`:

```json
{
  "name": "oak",
  "treeGrowerName": "oak",
  "saplingTexture": "minecraft:block/oak_sapling",
  "leavesTexture": "minecraft:block/oak_leaves",
  "originalSapling": "minecraft:oak_sapling",
  "originalLeaves": "minecraft:oak_leaves",
  "log": "minecraft:oak_log"
}
```

---

## Axe Speed Config

The Tree Simulator's growth speed scales with the axe placed inside it. You can customize the multiplier per axe type in:

```
config/resourcestrees/axe.json
```

Default values:

```json
{
  "values": {
    "minecraft:wooden_axe": 1.0,
    "minecraft:stone_axe": 2.0,
    "minecraft:iron_axe": 3.0,
    "minecraft:golden_axe": 6.0,
    "minecraft:diamond_axe": 4.0,
    "minecraft:netherite_axe": 5.0
  }
}
```

Higher values mean faster growth. A value of `0.0` (or omitting the entry) means that axe type provides no speed bonus. Only valid `AxeItem` registry names are accepted.

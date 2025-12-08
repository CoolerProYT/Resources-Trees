package com.coolerpromc.resourcestrees.datagen;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.block.entity.custom.TreeSimulatorBlockEntity;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.datagen.recipebuilder.TreeSimulatorRecipeBuilder;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.recipe.output.TreeSimulatorOutput;
import com.coolerpromc.resourcestrees.registry.ModRegistries;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.DataComponentMatchers;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModRecipeProvider extends RecipeProvider {
    public static final Map<ResourcesSaplingBlock, Item> SAPLINGS_BY_SAPLINGS = Map.of(
            ModBlocks.RESOURCES_OAK_SAPLING.get(), Items.OAK_SAPLING,
            ModBlocks.RESOURCES_SPRUCE_SAPLING.get(), Items.SPRUCE_SAPLING,
            ModBlocks.RESOURCES_BIRCH_SAPLING.get(), Items.BIRCH_SAPLING,
            ModBlocks.RESOURCES_JUNGLE_SAPLING.get(), Items.JUNGLE_SAPLING,
            ModBlocks.RESOURCES_ACACIA_SAPLING.get(), Items.ACACIA_SAPLING,
            ModBlocks.RESOURCES_DARK_OAK_SAPLING.get(), Items.DARK_OAK_SAPLING,
            ModBlocks.RESOURCES_CHERRY_SAPLING.get(), Items.CHERRY_SAPLING,
            ModBlocks.RESOURCES_PALE_OAK_SAPLING.get(),  Items.PALE_OAK_SAPLING
    );

    private final HolderGetter<Item> items;
    private final HolderGetter<ResourcesTypes> types;
    private final HolderLookup.Provider lookupProvider;

    public ModRecipeProvider(HolderLookup.Provider lookupProvider, RecipeOutput output) {
        super(lookupProvider, output);
        this.items = lookupProvider.lookupOrThrow(Registries.ITEM);
        this.types = lookupProvider.lookupOrThrow(ModRegistries.RESOURCES_TYPES_KEY);
        this.lookupProvider = lookupProvider;
    }

    @Override
    protected void buildRecipes() {
        // Leaf Fragment to resources recipes
        circleShape(Items.COBBLESTONE, 12, ResourcesTypes.STONE);
        circleSurroundedShape(Items.STONE, 12, ResourcesTypes.STONE, ResourcesTypes.COAL);
        circleShape(Items.COAL, 12, ResourcesTypes.COAL);
        circleShape(Items.IRON_INGOT, 6, ResourcesTypes.IRON);
        circleShape(Items.COPPER_INGOT, 8, ResourcesTypes.COPPER);
        circleShape(Items.GOLD_INGOT, 6, ResourcesTypes.GOLD);
        circleShape(Items.LAPIS_LAZULI, 12, ResourcesTypes.LAPIS);
        cubeShape(Items.EMERALD, 4, ResourcesTypes.EMERALD);
        cubeShape(Items.DIAMOND, 4, ResourcesTypes.DIAMOND);
        circleShape(Items.OBSIDIAN, 6, ResourcesTypes.OBSIDIAN);
        lineShape(Items.AMETHYST_SHARD, 6, ResourcesTypes.AMETHYST);
        cubeShape(Items.NETHERITE_INGOT, 1, ResourcesTypes.NETHERITE);
        lineShape(Items.QUARTZ, 8, ResourcesTypes.QUARTZ);
        lineShape(Items.PRISMARINE_SHARD, 8, ResourcesTypes.PRISMARINE);
        lineShape(Items.GLOWSTONE_DUST, 8, ResourcesTypes.GLOWSTONE);
        circleShape(Items.REDSTONE, 12, ResourcesTypes.REDSTONE);
        customShape(Items.OAK_LOG, 8, ResourcesTypes.WOOD, " A ", " A ", " A ");
        lineShape(Items.BIRCH_LOG, 8, ResourcesTypes.WOOD);
        customShape(Items.JUNGLE_LOG, 8, ResourcesTypes.WOOD, "A  ", " A ", "  A");
        customShape(Items.SPRUCE_LOG, 8, ResourcesTypes.WOOD, "  A", " A ", "A  ");
        customShape(Items.ACACIA_LOG, 8, ResourcesTypes.WOOD, "  A", " A ", " A ");
        customShape(Items.DARK_OAK_LOG, 8, ResourcesTypes.WOOD, "A  ", " A ", " A ");
        customShape(Items.CHERRY_LOG, 8, ResourcesTypes.WOOD, " A ", " A ", "A  ");
        customShape(Items.PALE_OAK_LOG, 8, ResourcesTypes.WOOD, " A ", " A ", "  A");
        customShape(Items.BAMBOO_BLOCK, 8, ResourcesTypes.WOOD, "   ", "AA ", "  A");
        customShape(Items.CRIMSON_STEM, 8, ResourcesTypes.WOOD, "A  ", " AA", "   ");
        customShape(Items.WARPED_STEM, 8, ResourcesTypes.WOOD, "   ", " AA", "A  ");
        circleShape(Items.COBBLED_DEEPSLATE, 12, ResourcesTypes.DEEPSLATE);
        circleSurroundedShape(Items.DEEPSLATE, 12, ResourcesTypes.DEEPSLATE, ResourcesTypes.COAL);
        circleSurroundedShape(Items.TUFF, 12, ResourcesTypes.STONE, ResourcesTypes.DEEPSLATE);
        circleShape(Items.DIRT, 12, ResourcesTypes.DIRT);
        twoByTwoShape(Items.SAND, 8, ResourcesTypes.DIRT, ResourcesTypes.FIRE);
        circleSurroundedShapeWithItem(Items.RED_SAND, 8, ResourcesTypes.FIRE, Items.SAND);
        circleShape(Items.NETHERRACK, 12, ResourcesTypes.NETHER);
        circleSurroundedShape(Items.BASALT, 12, ResourcesTypes.NETHER, ResourcesTypes.STONE);
        circleSurroundedShape(Items.END_STONE, 12, ResourcesTypes.END, ResourcesTypes.STONE);
        circleSurroundedShape(Items.GRASS_BLOCK, 12, ResourcesTypes.DIRT, ResourcesTypes.NATURE);
        twoItemCustomShape(Items.PODZOL, 12, ResourcesTypes.DIRT, ResourcesTypes.WOOD, "AAA", "ABA", "AAA");
        circleSurroundedShapeWithItem(Items.MYCELIUM, 12, ResourcesTypes.DIRT, Items.BROWN_MUSHROOM_BLOCK);
        twoByTwoShape(Items.CLAY, 8, ResourcesTypes.DIRT, ResourcesTypes.WATER);
        circleSurroundedShapeWithItem(Items.MUD, 12, ResourcesTypes.WATER, Items.CLAY_BALL);
        circleSurroundedShapeWithItem(Items.GRAVEL, 12, ResourcesTypes.STONE, Items.SAND);
        circleShape(Items.ICE, 12, ResourcesTypes.ICE);
        circleSurroundedShape(Items.SNOW, 12, ResourcesTypes.ICE, ResourcesTypes.WATER);
        circleSurroundedShape(Items.MOSS_BLOCK, 12, ResourcesTypes.NATURE, ResourcesTypes.DIRT);
        circleSurroundedShape(Items.MAGMA_BLOCK, 12, ResourcesTypes.NETHER, ResourcesTypes.FIRE);
        twoItemCustomShape(Items.CRIMSON_NYLIUM, 12, ResourcesTypes.NETHER, ResourcesTypes.NATURE, "BBB", "ABA", "AAA");
        twoItemCustomShape(Items.WARPED_NYLIUM, 12, ResourcesTypes.NETHER, ResourcesTypes.NATURE, "BBB", "BAB", "AAA");
        circleSurroundedShapeWithItem(Items.SOUL_SAND, 12, ResourcesTypes.NETHER, Items.SAND);
        circleSurroundedShape(Items.SOUL_SOIL, 12, ResourcesTypes.NETHER, ResourcesTypes.DIRT);
        twoItemCustomShape(Items.OAK_SAPLING, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, "   ", "ABA", "   ");
        twoItemCustomShape(Items.SPRUCE_SAPLING, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, " A ", " B ", " A ");
        twoItemCustomShape(Items.BIRCH_SAPLING, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, "A  ", " B ", "  A");
        twoItemCustomShape(Items.JUNGLE_SAPLING, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, "  A", " B ", "A  ");
        twoItemCustomShape(Items.ACACIA_SAPLING, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, "  A", " B ", " A ");
        twoItemCustomShape(Items.DARK_OAK_SAPLING, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, "A  ", " B ", " A ");
        twoItemCustomShape(Items.MANGROVE_PROPAGULE, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, " A ", " B ", "  A");
        twoItemCustomShape(Items.CHERRY_SAPLING, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, " A ", " B ", "A  ");
        twoItemCustomShape(Items.PALE_OAK_SAPLING, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, "   ", " BA", "A  ");
        twoItemCustomShape(Items.BROWN_MUSHROOM, 6, ResourcesTypes.NATURE, ResourcesTypes.DIRT, " A ", " B ", "A  ");
        twoItemCustomShape(Items.RED_MUSHROOM, 6, ResourcesTypes.NATURE, ResourcesTypes.DIRT, "ABA", "   ", "   ");
        twoItemCustomShape(Items.CRIMSON_FUNGUS, 6, ResourcesTypes.NATURE, ResourcesTypes.NETHER, "ABA", "   ", "   ");
        twoItemCustomShape(Items.WARPED_FUNGUS, 6, ResourcesTypes.NATURE, ResourcesTypes.NETHER, " A ", " B ", " A ");
        customShape(Items.SUGAR_CANE, 12, ResourcesTypes.NATURE, "A A", "A A", "A A");
        customShape(Items.CACTUS, 12, ResourcesTypes.NATURE, "AA ", "AA ", "   ");
        twoByTwoShape(Items.CHORUS_PLANT, 8, ResourcesTypes.END, ResourcesTypes.NATURE);
        lineShape(Items.LILY_PAD, 12, ResourcesTypes.NATURE);
        twoItemCustomShape(Items.KELP, 12, ResourcesTypes.NATURE, ResourcesTypes.WATER, "  A", " B ", "A  ");
        customShape(Items.PUMPKIN, 12, ResourcesTypes.NATURE, "AAA", "AAA", "   ");
        customShape(Items.APPLE, 12, ResourcesTypes.NATURE, "AAA", "A A", "AAA");
        customShape(Items.MELON_SLICE, 12, ResourcesTypes.NATURE, "A  ", " A ", "  A");
        customShape(Items.SWEET_BERRIES, 12, ResourcesTypes.NATURE, "  A", " A ", "A  ");
        customShape(Items.CARROT, 12, ResourcesTypes.NATURE, " A ", " A ", " A ");
        customShape(Items.POTATO, 12, ResourcesTypes.NATURE, "   ", " AA", "A  ");
        customShape(Items.BEETROOT, 12, ResourcesTypes.NATURE, "  A", " AA", "A  ");
        customShape(Items.WHEAT, 12, ResourcesTypes.NATURE, "  A", "AA ", "   ");

        // Block Recipe
        ShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, ModBlocks.TREE_SIMULATOR, 1)
                .pattern("   ")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', Items.CYAN_TERRACOTTA)
                .define('B', Items.GRASS_BLOCK)
                .unlockedBy(getHasName(Items.GRASS_BLOCK), has(Items.GRASS_BLOCK))
                .unlockedBy(getHasName(Items.CYAN_TERRACOTTA), has(Items.CYAN_TERRACOTTA))
                .save(output);

        // Item Recipe
        essenceItem(ModItems.FIRE_ESSENCE.get(), Items.FIRE_CHARGE, Items.MAGMA_BLOCK, Items.LAVA_BUCKET);
        essenceItem(ModItems.NATURE_ESSENCE.get(), Items.GRASS_BLOCK, Items.MOSS_BLOCK, Items.PUMPKIN, Items.MELON, Items.SUGAR_CANE, Items.CACTUS, Items.WHEAT, Items.CARROT, Items.POTATO);
        essenceItem(ModItems.END_ESSENCE.get(), Items.END_STONE, Items.CHORUS_FRUIT, Items.CHORUS_PLANT, Items.PURPUR_BLOCK);
        essenceItem(ModItems.WATER_ESSENCE.get(), Items.WATER_BUCKET, Items.KELP, Items.SEAGRASS, Items.CLAY_BALL);

        // Saplings & Tree Simulator recipes
        ResourcesTypes.getAllResourcesTypes(lookupProvider).forEach((key, value) -> {
            Field[] fields = ModBlocks.class.getDeclaredFields();

            for (Field field : fields){
                try {
                    Object obj = field.get(null);
                    if (obj instanceof Supplier<?> supplier){
                        if (supplier.get() instanceof ResourcesSaplingBlock resourcesSaplingBlock){
                            ItemStack sapling = resourcesSaplingBlock.asItem().getDefaultInstance();
                            sapling.set(ModDataComponents.TYPE, value);
                            if (value.value().material().left().isPresent()){
                                ShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, sapling)
                                        .pattern(" A ")
                                        .pattern("ABA")
                                        .pattern(" A ")
                                        .define('A', BuiltInRegistries.ITEM.getValue(value.value().material().left().get()))
                                        .define('B', SAPLINGS_BY_SAPLINGS.get(resourcesSaplingBlock))
                                        .unlockedBy(getHasName(BuiltInRegistries.ITEM.getValue(value.value().material().left().get())), has(BuiltInRegistries.ITEM.getValue(value.value().material().left().get())))
                                        .unlockedBy(getHasName(SAPLINGS_BY_SAPLINGS.get(resourcesSaplingBlock)), has(SAPLINGS_BY_SAPLINGS.get(resourcesSaplingBlock)))
                                        .save(output, ResourceKey.create(Registries.RECIPE, key.withSuffix(BuiltInRegistries.BLOCK.getKey(resourcesSaplingBlock).getPath().substring(9)).withPrefix("saplings/")));
                            }
                            else if (value.value().material().right().isPresent()){
                                ShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, sapling)
                                        .pattern(" A ")
                                        .pattern("ABA")
                                        .pattern(" A ")
                                        .define('A', (value.value().material().right().get()))
                                        .define('B', SAPLINGS_BY_SAPLINGS.get(resourcesSaplingBlock))
                                        .unlockedBy("has_" + value.value().material().right().get().location().getPath() + "_tags", has(value.value().material().right().get()))
                                        .unlockedBy(getHasName(SAPLINGS_BY_SAPLINGS.get(resourcesSaplingBlock)), has(SAPLINGS_BY_SAPLINGS.get(resourcesSaplingBlock)))
                                        .save(output, ResourceKey.create(Registries.RECIPE, key.withSuffix(BuiltInRegistries.BLOCK.getKey(resourcesSaplingBlock).getPath().substring(9)).withPrefix("saplings/")));
                            }

                            ItemStack leaf = ModItems.LEAF_FRAGMENT.toStack();
                            leaf.set(ModDataComponents.TYPE, value);

                            TreeSimulatorRecipeBuilder.builder()
                                    .setTree(sapling)
                                    .addDrops(TreeSimulatorOutput.of(TreeSimulatorBlockEntity.LOG_BY_SAPLINGS.get(resourcesSaplingBlock).getDefaultInstance(), 1, 2, 4))
                                    .addDrops(TreeSimulatorOutput.of(leaf, 1, 1, 1))
                                    .addDrops(TreeSimulatorOutput.of(leaf, value.value().leafDropChance(), 1, 1))
                                    .addDrops(TreeSimulatorOutput.of(sapling, value.value().saplingDropChance(), 1, 1))
                                    .addDrops(TreeSimulatorOutput.of(Items.STICK.getDefaultInstance(), 0.1f, 1, 2))
                                    .addDrops(TreeSimulatorOutput.of(Items.APPLE.getDefaultInstance(), 0.05f, 1, 1))
                                    .addDrops(TreeSimulatorOutput.of(SAPLINGS_BY_SAPLINGS.get(resourcesSaplingBlock).getDefaultInstance(), 0.1f, 1, 1))
                                    .setTicksToGrow(1200)
                                    .save(output, ResourceKey.create(Registries.RECIPE, key.withSuffix(BuiltInRegistries.BLOCK.getKey(resourcesSaplingBlock).getPath().substring(9)).withPrefix("tree_simulator/")));
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void essenceItem(Item ouputItem, Item... inputItems){
        ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, ouputItem, 1);

        for (Item item : inputItems){
            builder.requires(item);
            builder.unlockedBy(getHasName(item), has(item));
        }

        builder.save(output);
    }

    private void circleShape(Item outputItem, int count, ResourceKey<ResourcesTypes> resourceType){
        ShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("AAA")
                .pattern("A A")
                .pattern("AAA")
                .define('A', DataComponentIngredient.of(true, ModDataComponents.TYPE.get(), types.getOrThrow(resourceType), ModItems.LEAF_FRAGMENT))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).key().location()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType)))
                .save(output, ResourceKey.create(Registries.RECIPE, resourceType.location().withSuffix("_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/")));
    }

    private void circleSurroundedShapeWithItem(Item outputItem, int count, ResourceKey<ResourcesTypes> resourceType, Item item){
        ShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('B', DataComponentIngredient.of(true, ModDataComponents.TYPE.get(), types.getOrThrow(resourceType), ModItems.LEAF_FRAGMENT))
                .define('A', item)
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).key().location()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType)))
                .unlockedBy(getHasName(item), has(item))
                .save(output, ResourceKey.create(Registries.RECIPE, resourceType.location().withSuffix("_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/")));
    }

    private void circleSurroundedShape(Item outputItem, int count, ResourceKey<ResourcesTypes> resourceType, ResourceKey<ResourcesTypes> middleResourceType){
        ShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', DataComponentIngredient.of(true, ModDataComponents.TYPE.get(), types.getOrThrow(resourceType), ModItems.LEAF_FRAGMENT))
                .define('B', DataComponentIngredient.of(true, ModDataComponents.TYPE.get(), types.getOrThrow(middleResourceType), ModItems.LEAF_FRAGMENT))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).key().location()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType)))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT, types.getOrThrow(middleResourceType).key().location()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(middleResourceType)))
                .save(output, ResourceKey.create(Registries.RECIPE, resourceType.location().withSuffix("_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/")));
    }

    private void twoByTwoShape(Item outputItem, int count, ResourceKey<ResourcesTypes> resourceType, ResourceKey<ResourcesTypes> middleResourceType){
        ShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("BA ")
                .pattern("AB ")
                .pattern("   ")
                .define('A', DataComponentIngredient.of(true, ModDataComponents.TYPE.get(), types.getOrThrow(resourceType), ModItems.LEAF_FRAGMENT))
                .define('B', DataComponentIngredient.of(true, ModDataComponents.TYPE.get(), types.getOrThrow(middleResourceType), ModItems.LEAF_FRAGMENT))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).key().location()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType)))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT, types.getOrThrow(middleResourceType).key().location()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(middleResourceType)))
                .save(output, ResourceKey.create(Registries.RECIPE, resourceType.location().withSuffix("_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/")));
    }

    private void cubeShape(Item outputItem, int count, ResourceKey<ResourcesTypes> resourceType){
        ShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', DataComponentIngredient.of(true, ModDataComponents.TYPE.get(), types.getOrThrow(resourceType), ModItems.LEAF_FRAGMENT))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).key().location()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType)))
                .save(output, ResourceKey.create(Registries.RECIPE, resourceType.location().withSuffix("_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/")));
    }

    private void lineShape(Item outputItem, int count, ResourceKey<ResourcesTypes> resourceType){
        ShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("   ")
                .pattern("AAA")
                .pattern("   ")
                .define('A', DataComponentIngredient.of(true, ModDataComponents.TYPE.get(), types.getOrThrow(resourceType), ModItems.LEAF_FRAGMENT))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).key().location()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType)))
                .save(output, ResourceKey.create(Registries.RECIPE, resourceType.location().withSuffix("_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/")));
    }

    private void customShape(Item outputItem, int count, ResourceKey<ResourcesTypes> resourceType, String line1, String line2, String line3){
        ShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern(line1)
                .pattern(line2)
                .pattern(line3)
                .define('A', DataComponentIngredient.of(true, ModDataComponents.TYPE.get(), types.getOrThrow(resourceType), ModItems.LEAF_FRAGMENT))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).key().location()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType)))
                .save(output, ResourceKey.create(Registries.RECIPE, resourceType.location().withSuffix("_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/")));
    }

    private void twoItemCustomShape(Item outputItem, int count, ResourceKey<ResourcesTypes> resourceType, ResourceKey<ResourcesTypes> resourceType2, String line1, String line2, String line3){
        ShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern(line1)
                .pattern(line2)
                .pattern(line3)
                .define('A', DataComponentIngredient.of(true, ModDataComponents.TYPE.get(), types.getOrThrow(resourceType), ModItems.LEAF_FRAGMENT))
                .define('B', DataComponentIngredient.of(true, ModDataComponents.TYPE.get(), types.getOrThrow(resourceType2), ModItems.LEAF_FRAGMENT))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).key().location()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType)))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType2).key().location()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType2)))
                .save(output, ResourceKey.create(Registries.RECIPE, resourceType.location().withSuffix("_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/")));
    }

    protected Criterion<InventoryChangeTrigger.TriggerInstance> has(ItemLike itemLike, Holder<ResourcesTypes> key) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(this.items, itemLike).withComponents(DataComponentMatchers.Builder.components().exact(DataComponentExactPredicate.builder().expect(ModDataComponents.TYPE.get(), key).build()).build()));
    }

    protected static String getHasName(ItemLike itemLike, ResourceLocation key) {
        return "has_" + key.getPath() + "_" + getItemName(itemLike);
    }

    public static final class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider lookupProvider, RecipeOutput output) {
            return new ModRecipeProvider(lookupProvider, output);
        }

        @Override
        public String getName() {
            return "Resources Trees recipes";
        }
    }
}

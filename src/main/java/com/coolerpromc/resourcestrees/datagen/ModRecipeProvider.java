package com.coolerpromc.resourcestrees.datagen;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.block.entity.custom.TreeSimulatorBlockEntity;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.datagen.recipebuilder.StrictShapedRecipeBuilder;
import com.coolerpromc.resourcestrees.datagen.recipebuilder.TreeSimulatorRecipeBuilder;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.recipe.output.TreeSimulatorOutput;
import com.coolerpromc.resourcestrees.registry.ModRegistries;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
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
            ModBlocks.RESOURCES_CHERRY_SAPLING.get(), Items.CHERRY_SAPLING
    );

    private final HolderGetter<ResourcesTypes> types;
    private final HolderLookup.Provider lookupProvider;

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
        this.types = lookupProvider.join().lookupOrThrow(ModRegistries.RESOURCES_TYPES_KEY);
        this.lookupProvider = lookupProvider.join();
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        // Leaf Fragment to resources recipes
        circleShape(Items.COBBLESTONE, 12, ResourcesTypes.STONE, output);
        circleSurroundedShape(Items.STONE, 12, ResourcesTypes.STONE, ResourcesTypes.COAL, output);
        circleShape(Items.COAL, 12, ResourcesTypes.COAL, output);
        circleShape(Items.IRON_INGOT, 6, ResourcesTypes.IRON, output);
        circleShape(Items.COPPER_INGOT, 8, ResourcesTypes.COPPER, output);
        circleShape(Items.GOLD_INGOT, 6, ResourcesTypes.GOLD, output);
        circleShape(Items.LAPIS_LAZULI, 12, ResourcesTypes.LAPIS, output);
        cubeShape(Items.EMERALD, 4, ResourcesTypes.EMERALD, output);
        cubeShape(Items.DIAMOND, 4, ResourcesTypes.DIAMOND, output);
        circleShape(Items.OBSIDIAN, 6, ResourcesTypes.OBSIDIAN, output);
        lineShape(Items.AMETHYST_SHARD, 6, ResourcesTypes.AMETHYST, output);
        cubeShape(Items.NETHERITE_INGOT, 1, ResourcesTypes.NETHERITE, output);
        lineShape(Items.QUARTZ, 8, ResourcesTypes.QUARTZ, output);
        lineShape(Items.PRISMARINE_SHARD, 8, ResourcesTypes.PRISMARINE, output);
        lineShape(Items.GLOWSTONE_DUST, 8, ResourcesTypes.GLOWSTONE, output);
        circleShape(Items.REDSTONE, 12, ResourcesTypes.REDSTONE, output);
        customShape(Items.OAK_LOG, 8, ResourcesTypes.WOOD, " A ", " A ", " A ", output);
        lineShape(Items.BIRCH_LOG, 8, ResourcesTypes.WOOD, output);
        customShape(Items.JUNGLE_LOG, 8, ResourcesTypes.WOOD, "A  ", " A ", "  A", output);
        customShape(Items.SPRUCE_LOG, 8, ResourcesTypes.WOOD, "  A", " A ", "A  ", output);
        customShape(Items.ACACIA_LOG, 8, ResourcesTypes.WOOD, "  A", " A ", " A ", output);
        customShape(Items.DARK_OAK_LOG, 8, ResourcesTypes.WOOD, "A  ", " A ", " A ", output);
        customShape(Items.CHERRY_LOG, 8, ResourcesTypes.WOOD, " A ", " A ", "A  ", output);
        customShape(Items.BAMBOO_BLOCK, 8, ResourcesTypes.WOOD, "   ", "AA ", "  A", output);
        customShape(Items.CRIMSON_STEM, 8, ResourcesTypes.WOOD, "A  ", " AA", "   ", output);
        customShape(Items.WARPED_STEM, 8, ResourcesTypes.WOOD, "   ", " AA", "A  ", output);
        circleShape(Items.COBBLED_DEEPSLATE, 12, ResourcesTypes.DEEPSLATE, output);
        circleSurroundedShape(Items.DEEPSLATE, 12, ResourcesTypes.DEEPSLATE, ResourcesTypes.COAL, output);
        circleSurroundedShape(Items.TUFF, 12, ResourcesTypes.STONE, ResourcesTypes.DEEPSLATE, output);
        circleShape(Items.DIRT, 12, ResourcesTypes.DIRT, output);
        twoByTwoShape(Items.SAND, 8, ResourcesTypes.DIRT, ResourcesTypes.FIRE, output);
        circleSurroundedShapeWithItem(Items.RED_SAND, 8, ResourcesTypes.FIRE, Items.SAND, output);
        circleShape(Items.NETHERRACK, 12, ResourcesTypes.NETHER, output);
        circleSurroundedShape(Items.BASALT, 12, ResourcesTypes.NETHER, ResourcesTypes.STONE, output);
        circleSurroundedShape(Items.END_STONE, 12, ResourcesTypes.END, ResourcesTypes.STONE, output);
        circleSurroundedShape(Items.GRASS_BLOCK, 12, ResourcesTypes.DIRT, ResourcesTypes.NATURE, output);
        twoItemCustomShape(Items.PODZOL, 12, ResourcesTypes.DIRT, ResourcesTypes.WOOD, "AAA", "ABA", "AAA", output);
        circleSurroundedShapeWithItem(Items.MYCELIUM, 12, ResourcesTypes.DIRT, Items.BROWN_MUSHROOM_BLOCK, output);
        twoByTwoShape(Items.CLAY, 8, ResourcesTypes.DIRT, ResourcesTypes.WATER, output);
        circleSurroundedShapeWithItem(Items.MUD, 12, ResourcesTypes.WATER, Items.CLAY_BALL, output);
        circleSurroundedShapeWithItem(Items.GRAVEL, 12, ResourcesTypes.STONE, Items.SAND, output);
        circleShape(Items.ICE, 12, ResourcesTypes.ICE, output);
        circleSurroundedShape(Items.SNOW, 12, ResourcesTypes.ICE, ResourcesTypes.WATER, output);
        circleSurroundedShape(Items.MOSS_BLOCK, 12, ResourcesTypes.NATURE, ResourcesTypes.DIRT, output);
        circleSurroundedShape(Items.MAGMA_BLOCK, 12, ResourcesTypes.NETHER, ResourcesTypes.FIRE, output);
        twoItemCustomShape(Items.CRIMSON_NYLIUM, 12, ResourcesTypes.NETHER, ResourcesTypes.NATURE, "BBB", "ABA", "AAA", output);
        twoItemCustomShape(Items.WARPED_NYLIUM, 12, ResourcesTypes.NETHER, ResourcesTypes.NATURE, "BBB", "BAB", "AAA", output);
        circleSurroundedShapeWithItem(Items.SOUL_SAND, 12, ResourcesTypes.NETHER, Items.SAND, output);
        circleSurroundedShape(Items.SOUL_SOIL, 12, ResourcesTypes.NETHER, ResourcesTypes.DIRT, output);
        twoItemCustomShape(Items.OAK_SAPLING, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, "   ", "ABA", "   ", output);
        twoItemCustomShape(Items.SPRUCE_SAPLING, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, " A ", " B ", " A ", output);
        twoItemCustomShape(Items.BIRCH_SAPLING, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, "A  ", " B ", "  A", output);
        twoItemCustomShape(Items.JUNGLE_SAPLING, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, "  A", " B ", "A  ", output);
        twoItemCustomShape(Items.ACACIA_SAPLING, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, "  A", " B ", " A ", output);
        twoItemCustomShape(Items.DARK_OAK_SAPLING, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, "A  ", " B ", " A ", output);
        twoItemCustomShape(Items.MANGROVE_PROPAGULE, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, " A ", " B ", "  A", output);
        twoItemCustomShape(Items.CHERRY_SAPLING, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, " A ", " B ", "A  ", output);
        twoItemCustomShape(Items.BROWN_MUSHROOM, 6, ResourcesTypes.NATURE, ResourcesTypes.DIRT, " A ", " B ", "A  ", output);
        twoItemCustomShape(Items.RED_MUSHROOM, 6, ResourcesTypes.NATURE, ResourcesTypes.DIRT, "ABA", "   ", "   ", output);
        twoItemCustomShape(Items.CRIMSON_FUNGUS, 6, ResourcesTypes.NATURE, ResourcesTypes.NETHER, "ABA", "   ", "   ", output);
        twoItemCustomShape(Items.WARPED_FUNGUS, 6, ResourcesTypes.NATURE, ResourcesTypes.NETHER, " A ", " B ", " A ", output);
        customShape(Items.SUGAR_CANE, 12, ResourcesTypes.NATURE, "A A", "A A", "A A", output);
        customShape(Items.CACTUS, 12, ResourcesTypes.NATURE, "AA ", "AA ", "   ", output);
        twoByTwoShape(Items.CHORUS_PLANT, 8, ResourcesTypes.END, ResourcesTypes.NATURE, output);
        lineShape(Items.LILY_PAD, 12, ResourcesTypes.NATURE, output);
        twoItemCustomShape(Items.KELP, 12, ResourcesTypes.NATURE, ResourcesTypes.WATER, "  A", " B ", "A  ", output);
        customShape(Items.PUMPKIN, 12, ResourcesTypes.NATURE, "AAA", "AAA", "   ", output);
        customShape(Items.APPLE, 12, ResourcesTypes.NATURE, "AAA", "A A", "AAA", output);
        customShape(Items.MELON_SLICE, 12, ResourcesTypes.NATURE, "A  ", " A ", "  A", output);
        customShape(Items.SWEET_BERRIES, 12, ResourcesTypes.NATURE, "  A", " A ", "A  ", output);
        customShape(Items.CARROT, 12, ResourcesTypes.NATURE, " A ", " A ", " A ", output);
        customShape(Items.POTATO, 12, ResourcesTypes.NATURE, "   ", " AA", "A  ", output);
        customShape(Items.BEETROOT, 12, ResourcesTypes.NATURE, "  A", " AA", "A  ", output);
        customShape(Items.WHEAT, 12, ResourcesTypes.NATURE, "  A", "AA ", "   ", output);

        // Block Recipe
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.TREE_SIMULATOR, 1)
                .pattern("   ")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', Items.CYAN_TERRACOTTA)
                .define('B', Items.GRASS_BLOCK)
                .unlockedBy(getHasName(Items.GRASS_BLOCK), has(Items.GRASS_BLOCK))
                .unlockedBy(getHasName(Items.CYAN_TERRACOTTA), has(Items.CYAN_TERRACOTTA))
                .save(output);

        // Item Recipe
        essenceItem(ModItems.FIRE_ESSENCE.get(), output, Items.FIRE_CHARGE, Items.MAGMA_BLOCK, Items.LAVA_BUCKET);
        essenceItem(ModItems.NATURE_ESSENCE.get(), output, Items.GRASS_BLOCK, Items.MOSS_BLOCK, Items.PUMPKIN, Items.MELON, Items.SUGAR_CANE, Items.CACTUS, Items.WHEAT, Items.CARROT, Items.POTATO);
        essenceItem(ModItems.END_ESSENCE.get(), output, Items.END_STONE, Items.CHORUS_FRUIT, Items.CHORUS_PLANT, Items.PURPUR_BLOCK);
        essenceItem(ModItems.WATER_ESSENCE.get(), output, Items.WATER_BUCKET, Items.KELP, Items.SEAGRASS, Items.CLAY_BALL);

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
                                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, sapling)
                                        .pattern(" A ")
                                        .pattern("ABA")
                                        .pattern(" A ")
                                        .define('A', BuiltInRegistries.ITEM.get(value.value().material().left().get()))
                                        .define('B', SAPLINGS_BY_SAPLINGS.get(resourcesSaplingBlock))
                                        .unlockedBy(getHasName(BuiltInRegistries.ITEM.get(value.value().material().left().get())), has(BuiltInRegistries.ITEM.get(value.value().material().left().get())))
                                        .unlockedBy(getHasName(SAPLINGS_BY_SAPLINGS.get(resourcesSaplingBlock)), has(SAPLINGS_BY_SAPLINGS.get(resourcesSaplingBlock)))
                                        .save(output, key.withSuffix(BuiltInRegistries.BLOCK.getKey(resourcesSaplingBlock).getPath().substring(9)).withPrefix("saplings/"));
                            }
                            else if (value.value().material().right().isPresent()){
                                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, sapling)
                                        .pattern(" A ")
                                        .pattern("ABA")
                                        .pattern(" A ")
                                        .define('A', (value.value().material().right().get()))
                                        .define('B', SAPLINGS_BY_SAPLINGS.get(resourcesSaplingBlock))
                                        .unlockedBy("has_" + value.value().material().right().get().location().getPath() + "_tags", has(value.value().material().right().get()))
                                        .unlockedBy(getHasName(SAPLINGS_BY_SAPLINGS.get(resourcesSaplingBlock)), has(SAPLINGS_BY_SAPLINGS.get(resourcesSaplingBlock)))
                                        .save(output, key.withSuffix(BuiltInRegistries.BLOCK.getKey(resourcesSaplingBlock).getPath().substring(9)).withPrefix("saplings/"));
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
                                    .save(output, key.withSuffix(BuiltInRegistries.BLOCK.getKey(resourcesSaplingBlock).getPath().substring(9)).withPrefix("tree_simulator/"));
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    
    private void essenceItem(Item ouputItem, RecipeOutput output, Item... inputItems){
        ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ouputItem, 1);

        for (Item item : inputItems){
            builder.requires(item);
            builder.unlockedBy(getHasName(item), has(item));
        }

        builder.save(output);
    }

    private void circleShape(Item outputItem, int count, ResourceKey<ResourcesTypes> resourceType, RecipeOutput output){
        StrictShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("AAA")
                .pattern("A A")
                .pattern("AAA")
                .define('A', DataComponentIngredient.of(true, ModDataComponents.TYPE.get(), types.getOrThrow(resourceType), ModItems.LEAF_FRAGMENT))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).key().location()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType)))
                .save(output, resourceType.location().withSuffix("_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/"));
    }

    private void circleSurroundedShapeWithItem(Item outputItem, int count, ResourceKey<ResourcesTypes> resourceType, Item item, RecipeOutput output){
        StrictShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('B', DataComponentIngredient.of(true, ModDataComponents.TYPE.get(), types.getOrThrow(resourceType), ModItems.LEAF_FRAGMENT))
                .define('A', item)
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).key().location()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType)))
                .unlockedBy(getHasName(item), has(item))
                .save(output, resourceType.location().withSuffix("_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/"));
    }

    private void circleSurroundedShape(Item outputItem, int count, ResourceKey<ResourcesTypes> resourceType, ResourceKey<ResourcesTypes> middleResourceType, RecipeOutput output){
        StrictShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', DataComponentIngredient.of(true, ModDataComponents.TYPE.get(), types.getOrThrow(resourceType), ModItems.LEAF_FRAGMENT))
                .define('B', DataComponentIngredient.of(true, ModDataComponents.TYPE.get(), types.getOrThrow(middleResourceType), ModItems.LEAF_FRAGMENT))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).key().location()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType)))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT, types.getOrThrow(middleResourceType).key().location()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(middleResourceType)))
                .save(output, resourceType.location().withSuffix("_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/"));
    }

    private void twoByTwoShape(Item outputItem, int count, ResourceKey<ResourcesTypes> resourceType, ResourceKey<ResourcesTypes> middleResourceType, RecipeOutput output){
        StrictShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("BA ")
                .pattern("AB ")
                .pattern("   ")
                .define('A', DataComponentIngredient.of(true, ModDataComponents.TYPE.get(), types.getOrThrow(resourceType), ModItems.LEAF_FRAGMENT))
                .define('B', DataComponentIngredient.of(true, ModDataComponents.TYPE.get(), types.getOrThrow(middleResourceType), ModItems.LEAF_FRAGMENT))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).key().location()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType)))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT, types.getOrThrow(middleResourceType).key().location()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(middleResourceType)))
                .save(output, resourceType.location().withSuffix("_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/"));
    }

    private void cubeShape(Item outputItem, int count, ResourceKey<ResourcesTypes> resourceType, RecipeOutput output){
        StrictShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', DataComponentIngredient.of(true, ModDataComponents.TYPE.get(), types.getOrThrow(resourceType), ModItems.LEAF_FRAGMENT))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).key().location()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType)))
                .save(output, resourceType.location().withSuffix("_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/"));
    }

    private void lineShape(Item outputItem, int count, ResourceKey<ResourcesTypes> resourceType, RecipeOutput output){
        StrictShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("   ")
                .pattern("AAA")
                .pattern("   ")
                .define('A', DataComponentIngredient.of(true, ModDataComponents.TYPE.get(), types.getOrThrow(resourceType), ModItems.LEAF_FRAGMENT))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).key().location()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType)))
                .save(output, resourceType.location().withSuffix("_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/"));
    }

    private void customShape(Item outputItem, int count, ResourceKey<ResourcesTypes> resourceType, String line1, String line2, String line3, RecipeOutput output){
        StrictShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern(line1)
                .pattern(line2)
                .pattern(line3)
                .define('A', DataComponentIngredient.of(true, ModDataComponents.TYPE.get(), types.getOrThrow(resourceType), ModItems.LEAF_FRAGMENT))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).key().location()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType)))
                .save(output, resourceType.location().withSuffix("_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/"));
    }

    private void twoItemCustomShape(Item outputItem, int count, ResourceKey<ResourcesTypes> resourceType, ResourceKey<ResourcesTypes> resourceType2, String line1, String line2, String line3, RecipeOutput output){
        StrictShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern(line1)
                .pattern(line2)
                .pattern(line3)
                .define('A', DataComponentIngredient.of(true, ModDataComponents.TYPE.get(), types.getOrThrow(resourceType), ModItems.LEAF_FRAGMENT))
                .define('B', DataComponentIngredient.of(true, ModDataComponents.TYPE.get(), types.getOrThrow(resourceType2), ModItems.LEAF_FRAGMENT))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).key().location()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType)))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType2).key().location()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType2)))
                .save(output, resourceType.location().withSuffix("_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/"));
    }

    protected Criterion<InventoryChangeTrigger.TriggerInstance> has(ItemLike itemLike, Holder<ResourcesTypes> key) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(itemLike).hasComponents(DataComponentPredicate.builder().expect(ModDataComponents.TYPE.get(), key).build()));
    }

    protected static String getHasName(ItemLike itemLike, ResourceLocation key) {
        return "has_" + key.getPath() + "_" + getItemName(itemLike);
    }
}

package com.coolerpromc.resourcestrees.datagen;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.api.resources.ResourcesType;
import com.coolerpromc.resourcestrees.api.resources.ResourcesTypes;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.datagen.recipebuilder.StrictShapedRecipeBuilder;
import com.coolerpromc.resourcestrees.item.ModItems;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    private final HolderGetter<Item> items;

    public ModRecipeProvider(HolderLookup.Provider lookupProvider, RecipeOutput output) {
        super(lookupProvider, output);
        this.items = lookupProvider.lookupOrThrow(Registries.ITEM);
    }

    @Override
    protected void buildRecipes() {
        // Leaf Fragment to resources recipes
        // Logs
        customShape(Items.OAK_LOG, 8, "wood", " A ", " A ", " A ");
        lineShape(Items.BIRCH_LOG, 8, "wood");
        customShape(Items.JUNGLE_LOG, 8, "wood", "A  ", " A ", "  A");
        customShape(Items.SPRUCE_LOG, 8, "wood", "  A", " A ", "A  ");
        customShape(Items.ACACIA_LOG, 8, "wood", "  A", " A ", " A ");
        customShape(Items.DARK_OAK_LOG, 8, "wood", "A  ", " A ", " A ");
        customShape(Items.CHERRY_LOG, 8, "wood", " A ", " A ", "A  ");
        customShape(Items.BAMBOO_BLOCK, 8, "wood", "   ", "AA ", "  A");
        customShape(Items.CRIMSON_STEM, 8, "wood", "A  ", " AA", "   ");
        customShape(Items.WARPED_STEM, 8, "wood", "   ", " AA", "A  ");

        // Stones
        circleSurroundedShape(Items.STONE, 12, "stone", "coal");
        circleShape(Items.COBBLESTONE, 12, "stone");
        circleSurroundedShape(Items.DEEPSLATE, 12, "deepslate", "coal");
        circleShape(Items.COBBLED_DEEPSLATE, 12, "deepslate");
        circleSurroundedShape(Items.TUFF, 12, "stone", "deepslate");
        circleShape(Items.NETHERRACK, 12, "nether");
        circleSurroundedShape(Items.BASALT, 12, "nether", "stone");
        circleSurroundedShape(Items.END_STONE, 12, "end", "stone");

        // Natural Blocks
        circleSurroundedShape(Items.GRASS_BLOCK, 12, "dirt", "nature");
        twoItemCustomShape(Items.PODZOL, 12, "dirt", "wood", "AAA", "ABA", "AAA");
        circleSurroundedShapeWithItem(Items.MYCELIUM, 12, "dirt", Items.BROWN_MUSHROOM_BLOCK);
        circleShape(Items.DIRT, 12, "dirt");
        circleSurroundedShapeWithItem(Items.MUD, 12, "water", Items.CLAY_BALL);
        twoByTwoShape(Items.CLAY_BALL, 12, "dirt", "water");
        circleSurroundedShapeWithItem(Items.GRAVEL, 12, "stone", Items.SAND);
        twoByTwoShape(Items.SAND, 8, "dirt", "fire");
        circleSurroundedShapeWithItem(Items.RED_SAND, 8, "fire", Items.SAND);
        circleShape(Items.ICE, 12, "ice");
        circleSurroundedShape(Items.SNOWBALL, 12, "ice", "water");
        circleSurroundedShape(Items.MOSS_BLOCK, 12, "nature", "dirt");
        twoByTwoShape(Items.DRIPSTONE_BLOCK, 8, "water", "stone");
        twoItemCustomShape(Items.POINTED_DRIPSTONE, 12, "water", "stone", "B B", "B B", " A ");
        circleSurroundedShape(Items.MAGMA_BLOCK, 12, "nether", "fire");
        circleShape(Items.OBSIDIAN, 6, "obsidian");
        circleSurroundedShape(Items.CRYING_OBSIDIAN, 8, "obsidian", "nether");
        twoItemCustomShape(Items.CRIMSON_NYLIUM, 12, "nether", "nature", "BBB", "ABA", "AAA");
        twoItemCustomShape(Items.WARPED_NYLIUM, 12, "nether", "nature", "BBB", "BAB", "AAA");
        circleSurroundedShapeWithItem(Items.SOUL_SAND, 12, "nether", Items.SAND);
        circleSurroundedShape(Items.SOUL_SOIL, 12, "nether", "dirt");

        // Minerals
        circleShape(Items.COAL, 12, "coal");
        circleShape(Items.IRON_INGOT, 6, "iron");
        circleShape(Items.COPPER_INGOT, 8, "copper");
        circleShape(Items.GOLD_INGOT, 6, "gold");
        circleShape(Items.LAPIS_LAZULI, 12, "lapis");
        cubeShape(Items.EMERALD, 4, "emerald");
        cubeShape(Items.DIAMOND, 4, "diamond");
        lineShape(Items.AMETHYST_SHARD, 6, "amethyst");
        cubeShape(Items.NETHERITE_INGOT, 1, "netherite");
        lineShape(Items.QUARTZ, 8, "quartz");
        lineShape(Items.PRISMARINE_SHARD, 8, "prismarine");
        lineShape(Items.GLOWSTONE_DUST, 8, "glowstone");
        circleShape(Items.REDSTONE, 12, "redstone");
        twoByTwoShape(Items.FLINT, 8, "stone", "dirt");

        // Saplings
        twoItemCustomShape(Items.OAK_SAPLING, 6, "wood", "nature", "   ", "ABA", "   ");
        twoItemCustomShape(Items.SPRUCE_SAPLING, 6, "wood", "nature", " A ", " B ", " A ");
        twoItemCustomShape(Items.BIRCH_SAPLING, 6, "wood", "nature", "A  ", " B ", "  A");
        twoItemCustomShape(Items.JUNGLE_SAPLING, 6, "wood", "nature", "  A", " B ", "A  ");
        twoItemCustomShape(Items.ACACIA_SAPLING, 6, "wood", "nature", "  A", " B ", " A ");
        twoItemCustomShape(Items.DARK_OAK_SAPLING, 6, "wood", "nature", "A  ", " B ", " A ");
        twoItemCustomShape(Items.MANGROVE_PROPAGULE, 6, "wood", "nature", " A ", " B ", "  A");
        twoItemCustomShape(Items.CHERRY_SAPLING, 6, "wood", "nature", " A ", " B ", "A  ");
        twoItemCustomShape(Items.AZALEA, 6, "wood", "nature", " B ", " B ", "A  ");
        twoItemCustomShape(Items.FLOWERING_AZALEA, 6, "wood", "nature", " A ", " B ", "B  ");
        twoItemCustomShape(Items.BROWN_MUSHROOM, 6, "nature", "dirt", " A ", " B ", "A  ");
        twoItemCustomShape(Items.RED_MUSHROOM, 6, "nature", "dirt", "ABA", "   ", "   ");
        twoItemCustomShape(Items.CRIMSON_FUNGUS, 6, "nature", "nether", "ABA", "   ", "   ");
        twoItemCustomShape(Items.WARPED_FUNGUS, 6, "nature", "nether", " A ", " B ", " A ");

        // Plants
        twoItemCustomShape(Items.BAMBOO, 12, "nature", "wood", " A ", " B ", " A ");
        customShape(Items.SUGAR_CANE, 12, "nature", "A A", "A A", "A A");
        customShape(Items.CACTUS, 12, "nature", "AA ", "AA ", "   ");
        twoItemCustomShape(Items.WEEPING_VINES, 12, "nature", "nether", "A A", "B B", "A A");
        twoItemCustomShape(Items.TWISTING_VINES, 6, "nature", "nether", "A  ", " B ", "  A");
        customShape(Items.VINE, 6, "nature", "A A", "AAA", "A A");
        twoByTwoShape(Items.CHORUS_PLANT, 8, "end", "nature");
        circleSurroundedShape(Items.CHORUS_FLOWER, 6, "end", "nature");
        twoByTwoShape(Items.GLOW_BERRIES, 8, "nature", "glowstone");
        twoByTwoShape(Items.SWEET_BERRIES, 8, "nature", "fire");
        twoByTwoShape(Items.NETHER_WART, 8, "nether", "nature");
        lineShape(Items.LILY_PAD, 12, "nature");
        twoItemCustomShape(Items.KELP, 12, "nature", "water", "  A", " B ", "A  ");
        customShape(Items.MELON_SLICE, 12, "nature", "A  ", " A ", "  A");
        customShape(Items.PUMPKIN, 12, "nature", "AAA", "AAA", "   ");
        customShape(Items.WHEAT, 12, "nature", "  A", "AA ", "   ");
        lineShape(Items.HONEYCOMB, 8, "bee");
        circleShape(Items.HONEY_BLOCK, 8, "bee");
        lineShape(Items.SLIME_BALL, 8, "slime");
        circleShape(Items.SCULK, 4, "sculk");
        circleSurroundedShape(Items.SCULK_CATALYST, 4, "sculk", "stone");
        twoItemCustomShape(Items.SCULK_SHRIEKER, 2, "sculk", "stone", "B B", "B B", "AAA");
        customShape(Items.SCULK_SENSOR, 2, "sculk", "   ", " A ", "AAA");
        straightShape(Items.ECHO_SHARD, 4, "sculk");

        // Mob Drops
        lineShape(Items.BONE, 8, "skeleton");
        lineShape(Items.STRING, 8, "spider");
        circleShape(Items.SPIDER_EYE, 8, "spider");
        lineShape(Items.EGG, 8, "chicken");
        straightShape(Items.FEATHER, 8, "chicken");
        customShape(Items.CHICKEN, 8, "chicken", "A  ", " A ", "  A");
        lineShape(Items.LEATHER, 8, "cow");
        straightShape(Items.BEEF, 8, "cow");
        lineShape(Items.RABBIT, 8, "rabbit");
        straightShape(Items.RABBIT_HIDE, 8, "rabbit");
        customShape(Items.RABBIT_FOOT, 8, "rabbit", "  A", " A ", "A  ");
        lineShape(Items.INK_SAC, 8, "squid");
        twoItemCustomShape(Items.GLOW_INK_SAC, 8, "squid", "glowstone", "   ", "ABA", "   ");
        lineShape(Items.TURTLE_SCUTE, 8, "turtle");
        straightShape(Items.TURTLE_EGG, 8, "turtle");
        straightShape(Items.PRISMARINE_CRYSTALS, 8, "prismarine");
        straightShape(Items.BLAZE_ROD, 8, "blaze");
        straightShape(Items.BREEZE_ROD, 8, "breeze");
        lineShape(Items.ENDER_PEARL, 8, "ender_pearl");
        customShape(Items.SHULKER_SHELL, 4, "shulker", "AAA", "A A", "   ");
        lineShape(Items.GUNPOWDER, 8, "gunpowder");
        lineShape(Items.GHAST_TEAR, 6, "ghast");
        lineShape(Items.PORKCHOP, 8, "pig");
        lineShape(Items.MUTTON, 8, "sheep");
        customShape(Items.WHITE_WOOL, 8, "sheep", "AA ", "AA ", "   ");
        lineShape(Items.COD, 8, "fish");
        straightShape(Items.SALMON, 8, "fish");
        customShape(Items.TROPICAL_FISH, 8, "fish", "A  ", " A ", "  A");
        customShape(Items.PUFFERFISH, 8, "fish", "  A", " A ", "A  ");
        customShape(Items.NETHER_STAR, 2, "nether_star", " A ", "AAA", " A ");
        lineShape(Items.ROTTEN_FLESH, 8, "zombie");

        // Dyes
        lineShape(Items.WHITE_DYE, 8, "dye");
        straightShape(Items.LIGHT_GRAY_DYE, 8, "dye");
        customShape(Items.GRAY_DYE, 8, "dye", "A  ", " A ", "  A");
        customShape(Items.BLACK_DYE, 8, "dye", "  A", " A ", "A  ");
        customShape(Items.BROWN_DYE, 8, "dye", " A ", " A ", "A  ");
        customShape(Items.RED_DYE, 8, "dye", " A ", " A ", "  A");
        customShape(Items.ORANGE_DYE, 8, "dye", "   ", "AA ", "  A");
        customShape(Items.YELLOW_DYE, 8, "dye", "   ", " AA", "A  ");
        customShape(Items.LIME_DYE, 8, "dye", "AA ", "A  ", "   ");
        customShape(Items.GREEN_DYE, 8, "dye", " AA", "  A", "   ");
        customShape(Items.CYAN_DYE, 8, "dye", "   ", "  A", " AA");
        customShape(Items.LIGHT_BLUE_DYE, 8, "dye", "   ", "A  ", "AA ");
        customShape(Items.BLUE_DYE, 8, "dye", "  A", "   ", "AA ");
        customShape(Items.PURPLE_DYE, 8, "dye", "A  ", "   ", " AA");
        customShape(Items.MAGENTA_DYE, 8, "dye", "A  ", "  A", "  A");
        customShape(Items.PINK_DYE, 8, "dye", "  A", "A  ", "A  ");

        // Foods
        customShape(Items.APPLE, 12, "nature", "AAA", "A A", "AAA");
        customShape(Items.CARROT, 12, "nature", " A ", " A ", " A ");
        customShape(Items.POTATO, 12, "nature", "   ", " AA", "A  ");
        customShape(Items.BEETROOT, 12, "nature", "  A", " AA", "A  ");

        // Block Recipe
        StrictShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, ModBlocks.TREE_SIMULATOR.get(), 1)
                .pattern("   ")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', Items.CYAN_TERRACOTTA)
                .define('B', Items.DIRT)
                .unlockedBy(getHasName(Items.DIRT), has(Items.DIRT))
                .unlockedBy(getHasName(Items.CYAN_TERRACOTTA), has(Items.CYAN_TERRACOTTA))
                .save(output);

        // Item Recipe
        essenceItem(ModItems.FIRE_ESSENCE.get(), Items.FIRE_CHARGE, Items.MAGMA_BLOCK, Items.LAVA_BUCKET);
        essenceItem(ModItems.NATURE_ESSENCE.get(), Items.MOSS_BLOCK, Items.SUGAR_CANE, Items.CACTUS, Items.WHEAT, Items.CARROT, Items.POTATO);
        essenceItem(ModItems.END_ESSENCE.get(), Items.END_STONE, Items.CHORUS_FRUIT, Items.CHORUS_FLOWER, Items.PURPUR_BLOCK);
        essenceItem(ModItems.WATER_ESSENCE.get(), Items.WATER_BUCKET, Items.KELP, Items.SEAGRASS, Items.CLAY_BALL);
        essenceItem(ModItems.BEE_ESSENCE.get(), Items.HONEY_BLOCK, Items.HONEYCOMB_BLOCK, Items.HONEY_BLOCK, Items.HONEYCOMB_BLOCK);
        essenceItem(ModItems.SCULK_ESSENCE.get(), Items.SCULK, Items.SCULK_CATALYST, Items.SCULK_SENSOR, Items.SCULK_SHRIEKER, Items.ECHO_SHARD, Items.ECHO_SHARD);
        essenceItem(ModItems.SKELETON_ESSENCE.get(), Items.BONE, Items.BONE, Items.BONE, Items.BONE);
        essenceItem(ModItems.SPIDER_ESSENCE.get(), Items.SPIDER_EYE, Items.SPIDER_EYE, Items.STRING, Items.STRING, Items.SPIDER_EYE, Items.SPIDER_EYE, Items.STRING, Items.STRING);
        essenceItem(ModItems.CHICKEN_ESSENCE.get(), Items.CHICKEN, Items.CHICKEN, Items.FEATHER, Items.FEATHER, Items.EGG, Items.EGG);
        essenceItem(ModItems.COW_ESSENCE.get(), Items.LEATHER, Items.LEATHER, Items.LEATHER, Items.BEEF, Items.BEEF, Items.BEEF);
        essenceItem(ModItems.RABBIT_ESSENCE.get(), Items.RABBIT_HIDE, Items.RABBIT_HIDE, Items.RABBIT, Items.RABBIT, Items.RABBIT_FOOT, Items.RABBIT_FOOT);
        essenceItem(ModItems.SQUID_ESSENCE.get(), Items.INK_SAC, Items.INK_SAC, Items.INK_SAC, Items.GLOW_INK_SAC, Items.GLOW_INK_SAC, Items.GLOW_INK_SAC);
        essenceItem(ModItems.TURTLE_ESSENCE.get(), Items.TURTLE_EGG, Items.TURTLE_EGG, Items.TURTLE_EGG, Items.TURTLE_SCUTE, Items.TURTLE_SCUTE, Items.TURTLE_SCUTE);
        essenceItem(ModItems.BLAZE_ESSENCE.get(), Items.BLAZE_ROD, Items.BLAZE_ROD, Items.BLAZE_ROD, Items.BLAZE_ROD);
        essenceItem(ModItems.BREEZE_ESSENCE.get(), Items.BREEZE_ROD, Items.BREEZE_ROD, Items.BREEZE_ROD, Items.BREEZE_ROD);
        essenceItem(ModItems.DYE_ESSENCE.get(), Items.LIME_DYE, Items.LIGHT_BLUE_DYE, Items.CYAN_DYE, Items.PURPLE_DYE);
        essenceItem(ModItems.GHAST_ESSENCE.get(), Items.GHAST_TEAR, Items.GHAST_TEAR, Items.GHAST_TEAR, Items.GHAST_TEAR);
        essenceItem(ModItems.PIG_ESSENCE.get(), Items.PORKCHOP, Items.PORKCHOP, Items.PORKCHOP, Items.PORKCHOP);
        essenceItem(ModItems.SHEEP_ESSENCE.get(), Items.MUTTON, Items.MUTTON, Items.WHITE_WOOL, Items.WHITE_WOOL);
        essenceItem(ModItems.FISH_ESSENCE.get(), Items.COD, Items.SALMON, Items.TROPICAL_FISH, Items.PUFFERFISH);
        essenceItem(ModItems.ZOMBIE_ESSENCE.get(), Items.ROTTEN_FLESH, Items.ROTTEN_FLESH, Items.ROTTEN_FLESH, Items.ROTTEN_FLESH);
    }

    private void essenceItem(ItemLike outputItem, Item... inputItems){
        ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, outputItem, 1);

        for (Item item : inputItems){
            builder.requires(item);
            builder.unlockedBy(getHasName(item), has(item));
        }

        builder.save(output);
    }

    private void circleShape(Item outputItem, int count, String resourceType){
        ResourcesType type = ResourcesTypes.getType(resourceType);
        StrictShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("AAA")
                .pattern("A A")
                .pattern("AAA")
                .define('A', ResourcesTypes.getType(resourceType).leafFragmentItem().get())
                .unlockedBy(getHasName(type.leafFragmentItem().get(), type.name()), has(type.leafFragmentItem().get()))
                .save(output, ResourceKey.create(Registries.RECIPE, Constants.id(ResourcesTypes.getType(resourceType).name()).withSuffix("_leaf_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/")));
    }

    private void circleSurroundedShapeWithItem(Item outputItem, int count, String resourceType, Item item){
        ResourcesType type = ResourcesTypes.getType(resourceType);
        StrictShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('B', ResourcesTypes.getType(resourceType).leafFragmentItem().get())
                .define('A', item)
                .unlockedBy(getHasName(type.leafFragmentItem().get(), type.name()), has(type.leafFragmentItem().get()))
                .unlockedBy(getHasName(item), has(item))
                .save(output, ResourceKey.create(Registries.RECIPE, Constants.id(ResourcesTypes.getType(resourceType).name()).withSuffix("_leaf_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/")));
    }

    private void circleSurroundedShape(Item outputItem, int count, String resourceType, String middleResourceType){
        ResourcesType type = ResourcesTypes.getType(resourceType);
        ResourcesType middleType = ResourcesTypes.getType(middleResourceType);
        StrictShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', ResourcesTypes.getType(resourceType).leafFragmentItem().get())
                .define('B', ResourcesTypes.getType(middleResourceType).leafFragmentItem().get())
                .unlockedBy(getHasName(type.leafFragmentItem().get(), type.name()), has(type.leafFragmentItem().get()))
                .unlockedBy(getHasName(middleType.leafFragmentItem().get(), middleType.name()), has(middleType.leafFragmentItem().get()))
                .save(output, ResourceKey.create(Registries.RECIPE, Constants.id(ResourcesTypes.getType(resourceType).name()).withSuffix("_leaf_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/")));
    }

    private void twoByTwoShape(Item outputItem, int count, String resourceType, String middleResourceType){
        ResourcesType type = ResourcesTypes.getType(resourceType);
        ResourcesType middleType = ResourcesTypes.getType(middleResourceType);
        StrictShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("BA ")
                .pattern("AB ")
                .pattern("   ")
                .define('A', ResourcesTypes.getType(resourceType).leafFragmentItem().get())
                .define('B', ResourcesTypes.getType(middleResourceType).leafFragmentItem().get())
                .unlockedBy(getHasName(type.leafFragmentItem().get(), type.name()), has(type.leafFragmentItem().get()))
                .unlockedBy(getHasName(middleType.leafFragmentItem().get(), middleType.name()), has(middleType.leafFragmentItem().get()))
                .save(output, ResourceKey.create(Registries.RECIPE, Constants.id(ResourcesTypes.getType(resourceType).name()).withSuffix("_leaf_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/")));
    }

    private void cubeShape(Item outputItem, int count, String resourceType){
        ResourcesType type = ResourcesTypes.getType(resourceType);
        StrictShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ResourcesTypes.getType(resourceType).leafFragmentItem().get())
                .unlockedBy(getHasName(type.leafFragmentItem().get(), type.name()), has(type.leafFragmentItem().get()))
                .save(output, ResourceKey.create(Registries.RECIPE, Constants.id(ResourcesTypes.getType(resourceType).name()).withSuffix("_leaf_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/")));
    }

    private void lineShape(Item outputItem, int count, String resourceType){
        ResourcesType type = ResourcesTypes.getType(resourceType);
        StrictShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("   ")
                .pattern("AAA")
                .pattern("   ")
                .define('A', ResourcesTypes.getType(resourceType).leafFragmentItem().get())
                .unlockedBy(getHasName(type.leafFragmentItem().get(), type.name()), has(type.leafFragmentItem().get()))
                .save(output, ResourceKey.create(Registries.RECIPE, Constants.id(ResourcesTypes.getType(resourceType).name()).withSuffix("_leaf_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/")));
    }

    private void straightShape(Item outputItem, int count, String resourceType){
        ResourcesType type = ResourcesTypes.getType(resourceType);
        StrictShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern(" A ")
                .pattern(" A ")
                .pattern(" A ")
                .define('A', ResourcesTypes.getType(resourceType).leafFragmentItem().get())
                .unlockedBy(getHasName(type.leafFragmentItem().get(), type.name()), has(type.leafFragmentItem().get()))
                .save(output, ResourceKey.create(Registries.RECIPE, Constants.id(ResourcesTypes.getType(resourceType).name()).withSuffix("_leaf_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/")));
    }

    private void customShape(Item outputItem, int count, String resourceType, String line1, String line2, String line3){
        ResourcesType type = ResourcesTypes.getType(resourceType);
        StrictShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern(line1)
                .pattern(line2)
                .pattern(line3)
                .define('A', ResourcesTypes.getType(resourceType).leafFragmentItem().get())
                .unlockedBy(getHasName(type.leafFragmentItem().get(), type.name()), has(type.leafFragmentItem().get()))
                .save(output, ResourceKey.create(Registries.RECIPE, Constants.id(ResourcesTypes.getType(resourceType).name()).withSuffix("_leaf_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/")));
    }

    private void twoItemCustomShape(Item outputItem, int count, String resourceType, String resourceType2, String line1, String line2, String line3){
        ResourcesType type = ResourcesTypes.getType(resourceType);
        ResourcesType type2 = ResourcesTypes.getType(resourceType2);
        StrictShapedRecipeBuilder.shaped(items, RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern(line1)
                .pattern(line2)
                .pattern(line3)
                .define('A', ResourcesTypes.getType(resourceType).leafFragmentItem().get())
                .define('B', ResourcesTypes.getType(resourceType2).leafFragmentItem().get())
                .unlockedBy(getHasName(type.leafFragmentItem().get(), type.name()), has(type.leafFragmentItem().get()))
                .unlockedBy(getHasName(type2.leafFragmentItem().get(), type2.name()), has(type2.leafFragmentItem().get()))
                .save(output, ResourceKey.create(Registries.RECIPE, Constants.id(ResourcesTypes.getType(resourceType).name()).withSuffix("_leaf_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/")));
    }

    private ResourceKey<Recipe<?>> key(String name){
        return ResourceKey.create(Registries.RECIPE, Constants.id(name));
    }

    protected static String getHasName(ItemLike itemLike, String key) {
        return "has_" + key + "_" + getItemName(itemLike);
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

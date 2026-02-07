package com.coolerpromc.resourcestrees.datagen;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.block.entity.custom.TreeSimulatorBlockEntity;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datagen.recipebuilder.ShapedRecipeNBTOutputBuilder;
import com.coolerpromc.resourcestrees.datagen.recipebuilder.StrictShapedRecipeBuilder;
import com.coolerpromc.resourcestrees.datagen.recipebuilder.TreeSimulatorRecipeBuilder;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.recipe.output.TreeSimulatorOutput;
import com.coolerpromc.resourcestrees.registry.ModRegistries;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.PartialNBTIngredient;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
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
        super(output);
        this.types = lookupProvider.join().lookupOrThrow(ModRegistries.RESOURCES_TYPES_KEY);
        this.lookupProvider = lookupProvider.join();
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> output) {
        // Leaf Fragment to resources recipes
        // Logs
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

        // Stones
        circleSurroundedShape(Items.STONE, 12, ResourcesTypes.STONE, ResourcesTypes.COAL, output);
        circleShape(Items.COBBLESTONE, 12, ResourcesTypes.STONE, output);
        circleSurroundedShape(Items.DEEPSLATE, 12, ResourcesTypes.DEEPSLATE, ResourcesTypes.COAL, output);
        circleShape(Items.COBBLED_DEEPSLATE, 12, ResourcesTypes.DEEPSLATE, output);
        circleSurroundedShape(Items.TUFF, 12, ResourcesTypes.STONE, ResourcesTypes.DEEPSLATE, output);
        circleShape(Items.NETHERRACK, 12, ResourcesTypes.NETHER, output);
        circleSurroundedShape(Items.BASALT, 12, ResourcesTypes.NETHER, ResourcesTypes.STONE, output);
        circleSurroundedShape(Items.END_STONE, 12, ResourcesTypes.END, ResourcesTypes.STONE, output);

        // Natural Blocks
        circleSurroundedShape(Items.GRASS_BLOCK, 12, ResourcesTypes.DIRT, ResourcesTypes.NATURE, output);
        twoItemCustomShape(Items.PODZOL, 12, ResourcesTypes.DIRT, ResourcesTypes.WOOD, "AAA", "ABA", "AAA", output);
        circleSurroundedShapeWithItem(Items.MYCELIUM, 12, ResourcesTypes.DIRT, Items.BROWN_MUSHROOM_BLOCK, output);
        circleShape(Items.DIRT, 12, ResourcesTypes.DIRT, output);
        circleSurroundedShapeWithItem(Items.MUD, 12, ResourcesTypes.WATER, Items.CLAY_BALL, output);
        twoByTwoShape(Items.CLAY_BALL, 12, ResourcesTypes.DIRT, ResourcesTypes.WATER, output);
        circleSurroundedShapeWithItem(Items.GRAVEL, 12, ResourcesTypes.STONE, Items.SAND, output);
        twoByTwoShape(Items.SAND, 8, ResourcesTypes.DIRT, ResourcesTypes.FIRE, output);
        circleSurroundedShapeWithItem(Items.RED_SAND, 8, ResourcesTypes.FIRE, Items.SAND, output);
        circleShape(Items.ICE, 12, ResourcesTypes.ICE, output);
        circleSurroundedShape(Items.SNOWBALL, 12, ResourcesTypes.ICE, ResourcesTypes.WATER, output);
        circleSurroundedShape(Items.MOSS_BLOCK, 12, ResourcesTypes.NATURE, ResourcesTypes.DIRT, output);
        twoByTwoShape(Items.DRIPSTONE_BLOCK, 8, ResourcesTypes.WATER, ResourcesTypes.STONE, output);
        twoItemCustomShape(Items.POINTED_DRIPSTONE, 12, ResourcesTypes.WATER, ResourcesTypes.STONE, "B B", "B B", " A ", output);
        circleSurroundedShape(Items.MAGMA_BLOCK, 12, ResourcesTypes.NETHER, ResourcesTypes.FIRE, output);
        circleShape(Items.OBSIDIAN, 6, ResourcesTypes.OBSIDIAN, output);
        circleSurroundedShape(Items.CRYING_OBSIDIAN, 8, ResourcesTypes.OBSIDIAN, ResourcesTypes.NETHER, output);
        twoItemCustomShape(Items.CRIMSON_NYLIUM, 12, ResourcesTypes.NETHER, ResourcesTypes.NATURE, "BBB", "ABA", "AAA", output);
        twoItemCustomShape(Items.WARPED_NYLIUM, 12, ResourcesTypes.NETHER, ResourcesTypes.NATURE, "BBB", "BAB", "AAA", output);
        circleSurroundedShapeWithItem(Items.SOUL_SAND, 12, ResourcesTypes.NETHER, Items.SAND, output);
        circleSurroundedShape(Items.SOUL_SOIL, 12, ResourcesTypes.NETHER, ResourcesTypes.DIRT, output);

        // Minerals
        circleShape(Items.COAL, 12, ResourcesTypes.COAL, output);
        circleShape(Items.IRON_INGOT, 6, ResourcesTypes.IRON, output);
        circleShape(Items.COPPER_INGOT, 8, ResourcesTypes.COPPER, output);
        circleShape(Items.GOLD_INGOT, 6, ResourcesTypes.GOLD, output);
        circleShape(Items.LAPIS_LAZULI, 12, ResourcesTypes.LAPIS, output);
        cubeShape(Items.EMERALD, 4, ResourcesTypes.EMERALD, output);
        cubeShape(Items.DIAMOND, 4, ResourcesTypes.DIAMOND, output);
        lineShape(Items.AMETHYST_SHARD, 6, ResourcesTypes.AMETHYST, output);
        cubeShape(Items.NETHERITE_INGOT, 1, ResourcesTypes.NETHERITE, output);
        lineShape(Items.QUARTZ, 8, ResourcesTypes.QUARTZ, output);
        lineShape(Items.PRISMARINE_SHARD, 8, ResourcesTypes.PRISMARINE, output);
        lineShape(Items.GLOWSTONE_DUST, 8, ResourcesTypes.GLOWSTONE, output);
        circleShape(Items.REDSTONE, 12, ResourcesTypes.REDSTONE, output);
        twoByTwoShape(Items.FLINT, 8, ResourcesTypes.STONE, ResourcesTypes.DIRT, output);

        // Saplings
        twoItemCustomShape(Items.OAK_SAPLING, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, "   ", "ABA", "   ", output);
        twoItemCustomShape(Items.SPRUCE_SAPLING, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, " A ", " B ", " A ", output);
        twoItemCustomShape(Items.BIRCH_SAPLING, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, "A  ", " B ", "  A", output);
        twoItemCustomShape(Items.JUNGLE_SAPLING, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, "  A", " B ", "A  ", output);
        twoItemCustomShape(Items.ACACIA_SAPLING, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, "  A", " B ", " A ", output);
        twoItemCustomShape(Items.DARK_OAK_SAPLING, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, "A  ", " B ", " A ", output);
        twoItemCustomShape(Items.MANGROVE_PROPAGULE, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, " A ", " B ", "  A", output);
        twoItemCustomShape(Items.CHERRY_SAPLING, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, " A ", " B ", "A  ", output);
        twoItemCustomShape(Items.AZALEA, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, " B ", " B ", "A  ", output);
        twoItemCustomShape(Items.FLOWERING_AZALEA, 6, ResourcesTypes.WOOD, ResourcesTypes.NATURE, " A ", " B ", "B  ", output);
        twoItemCustomShape(Items.BROWN_MUSHROOM, 6, ResourcesTypes.NATURE, ResourcesTypes.DIRT, " A ", " B ", "A  ", output);
        twoItemCustomShape(Items.RED_MUSHROOM, 6, ResourcesTypes.NATURE, ResourcesTypes.DIRT, "ABA", "   ", "   ", output);
        twoItemCustomShape(Items.CRIMSON_FUNGUS, 6, ResourcesTypes.NATURE, ResourcesTypes.NETHER, "ABA", "   ", "   ", output);
        twoItemCustomShape(Items.WARPED_FUNGUS, 6, ResourcesTypes.NATURE, ResourcesTypes.NETHER, " A ", " B ", " A ", output);

        // Plants
        twoItemCustomShape(Items.BAMBOO, 12, ResourcesTypes.NATURE, ResourcesTypes.WOOD, " A ", " B ", " A ", output);
        customShape(Items.SUGAR_CANE, 12, ResourcesTypes.NATURE, "A A", "A A", "A A", output);
        customShape(Items.CACTUS, 12, ResourcesTypes.NATURE, "AA ", "AA ", "   ", output);
        twoItemCustomShape(Items.WEEPING_VINES, 12, ResourcesTypes.NATURE, ResourcesTypes.NETHER, "A A", "B B", "A A", output);
        twoItemCustomShape(Items.TWISTING_VINES, 6, ResourcesTypes.NATURE, ResourcesTypes.NETHER, "A  ", " B ", "  A", output);
        customShape(Items.VINE, 6, ResourcesTypes.NATURE, "A A", "AAA", "A A", output);
        twoByTwoShape(Items.CHORUS_PLANT, 8, ResourcesTypes.END, ResourcesTypes.NATURE, output);
        circleSurroundedShape(Items.CHORUS_FLOWER, 6, ResourcesTypes.END, ResourcesTypes.NATURE, output);
        twoByTwoShape(Items.GLOW_BERRIES, 8, ResourcesTypes.NATURE, ResourcesTypes.GLOWSTONE, output);
        twoByTwoShape(Items.SWEET_BERRIES, 8, ResourcesTypes.NATURE, ResourcesTypes.FIRE, output);
        twoByTwoShape(Items.NETHER_WART, 8, ResourcesTypes.NETHER, ResourcesTypes.NATURE, output);
        lineShape(Items.LILY_PAD, 12, ResourcesTypes.NATURE, output);
        twoItemCustomShape(Items.KELP, 12, ResourcesTypes.NATURE, ResourcesTypes.WATER, "  A", " B ", "A  ", output);
        customShape(Items.MELON_SLICE, 12, ResourcesTypes.NATURE, "A  ", " A ", "  A", output);
        customShape(Items.PUMPKIN, 12, ResourcesTypes.NATURE, "AAA", "AAA", "   ", output);
        customShape(Items.WHEAT, 12, ResourcesTypes.NATURE, "  A", "AA ", "   ", output);
        lineShape(Items.HONEYCOMB, 8, ResourcesTypes.BEE, output);
        circleShape(Items.HONEY_BLOCK, 8, ResourcesTypes.BEE, output);
        lineShape(Items.SLIME_BALL, 8, ResourcesTypes.SLIME, output);
        circleShape(Items.SCULK, 4, ResourcesTypes.SCULK, output);
        circleSurroundedShape(Items.SCULK_CATALYST, 4, ResourcesTypes.SCULK, ResourcesTypes.STONE, output);
        twoItemCustomShape(Items.SCULK_SHRIEKER, 2, ResourcesTypes.SCULK, ResourcesTypes.STONE, "B B", "B B", "AAA", output);
        customShape(Items.SCULK_SENSOR, 2, ResourcesTypes.SCULK, "   ", " A ", "AAA", output);
        straightShape(Items.ECHO_SHARD, 4, ResourcesTypes.SCULK, output);

        // Mob Drops
        lineShape(Items.BONE, 8, ResourcesTypes.SKELETON, output);
        lineShape(Items.STRING, 8, ResourcesTypes.SPIDER, output);
        circleShape(Items.SPIDER_EYE, 8, ResourcesTypes.SPIDER, output);
        lineShape(Items.EGG, 8, ResourcesTypes.CHICKEN, output);
        straightShape(Items.FEATHER, 8, ResourcesTypes.CHICKEN, output);
        customShape(Items.CHICKEN, 8, ResourcesTypes.CHICKEN, "A  ", " A ", "  A", output);
        lineShape(Items.LEATHER, 8, ResourcesTypes.COW, output);
        straightShape(Items.BEEF, 8, ResourcesTypes.COW, output);
        lineShape(Items.RABBIT, 8, ResourcesTypes.RABBIT, output);
        straightShape(Items.RABBIT_HIDE, 8, ResourcesTypes.RABBIT, output);
        customShape(Items.RABBIT_FOOT, 8, ResourcesTypes.RABBIT, "  A", " A ", "A  ", output);
        lineShape(Items.INK_SAC, 8, ResourcesTypes.SQUID, output);
        twoItemCustomShape(Items.GLOW_INK_SAC, 8, ResourcesTypes.SQUID, ResourcesTypes.GLOWSTONE, "   ", "ABA", "   ", output);
        straightShape(Items.TURTLE_EGG, 8, ResourcesTypes.TURTLE, output);
        straightShape(Items.PRISMARINE_CRYSTALS, 8, ResourcesTypes.PRISMARINE, output);
        straightShape(Items.BLAZE_ROD, 8, ResourcesTypes.BLAZE, output);
        lineShape(Items.ENDER_PEARL, 8, ResourcesTypes.ENDER_PEARL, output);
        customShape(Items.SHULKER_SHELL, 4, ResourcesTypes.SHULKER, "AAA", "A A", "   ", output);
        lineShape(Items.GUNPOWDER, 8, ResourcesTypes.GUNPOWDER, output);
        lineShape(Items.GHAST_TEAR, 6, ResourcesTypes.GHAST, output);
        lineShape(Items.PORKCHOP, 8, ResourcesTypes.PIG, output);
        lineShape(Items.MUTTON, 8, ResourcesTypes.SHEEP, output);
        customShape(Items.WHITE_WOOL, 8, ResourcesTypes.SHEEP, "AA ", "AA ", "   ", output);
        lineShape(Items.COD, 8, ResourcesTypes.FISH, output);
        straightShape(Items.SALMON, 8, ResourcesTypes.FISH, output);
        customShape(Items.TROPICAL_FISH, 8, ResourcesTypes.FISH, "A  ", " A ", "  A", output);
        customShape(Items.PUFFERFISH, 8, ResourcesTypes.FISH, "  A", " A ", "A  ", output);
        customShape(Items.NETHER_STAR, 2, ResourcesTypes.NETHER_STAR, " A ", "AAA", " A ", output);
        lineShape(Items.ROTTEN_FLESH, 8, ResourcesTypes.ZOMBIE, output);

        // Dyes
        lineShape(Items.WHITE_DYE, 8, ResourcesTypes.DYE, output);
        straightShape(Items.LIGHT_GRAY_DYE, 8, ResourcesTypes.DYE, output);
        customShape(Items.GRAY_DYE, 8, ResourcesTypes.DYE, "A  ", " A ", "  A", output);
        customShape(Items.BLACK_DYE, 8, ResourcesTypes.DYE, "  A", " A ", "A  ", output);
        customShape(Items.BROWN_DYE, 8, ResourcesTypes.DYE, " A ", " A ", "A  ", output);
        customShape(Items.RED_DYE, 8, ResourcesTypes.DYE, " A ", " A ", "  A", output);
        customShape(Items.ORANGE_DYE, 8, ResourcesTypes.DYE, "   ", "AA ", "  A", output);
        customShape(Items.YELLOW_DYE, 8, ResourcesTypes.DYE, "   ", " AA", "A  ", output);
        customShape(Items.LIME_DYE, 8, ResourcesTypes.DYE, "AA ", "A  ", "   ", output);
        customShape(Items.GREEN_DYE, 8, ResourcesTypes.DYE, " AA", "  A", "   ", output);
        customShape(Items.CYAN_DYE, 8, ResourcesTypes.DYE, "   ", "  A", " AA", output);
        customShape(Items.LIGHT_BLUE_DYE, 8, ResourcesTypes.DYE, "   ", "A  ", "AA ", output);
        customShape(Items.BLUE_DYE, 8, ResourcesTypes.DYE, "  A", "   ", "AA ", output);
        customShape(Items.PURPLE_DYE, 8, ResourcesTypes.DYE, "A  ", "   ", " AA", output);
        customShape(Items.MAGENTA_DYE, 8, ResourcesTypes.DYE, "A  ", "  A", "  A", output);
        customShape(Items.PINK_DYE, 8, ResourcesTypes.DYE, "  A", "A  ", "A  ", output);

        // Foods
        customShape(Items.APPLE, 12, ResourcesTypes.NATURE, "AAA", "A A", "AAA", output);
        customShape(Items.CARROT, 12, ResourcesTypes.NATURE, " A ", " A ", " A ", output);
        customShape(Items.POTATO, 12, ResourcesTypes.NATURE, "   ", " AA", "A  ", output);
        customShape(Items.BEETROOT, 12, ResourcesTypes.NATURE, "  A", " AA", "A  ", output);

        // Block Recipe
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.TREE_SIMULATOR.get(), 1)
                .pattern("   ")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', Items.CYAN_TERRACOTTA)
                .define('B', Items.DIRT)
                .unlockedBy(getHasName(Items.DIRT), has(Items.DIRT))
                .unlockedBy(getHasName(Items.CYAN_TERRACOTTA), has(Items.CYAN_TERRACOTTA))
                .save(output);

        // Item Recipe
        essenceItem(ModItems.FIRE_ESSENCE.get(), output, Items.FIRE_CHARGE, Items.MAGMA_BLOCK, Items.LAVA_BUCKET);
        essenceItem(ModItems.NATURE_ESSENCE.get(), output, Items.MOSS_BLOCK, Items.SUGAR_CANE, Items.CACTUS, Items.WHEAT, Items.CARROT, Items.POTATO);
        essenceItem(ModItems.END_ESSENCE.get(), output, Items.END_STONE, Items.CHORUS_FRUIT, Items.CHORUS_PLANT, Items.PURPUR_BLOCK);
        essenceItem(ModItems.WATER_ESSENCE.get(), output, Items.WATER_BUCKET, Items.KELP, Items.SEAGRASS, Items.CLAY_BALL);
        essenceItem(ModItems.BEE_ESSENCE.get(), output, Items.HONEY_BLOCK, Items.HONEYCOMB_BLOCK, Items.HONEY_BLOCK, Items.HONEYCOMB_BLOCK);
        essenceItem(ModItems.SCULK_ESSENCE.get(), output, Items.SCULK, Items.SCULK_CATALYST, Items.SCULK_SENSOR, Items.SCULK_SHRIEKER, Items.ECHO_SHARD, Items.ECHO_SHARD);
        essenceItem(ModItems.SKELETON_ESSENCE.get(), output, Items.BONE, Items.BONE, Items.BONE, Items.BONE);
        essenceItem(ModItems.SPIDER_ESSENCE.get(), output, Items.SPIDER_EYE, Items.SPIDER_EYE, Items.STRING, Items.STRING, Items.SPIDER_EYE, Items.SPIDER_EYE, Items.STRING, Items.STRING);
        essenceItem(ModItems.CHICKEN_ESSENCE.get(), output, Items.CHICKEN, Items.CHICKEN, Items.FEATHER, Items.FEATHER, Items.EGG, Items.EGG);
        essenceItem(ModItems.COW_ESSENCE.get(), output, Items.LEATHER, Items.LEATHER, Items.LEATHER, Items.BEEF, Items.BEEF, Items.BEEF);
        essenceItem(ModItems.RABBIT_ESSENCE.get(), output, Items.RABBIT_HIDE, Items.RABBIT_HIDE, Items.RABBIT, Items.RABBIT, Items.RABBIT_FOOT, Items.RABBIT_FOOT);
        essenceItem(ModItems.SQUID_ESSENCE.get(), output, Items.INK_SAC, Items.INK_SAC, Items.INK_SAC, Items.GLOW_INK_SAC, Items.GLOW_INK_SAC, Items.GLOW_INK_SAC);
        essenceItem(ModItems.TURTLE_ESSENCE.get(), output, Items.TURTLE_EGG, Items.TURTLE_EGG, Items.TURTLE_EGG);
        essenceItem(ModItems.BLAZE_ESSENCE.get(), output, Items.BLAZE_ROD, Items.BLAZE_ROD, Items.BLAZE_ROD, Items.BLAZE_ROD);
        essenceItem(ModItems.DYE_ESSENCE.get(), output, Items.LIME_DYE, Items.LIGHT_BLUE_DYE, Items.CYAN_DYE, Items.PURPLE_DYE);
        essenceItem(ModItems.GHAST_ESSENCE.get(), output, Items.GHAST_TEAR, Items.GHAST_TEAR, Items.GHAST_TEAR, Items.GHAST_TEAR);
        essenceItem(ModItems.PIG_ESSENCE.get(), output, Items.PORKCHOP, Items.PORKCHOP, Items.PORKCHOP, Items.PORKCHOP);
        essenceItem(ModItems.SHEEP_ESSENCE.get(), output, Items.MUTTON, Items.MUTTON, Items.WHITE_WOOL, Items.WHITE_WOOL);
        essenceItem(ModItems.FISH_ESSENCE.get(), output, Items.COD, Items.SALMON, Items.TROPICAL_FISH, Items.PUFFERFISH);
        essenceItem(ModItems.ZOMBIE_ESSENCE.get(), output, Items.ROTTEN_FLESH, Items.ROTTEN_FLESH, Items.ROTTEN_FLESH, Items.ROTTEN_FLESH);

        // Saplings & Tree Simulator recipes
        ResourcesTypes.getAllResourcesTypes(lookupProvider).forEach((key, value) -> {
            Field[] fields = ModBlocks.class.getDeclaredFields();

            for (Field field : fields){
                try {
                    Object obj = field.get(null);
                    if (obj instanceof Supplier<?> supplier){
                        if (supplier.get() instanceof ResourcesSaplingBlock resourcesSaplingBlock){
                            ItemStack sapling = resourcesSaplingBlock.asItem().getDefaultInstance();
                            sapling.getOrCreateTag().putString("type", key.toString());
                            if (value.value().material().left().isPresent()){
                                ShapedRecipeNBTOutputBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, sapling)
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
                                ShapedRecipeNBTOutputBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, sapling)
                                        .pattern(" A ")
                                        .pattern("ABA")
                                        .pattern(" A ")
                                        .define('A', (value.value().material().right().get()))
                                        .define('B', SAPLINGS_BY_SAPLINGS.get(resourcesSaplingBlock))
                                        .unlockedBy("has_" + value.value().material().right().get().location().getPath() + "_tags", has(value.value().material().right().get()))
                                        .unlockedBy(getHasName(SAPLINGS_BY_SAPLINGS.get(resourcesSaplingBlock)), has(SAPLINGS_BY_SAPLINGS.get(resourcesSaplingBlock)))
                                        .save(output, key.withSuffix(BuiltInRegistries.BLOCK.getKey(resourcesSaplingBlock).getPath().substring(9)).withPrefix("saplings/"));
                            }

                            ItemStack leaf = ModItems.LEAF_FRAGMENT.get().getDefaultInstance();
                            leaf.getOrCreateTag().putString("type", key.toString());

                            TreeSimulatorRecipeBuilder.builder()
                                    .setTree(sapling)
                                    .addDrops(TreeSimulatorOutput.of(TreeSimulatorBlockEntity.LOG_BY_SAPLINGS.get(resourcesSaplingBlock).getDefaultInstance(), 0.5f, 1, 4))
                                    .addDrops(TreeSimulatorOutput.of(leaf, 1, 1, 1))
                                    .addDrops(TreeSimulatorOutput.of(leaf, value.value().leafDropChance(), 1, 4))
                                    .addDrops(TreeSimulatorOutput.of(sapling, value.value().saplingDropChance(), 1, 1))
                                    .addDrops(TreeSimulatorOutput.of(Items.STICK.getDefaultInstance(), 0.1f, 1, 2))
                                    .addDrops(TreeSimulatorOutput.of(Items.APPLE.getDefaultInstance(), 0.05f, 1, 1))
                                    .setTicksToGrow(value.value().treeSimulatorTicks())
                                    .save(output, key.withSuffix(BuiltInRegistries.BLOCK.getKey(resourcesSaplingBlock).getPath().substring(9)).withPrefix("tree_simulator/"));
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    
    private void essenceItem(ItemLike ouputItem, Consumer<FinishedRecipe> output, Item... inputItems){
        ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ouputItem, 1);
        List<Item> added = new ArrayList<>();

        for (Item item : inputItems){
            builder.requires(item);
            if (!added.contains(item)){
                builder.unlockedBy(getHasName(item), has(item));
                added.add(item);
            }
        }

        builder.save(output);
    }

    private void circleShape(Item outputItem, int count, ResourceKey<ResourcesTypes> resourceType, Consumer<FinishedRecipe> output){
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("type", types.getOrThrow(resourceType).key().location().toString());

        StrictShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("AAA")
                .pattern("A A")
                .pattern("AAA")
                .define('A', PartialNBTIngredient.of(compoundTag, ModItems.LEAF_FRAGMENT.get()))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT.get(), types.getOrThrow(resourceType).key().location()), has(ModItems.LEAF_FRAGMENT.get(), compoundTag))
                .save(output, resourceType.location().withSuffix("_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/"));
    }

    private void circleSurroundedShapeWithItem(Item outputItem, int count, ResourceKey<ResourcesTypes> resourceType, Item item, Consumer<FinishedRecipe> output){
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("type", types.getOrThrow(resourceType).key().location().toString());

        StrictShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('B', PartialNBTIngredient.of(compoundTag, ModItems.LEAF_FRAGMENT.get()))
                .define('A', item)
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT.get(), types.getOrThrow(resourceType).key().location()), has(ModItems.LEAF_FRAGMENT.get(), compoundTag))
                .unlockedBy(getHasName(item), has(item))
                .save(output, resourceType.location().withSuffix("_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/"));
    }

    private void circleSurroundedShape(Item outputItem, int count, ResourceKey<ResourcesTypes> resourceType, ResourceKey<ResourcesTypes> middleResourceType, Consumer<FinishedRecipe> output){
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("type", types.getOrThrow(resourceType).key().location().toString());

        CompoundTag compoundTag2 = new CompoundTag();
        compoundTag2.putString("type", types.getOrThrow(middleResourceType).key().location().toString());

        StrictShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', PartialNBTIngredient.of(compoundTag, ModItems.LEAF_FRAGMENT.get()))
                .define('B', PartialNBTIngredient.of(compoundTag2, ModItems.LEAF_FRAGMENT.get()))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT.get(), types.getOrThrow(resourceType).key().location()), has(ModItems.LEAF_FRAGMENT.get(), compoundTag))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT.get(), types.getOrThrow(middleResourceType).key().location()), has(ModItems.LEAF_FRAGMENT.get(), compoundTag2))
                .save(output, resourceType.location().withSuffix("_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/"));
    }

    private void twoByTwoShape(Item outputItem, int count, ResourceKey<ResourcesTypes> resourceType, ResourceKey<ResourcesTypes> middleResourceType, Consumer<FinishedRecipe> output){
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("type", types.getOrThrow(resourceType).key().location().toString());

        CompoundTag compoundTag2 = new CompoundTag();
        compoundTag2.putString("type", types.getOrThrow(middleResourceType).key().location().toString());

        StrictShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("BA ")
                .pattern("AB ")
                .pattern("   ")
                .define('A', PartialNBTIngredient.of(compoundTag, ModItems.LEAF_FRAGMENT.get()))
                .define('B', PartialNBTIngredient.of(compoundTag2, ModItems.LEAF_FRAGMENT.get()))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT.get(), types.getOrThrow(resourceType).key().location()), has(ModItems.LEAF_FRAGMENT.get(), compoundTag))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT.get(), types.getOrThrow(middleResourceType).key().location()), has(ModItems.LEAF_FRAGMENT.get(), compoundTag2))
                .save(output, resourceType.location().withSuffix("_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/"));
    }

    private void cubeShape(Item outputItem, int count, ResourceKey<ResourcesTypes> resourceType, Consumer<FinishedRecipe> output){
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("type", types.getOrThrow(resourceType).key().location().toString());

        StrictShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', PartialNBTIngredient.of(compoundTag, ModItems.LEAF_FRAGMENT.get()))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT.get(), types.getOrThrow(resourceType).key().location()), has(ModItems.LEAF_FRAGMENT.get(), compoundTag))
                .save(output, resourceType.location().withSuffix("_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/"));
    }

    private void lineShape(Item outputItem, int count, ResourceKey<ResourcesTypes> resourceType, Consumer<FinishedRecipe> output){
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("type", types.getOrThrow(resourceType).key().location().toString());

        StrictShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("   ")
                .pattern("AAA")
                .pattern("   ")
                .define('A', PartialNBTIngredient.of(compoundTag, ModItems.LEAF_FRAGMENT.get()))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT.get(), types.getOrThrow(resourceType).key().location()), has(ModItems.LEAF_FRAGMENT.get(), compoundTag))
                .save(output, resourceType.location().withSuffix("_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/"));
    }

    private void straightShape(Item outputItem, int count, ResourceKey<ResourcesTypes> resourceType, Consumer<FinishedRecipe> output){
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("type", types.getOrThrow(resourceType).key().location().toString());

        StrictShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern(" A ")
                .pattern(" A ")
                .pattern(" A ")
                .define('A', PartialNBTIngredient.of(compoundTag, ModItems.LEAF_FRAGMENT.get()))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT.get(), types.getOrThrow(resourceType).key().location()), has(ModItems.LEAF_FRAGMENT.get(), compoundTag))
                .save(output, resourceType.location().withSuffix("_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/"));
    }

    private void customShape(Item outputItem, int count, ResourceKey<ResourcesTypes> resourceType, String line1, String line2, String line3, Consumer<FinishedRecipe> output){
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("type", types.getOrThrow(resourceType).key().location().toString());

        StrictShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern(line1)
                .pattern(line2)
                .pattern(line3)
                .define('A', PartialNBTIngredient.of(compoundTag, ModItems.LEAF_FRAGMENT.get()))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT.get(), types.getOrThrow(resourceType).key().location()), has(ModItems.LEAF_FRAGMENT.get(), compoundTag))
                .save(output, resourceType.location().withSuffix("_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/"));
    }

    private void twoItemCustomShape(Item outputItem, int count, ResourceKey<ResourcesTypes> resourceType, ResourceKey<ResourcesTypes> resourceType2, String line1, String line2, String line3, Consumer<FinishedRecipe> output){
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("type", types.getOrThrow(resourceType).key().location().toString());

        CompoundTag compoundTag2 = new CompoundTag();
        compoundTag2.putString("type", types.getOrThrow(resourceType2).key().location().toString());

        StrictShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern(line1)
                .pattern(line2)
                .pattern(line3)
                .define('A', PartialNBTIngredient.of(compoundTag, ModItems.LEAF_FRAGMENT.get()))
                .define('B', PartialNBTIngredient.of(compoundTag2, ModItems.LEAF_FRAGMENT.get()))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT.get(), types.getOrThrow(resourceType).key().location()), has(ModItems.LEAF_FRAGMENT.get(), compoundTag))
                .unlockedBy(getHasName(ModItems.LEAF_FRAGMENT.get(), types.getOrThrow(resourceType2).key().location()), has(ModItems.LEAF_FRAGMENT.get(), compoundTag2))
                .save(output, resourceType.location().withSuffix("_fragment_to_" + getItemName(outputItem)).withPrefix("fragment_crafting/"));
    }

    protected InventoryChangeTrigger.TriggerInstance has(ItemLike itemLike, CompoundTag key) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(itemLike).hasNbt(key).build());
    }

    protected static String getHasName(ItemLike itemLike, ResourceLocation key) {
        return "has_" + key.getPath() + "_" + getItemName(itemLike);
    }
}

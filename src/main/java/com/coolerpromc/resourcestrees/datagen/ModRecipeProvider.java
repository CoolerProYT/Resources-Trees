package com.coolerpromc.resourcestrees.datagen;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.block.entity.custom.TreeSimulatorBlockEntity;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.datagen.recipebuilder.ExtendedShapedRecipeBuilder;
import com.coolerpromc.resourcestrees.datagen.recipebuilder.TreeSimulatorRecipeBuilder;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.recipe.output.TreeSimulatorOutput;
import com.coolerpromc.resourcestrees.registry.ModRegistries;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.impl.recipe.ingredient.builtin.ComponentsIngredient;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.component.ComponentChanges;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.ComponentPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public static final Map<ResourcesSaplingBlock, Item> SAPLINGS_BY_SAPLINGS = Map.of(
            ModBlocks.RESOURCES_OAK_SAPLING, Items.OAK_SAPLING,
            ModBlocks.RESOURCES_SPRUCE_SAPLING, Items.SPRUCE_SAPLING,
            ModBlocks.RESOURCES_BIRCH_SAPLING, Items.BIRCH_SAPLING,
            ModBlocks.RESOURCES_JUNGLE_SAPLING, Items.JUNGLE_SAPLING,
            ModBlocks.RESOURCES_ACACIA_SAPLING, Items.ACACIA_SAPLING,
            ModBlocks.RESOURCES_DARK_OAK_SAPLING, Items.DARK_OAK_SAPLING,
            ModBlocks.RESOURCES_CHERRY_SAPLING, Items.CHERRY_SAPLING
    );
    
    private final RegistryEntryLookup<ResourcesTypes> types;
    private final RegistryWrapper.WrapperLookup wrapperLookup;
    private RecipeExporter output;

    public ModRecipeProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(dataOutput, completableFuture);
        this.types = completableFuture.join().getWrapperOrThrow(ModRegistries.RESOURCES_TYPES_KEY);
        this.wrapperLookup = completableFuture.join();
    }

    @Override
    public void generate(RecipeExporter output) {
        this.output = output;

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
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.TREE_SIMULATOR, 1)
                .pattern("   ")
                .pattern("ABA")
                .pattern("AAA")
                .input('A', Items.CYAN_TERRACOTTA)
                .input('B', Items.GRASS_BLOCK)
                .criterion(hasItem(Items.GRASS_BLOCK), has(Items.GRASS_BLOCK))
                .criterion(hasItem(Items.CYAN_TERRACOTTA), has(Items.CYAN_TERRACOTTA))
                .offerTo(output);

        // Item Recipe
        essenceItem(ModItems.FIRE_ESSENCE, Items.FIRE_CHARGE, Items.MAGMA_BLOCK, Items.LAVA_BUCKET);
        essenceItem(ModItems.NATURE_ESSENCE, Items.GRASS_BLOCK, Items.MOSS_BLOCK, Items.PUMPKIN, Items.MELON, Items.SUGAR_CANE, Items.CACTUS, Items.WHEAT, Items.CARROT, Items.POTATO);
        essenceItem(ModItems.END_ESSENCE, Items.END_STONE, Items.CHORUS_FRUIT, Items.CHORUS_PLANT, Items.PURPUR_BLOCK);
        essenceItem(ModItems.WATER_ESSENCE, Items.WATER_BUCKET, Items.KELP, Items.SEAGRASS, Items.CLAY_BALL);

        // Saplings & Tree Simulator recipes
        ResourcesTypes.getAllResourcesTypes(wrapperLookup).forEach((key, value) -> {
            Field[] fields = ModBlocks.class.getDeclaredFields();

            for (Field field : fields){
                try {
                    Object obj = field.get(null);
                    if (obj instanceof ResourcesSaplingBlock resourcesSaplingBlock){
                        ItemStack sapling = resourcesSaplingBlock.asItem().getDefaultStack();
                        sapling.set(ModDataComponents.TYPE, key);
                        if (value.material().left().isPresent()){
                            ExtendedShapedRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, sapling)
                                    .pattern(" A ")
                                    .pattern("ABA")
                                    .pattern(" A ")
                                    .input('A', Registries.ITEM.get(value.material().left().get()))
                                    .input('B', SAPLINGS_BY_SAPLINGS.get(resourcesSaplingBlock))
                                    .criterion(hasItem(Registries.ITEM.get(value.material().left().get())), has(Registries.ITEM.get(value.material().left().get())))
                                    .criterion(hasItem(SAPLINGS_BY_SAPLINGS.get(resourcesSaplingBlock)), has(SAPLINGS_BY_SAPLINGS.get(resourcesSaplingBlock)))
                                    .offerTo(output, key.withSuffixedPath(Registries.BLOCK.getId(resourcesSaplingBlock).getPath().substring(9)).withPrefixedPath("saplings/"));
                        }
                        else if (value.material().right().isPresent()){
                            ExtendedShapedRecipeBuilder.create(RecipeCategory.BUILDING_BLOCKS, sapling)
                                    .pattern(" A ")
                                    .pattern("ABA")
                                    .pattern(" A ")
                                    .input('A', (value.material().right().get()))
                                    .input('B', SAPLINGS_BY_SAPLINGS.get(resourcesSaplingBlock))
                                    .criterion("has_" + value.material().right().get().id().getPath() + "_tags", has(value.material().right().get()))
                                    .criterion(hasItem(SAPLINGS_BY_SAPLINGS.get(resourcesSaplingBlock)), has(SAPLINGS_BY_SAPLINGS.get(resourcesSaplingBlock)))
                                    .offerTo(output, key.withSuffixedPath(Registries.BLOCK.getId(resourcesSaplingBlock).getPath().substring(9)).withPrefixedPath("saplings/"));
                        }

                        ItemStack leaf = ModItems.LEAF_FRAGMENT.getDefaultStack();
                        leaf.set(ModDataComponents.TYPE, key);

                        TreeSimulatorRecipeBuilder.builder()
                                .setTree(sapling)
                                .addDrops(TreeSimulatorOutput.of(TreeSimulatorBlockEntity.LOG_BY_SAPLINGS.get(resourcesSaplingBlock).getDefaultStack(), 1, 2, 4))
                                .addDrops(TreeSimulatorOutput.of(leaf, 1, 1, 1))
                                .addDrops(TreeSimulatorOutput.of(leaf, value.secondaryDropChance(), 1, 1))
                                .addDrops(TreeSimulatorOutput.of(sapling, value.saplingChance(), 1, 1))
                                .addDrops(TreeSimulatorOutput.of(Items.STICK.getDefaultStack(), 0.1f, 1, 2))
                                .addDrops(TreeSimulatorOutput.of(Items.APPLE.getDefaultStack(), 0.05f, 1, 1))
                                .addDrops(TreeSimulatorOutput.of(SAPLINGS_BY_SAPLINGS.get(resourcesSaplingBlock).getDefaultStack(), 0.1f, 1, 1))
                                .setTicksToGrow(1200)
                                .offerTo(output, key.withSuffixedPath(Registries.BLOCK.getId(resourcesSaplingBlock).getPath().substring(9)).withPrefixedPath("tree_simulator/"));
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void essenceItem(Item ouputItem, Item... inputItems){
        ShapelessRecipeJsonBuilder builder = ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ouputItem, 1);

        for (Item item : inputItems){
            builder.input(item);
            builder.criterion(hasItem(item), has(item));
        }

        builder.offerTo(output);
    }

    private void circleShape(Item outputItem, int count, RegistryKey<ResourcesTypes> resourceType){
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("AAA")
                .pattern("A A")
                .pattern("AAA")
                .input('A', new ComponentsIngredient(Ingredient.ofItems(ModItems.LEAF_FRAGMENT), ComponentChanges.builder().add(ModDataComponents.TYPE, types.getOrThrow(resourceType).registryKey().getValue()).build()).toVanilla())
                .criterion(hasItem(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).registryKey().getValue()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).registryKey().getValue()))
                .offerTo(output, resourceType.getValue().withSuffixedPath("_fragment_to_" + getItemPath(outputItem)).withPrefixedPath("fragment_crafting/"));
    }

    private void circleSurroundedShapeWithItem(Item outputItem, int count, RegistryKey<ResourcesTypes> resourceType, Item item){
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .input('B', new ComponentsIngredient(Ingredient.ofItems(ModItems.LEAF_FRAGMENT), ComponentChanges.builder().add(ModDataComponents.TYPE, types.getOrThrow(resourceType).registryKey().getValue()).build()).toVanilla())
                .input('A', item)
                .criterion(hasItem(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).registryKey().getValue()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).registryKey().getValue()))
                .criterion(hasItem(item), has(item))
                .offerTo(output, resourceType.getValue().withSuffixedPath("_fragment_to_" + getItemPath(outputItem)).withPrefixedPath("fragment_crafting/"));
    }

    private void circleSurroundedShape(Item outputItem, int count, RegistryKey<ResourcesTypes> resourceType, RegistryKey<ResourcesTypes> middleResourceType){
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .input('A', new ComponentsIngredient(Ingredient.ofItems(ModItems.LEAF_FRAGMENT), ComponentChanges.builder().add(ModDataComponents.TYPE, types.getOrThrow(resourceType).registryKey().getValue()).build()).toVanilla())
                .input('B', new ComponentsIngredient(Ingredient.ofItems(ModItems.LEAF_FRAGMENT), ComponentChanges.builder().add(ModDataComponents.TYPE, types.getOrThrow(middleResourceType).registryKey().getValue()).build()).toVanilla())
                .criterion(hasItem(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).registryKey().getValue()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).registryKey().getValue()))
                .criterion(hasItem(ModItems.LEAF_FRAGMENT, types.getOrThrow(middleResourceType).registryKey().getValue()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(middleResourceType).registryKey().getValue()))
                .offerTo(output, resourceType.getValue().withSuffixedPath("_fragment_to_" + getItemPath(outputItem)).withPrefixedPath("fragment_crafting/"));
    }

    private void twoByTwoShape(Item outputItem, int count, RegistryKey<ResourcesTypes> resourceType, RegistryKey<ResourcesTypes> middleResourceType){
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("BA ")
                .pattern("AB ")
                .pattern("   ")
                .input('A', new ComponentsIngredient(Ingredient.ofItems(ModItems.LEAF_FRAGMENT), ComponentChanges.builder().add(ModDataComponents.TYPE, types.getOrThrow(resourceType).registryKey().getValue()).build()).toVanilla())
                .input('B', new ComponentsIngredient(Ingredient.ofItems(ModItems.LEAF_FRAGMENT), ComponentChanges.builder().add(ModDataComponents.TYPE, types.getOrThrow(middleResourceType).registryKey().getValue()).build()).toVanilla())
                .criterion(hasItem(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).registryKey().getValue()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).registryKey().getValue()))
                .criterion(hasItem(ModItems.LEAF_FRAGMENT, types.getOrThrow(middleResourceType).registryKey().getValue()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(middleResourceType).registryKey().getValue()))
                .offerTo(output, resourceType.getValue().withSuffixedPath("_fragment_to_" + getItemPath(outputItem)).withPrefixedPath("fragment_crafting/"));
    }

    private void cubeShape(Item outputItem, int count, RegistryKey<ResourcesTypes> resourceType){
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .input('A', new ComponentsIngredient(Ingredient.ofItems(ModItems.LEAF_FRAGMENT), ComponentChanges.builder().add(ModDataComponents.TYPE, types.getOrThrow(resourceType).registryKey().getValue()).build()).toVanilla())
                .criterion(hasItem(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).registryKey().getValue()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).registryKey().getValue()))
                .offerTo(output, resourceType.getValue().withSuffixedPath("_fragment_to_" + getItemPath(outputItem)).withPrefixedPath("fragment_crafting/"));
    }

    private void lineShape(Item outputItem, int count, RegistryKey<ResourcesTypes> resourceType){
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern("   ")
                .pattern("AAA")
                .pattern("   ")
                .input('A', new ComponentsIngredient(Ingredient.ofItems(ModItems.LEAF_FRAGMENT), ComponentChanges.builder().add(ModDataComponents.TYPE, types.getOrThrow(resourceType).registryKey().getValue()).build()).toVanilla())
                .criterion(hasItem(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).registryKey().getValue()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).registryKey().getValue()))
                .offerTo(output, resourceType.getValue().withSuffixedPath("_fragment_to_" + getItemPath(outputItem)).withPrefixedPath("fragment_crafting/"));
    }

    private void customShape(Item outputItem, int count, RegistryKey<ResourcesTypes> resourceType, String line1, String line2, String line3){
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern(line1)
                .pattern(line2)
                .pattern(line3)
                .input('A', new ComponentsIngredient(Ingredient.ofItems(ModItems.LEAF_FRAGMENT), ComponentChanges.builder().add(ModDataComponents.TYPE, types.getOrThrow(resourceType).registryKey().getValue()).build()).toVanilla())
                .criterion(hasItem(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).registryKey().getValue()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).registryKey().getValue()))
                .offerTo(output, resourceType.getValue().withSuffixedPath("_fragment_to_" + getItemPath(outputItem)).withPrefixedPath("fragment_crafting/"));
    }

    private void twoItemCustomShape(Item outputItem, int count, RegistryKey<ResourcesTypes> resourceType, RegistryKey<ResourcesTypes> resourceType2, String line1, String line2, String line3){
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, outputItem, count)
                .pattern(line1)
                .pattern(line2)
                .pattern(line3)
                .input('A', new ComponentsIngredient(Ingredient.ofItems(ModItems.LEAF_FRAGMENT), ComponentChanges.builder().add(ModDataComponents.TYPE, types.getOrThrow(resourceType).registryKey().getValue()).build()).toVanilla())
                .input('B', new ComponentsIngredient(Ingredient.ofItems(ModItems.LEAF_FRAGMENT), ComponentChanges.builder().add(ModDataComponents.TYPE, types.getOrThrow(resourceType2).registryKey().getValue()).build()).toVanilla())
                .criterion(hasItem(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).registryKey().getValue()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType).registryKey().getValue()))
                .criterion(hasItem(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType2).registryKey().getValue()), has(ModItems.LEAF_FRAGMENT, types.getOrThrow(resourceType2).registryKey().getValue()))
                .offerTo(output, resourceType.getValue().withSuffixedPath("_fragment_to_" + getItemPath(outputItem)).withPrefixedPath("fragment_crafting/"));
    }

    private AdvancementCriterion<?> has(ItemConvertible item) {
        return inventoryTrigger(ItemPredicate.Builder.create().items(item));
    }

    private AdvancementCriterion<?> has(TagKey<Item> item) {
        return inventoryTrigger(ItemPredicate.Builder.create().tag(item));
    }

    protected AdvancementCriterion<?> has(ItemConvertible itemLike, Identifier key) {
        return inventoryTrigger(ItemPredicate.Builder.create().items(itemLike).component(ComponentPredicate.builder().add(ModDataComponents.TYPE, key).build()).build());
    }

    private AdvancementCriterion<?> inventoryTrigger(ItemPredicate.Builder... pItems) {
        return inventoryTrigger(Arrays.stream(pItems).map(ItemPredicate.Builder::build).toArray(ItemPredicate[]::new));
    }

    private AdvancementCriterion<?> inventoryTrigger(ItemPredicate... pPredicates) {
        return Criteria.INVENTORY_CHANGED
                .create(new InventoryChangedCriterion.Conditions(Optional.empty(), InventoryChangedCriterion.Conditions.Slots.ANY, List.of(pPredicates)));
    }

    protected static String hasItem(ItemConvertible itemLike, Identifier key) {
        return "has_" + key.getPath() + "_" + getItemPath(itemLike);
    }

    @Override
    public String getName() {
        return "Resources Trees Recipes";
    }
}

package com.coolerpromc.resourcestrees.api.internal;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.api.IResourcesTreesPlugin;
import com.coolerpromc.resourcestrees.api.ResourcesTreesPlugin;
import com.coolerpromc.resourcestrees.api.resources.IResourcesTypeRegistry;
import com.coolerpromc.resourcestrees.api.resources.ResourcesType;
import com.coolerpromc.resourcestrees.api.tree.ITreeTypeRegistry;
import com.coolerpromc.resourcestrees.api.tree.TreeType;
import com.coolerpromc.resourcestrees.item.ModItems;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

@ResourcesTreesPlugin
public final class InternalResourcesTreesPlugin implements IResourcesTreesPlugin {
    @Override
    public void registerResourcesType(IResourcesTypeRegistry registry) {
        registry.register(new ResourcesType.Builder("stone", Items.COBBLESTONE, 0xFF4D4B49).treeSimulatorTicks(800));
        registry.register(new ResourcesType.Builder("coal", Items.COAL_BLOCK, 0xFF000000).treeSimulatorTicks(1000));
        registry.register(new ResourcesType.Builder("iron", Items.IRON_BLOCK, 0xFFB0BEC5));
        registry.register(new ResourcesType.Builder("copper", Items.COPPER_BLOCK.weathering().unaffected(), 0xFFD46D44));
        registry.register(new ResourcesType.Builder("gold", Items.GOLD_BLOCK, 0xFFFFD600));
        registry.register(new ResourcesType.Builder("lapis", Items.LAPIS_BLOCK, 0xFF3F51B5));
        registry.register(new ResourcesType.Builder("emerald", Items.EMERALD_BLOCK, 0xFF00C853).treeSimulatorTicks(1400));
        registry.register(new ResourcesType.Builder("diamond", Items.DIAMOND_BLOCK, 0xFF40C4FF).saplingDropChance(0.1f).leafDropChance(0.2f).treeSimulatorTicks(1600));
        registry.register(new ResourcesType.Builder("obsidian", Items.OBSIDIAN, 0xFF2E1A47));
        registry.register(new ResourcesType.Builder("amethyst", Items.AMETHYST_BLOCK, 0xFF9C27B0).treeSimulatorTicks(800));
        registry.register(new ResourcesType.Builder("netherite", Items.NETHERITE_BLOCK, 0xFF3E3E3E).saplingDropChance(0.075f).leafDropChance(0.15f).treeSimulatorTicks(1800));
        registry.register(new ResourcesType.Builder("wood", ItemTags.LOGS, 0xFF8D6E63).treeSimulatorTicks(800));
        registry.register(new ResourcesType.Builder("quartz", Items.QUARTZ_BLOCK, 0xFFF5F5F5).treeSimulatorTicks(800));
        registry.register(new ResourcesType.Builder("prismarine", Items.PRISMARINE, 0xFF5EC8C8).treeSimulatorTicks(800));
        registry.register(new ResourcesType.Builder("glowstone", Items.GLOWSTONE, 0xFFFFF176).treeSimulatorTicks(800));
        registry.register(new ResourcesType.Builder("redstone", Items.REDSTONE_BLOCK, 0xFFFF1744).treeSimulatorTicks(1000));
        registry.register(new ResourcesType.Builder("deepslate", Items.DEEPSLATE, 0xFF2B2B24).treeSimulatorTicks(800));
        registry.register(new ResourcesType.Builder("dirt", Items.DIRT, 0xFF9B7653).treeSimulatorTicks(800));
        registry.register(new ResourcesType.Builder("fire", ModItems.FIRE_ESSENCE, 0xFFE45323).treeSimulatorTicks(800));
        registry.register(new ResourcesType.Builder("nether", Items.NETHERRACK, 0xFF511515).treeSimulatorTicks(800));
        registry.register(new ResourcesType.Builder("end", ModItems.END_ESSENCE, 0xFFC5BE8B).treeSimulatorTicks(800));
        registry.register(new ResourcesType.Builder("nature", ModItems.NATURE_ESSENCE, 0xFF1a6e08).treeSimulatorTicks(800));
        registry.register(new ResourcesType.Builder("water", ModItems.WATER_ESSENCE, 0xFF1787D4).treeSimulatorTicks(800));
        registry.register(new ResourcesType.Builder("ice", Items.ICE, 0xFFb9e8ea).treeSimulatorTicks(800));
        registry.register(new ResourcesType.Builder("bee", ModItems.BEE_ESSENCE, 0xFFEDC343));
        registry.register(new ResourcesType.Builder("slime", Items.SLIME_BLOCK, 0xFF6aa84f));
        registry.register(new ResourcesType.Builder("sculk", ModItems.SCULK_ESSENCE, 0xFF041820).treeSimulatorTicks(1400));
        registry.register(new ResourcesType.Builder("skeleton", ModItems.SKELETON_ESSENCE, 0xFFeeeeee));
        registry.register(new ResourcesType.Builder("spider", ModItems.SPIDER_ESSENCE, 0xFF1a0c20));
        registry.register(new ResourcesType.Builder("chicken", ModItems.CHICKEN_ESSENCE, 0xFFA1A1A1));
        registry.register(new ResourcesType.Builder("cow", ModItems.COW_ESSENCE, 0xFF543936));
        registry.register(new ResourcesType.Builder("rabbit", ModItems.RABBIT_ESSENCE, 0xFF8B5A2B));
        registry.register(new ResourcesType.Builder("squid", ModItems.SQUID_ESSENCE, 0xFF223B4D));
        registry.register(new ResourcesType.Builder("turtle", ModItems.TURTLE_ESSENCE, 0xFF315410));
        registry.register(new ResourcesType.Builder("blaze", ModItems.BLAZE_ESSENCE, 0xFFd4ae37));
        registry.register(new ResourcesType.Builder("breeze", ModItems.BREEZE_ESSENCE, 0xFFd5d6ff));
        registry.register(new ResourcesType.Builder("nether_star", Items.NETHER_STAR, 0xFFD8E0D4).treeSimulatorTicks(2000));
        registry.register(new ResourcesType.Builder("ender_pearl", Items.ENDER_PEARL, 0xFF032620).treeSimulatorTicks(1400));
        registry.register(new ResourcesType.Builder("shulker", Items.SHULKER_SHELL, 0xFFcfc2d6).treeSimulatorTicks(1400));
        registry.register(new ResourcesType.Builder("dye", ModItems.DYE_ESSENCE, 0xFF72d4b3).treeSimulatorTicks(800));
        registry.register(new ResourcesType.Builder("gunpowder", Items.GUNPOWDER, 0xFF414257));
        registry.register(new ResourcesType.Builder("ghast", ModItems.GHAST_ESSENCE, 0xFFF9F9F9));
        registry.register(new ResourcesType.Builder("pig", ModItems.PIG_ESSENCE, 0xFFF9A195));
        registry.register(new ResourcesType.Builder("sheep", ModItems.SHEEP_ESSENCE, 0xFFFFFFFF));
        registry.register(new ResourcesType.Builder("fish", ModItems.FISH_ESSENCE, 0xFFC1A76A));
        registry.register(new ResourcesType.Builder("zombie", ModItems.ZOMBIE_ESSENCE, 0xFF3e692d));
    }

    @Override
    public void registerTreeType(ITreeTypeRegistry registry) {
        String[] trees = {"oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "cherry", "pale_oak"};
        for (String tree : trees) {
            registry.register(new TreeType(tree, tree, Constants.id("block/resources_" + tree + "_sapling"), Constants.id("block/resources_" + tree + "_leaves"), "minecraft:" + tree + "_sapling", "minecraft:" + tree + "_leaves", "minecraft:" + tree + "_log"));
        }
    }
}

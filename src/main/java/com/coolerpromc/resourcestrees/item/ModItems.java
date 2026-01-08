package com.coolerpromc.resourcestrees.item;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.item.custom.LeafFragmentItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Function;

public class ModItems {
    public static final LeafFragmentItem LEAF_FRAGMENT = registerItem("leaf_fragment", LeafFragmentItem::new);
    public static final Item FIRE_ESSENCE = registerItem("fire_essence", Item::new);
    public static final Item WATER_ESSENCE = registerItem("water_essence", Item::new);
    public static final Item NATURE_ESSENCE = registerItem("nature_essence", Item::new);
    public static final Item END_ESSENCE = registerItem("end_essence", Item::new);
    public static final Item BEE_ESSENCE = registerItem("bee_essence", Item::new);
    public static final Item SCULK_ESSENCE = registerItem("sculk_essence", Item::new);
    public static final Item SKELETON_ESSENCE = registerItem("skeleton_essence", Item::new);
    public static final Item SPIDER_ESSENCE = registerItem("spider_essence", Item::new);
    public static final Item CHICKEN_ESSENCE = registerItem("chicken_essence", Item::new);
    public static final Item COW_ESSENCE = registerItem("cow_essence", Item::new);
    public static final Item RABBIT_ESSENCE = registerItem("rabbit_essence", Item::new);
    public static final Item SQUID_ESSENCE = registerItem("squid_essence", Item::new);
    public static final Item TURTLE_ESSENCE = registerItem("turtle_essence", Item::new);
    public static final Item BLAZE_ESSENCE = registerItem("blaze_essence", Item::new);
    public static final Item BREEZE_ESSENCE = registerItem("breeze_essence", Item::new);
    public static final Item DYE_ESSENCE = registerItem("dye_essence", Item::new);
    public static final Item GHAST_ESSENCE = registerItem("ghast_essence", Item::new);
    public static final Item PIG_ESSENCE = registerItem("pig_essence", Item::new);
    public static final Item SHEEP_ESSENCE = registerItem("sheep_essence", Item::new);
    public static final Item FISH_ESSENCE = registerItem("fish_essence", Item::new);
    public static final Item ZOMBIE_ESSENCE = registerItem("zombie_essence", Item::new);

    public static <T extends Item> T registerItem(String name, Function<Item.Settings, ? extends T> func){
        return Registry.register(Registries.ITEM, ResourcesTrees.id(name), func.apply(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, ResourcesTrees.id(name)))));
    }

    public static void register(){
        ResourcesTrees.LOGGER.info("Registering items.");
    }
}
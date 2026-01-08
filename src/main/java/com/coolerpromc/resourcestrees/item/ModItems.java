package com.coolerpromc.resourcestrees.item;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.item.custom.EssenceItem;
import com.coolerpromc.resourcestrees.item.custom.LeafFragmentItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.Function;

public class ModItems {
    public static final LeafFragmentItem LEAF_FRAGMENT = registerItem("leaf_fragment", LeafFragmentItem::new);

    public static final Item FIRE_ESSENCE = registerItem("fire_essence", properties -> new EssenceItem(properties, 0xFFE45323));
    public static final Item WATER_ESSENCE = registerItem("water_essence", properties -> new EssenceItem(properties, 0xFF1787D4));
    public static final Item NATURE_ESSENCE = registerItem("nature_essence", properties -> new EssenceItem(properties, 0xFF1a6e08));
    public static final Item END_ESSENCE = registerItem("end_essence", properties -> new EssenceItem(properties, 0xFFC5BE8B));
    public static final Item BEE_ESSENCE = registerItem("bee_essence", properties -> new EssenceItem(properties, 0xFFEDC343));
    public static final Item SCULK_ESSENCE = registerItem("sculk_essence", properties -> new EssenceItem(properties, 0xFF041820));
    public static final Item SKELETON_ESSENCE = registerItem("skeleton_essence", properties -> new EssenceItem(properties, 0xFFeeeeee));
    public static final Item SPIDER_ESSENCE = registerItem("spider_essence", properties -> new EssenceItem(properties, 0xFF1a0c20));
    public static final Item CHICKEN_ESSENCE = registerItem("chicken_essence", properties -> new EssenceItem(properties, 0xFFA1A1A1));
    public static final Item COW_ESSENCE = registerItem("cow_essence", properties -> new EssenceItem(properties, 0xFF543936));
    public static final Item RABBIT_ESSENCE = registerItem("rabbit_essence", properties -> new EssenceItem(properties, 0xFF8B5A2B));
    public static final Item SQUID_ESSENCE = registerItem("squid_essence", properties -> new EssenceItem(properties, 0xFF223B4D));
    public static final Item TURTLE_ESSENCE = registerItem("turtle_essence", properties -> new EssenceItem(properties, 0xFF315410));
    public static final Item BLAZE_ESSENCE = registerItem("blaze_essence", properties -> new EssenceItem(properties, 0xFFd4ae37));
    public static final Item BREEZE_ESSENCE = registerItem("breeze_essence", properties -> new EssenceItem(properties, 0xFFd5d6ff));
    public static final Item DYE_ESSENCE = registerItem("dye_essence", properties -> new EssenceItem(properties, 0xFF72d4b3));
    public static final Item GHAST_ESSENCE = registerItem("ghast_essence", properties -> new EssenceItem(properties, 0xFFF9F9F9));
    public static final Item PIG_ESSENCE = registerItem("pig_essence", properties -> new EssenceItem(properties, 0xFFF9A195));
    public static final Item SHEEP_ESSENCE = registerItem("sheep_essence", properties -> new EssenceItem(properties, 0xFFFFFFFF));
    public static final Item FISH_ESSENCE = registerItem("fish_essence", properties -> new EssenceItem(properties, 0xFFC1A76A));
    public static final Item ZOMBIE_ESSENCE = registerItem("zombie_essence", properties -> new EssenceItem(properties, 0xFF3e692d));

    public static <T extends Item> T registerItem(String name, Function<Item.Settings, ? extends T> func){
        return Registry.register(Registries.ITEM, ResourcesTrees.id(name), func.apply(new Item.Settings()));
    }

    public static void register(){
        ResourcesTrees.LOGGER.info("Registering items.");
    }
}
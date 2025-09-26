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

    public static <T extends Item> T registerItem(String name, Function<Item.Settings, ? extends T> func){
        return Registry.register(Registries.ITEM, ResourcesTrees.id(name), func.apply(new Item.Settings()));
    }

    public static void register(){
        ResourcesTrees.LOGGER.info("Registering items.");
    }
}
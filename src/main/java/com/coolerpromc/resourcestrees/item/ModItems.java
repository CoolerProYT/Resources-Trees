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

    public static <T extends Item> T registerItem(String name, Function<Item.Settings, ? extends T> func){
        return Registry.register(Registries.ITEM, ResourcesTrees.id(name), func.apply(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, ResourcesTrees.id(name)))));
    }

    public static void register(){
        ResourcesTrees.LOGGER.info("Registering items.");
    }
}
package com.coolerpromc.resourcestrees.item;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.item.custom.LeafFragmentItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, ResourcesTrees.MODID);

    public static final RegistryObject<LeafFragmentItem> LEAF_FRAGMENT = register("leaf_fragment", LeafFragmentItem::new);
    public static final RegistryObject<Item> FIRE_ESSENCE = register("fire_essence", Item::new);
    public static final RegistryObject<Item> WATER_ESSENCE = register("water_essence", Item::new);
    public static final RegistryObject<Item> NATURE_ESSENCE = register("nature_essence", Item::new);
    public static final RegistryObject<Item> END_ESSENCE = register("end_essence", Item::new);

    public static <T extends Item> RegistryObject<T> register(String name, Function<Item.Properties, ? extends T> func){
        return ITEMS.register(name, () -> func.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, ResourcesTrees.id(name)))));
    }

    public static void register(BusGroup eventBus){
        ITEMS.register(eventBus);
    }
}
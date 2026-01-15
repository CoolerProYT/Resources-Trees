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
    public static final RegistryObject<Item> BEE_ESSENCE = register("bee_essence", Item::new);
    public static final RegistryObject<Item> SCULK_ESSENCE = register("sculk_essence", Item::new);
    public static final RegistryObject<Item> SKELETON_ESSENCE = register("skeleton_essence", Item::new);
    public static final RegistryObject<Item> SPIDER_ESSENCE = register("spider_essence", Item::new);
    public static final RegistryObject<Item> CHICKEN_ESSENCE = register("chicken_essence", Item::new);
    public static final RegistryObject<Item> COW_ESSENCE = register("cow_essence", Item::new);
    public static final RegistryObject<Item> RABBIT_ESSENCE = register("rabbit_essence", Item::new);
    public static final RegistryObject<Item> SQUID_ESSENCE = register("squid_essence", Item::new);
    public static final RegistryObject<Item> TURTLE_ESSENCE = register("turtle_essence", Item::new);
    public static final RegistryObject<Item> BLAZE_ESSENCE = register("blaze_essence", Item::new);
    public static final RegistryObject<Item> BREEZE_ESSENCE = register("breeze_essence", Item::new);
    public static final RegistryObject<Item> DYE_ESSENCE = register("dye_essence", Item::new);
    public static final RegistryObject<Item> GHAST_ESSENCE = register("ghast_essence", Item::new);
    public static final RegistryObject<Item> PIG_ESSENCE = register("pig_essence", Item::new);
    public static final RegistryObject<Item> SHEEP_ESSENCE = register("sheep_essence", Item::new);
    public static final RegistryObject<Item> FISH_ESSENCE = register("fish_essence", Item::new);
    public static final RegistryObject<Item> ZOMBIE_ESSENCE = register("zombie_essence", Item::new);

    public static <T extends Item> RegistryObject<T> register(String name, Function<Item.Properties, ? extends T> func){
        return ITEMS.register(name, () -> func.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, ResourcesTrees.id(name)))));
    }

    public static void register(BusGroup eventBus){
        ITEMS.register(eventBus);
    }
}
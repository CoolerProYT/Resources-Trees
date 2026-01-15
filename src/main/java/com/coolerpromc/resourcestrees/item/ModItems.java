package com.coolerpromc.resourcestrees.item;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.item.custom.EssenceItem;
import com.coolerpromc.resourcestrees.item.custom.LeafFragmentItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, ResourcesTrees.MODID);

    public static final RegistryObject<LeafFragmentItem> LEAF_FRAGMENT = ITEMS.register("leaf_fragment", () -> new LeafFragmentItem(new Item.Properties()));
    public static final RegistryObject<Item> FIRE_ESSENCE = ITEMS.register("fire_essence", () -> new EssenceItem(new Item.Properties(), 0xFFE45323));
    public static final RegistryObject<Item> WATER_ESSENCE = ITEMS.register("water_essence", () -> new EssenceItem(new Item.Properties(), 0xFF1787D4));
    public static final RegistryObject<Item> NATURE_ESSENCE = ITEMS.register("nature_essence", () -> new EssenceItem(new Item.Properties(), 0xFF1a6e08));
    public static final RegistryObject<Item> END_ESSENCE = ITEMS.register("end_essence", () -> new EssenceItem(new Item.Properties(), 0xFFC5BE8B));
    public static final RegistryObject<Item> BEE_ESSENCE = ITEMS.register("bee_essence", () -> new EssenceItem(new Item.Properties(), 0xFFEDC343));
    public static final RegistryObject<Item> SCULK_ESSENCE = ITEMS.register("sculk_essence", () -> new EssenceItem(new Item.Properties(), 0xFF041820));
    public static final RegistryObject<Item> SKELETON_ESSENCE = ITEMS.register("skeleton_essence", () -> new EssenceItem(new Item.Properties(), 0xFFeeeeee));
    public static final RegistryObject<Item> SPIDER_ESSENCE = ITEMS.register("spider_essence", () -> new EssenceItem(new Item.Properties(), 0xFF1a0c20));
    public static final RegistryObject<Item> CHICKEN_ESSENCE = ITEMS.register("chicken_essence", () -> new EssenceItem(new Item.Properties(), 0xFFA1A1A1));
    public static final RegistryObject<Item> COW_ESSENCE = ITEMS.register("cow_essence", () -> new EssenceItem(new Item.Properties(), 0xFF543936));
    public static final RegistryObject<Item> RABBIT_ESSENCE = ITEMS.register("rabbit_essence", () -> new EssenceItem(new Item.Properties(), 0xFF8B5A2B));
    public static final RegistryObject<Item> SQUID_ESSENCE = ITEMS.register("squid_essence", () -> new EssenceItem(new Item.Properties(), 0xFF223B4D));
    public static final RegistryObject<Item> TURTLE_ESSENCE = ITEMS.register("turtle_essence", () -> new EssenceItem(new Item.Properties(), 0xFF315410));
    public static final RegistryObject<Item> BLAZE_ESSENCE = ITEMS.register("blaze_essence", () -> new EssenceItem(new Item.Properties(), 0xFFd4ae37));
    public static final RegistryObject<Item> DYE_ESSENCE = ITEMS.register("dye_essence", () -> new EssenceItem(new Item.Properties(), 0xFF72d4b3));
    public static final RegistryObject<Item> GHAST_ESSENCE = ITEMS.register("ghast_essence", () -> new EssenceItem(new Item.Properties(), 0xFFF9F9F9));
    public static final RegistryObject<Item> PIG_ESSENCE = ITEMS.register("pig_essence", () -> new EssenceItem(new Item.Properties(), 0xFFF9A195));
    public static final RegistryObject<Item> SHEEP_ESSENCE = ITEMS.register("sheep_essence", () -> new EssenceItem(new Item.Properties(), 0xFFFFFFFF));
    public static final RegistryObject<Item> FISH_ESSENCE = ITEMS.register("fish_essence", () -> new EssenceItem(new Item.Properties(), 0xFFC1A76A));
    public static final RegistryObject<Item> ZOMBIE_ESSENCE = ITEMS.register("zombie_essence", () -> new EssenceItem(new Item.Properties(), 0xFF3e692d));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
package com.coolerpromc.resourcestrees.item;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.item.custom.EssenceItem;
import com.coolerpromc.resourcestrees.item.custom.LeafFragmentItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ResourcesTrees.MODID);

    public static final DeferredItem<LeafFragmentItem> LEAF_FRAGMENT = ITEMS.registerItem("leaf_fragment", LeafFragmentItem::new);
    public static final DeferredItem<Item> FIRE_ESSENCE = ITEMS.registerItem("fire_essence", properties -> new EssenceItem(properties, 0xFFE45323));
    public static final DeferredItem<Item> WATER_ESSENCE = ITEMS.registerItem("water_essence", properties -> new EssenceItem(properties, 0xFF1787D4));
    public static final DeferredItem<Item> NATURE_ESSENCE = ITEMS.registerItem("nature_essence", properties -> new EssenceItem(properties, 0xFF1a6e08));
    public static final DeferredItem<Item> END_ESSENCE = ITEMS.registerItem("end_essence", properties -> new EssenceItem(properties, 0xFFC5BE8B));
    public static final DeferredItem<Item> BEE_ESSENCE = ITEMS.registerItem("bee_essence", properties -> new EssenceItem(properties, 0xFFEDC343));
    public static final DeferredItem<Item> SCULK_ESSENCE = ITEMS.registerItem("sculk_essence", properties -> new EssenceItem(properties, 0xFF041820));
    public static final DeferredItem<Item> SKELETON_ESSENCE = ITEMS.registerItem("skeleton_essence", properties -> new EssenceItem(properties, 0xFFeeeeee));
    public static final DeferredItem<Item> SPIDER_ESSENCE = ITEMS.registerItem("spider_essence", properties -> new EssenceItem(properties, 0xFF1a0c20));
    public static final DeferredItem<Item> CHICKEN_ESSENCE = ITEMS.registerItem("chicken_essence", properties -> new EssenceItem(properties, 0xFFA1A1A1));
    public static final DeferredItem<Item> COW_ESSENCE = ITEMS.registerItem("cow_essence", properties -> new EssenceItem(properties, 0xFF543936));
    public static final DeferredItem<Item> RABBIT_ESSENCE = ITEMS.registerItem("rabbit_essence", properties -> new EssenceItem(properties, 0xFF8B5A2B));
    public static final DeferredItem<Item> SQUID_ESSENCE = ITEMS.registerItem("squid_essence", properties -> new EssenceItem(properties, 0xFF223B4D));
    public static final DeferredItem<Item> TURTLE_ESSENCE = ITEMS.registerItem("turtle_essence", properties -> new EssenceItem(properties, 0xFF315410));
    public static final DeferredItem<Item> BLAZE_ESSENCE = ITEMS.registerItem("blaze_essence", properties -> new EssenceItem(properties, 0xFFd4ae37));
    public static final DeferredItem<Item> BREEZE_ESSENCE = ITEMS.registerItem("breeze_essence", properties -> new EssenceItem(properties, 0xFFd5d6ff));
    public static final DeferredItem<Item> DYE_ESSENCE = ITEMS.registerItem("dye_essence", properties -> new EssenceItem(properties, 0xFF72d4b3));
    public static final DeferredItem<Item> GHAST_ESSENCE = ITEMS.registerItem("ghast_essence", properties -> new EssenceItem(properties, 0xFFF9F9F9));
    public static final DeferredItem<Item> PIG_ESSENCE = ITEMS.registerItem("pig_essence", properties -> new EssenceItem(properties, 0xFFF9A195));
    public static final DeferredItem<Item> SHEEP_ESSENCE = ITEMS.registerItem("sheep_essence", properties -> new EssenceItem(properties, 0xFFFFFFFF));
    public static final DeferredItem<Item> FISH_ESSENCE = ITEMS.registerItem("fish_essence", properties -> new EssenceItem(properties, 0xFFC1A76A));
    public static final DeferredItem<Item> ZOMBIE_ESSENCE = ITEMS.registerItem("zombie_essence", properties -> new EssenceItem(properties, 0xFF3e692d));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
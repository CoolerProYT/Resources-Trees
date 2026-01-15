package com.coolerpromc.resourcestrees.item;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.item.custom.LeafFragmentItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ResourcesTrees.MODID);

    public static final DeferredItem<LeafFragmentItem> LEAF_FRAGMENT = ITEMS.registerItem("leaf_fragment", LeafFragmentItem::new);
    public static final DeferredItem<Item> FIRE_ESSENCE = ITEMS.registerItem("fire_essence", Item::new);
    public static final DeferredItem<Item> WATER_ESSENCE = ITEMS.registerItem("water_essence", Item::new);
    public static final DeferredItem<Item> NATURE_ESSENCE = ITEMS.registerItem("nature_essence", Item::new);
    public static final DeferredItem<Item> END_ESSENCE = ITEMS.registerItem("end_essence", Item::new);
    public static final DeferredItem<Item> BEE_ESSENCE = ITEMS.registerItem("bee_essence", Item::new);
    public static final DeferredItem<Item> SCULK_ESSENCE = ITEMS.registerItem("sculk_essence", Item::new);
    public static final DeferredItem<Item> SKELETON_ESSENCE = ITEMS.registerItem("skeleton_essence", Item::new);
    public static final DeferredItem<Item> SPIDER_ESSENCE = ITEMS.registerItem("spider_essence", Item::new);
    public static final DeferredItem<Item> CHICKEN_ESSENCE = ITEMS.registerItem("chicken_essence", Item::new);
    public static final DeferredItem<Item> COW_ESSENCE = ITEMS.registerItem("cow_essence", Item::new);
    public static final DeferredItem<Item> RABBIT_ESSENCE = ITEMS.registerItem("rabbit_essence", Item::new);
    public static final DeferredItem<Item> SQUID_ESSENCE = ITEMS.registerItem("squid_essence", Item::new);
    public static final DeferredItem<Item> TURTLE_ESSENCE = ITEMS.registerItem("turtle_essence", Item::new);
    public static final DeferredItem<Item> BLAZE_ESSENCE = ITEMS.registerItem("blaze_essence", Item::new);
    public static final DeferredItem<Item> BREEZE_ESSENCE = ITEMS.registerItem("breeze_essence", Item::new);
    public static final DeferredItem<Item> DYE_ESSENCE = ITEMS.registerItem("dye_essence", Item::new);
    public static final DeferredItem<Item> GHAST_ESSENCE = ITEMS.registerItem("ghast_essence", Item::new);
    public static final DeferredItem<Item> PIG_ESSENCE = ITEMS.registerItem("pig_essence", Item::new);
    public static final DeferredItem<Item> SHEEP_ESSENCE = ITEMS.registerItem("sheep_essence", Item::new);
    public static final DeferredItem<Item> FISH_ESSENCE = ITEMS.registerItem("fish_essence", Item::new);
    public static final DeferredItem<Item> ZOMBIE_ESSENCE = ITEMS.registerItem("zombie_essence", Item::new);

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
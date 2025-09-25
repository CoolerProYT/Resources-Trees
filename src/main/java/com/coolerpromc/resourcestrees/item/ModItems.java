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

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
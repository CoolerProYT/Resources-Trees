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

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
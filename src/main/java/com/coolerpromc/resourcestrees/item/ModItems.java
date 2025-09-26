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

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
package com.coolerpromc.resourcestrees.item;

import com.coolerpromc.resourcestrees.api.resources.ResourcesTypes;
import com.coolerpromc.resourcestrees.item.custom.LeafFragmentItem;
import com.coolerpromc.resourcestrees.platform.Services;
import com.coolerpromc.resourcestrees.platform.util.RegistryHandler;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ModItems {
    public static final List<RegistryHandler<? extends Item>> LEAF_FRAGMENTS = new ArrayList<>();

    public static final RegistryHandler<Item> FIRE_ESSENCE = Services.REGISTRY.registerItem("fire_essence", Item::new);
    public static final RegistryHandler<Item> WATER_ESSENCE = Services.REGISTRY.registerItem("water_essence", Item::new);
    public static final RegistryHandler<Item> NATURE_ESSENCE = Services.REGISTRY.registerItem("nature_essence", Item::new);
    public static final RegistryHandler<Item> END_ESSENCE = Services.REGISTRY.registerItem("end_essence", Item::new);
    public static final RegistryHandler<Item> BEE_ESSENCE = Services.REGISTRY.registerItem("bee_essence", Item::new);
    public static final RegistryHandler<Item> SCULK_ESSENCE = Services.REGISTRY.registerItem("sculk_essence", Item::new);
    public static final RegistryHandler<Item> SKELETON_ESSENCE = Services.REGISTRY.registerItem("skeleton_essence", Item::new);
    public static final RegistryHandler<Item> SPIDER_ESSENCE = Services.REGISTRY.registerItem("spider_essence", Item::new);
    public static final RegistryHandler<Item> CHICKEN_ESSENCE = Services.REGISTRY.registerItem("chicken_essence", Item::new);
    public static final RegistryHandler<Item> COW_ESSENCE = Services.REGISTRY.registerItem("cow_essence", Item::new);
    public static final RegistryHandler<Item> RABBIT_ESSENCE = Services.REGISTRY.registerItem("rabbit_essence", Item::new);
    public static final RegistryHandler<Item> SQUID_ESSENCE = Services.REGISTRY.registerItem("squid_essence", Item::new);
    public static final RegistryHandler<Item> TURTLE_ESSENCE = Services.REGISTRY.registerItem("turtle_essence", Item::new);
    public static final RegistryHandler<Item> BLAZE_ESSENCE = Services.REGISTRY.registerItem("blaze_essence", Item::new);
    public static final RegistryHandler<Item> BREEZE_ESSENCE = Services.REGISTRY.registerItem("breeze_essence", Item::new);
    public static final RegistryHandler<Item> DYE_ESSENCE = Services.REGISTRY.registerItem("dye_essence", Item::new);
    public static final RegistryHandler<Item> GHAST_ESSENCE = Services.REGISTRY.registerItem("ghast_essence", Item::new);
    public static final RegistryHandler<Item> PIG_ESSENCE = Services.REGISTRY.registerItem("pig_essence", Item::new);
    public static final RegistryHandler<Item> SHEEP_ESSENCE = Services.REGISTRY.registerItem("sheep_essence", Item::new);
    public static final RegistryHandler<Item> FISH_ESSENCE = Services.REGISTRY.registerItem("fish_essence", Item::new);
    public static final RegistryHandler<Item> ZOMBIE_ESSENCE = Services.REGISTRY.registerItem("zombie_essence", Item::new);

    public static void init() {
        ResourcesTypes.getTypes().forEach(resourcesType -> {
            RegistryHandler<LeafFragmentItem> leafFragment = Services.REGISTRY.registerItem(resourcesType.name() + "_leaf_fragment", properties -> new LeafFragmentItem(properties, resourcesType));
            LEAF_FRAGMENTS.add(leafFragment);
            resourcesType.setLeafFragmentItem(leafFragment);
        });
    }
}
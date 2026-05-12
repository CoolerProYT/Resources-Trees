package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.api.resources.ResourcesTypeRegistryImpl;
import com.coolerpromc.resourcestrees.api.tree.TreeTypeRegistryImpl;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.component.ModDataComponents;
import com.coolerpromc.resourcestrees.config.ResourcesTreesConfig;
import com.coolerpromc.resourcestrees.item.ModCreativeTab;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.screen.ModMenuTypes;

public class CommonClass {
    public static void init() {
        ResourcesTypeRegistryImpl.registerResourcesTypes();
        TreeTypeRegistryImpl.registerTreeTypes();

        ModBlocks.init();
        ModItems.init();
        ModBlockEntities.init();
        ModRecipes.init();
        ModCreativeTab.init();
        ModMenuTypes.init();
        ModDataComponents.init();

        ResourcesTreesConfig.init();
    }
}
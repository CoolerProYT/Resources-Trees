package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesLeavesBlock;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.item.ModCreativeTab;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.screen.ModMenuTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class CommonClass {
    public static void init() {
        ModBlocks.init();
        ModItems.init();
        ModDataComponents.init();
        ModBlockEntities.init();
        ModRecipes.init();
        ModCreativeTab.init();
        ModMenuTypes.init();
    }

    public static Block[] leavesBlock(){
        List<Block> blocks = new ArrayList<>();

        for (Block block : BuiltInRegistries.BLOCK){
            if (block instanceof ResourcesLeavesBlock) {
                blocks.add(block);
            }
        }

        return blocks.toArray(new Block[0]);
    }

    public static Block[] saplingBlock(){
        List<Block> blocks = new ArrayList<>();

        for (Block block : BuiltInRegistries.BLOCK){
            if (block instanceof ResourcesSaplingBlock) {
                blocks.add(block);
            }
        }

        return blocks.toArray(new Block[0]);
    }

    public static List<Block> blocks(){
        List<Block> blocks = new ArrayList<>();

        for (Block block : BuiltInRegistries.BLOCK){
            if (block instanceof ResourcesSaplingBlock || block instanceof ResourcesLeavesBlock){
                if (block.builtInRegistryHolder().key().identifier().getNamespace().equals("kubejs")){
                    blocks.add(block);
                }
            }
        }

        return blocks;
    }
}
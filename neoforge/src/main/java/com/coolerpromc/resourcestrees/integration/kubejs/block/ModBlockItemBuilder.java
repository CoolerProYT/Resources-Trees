package com.coolerpromc.resourcestrees.integration.kubejs.block;

import com.coolerpromc.resourcestrees.item.custom.ModBlockItem;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class ModBlockItemBuilder extends BlockItemBuilder {
    public ModBlockItemBuilder(BlockBuilder builder, Identifier id) {
        super(builder, id);
    }

    @Override
    public Item createObject() {
        return new ModBlockItem(blockBuilder.get(), createItemProperties());
    }
}
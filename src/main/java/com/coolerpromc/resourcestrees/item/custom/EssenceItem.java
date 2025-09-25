package com.coolerpromc.resourcestrees.item.custom;

import net.minecraft.world.item.Item;

public class EssenceItem extends Item {
    private final int color;

    public EssenceItem(Properties properties, int color) {
        super(properties);
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}

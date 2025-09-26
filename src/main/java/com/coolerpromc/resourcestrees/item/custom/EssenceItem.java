package com.coolerpromc.resourcestrees.item.custom;

import net.minecraft.item.Item;

public class EssenceItem extends Item {
    private final int color;

    public EssenceItem(Settings properties, int color) {
        super(properties);
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
package com.coolerpromc.resourcestrees.api.tree;

import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiStatus.Internal
public final class TreeTypes {
    protected static final List<TreeType> TYPES = new ArrayList<>();
    protected static final Map<String, TreeType> TYPE_BY_NAME = new HashMap<>();

    public static List<TreeType> getTypes() {
        return List.copyOf(TYPES);
    }

    public static TreeType getType(String name){
        return TYPE_BY_NAME.get(name);
    }
}

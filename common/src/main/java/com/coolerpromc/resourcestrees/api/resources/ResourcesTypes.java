package com.coolerpromc.resourcestrees.api.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourcesTypes {
    protected static final List<ResourcesType> TYPES = new ArrayList<>();
    protected static final Map<String, ResourcesType> TYPE_BY_NAME = new HashMap<>();

    public static List<ResourcesType> getTypes() {
        return List.copyOf(TYPES);
    }

    public static ResourcesType getType(String name){
        return TYPE_BY_NAME.get(name);
    }
}

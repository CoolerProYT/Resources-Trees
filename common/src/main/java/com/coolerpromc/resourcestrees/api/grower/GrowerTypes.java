package com.coolerpromc.resourcestrees.api.grower;

import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiStatus.Internal
public final class GrowerTypes {
    protected static final List<GrowerType> TYPES = new ArrayList<>();
    protected static final Map<String, GrowerType> TYPE_BY_NAME = new HashMap<>();

    /**
     * Returns an immutable snapshot of all registered {@link GrowerType}s.
     *
     * @return an immutable copy of every registered grower type
     */
    public static List<GrowerType> getTypes() {
        return List.copyOf(TYPES);
    }

    /**
     * Looks up a registered {@link GrowerType} by its {@link GrowerType#name() name}.
     *
     * @param name the unique name of the grower type to retrieve
     * @return the matching {@link GrowerType}, or {@code null} if none is registered under that name
     */
    public static GrowerType getType(String name){
        return TYPE_BY_NAME.get(name);
    }
}

package com.coolerpromc.resourcestrees.util;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.api.resources.ResourcesType;
import com.coolerpromc.resourcestrees.component.ModDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

@Deprecated(forRemoval = true)
public class LegacyItemMigrator {

    public static Item migrate(Holder<Item> item, PatchedDataComponentMap patch) {
        Item legacyItem = item.value();
        Identifier legacyId = BuiltInRegistries.ITEM.getKey(legacyItem);
        Identifier typeId = Constants.id("invalid_type");

        Holder<ResourcesType> type = patch.get(ModDataComponents.LEGACY_TYPE.get());

        if (type == null) {
            return Items.AIR;
        }

        try{
            typeId = type.unwrapKey().orElseThrow().identifier();
            String typeName = typeId.getPath();
            String oldPath = legacyId.getPath();

            String newPath;

            if (oldPath.equals("leaf_fragment")) {
                newPath = typeName + "_leaf_fragment";
            } else {
                if (!oldPath.startsWith("resources_")) {
                    return Items.AIR;
                }

                newPath = typeName + "_" + oldPath.substring("resources_".length());
            }

            Constants.LOG.info("Migrating {} with {} type to {}", item.unwrapKey().get().identifier(), typeId, Constants.id(newPath));
            return BuiltInRegistries.ITEM.getValue(Constants.id(newPath));
        }
        catch (Exception e){
            Constants.LOG.error("Failed to migrate {} with resources type of {} to new resources trees system. It will be kept as legacy item and will be removed completely in future. If the resources type are added by datapack please move the json file to /config/resourcestrees/resources_type", legacyId, typeId);
            Constants.LOG.debug("Failed to migrate item stack.", e);
        }

        return Items.AIR;
    }
}
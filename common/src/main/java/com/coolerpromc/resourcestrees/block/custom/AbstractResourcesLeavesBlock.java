package com.coolerpromc.resourcestrees.block.custom;

import com.coolerpromc.resourcestrees.api.resources.ResourcesType;
import com.coolerpromc.resourcestrees.api.tree.TreeType;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.sounds.AmbientLeavesBlockSoundPlayer;

public class AbstractResourcesLeavesBlock extends LeavesBlock {
    private final ResourcesType resourcesType;
    private final TreeType treeType;

    public AbstractResourcesLeavesBlock(AmbientLeavesBlockSoundPlayer ambientLeavesBlockSoundPlayer, Properties properties, ResourcesType resourcesType, TreeType treeType) {
        super(ambientLeavesBlockSoundPlayer, properties);
        this.resourcesType = resourcesType;
        this.treeType = treeType;
    }

    public ResourcesType getResourcesType() {
        return resourcesType;
    }

    public TreeType getTreeType() {
        return treeType;
    }
}

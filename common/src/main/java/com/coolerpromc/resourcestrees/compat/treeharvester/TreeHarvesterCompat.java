package com.coolerpromc.resourcestrees.compat.treeharvester;

import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.item.custom.LeafFragmentItem;
import com.coolerpromc.resourcestrees.network.packet.ResourceTypeSyncS2CPacket;
import com.coolerpromc.resourcestrees.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Common compat logic for the Tree Harvester mod.
 * Both NeoForge and Fabric entry points delegate to this class.
 */
public final class TreeHarvesterCompat {

    private TreeHarvesterCompat() {}

    /**
     * Handles an item entity spawning in the world, checking if it needs
     * to propagate a resources type to nearby sapling blocks.
     * This should be called from the loader-specific entity spawn event
     * after verifying the entity is an {@link ItemEntity} on a {@link ServerLevel}.
     */
    public static void handleItemEntitySpawn(ItemEntity itemEntity, ServerLevel level) {
        ItemStack itemStack = itemEntity.getItem();
        Item item = itemStack.getItem();
        if (!(item instanceof BlockItem || item instanceof LeafFragmentItem)) return;

        if (item instanceof BlockItem) {
            Block block = Block.byItem(item);
            if (!(block instanceof ResourcesSaplingBlock)) return;
        }
        if (!itemStack.has(ModDataComponents.TYPE.get())) return;

        Holder<ResourcesTypes> type = itemStack.get(ModDataComponents.TYPE.get());

        if (type != null) {
            if (itemEntity.isRemoved()) return;

            BlockPos pos = findNearbyBlock(level, itemEntity.getOnPos(), 5, 50);
            if (pos.equals(BlockPos.ZERO)) return;

            if (!(level.getBlockState(pos).getBlock() instanceof ResourcesSaplingBlock)) return;

            if (!level.isLoaded(pos)) return;

            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (!(blockEntity instanceof ResourcesTypesBlockEntity be)) return;

            be.setResourcesType(type);
            be.setChanged();
            level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), 3);
            Services.PLATFORM.sendToAllPlayers(new ResourceTypeSyncS2CPacket(be.getBlockPos(), type), level);

            List<BlockPos> nearbyPos = findAllNearbyBlock(level, pos, 1);
            for (BlockPos blockPos : nearbyPos) {
                if (blockPos.equals(pos)) continue;

                BlockEntity neighbourBe = level.getBlockEntity(blockPos);
                if (neighbourBe instanceof ResourcesTypesBlockEntity be2) {
                    be2.setResourcesType(type);
                    be2.setChanged();
                    Services.PLATFORM.sendToAllPlayers(new ResourceTypeSyncS2CPacket(be2.getBlockPos(), type), level);
                }
            }
        }
    }

    public static BlockPos findNearbyBlock(Level level, BlockPos center, int radiusXZ, int radiusY) {
        for (int x = -radiusXZ; x <= radiusXZ; x++) {
            for (int y = -radiusY; y <= radiusY; y++) {
                for (int z = -radiusXZ; z <= radiusXZ; z++) {
                    BlockPos checkPos = center.offset(x, y, z);
                    if (level.getBlockState(checkPos).getBlock() instanceof ResourcesSaplingBlock) {
                        BlockEntity entity = level.getBlockEntity(checkPos);
                        if (entity instanceof ResourcesTypesBlockEntity be && (be.getResourcesType() == null)) {
                            return checkPos;
                        }
                    }
                }
            }
        }
        return BlockPos.ZERO;
    }

    public static List<BlockPos> findAllNearbyBlock(Level level, BlockPos center, int radius) {
        List<BlockPos> pos = new ArrayList<>();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos checkPos = center.offset(x, y, z);
                    if (level.getBlockState(checkPos).getBlock() instanceof ResourcesSaplingBlock) {
                        pos.add(checkPos);
                    }
                }
            }
        }
        return pos;
    }
}


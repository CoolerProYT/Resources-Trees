package com.coolerpromc.resourcestrees.block.entity.custom;

import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ResourcesTypesBlockEntity extends BlockEntity {
    private ResourcesTypes resourcesType;

    public ResourcesTypesBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.RESOURCES_TYPE_BE.get(), pos, blockState);
        this.resourcesType = ResourcesTypes.EMPTY;
    }

    public void setResourcesType(ResourcesTypes resourcesType) {
        this.resourcesType = resourcesType;
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.blockEntityChanged(worldPosition);
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL_IMMEDIATE);
        }
    }

    public ResourcesTypes getResourcesType() {
        return resourcesType;
    }

    public int getColor(){
        return getResourcesType().color();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if (resourcesType != null){
            tag.put("type", ResourcesTypes.CODEC.encodeStart(NbtOps.INSTANCE, resourcesType).getOrThrow());
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.setResourcesType(ResourcesTypes.CODEC.parse(NbtOps.INSTANCE, tag.get("type")).getOrThrow());
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);
        loadAdditional(tag, lookupProvider);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        super.onDataPacket(net, pkt, lookupProvider);
        handleUpdateTag(pkt.getTag(), lookupProvider);
        if (level != null && level.isClientSide()){
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }
}

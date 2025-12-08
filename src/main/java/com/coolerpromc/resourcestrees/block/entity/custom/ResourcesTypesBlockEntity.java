package com.coolerpromc.resourcestrees.block.entity.custom;

import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.registry.ModRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class ResourcesTypesBlockEntity extends BlockEntity {
    private @Nullable Holder<ResourcesTypes> resourcesType;

    public ResourcesTypesBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.RESOURCES_TYPE_BE.get(), pos, blockState);
        this.resourcesType = null;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null && !level.isClientSide()){
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public void setResourcesType(Holder<ResourcesTypes> resourcesType) {
        this.resourcesType = resourcesType;
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.blockEntityChanged(worldPosition);
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL_IMMEDIATE);
        }
    }

    public @Nullable Holder<ResourcesTypes> getResourcesType() {
        return resourcesType;
    }

    public int getColor(){
        if (getResourcesType() != null){
            return getResourcesType().value().color();
        }
        return -1;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (resourcesType != null){
            output.store("type", RegistryFileCodec.create(ModRegistries.RESOURCES_TYPES_KEY, ResourcesTypes.CODEC), resourcesType);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.read("type", RegistryFileCodec.create(ModRegistries.RESOURCES_TYPES_KEY, ResourcesTypes.CODEC)).ifPresent(this::setResourcesType);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(ValueInput tag, HolderLookup.Provider holders) {
        super.handleUpdateTag(tag, holders);
        loadAdditional(tag);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public void onDataPacket(Connection connection, ValueInput data, HolderLookup.Provider lookup) {
        handleUpdateTag(data, lookup);
        if (level != null && level.isClientSide()){
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }
}

package com.coolerpromc.resourcestrees.block.entity.custom;

import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ResourcesTypesBlockEntity extends BlockEntity {
    private ResourcesTypes resourcesType;

    public ResourcesTypesBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.RESOURCES_TYPE_BE, pos, blockState);
        this.resourcesType = ResourcesTypes.EMPTY;
    }

    public void setResourcesType(ResourcesTypes resourcesType) {
        this.resourcesType = resourcesType;
        markDirty();
        if (world != null && !world.isClient()) {
            world.markDirty(pos);
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        }
    }

    public ResourcesTypes getResourcesType() {
        return resourcesType;
    }

    public int getColor(){
        return getResourcesType().color();
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.put("type", ResourcesTypes.CODEC.encodeStart(NbtOps.INSTANCE, resourcesType).getOrThrow());
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.setResourcesType(ResourcesTypes.CODEC.parse(NbtOps.INSTANCE, nbt.get("type")).getOrThrow());
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        return createNbtWithIdentifyingData(registries);
    }
}

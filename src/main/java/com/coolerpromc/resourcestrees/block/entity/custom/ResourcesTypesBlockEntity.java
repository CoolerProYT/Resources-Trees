package com.coolerpromc.resourcestrees.block.entity.custom;

import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.registry.ModRegistries;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ResourcesTypesBlockEntity extends BlockEntity {
    private @Nullable RegistryEntry<ResourcesTypes> resourcesType;

    public ResourcesTypesBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.RESOURCES_TYPE_BE, pos, blockState);
        this.resourcesType = null;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (world != null && !world.isClient()){
            world.updateListeners(getPos(), getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        }
    }

    public void setResourcesType(RegistryEntry<ResourcesTypes> resourcesType) {
        this.resourcesType = resourcesType;
        markDirty();
        if (world != null && !world.isClient()) {
            world.markDirty(pos);
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        }
    }

    public @Nullable RegistryEntry<ResourcesTypes> getResourcesType() {
        return resourcesType;
    }

    public int getColor(){
        if (getResourcesType() != null){
            return getResourcesType().value().color();
        }
        return -1;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        if (resourcesType != null){
            resourcesType.getKey().ifPresent(key -> {
                nbt.putString("type", key.getValue().toString());
            });
        }
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        if (nbt.contains("type")){
            Identifier id = Identifier.tryParse(nbt.getString("type"));
            if (id != null) {
                RegistryKey<ResourcesTypes> key = RegistryKey.of(ModRegistries.RESOURCES_TYPES_KEY, id);
                registryLookup.getWrapperOrThrow(ModRegistries.RESOURCES_TYPES_KEY).getOptional(key).ifPresent(this::setResourcesType);
            }
        }
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

package com.coolerpromc.resourcestrees.block.entity.custom;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.api.resources.ResourcesType;
import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.registry.ModRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

@Deprecated(forRemoval = true)
public class LegacyResourcesTypesBlockEntity extends BlockEntity {
    private @Nullable Holder<ResourcesType> resourcesType;

    public LegacyResourcesTypesBlockEntity(BlockPos pos, BlockState blockState) {
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

    public void setResourcesType(Holder<ResourcesType> resourcesType) {
        this.resourcesType = resourcesType;
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.blockEntityChanged(worldPosition);
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL_IMMEDIATE);
        }
    }

    public @Nullable Holder<ResourcesType> getResourcesType() {
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
            output.store("type", RegistryFileCodec.create(ModRegistries.RESOURCES_TYPES_KEY, ResourcesType.CODEC), resourcesType);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.read("type", RegistryFileCodec.create(ModRegistries.RESOURCES_TYPES_KEY, ResourcesType.CODEC)).ifPresent(this::setResourcesType);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level instanceof ServerLevel serverLevel){
            migrate(serverLevel, pos, state);
        }
    }

    private void migrate(ServerLevel level, BlockPos pos, BlockState oldState) {
        if (getResourcesType() == null) {
            return;
        }

        String typeName = "";
        Identifier oldId = null;

        try{
            typeName = getResourcesType().unwrapKey().orElseThrow().identifier().getPath();
            oldId = BuiltInRegistries.BLOCK.getKey(oldState.getBlock());
            String oldPath = oldId.getPath();

            if (!oldPath.startsWith("resources_")) {
                return;
            }

            String newPath = typeName + "_" + oldPath.substring("resources_".length());
            Block replacement = BuiltInRegistries.BLOCK.getValue(Constants.id(newPath));

            if (replacement == Blocks.AIR) {
                return;
            }

            BlockState newState = replacement.defaultBlockState();
            Constants.LOG.info("Migrating {} with {} type to {}", oldId, Constants.id(typeName), Constants.id(newPath));
            level.setBlock(pos, newState, Block.UPDATE_ALL);
        }
        catch (Exception e){
            Constants.LOG.error("Failed to migrate {} with resources type of {} to new resources trees system. It will be kept as legacy block and will be removed completely in future. If the resources type are added by datapack please move the json file to /config/resourcestrees/resources_type", oldId, typeName);
            Constants.LOG.debug("Failed to migrate block.", e);
        }
    }
}
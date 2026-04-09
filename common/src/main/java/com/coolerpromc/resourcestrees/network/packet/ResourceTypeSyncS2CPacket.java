package com.coolerpromc.resourcestrees.network.packet;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.registry.ModRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public record ResourceTypeSyncS2CPacket(BlockPos pos, Holder<ResourcesTypes> resourcesTypes) implements CustomPacketPayload {
    public static final Type<ResourceTypeSyncS2CPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Constants.MODID, "resource_type_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ResourceTypeSyncS2CPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            ResourceTypeSyncS2CPacket::pos,
            ByteBufCodecs.holder(ModRegistries.RESOURCES_TYPES_KEY, ResourcesTypes.STREAM_CODEC),
            ResourceTypeSyncS2CPacket::resourcesTypes,
            ResourceTypeSyncS2CPacket::new
    );

    public void handleOnClient(Level level) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ResourcesTypesBlockEntity be) {
            be.setResourcesType(resourcesTypes);
            be.setChanged();
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
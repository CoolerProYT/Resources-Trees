package com.coolerpromc.resourcestrees.network.packet;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.registry.ModRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.network.CustomPayloadEvent;

public record ResourceTypeSyncS2CPacket(BlockPos pos, Holder<ResourcesTypes> resourcesTypes) implements CustomPacketPayload {
    public static final Type<ResourceTypeSyncS2CPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ResourcesTrees.MODID, "resource_type_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ResourceTypeSyncS2CPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            ResourceTypeSyncS2CPacket::pos,
            ByteBufCodecs.holder(ModRegistries.RESOURCES_TYPES_KEY, ResourcesTypes.STREAM_CODEC),
            ResourceTypeSyncS2CPacket::resourcesTypes,
            ResourceTypeSyncS2CPacket::new
    );

    public static void handle(ResourceTypeSyncS2CPacket packet, CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            if (context.isClientSide()){
                BlockEntity blockEntity = Minecraft.getInstance().level.getBlockEntity(packet.pos);

                if (blockEntity instanceof ResourcesTypesBlockEntity be) {
                    be.setResourcesType(packet.resourcesTypes);
                    be.setChanged();
                }
            }
        }).exceptionally(e -> {
            context.getConnection().disconnect(Component.literal(e.getMessage()));
            return null;
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
package com.coolerpromc.resourcestrees.network.packet;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.registry.ModRegistries;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record ResourceTypeSyncS2CPacket(BlockPos pos, RegistryEntry<ResourcesTypes> resourcesTypes) implements CustomPayload {
    public static final Id<ResourceTypeSyncS2CPacket> TYPE = new Id<>(Identifier.of(ResourcesTrees.MODID, "resource_type_sync"));

    public static final PacketCodec<RegistryByteBuf, ResourceTypeSyncS2CPacket> STREAM_CODEC = PacketCodec.tuple(
            BlockPos.PACKET_CODEC,
            ResourceTypeSyncS2CPacket::pos,
            PacketCodecs.registryEntry(ModRegistries.RESOURCES_TYPES_KEY, ResourcesTypes.STREAM_CODEC),
            ResourceTypeSyncS2CPacket::resourcesTypes,
            ResourceTypeSyncS2CPacket::new
    );

    public static void handle(ResourceTypeSyncS2CPacket packet, ClientPlayNetworking.Context context) {
        context.client().executeAsync((future) -> {
            BlockEntity blockEntity = context.player().getEntityWorld().getBlockEntity(packet.pos);

            if (blockEntity instanceof ResourcesTypesBlockEntity be) {
                be.setResourcesType(packet.resourcesTypes);
                be.markDirty();
            }
        }).exceptionally(e -> {
            context.client().disconnect(Text.literal(e.getMessage()));
            return null;
        });
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}
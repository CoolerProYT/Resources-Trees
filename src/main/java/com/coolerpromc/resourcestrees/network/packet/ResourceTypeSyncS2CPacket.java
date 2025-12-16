package com.coolerpromc.resourcestrees.network.packet;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public record ResourceTypeSyncS2CPacket(BlockPos pos, ResourceLocation resourceLocation) {
    private static final String PROTOCOL_VERSION = "1";
    public static final ResourceLocation TYPE = ResourcesTrees.id("resource_type_sync");

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(TYPE, () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    private static int packetId = 0;
    private static int nextId() {
        return packetId++;
    }

    public static void encode(ResourceTypeSyncS2CPacket payload, FriendlyByteBuf byteBuf){
        byteBuf.writeBlockPos(payload.pos);
        byteBuf.writeResourceLocation(payload.resourceLocation);
    }

    public static ResourceTypeSyncS2CPacket decode(FriendlyByteBuf byteBuf){
        return new ResourceTypeSyncS2CPacket(byteBuf.readBlockPos(), byteBuf.readResourceLocation());
    }

    public static void handle(ResourceTypeSyncS2CPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            BlockEntity blockEntity = context.get().getSender().level().getBlockEntity(packet.pos);
            System.out.println(blockEntity);
            if (blockEntity instanceof ResourcesTypesBlockEntity be) {
                be.setResourcesType(packet.resourceLocation);
                be.setChanged();
            }
        });
        context.get().setPacketHandled(true);
    }

    public static void register(){
        INSTANCE.registerMessage(
                nextId(),
                ResourceTypeSyncS2CPacket.class,
                ResourceTypeSyncS2CPacket::encode,
                ResourceTypeSyncS2CPacket::decode,
                ResourceTypeSyncS2CPacket::handle
        );
    }
}
package com.coolerpromc.resourcestrees.networking;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.recipe.RecipeEntry;

import java.util.List;

public record RecipeSyncPayload(List<RecipeEntry<?>> recipes) implements CustomPayload {
    public static final Id<RecipeSyncPayload> ID = new Id<>(ResourcesTrees.id("recipe_sync"));
    public static final PacketCodec<RegistryByteBuf, RecipeSyncPayload> PACKET_CODEC = PacketCodec.tuple(
            RecipeEntry.PACKET_CODEC.collect(PacketCodecs.toList()), RecipeSyncPayload::recipes,
            RecipeSyncPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

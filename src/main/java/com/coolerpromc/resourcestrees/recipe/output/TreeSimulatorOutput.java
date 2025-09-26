package com.coolerpromc.resourcestrees.recipe.output;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.math.random.Random;

public record TreeSimulatorOutput(ItemStack output, float chance, int minRolls, int maxRolls) {
    public static TreeSimulatorOutput of(ItemStack output, float chance, int minRolls, int maxRolls){
        return new TreeSimulatorOutput(output, chance, minRolls, maxRolls);
    }

    public static final Codec<TreeSimulatorOutput> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.fieldOf("output").forGetter(TreeSimulatorOutput::output),
            Codec.FLOAT.fieldOf("chance").forGetter(TreeSimulatorOutput::chance),
            Codec.INT.fieldOf("minRolls").forGetter(TreeSimulatorOutput::minRolls),
            Codec.INT.fieldOf("maxRolls").forGetter(TreeSimulatorOutput::maxRolls)
    ).apply(instance, TreeSimulatorOutput::new));

    public static final PacketCodec<RegistryByteBuf, TreeSimulatorOutput> STREAM_CODEC = PacketCodec.tuple(
            ItemStack.PACKET_CODEC,
            TreeSimulatorOutput::output,
            PacketCodecs.FLOAT,
            TreeSimulatorOutput::chance,
            PacketCodecs.INTEGER,
            TreeSimulatorOutput::minRolls,
            PacketCodecs.INTEGER,
            TreeSimulatorOutput::maxRolls,
            TreeSimulatorOutput::new
    );

    public int getRolls(Random random){
        if (minRolls > maxRolls) {
            throw new IllegalArgumentException("minRolls cannot be greater than maxRolls");
        }
        if (minRolls == maxRolls) {
            return minRolls;
        }
        return random.nextInt(maxRolls - minRolls + 1) + minRolls;
    }
}

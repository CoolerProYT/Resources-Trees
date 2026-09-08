package com.coolerpromc.resourcestrees.recipe.output;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.ItemLike;

public record TreeSimulatorOutput(ItemStackTemplate output, float chance, int minRolls, int maxRolls) {
    public static TreeSimulatorOutput of(ItemStackTemplate output, float chance, int minRolls, int maxRolls){
        return new TreeSimulatorOutput(output, chance, minRolls, maxRolls);
    }

    public static TreeSimulatorOutput of(ItemLike output, float chance, int minRolls, int maxRolls){
        return new TreeSimulatorOutput(new ItemStackTemplate(output.asItem(), 1), chance, minRolls, maxRolls);
    }

    public static final Codec<TreeSimulatorOutput> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStackTemplate.CODEC.fieldOf("output").forGetter(TreeSimulatorOutput::output),
            Codec.FLOAT.fieldOf("chance").forGetter(TreeSimulatorOutput::chance),
            Codec.INT.fieldOf("minRolls").forGetter(TreeSimulatorOutput::minRolls),
            Codec.INT.fieldOf("maxRolls").forGetter(TreeSimulatorOutput::maxRolls)
    ).apply(instance, TreeSimulatorOutput::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, TreeSimulatorOutput> STREAM_CODEC = StreamCodec.composite(
            ItemStackTemplate.STREAM_CODEC,
            TreeSimulatorOutput::output,
            ByteBufCodecs.FLOAT,
            TreeSimulatorOutput::chance,
            ByteBufCodecs.INT,
            TreeSimulatorOutput::minRolls,
            ByteBufCodecs.INT,
            TreeSimulatorOutput::maxRolls,
            TreeSimulatorOutput::new
    );

    public int getRolls(RandomSource random){
        if (minRolls > maxRolls) {
            throw new IllegalArgumentException("minRolls cannot be greater than maxRolls");
        }
        if (minRolls == maxRolls) {
            return minRolls;
        }
        return random.nextInt(maxRolls - minRolls + 1) + minRolls;
    }

    public int getRolls(RandomSource random, int fortuneLevel){
        return getRolls(random) + Math.max(0, fortuneLevel);
    }
}

package com.coolerpromc.resourcestrees.block.custom;

import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.ParticleUtil;
import net.minecraft.particle.TintedParticleEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class ResourcesLeavesBlock extends LeavesBlock implements BlockEntityProvider {
    public static final MapCodec<ResourcesLeavesBlock> CODEC = RecordCodecBuilder.mapCodec((p_400250_) ->
            p_400250_.group(
                    Codecs.rangedInclusiveFloat(0.0F, 1.0F).fieldOf("leaf_particle_chance").forGetter((p_399927_) -> p_399927_.leafParticleChance),
                    createSettingsCodec(),
                    ResourcesSaplingBlock.CODEC.fieldOf("sapling").forGetter(resourcesLeavesBlock -> resourcesLeavesBlock.sapling.get())
            ).apply(p_400250_, (c, p, s) -> new ResourcesLeavesBlock(c, p, () -> s)));

    private final Supplier<ResourcesSaplingBlock> sapling;

    public ResourcesLeavesBlock(float leafParticleChance, Settings properties, Supplier<ResourcesSaplingBlock> sapling) {
        super(leafParticleChance, properties);
        this.sapling = sapling;
    }

    @Override
    public MapCodec<? extends LeavesBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void spawnLeafParticle(World world, BlockPos pos, Random random) {
        TintedParticleEffect colorparticleoption = TintedParticleEffect.create(ParticleTypes.TINTED_LEAVES, world.getBlockColor(pos));
        ParticleUtil.spawnParticle(world, pos, random, colorparticleoption);
    }

    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, LootWorldContext.Builder builder) {
        List<ItemStack> drops = super.getDroppedStacks(state, builder);
        BlockEntity blockEntity = builder.get(LootContextParameters.BLOCK_ENTITY);

        if (blockEntity instanceof ResourcesTypesBlockEntity be){
            Identifier type = be.getResourcesType();
            ResourcesTypes resourcesTypes = ResourcesTypes.byId(type, builder.getWorld());

            if (drops.isEmpty()){
                if (builder.getWorld().getRandom().nextFloat() < resourcesTypes.saplingChance()) {
                    ItemStack saplingDrop = sapling.get().asItem().getDefaultStack();
                    saplingDrop.set(ModDataComponents.TYPE, type);
                    drops.add(saplingDrop);
                }

                ItemStack fragment = ModItems.LEAF_FRAGMENT.getDefaultStack();
                fragment.set(ModDataComponents.TYPE, type);

                drops.add(fragment.copy());

                if (builder.getWorld().getRandom().nextFloat() < resourcesTypes.secondaryDropChance()){
                    drops.add(fragment.copy());
                }
            }
            else{
                if (Block.getBlockFromItem(drops.getFirst().getItem()) instanceof ResourcesLeavesBlock){
                    drops.getFirst().set(ModDataComponents.TYPE, type);
                }
            }
        }

        return drops;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ResourcesTypesBlockEntity be && itemStack.contains(ModDataComponents.TYPE)){
            be.setResourcesType(itemStack.get(ModDataComponents.TYPE));
        }
    }

    @Override
    protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ResourcesTypesBlockEntity be && be.getResourcesType() != null){
            ItemStack stack = super.getPickStack(world, pos, state, includeData);
            stack.set(ModDataComponents.TYPE, be.getResourcesType());
            return stack;
        }
        return super.getPickStack(world, pos, state, includeData);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ResourcesTypesBlockEntity(pos, state);
    }
}

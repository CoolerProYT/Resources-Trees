package com.coolerpromc.resourcestrees.block.custom;

import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class ResourcesLeavesBlock extends LeavesBlock implements EntityBlock {
    public static final MapCodec<ResourcesLeavesBlock> CODEC = RecordCodecBuilder.mapCodec((p_400250_) ->
            p_400250_.group(
                    ExtraCodecs.floatRange(0.0F, 1.0F).fieldOf("leaf_particle_chance").forGetter((p_399927_) -> p_399927_.leafParticleChance),
                    propertiesCodec(),
                    ResourcesSaplingBlock.CODEC.fieldOf("sapling").forGetter(resourcesLeavesBlock -> resourcesLeavesBlock.sapling.get())
            ).apply(p_400250_, (c, p, s) -> new ResourcesLeavesBlock(c, p, () -> s)));

    private final Supplier<ResourcesSaplingBlock> sapling;

    public ResourcesLeavesBlock(float leafParticleChance, Properties properties, Supplier<ResourcesSaplingBlock> sapling) {
        super(leafParticleChance, properties);
        this.sapling = sapling;
    }

    @Override
    public MapCodec<? extends LeavesBlock> codec() {
        return CODEC;
    }

    @Override
    protected void spawnFallingLeavesParticle(Level level, BlockPos blockPos, RandomSource randomSource) {
        ColorParticleOption colorparticleoption = ColorParticleOption.create(ParticleTypes.TINTED_LEAVES, level.getClientLeafTintColor(blockPos));
        ParticleUtils.spawnParticleBelow(level, blockPos, randomSource, colorparticleoption);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        Vec3 pos = builder.getOptionalParameter(LootContextParams.ORIGIN);
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);

        if(blockEntity instanceof ResourcesTypesBlockEntity be){
            calculateDrops(be, builder, drops);
        }
        else if (builder.getLevel().getBlockEntity(new BlockPos((int) pos.x, (int) pos.y, (int) pos.z)) instanceof ResourcesTypesBlockEntity be){
            calculateDrops(be, builder, drops);
        }

        return drops;
    }

    private void calculateDrops(ResourcesTypesBlockEntity be, LootParams.Builder builder, List<ItemStack> drops){
        Holder<ResourcesTypes> resourcesTypes = be.getResourcesType();

        if (resourcesTypes != null){
            if (drops.isEmpty()){
                if (builder.getLevel().getRandom().nextFloat() < resourcesTypes.value().saplingDropChance() / 2) {
                    ItemStack saplingDrop = sapling.get().asItem().getDefaultInstance();
                    saplingDrop.set(ModDataComponents.TYPE.get(), resourcesTypes);
                    drops.add(saplingDrop);
                }

                ItemStack fragment = ModItems.LEAF_FRAGMENT.get().getDefaultInstance();
                fragment.set(ModDataComponents.TYPE.get(), resourcesTypes);

                if (builder.getLevel().getRandom().nextFloat() < resourcesTypes.value().leafDropChance()){
                    drops.add(fragment.copy());
                }

                if (builder.getLevel().getRandom().nextFloat() < resourcesTypes.value().leafDropChance() / 2){ // Secondary Drop Chance
                    drops.add(fragment.copy());
                }
            }
            else{
                if (Block.byItem(drops.getFirst().getItem()) instanceof ResourcesLeavesBlock){
                    drops.getFirst().set(ModDataComponents.TYPE.get(), resourcesTypes);
                }
            }
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ResourcesTypesBlockEntity be && stack.has(ModDataComponents.TYPE.get())){
            Holder<ResourcesTypes> holder = stack.get(ModDataComponents.TYPE.get());
            if (holder != null){
                be.setResourcesType(holder);
            }
        }
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ResourcesTypesBlockEntity be && be.getResourcesType() != null){
            ItemStack stack = super.getCloneItemStack(level, pos, state, includeData);
            stack.set(ModDataComponents.TYPE.get(), be.getResourcesType());
            return stack;
        }
        return super.getCloneItemStack(level, pos, state, includeData);
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 60;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 30;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ResourcesTypesBlockEntity(blockPos, blockState);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (this.decaying(state)) {
            List<ItemStack> drops = this.getDrops(state, new LootParams.Builder(level).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, level.getBlockEntity(pos)).withParameter(LootContextParams.ORIGIN, pos.getCenter()));
            drops.forEach(drop -> level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), drop)));
            level.removeBlock(pos, false);
        }
    }
}

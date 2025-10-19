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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class ResourcesLeavesBlock extends LeavesBlock implements EntityBlock {
    public static final MapCodec<ResourcesLeavesBlock> CODEC = RecordCodecBuilder.mapCodec((p_400250_) ->
            p_400250_.group(
                    propertiesCodec(),
                    ResourcesSaplingBlock.CODEC.fieldOf("sapling").forGetter(resourcesLeavesBlock -> resourcesLeavesBlock.sapling.get())
            ).apply(p_400250_, (p, s) -> new ResourcesLeavesBlock(p, () -> s)));

    private final Supplier<ResourcesSaplingBlock> sapling;

    public ResourcesLeavesBlock(Properties properties, Supplier<ResourcesSaplingBlock> sapling) {
        super(properties);
        this.sapling = sapling;
    }

    @Override
    public MapCodec<? extends LeavesBlock> codec() {
        return CODEC;
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
        ResourcesTypes resourcesTypes = be.getResourcesType();

        if (drops.isEmpty()){
            if (builder.getLevel().getRandom().nextFloat() < resourcesTypes.saplingChance()) {
                ItemStack saplingDrop = sapling.get().asItem().getDefaultInstance();
                saplingDrop.set(ModDataComponents.TYPE, resourcesTypes.asHolder(builder.getLevel()));
                drops.add(saplingDrop);
            }

            ItemStack fragment = ModItems.LEAF_FRAGMENT.toStack();
            fragment.set(ModDataComponents.TYPE, resourcesTypes.asHolder(builder.getLevel()));

            drops.add(fragment.copy());

            if (builder.getLevel().getRandom().nextFloat() < resourcesTypes.secondaryDropChance()){
                drops.add(fragment.copy());
            }
        }
        else{
            if (Block.byItem(drops.getFirst().getItem()) instanceof ResourcesLeavesBlock){
                drops.getFirst().set(ModDataComponents.TYPE, resourcesTypes.asHolder(builder.getLevel()));
            }
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ResourcesTypesBlockEntity be && stack.has(ModDataComponents.TYPE.get())){
            Holder<ResourcesTypes> holder = stack.get(ModDataComponents.TYPE.get());
            if (holder != null){
                be.setResourcesType(holder.value());
            }
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ResourcesTypesBlockEntity be && be.getResourcesType() != null){
            ItemStack stack = super.getCloneItemStack(state, target, level, pos, player);
            stack.set(ModDataComponents.TYPE, be.getResourcesType().asHolder(player.level()));
            return stack;
        }
        return super.getCloneItemStack(state, target, level, pos, player);
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
}

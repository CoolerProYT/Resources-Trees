package com.coolerpromc.resourcestrees.block.custom;

import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
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
    private final Supplier<ResourcesSaplingBlock> sapling;

    public ResourcesLeavesBlock(Properties properties, Supplier<ResourcesSaplingBlock> sapling) {
        super(properties);
        this.sapling = sapling;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
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
        ResourceLocation type = be.getResourcesType();
        ResourcesTypes resourcesTypes = ResourcesTypes.asHolder(builder.getLevel(), type).value();

        if (drops.isEmpty()){
            if (builder.getLevel().getRandom().nextFloat() < resourcesTypes.saplingDropChance()) {
                ItemStack saplingDrop = sapling.get().asItem().getDefaultInstance();
                CompoundTag tag = saplingDrop.getOrCreateTag();
                tag.putString("type", type.toString());
                drops.add(saplingDrop);
            }

            ItemStack fragment = ModItems.LEAF_FRAGMENT.get().getDefaultInstance();
            CompoundTag tag = fragment.getOrCreateTag();
            tag.putString("type", type.toString());

            drops.add(fragment.copy());

            if (builder.getLevel().getRandom().nextFloat() < resourcesTypes.leafDropChance()){
                drops.add(fragment.copy());
            }

            if (builder.getLevel().getRandom().nextFloat() < resourcesTypes.leafDropChance() / 2){ // Secondary Drop Chance
                drops.add(fragment.copy());
            }
        }
        else{
            if (Block.byItem(drops.get(0).getItem()) instanceof ResourcesLeavesBlock){
                drops.get(0).getOrCreateTag().putString("type", type.toString());
            }
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ResourcesTypesBlockEntity be && stack.hasTag() && stack.getTag().contains("type")){
            be.setResourcesType(new ResourceLocation(stack.getTag().getString("type")));
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ResourcesTypesBlockEntity be && be.getResourcesType() != null){
            ItemStack stack = super.getCloneItemStack(state, target, level, pos, player);
            stack.getOrCreateTag().putString("type", be.getResourcesType().toString());
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

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (this.decaying(state)) {
            List<ItemStack> drops = this.getDrops(state, new LootParams.Builder(level).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, level.getBlockEntity(pos)).withParameter(LootContextParams.ORIGIN, pos.getCenter()));
            drops.forEach(drop -> level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), drop)));
            level.removeBlock(pos, false);
        }
    }
}

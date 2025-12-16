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
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class ResourcesLeavesBlock extends LeavesBlock implements BlockEntityProvider {
    public static final MapCodec<ResourcesLeavesBlock> CODEC = RecordCodecBuilder.mapCodec((p_400250_) ->
            p_400250_.group(
                    createSettingsCodec(),
                    ResourcesSaplingBlock.CODEC.fieldOf("sapling").forGetter(resourcesLeavesBlock -> resourcesLeavesBlock.sapling.get())
            ).apply(p_400250_, (p, s) -> new ResourcesLeavesBlock(p, () -> s)));

    private final Supplier<ResourcesSaplingBlock> sapling;

    public ResourcesLeavesBlock(Settings properties, Supplier<ResourcesSaplingBlock> sapling) {
        super(properties);
        this.sapling = sapling;
    }

    @Override
    public MapCodec<? extends LeavesBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        List<ItemStack> drops = super.getDroppedStacks(state, builder);
        Vec3d pos = builder.getOptional(LootContextParameters.ORIGIN);
        BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);

        if(blockEntity instanceof ResourcesTypesBlockEntity be){
            calculateDrops(be, builder, drops);
        }
        else if (builder.getWorld().getBlockEntity(new BlockPos((int) pos.x, (int) pos.y, (int) pos.z)) instanceof ResourcesTypesBlockEntity be){
            calculateDrops(be, builder, drops);
        }

        return drops;
    }

    private void calculateDrops(ResourcesTypesBlockEntity be, LootContextParameterSet.Builder builder, List<ItemStack> drops){
        RegistryEntry<ResourcesTypes> resourcesTypes = be.getResourcesType();

        if (resourcesTypes != null){
            if (drops.isEmpty()){
                if (builder.getWorld().getRandom().nextFloat() < resourcesTypes.value().saplingDropChance()) {
                    ItemStack saplingDrop = sapling.get().asItem().getDefaultStack();
                    saplingDrop.set(ModDataComponents.TYPE, resourcesTypes);
                    drops.add(saplingDrop);
                }

                ItemStack fragment = ModItems.LEAF_FRAGMENT.getDefaultStack();
                fragment.set(ModDataComponents.TYPE, resourcesTypes);

                drops.add(fragment.copy());

                if (builder.getWorld().getRandom().nextFloat() < resourcesTypes.value().leafDropChance()){
                    drops.add(fragment.copy());
                }

                if (builder.getWorld().getRandom().nextFloat() < resourcesTypes.value().leafDropChance() / 2){ // Secondary Drop Chance
                    drops.add(fragment.copy());
                }
            }
            else{
                if (Block.getBlockFromItem(drops.getFirst().getItem()) instanceof ResourcesLeavesBlock){
                    drops.getFirst().set(ModDataComponents.TYPE, resourcesTypes);
                }
            }
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ResourcesTypesBlockEntity be && itemStack.contains(ModDataComponents.TYPE)){
            RegistryEntry<ResourcesTypes> holder = itemStack.get(ModDataComponents.TYPE);
            if (holder != null){
                be.setResourcesType(holder);
            }
        }
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ResourcesTypesBlockEntity be && be.getResourcesType() != null){
            ItemStack stack = super.getPickStack(world, pos, state);
            stack.set(ModDataComponents.TYPE, be.getResourcesType());
            return stack;
        }
        return super.getPickStack(world, pos, state);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ResourcesTypesBlockEntity(pos, state);
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (this.shouldDecay(state)) {
            List<ItemStack> drops = this.getDroppedStacks(state, new LootContextParameterSet.Builder(world).add(LootContextParameters.TOOL, ItemStack.EMPTY).add(LootContextParameters.BLOCK_ENTITY, world.getBlockEntity(pos)).add(LootContextParameters.ORIGIN, pos.toCenterPos()));
            drops.forEach(drop -> world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), drop)));
            world.removeBlock(pos, false);
        }
    }
}

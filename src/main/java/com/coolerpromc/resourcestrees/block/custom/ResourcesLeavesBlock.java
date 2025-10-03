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
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
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
        BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);

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
}

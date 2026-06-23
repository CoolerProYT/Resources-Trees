package com.coolerpromc.resourcestrees.block.custom;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.api.resources.ResourcesType;
import com.coolerpromc.resourcestrees.api.tree.TreeType;
import com.coolerpromc.resourcestrees.platform.Services;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Objects;

public class ResourcesTintedParticlesLeavesBlock extends AbstractResourcesLeavesBlock {
    public static final MapCodec<ResourcesTintedParticlesLeavesBlock> CODEC = RecordCodecBuilder.mapCodec((p_400250_) ->
            p_400250_.group(
                    propertiesCodec(),
                    ResourcesType.CODEC.fieldOf("resources_type").forGetter(ResourcesTintedParticlesLeavesBlock::getResourcesType),
                    TreeType.CODEC.fieldOf("tree_type").forGetter(ResourcesTintedParticlesLeavesBlock::getTreeType)
            ).apply(p_400250_, ResourcesTintedParticlesLeavesBlock::new));

    protected final float leafParticleChance;

    public ResourcesTintedParticlesLeavesBlock(Properties properties, ResourcesType resourcesType, TreeType treeType) {
        super(treeType.leavesBlockSoundPlayer(), properties, resourcesType, treeType);
        this.leafParticleChance = treeType.particle();
    }

    @Override
    public MapCodec<? extends ResourcesTintedParticlesLeavesBlock> codec() {
        return CODEC;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        this.makeFallingLeavesParticles(level, pos, random);
    }

    private void makeFallingLeavesParticles(Level level, BlockPos pos, RandomSource random) {
        BlockPos below = pos.below();
        BlockState belowState = level.getBlockState(below);
        if (!(random.nextFloat() >= this.leafParticleChance)) {
            if (!isFaceFull(belowState.getCollisionShape(level, below), Direction.UP)) {
                this.spawnFallingLeavesParticle(level, pos, random);
            }
        }
    }

    protected void spawnFallingLeavesParticle(Level level, BlockPos pos, RandomSource random) {
        ColorParticleOption particle = ColorParticleOption.create(ParticleTypes.TINTED_LEAVES, level.getClientLeafTintColor(pos));
        ParticleUtils.spawnParticleBelow(level, pos, random, particle);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        if (drops.isEmpty()) {
            ItemInstance instance = builder.getOptionalParameter(LootContextParams.TOOL);
            if (instance != null && instance.is(Services.PLATFORM.getShearTag())){
                return List.of(asItem().getDefaultInstance());
            }
            if (instance != null){
                ItemEnchantments enchantments = instance.get(DataComponents.ENCHANTMENTS);
                if (enchantments != null && enchantments.getLevel(builder.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH)) > 0){
                    return List.of(asItem().getDefaultInstance());
                }
            }
            calculateDrops(builder, drops);
        }
        return drops;
    }

    private void calculateDrops(LootParams.Builder builder, List<ItemStack> drops) {
        if (this.getResourcesType() == null) return;
        RandomSource random = builder.getLevel().getRandom();

        if (random.nextFloat() < this.getResourcesType().saplingDropChance()) {
            drops.add(this.getResourcesType().saplingBlock(this.getTreeType().name()).get().asItem().getDefaultInstance());
        }

        ItemStack fragment = this.getResourcesType().leafFragmentItem().get().getDefaultInstance();
        if (random.nextFloat() < this.getResourcesType().leafDropChance()) {
            drops.add(fragment.copy());
        }
        if (random.nextFloat() < this.getResourcesType().leafDropChance() * 0.5F) {
            drops.add(fragment.copy());
        }
    }

    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 60;
    }

    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 30;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (this.decaying(state)) {
            List<ItemStack> drops = this.getDrops(state, new LootParams.Builder(level).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, level.getBlockEntity(pos)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)));
            drops.forEach(drop -> level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), drop)));
            level.removeBlock(pos, false);
        }
    }

    @Override
    public MutableComponent getName() {
        if (!Objects.equals(super.getName().getString(), getDescriptionId())){
            return super.getName();
        }
        return Component.translatable("item.resourcestrees.trees",
                Constants.getOrFallback("resources_type.resourcestrees." + this.getResourcesType().name(), this.getResourcesType().name()),
                Constants.getOrFallback("tree_type.resourcestrees." + this.getTreeType().name(), this.getTreeType().name()),
                Constants.getOrFallback("item.resourcestrees.leaves", "Leaves"));
    }
}

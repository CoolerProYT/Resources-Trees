package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.item.ModCreativeTab;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.item.custom.LeafFragmentItem;
import com.coolerpromc.resourcestrees.network.packet.ResourceTypeSyncS2CPacket;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.screen.ModMenuTypes;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.PacketDistributor;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mod(ResourcesTrees.MODID)
public class ResourcesTrees {
    public static final String MODID = "resourcestrees";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ResourcesTrees() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModCreativeTab.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ResourceTypeSyncS2CPacket::register);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!ModList.get().isLoaded("treeharvester")) return;
        Entity entity = event.getEntity();
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!(entity instanceof ItemEntity itemEntity)) return;

        level.getServer().tell(new TickTask(5, () -> {
            ItemStack itemStack = itemEntity.getItem();
            Item item = itemStack.getItem();
            if (!(item instanceof BlockItem || item instanceof LeafFragmentItem)) return;

            if (item instanceof BlockItem){
                Block block = Block.byItem(item);
                if (!(block instanceof ResourcesSaplingBlock)) return;
            }
            if (!itemStack.hasTag()) return;
            if (!itemStack.getTag().contains("type")) return;

            ResourceLocation rl = new ResourceLocation(itemStack.getOrCreateTag().getString("type"));
            Holder<ResourcesTypes> type = ResourcesTypes.asHolder(level, rl);

            if (type != null){
                if (itemEntity.isRemoved()) return;

                BlockPos pos = findNearbyBlock(level, itemEntity.getOnPos(), 5, 50);
                if (pos.equals(BlockPos.ZERO)) {
                    return;
                }

                if (!(level.getBlockState(pos).getBlock() instanceof ResourcesSaplingBlock)) {
                    return;
                }

                if (!level.isLoaded(pos)) {
                    return;
                }

                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (!(blockEntity instanceof ResourcesTypesBlockEntity be)) {
                    return;
                }

                be.setResourcesType(rl);
                ResourceTypeSyncS2CPacket.INSTANCE.send(PacketDistributor.ALL.noArg(), new ResourceTypeSyncS2CPacket(pos, rl));

                List<BlockPos> nearbyPos = findAllNearbyBlock(level, pos, 1);
                for (BlockPos blockPos : nearbyPos) {
                    if (blockPos.equals(pos)) continue;

                    BlockEntity neighbourBe = level.getBlockEntity(blockPos);
                    if (neighbourBe instanceof ResourcesTypesBlockEntity be2) {
                        be2.setResourcesType(rl);
                        ResourceTypeSyncS2CPacket.INSTANCE.send(PacketDistributor.ALL.noArg(), new ResourceTypeSyncS2CPacket(pos, rl));
                    }
                }
            }
        }));
    }

    public static BlockPos findNearbyBlock(Level level, BlockPos center, int radiusXZ, int radiusY) {
        for (int x = -radiusXZ; x <= radiusXZ; x++) {
            for (int y = -radiusY; y <= radiusY; y++) {
                for (int z = -radiusXZ; z <= radiusXZ; z++) {
                    BlockPos checkPos = center.offset(x, y, z);
                    if (level.getBlockState(checkPos).getBlock() instanceof ResourcesSaplingBlock) {
                        BlockEntity entity = level.getBlockEntity(checkPos);
                        if (entity instanceof ResourcesTypesBlockEntity be && (be.getResourcesType() == null || Objects.equals(be.getResourcesType(), id("empty")))){
                            return checkPos;
                        }
                    }
                }
            }
        }
        return BlockPos.ZERO;
    }

    public static List<BlockPos> findAllNearbyBlock(Level level, BlockPos center, int radius) {
        List<BlockPos> pos = new ArrayList<>();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos checkPos = center.offset(x, y, z);
                    if (level.getBlockState(checkPos).getBlock() instanceof ResourcesSaplingBlock) {
                        pos.add(checkPos);
                    }
                }
            }
        }
        return pos;
    }

    public static ResourceLocation id(String path){
        return new ResourceLocation(MODID, path);
    }
}

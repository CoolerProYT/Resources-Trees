package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.block.entity.custom.ResourcesTypesBlockEntity;
import com.coolerpromc.resourcestrees.block.entity.renderer.TreeSimulatorBlockEntityRenderer;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.datagen.model.ResourcesTypeTintSource;
import com.coolerpromc.resourcestrees.item.ModCreativeTab;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.item.custom.LeafFragmentItem;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.screen.ModMenuTypes;
import com.coolerpromc.resourcestrees.screen.custom.TreeSimulatorScreen;
import com.coolerpromc.resourcestrees.util.DataComponentIngredient;
import com.mojang.logging.LogUtils;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.listener.Priority;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mod(ResourcesTrees.MODID)
public final class ResourcesTrees {
    public static final String MODID = "resourcestrees";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<IIngredientSerializer<?>> INGREDIENT_SERIALIZERS = DeferredRegister.create(ForgeRegistries.INGREDIENT_SERIALIZERS, MODID);
    public static final RegistryObject<IIngredientSerializer<DataComponentIngredient>> DATA_COMPONENT_INGREDIENT = INGREDIENT_SERIALIZERS.register("data_component", DataComponentIngredient.Serializer::new);

    public ResourcesTrees(FMLJavaModLoadingContext context) {
        var modBusGroup = context.getModBusGroup();
        EntityJoinLevelEvent.BUS.addListener(ResourcesTrees::onEntityJoinLevel);

        ModItems.register(modBusGroup);
        ModBlocks.register(modBusGroup);
        ModBlockEntities.register(modBusGroup);
        ModCreativeTab.register(modBusGroup);
        ModDataComponents.register(modBusGroup);
        ModMenuTypes.register(modBusGroup);
        ModRecipes.register(modBusGroup);
        INGREDIENT_SERIALIZERS.register(modBusGroup);
    }

    @SubscribeEvent(priority = Priority.LOWEST)
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!ModList.get().isLoaded("treeharvester")) return;
        Entity entity = event.getEntity();
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!(entity instanceof ItemEntity itemEntity)) return;

        ItemStack itemStack = itemEntity.getItem();
        Item item = itemStack.getItem();
        if (!(item instanceof BlockItem || item instanceof LeafFragmentItem)) return;

        if (item instanceof BlockItem){
            Block block = Block.byItem(item);
            if (!(block instanceof ResourcesSaplingBlock)) return;
        }
        if (!itemStack.has(ModDataComponents.TYPE.get())) return;

        Holder<ResourcesTypes> type = itemStack.get(ModDataComponents.TYPE.get());

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

            be.setResourcesType(type.value());

            List<BlockPos> nearbyPos = findAllNearbyBlock(level, pos, 1);
            for (BlockPos blockPos : nearbyPos) {
                if (blockPos.equals(pos)) continue;

                BlockEntity neighbourBe = level.getBlockEntity(blockPos);
                if (neighbourBe instanceof ResourcesTypesBlockEntity be2) {
                    be2.setResourcesType(type.value());
                }
            }
        }
    }

    public static BlockPos findNearbyBlock(Level level, BlockPos center, int radiusXZ, int radiusY) {
        for (int x = -radiusXZ; x <= radiusXZ; x++) {
            for (int y = -radiusY; y <= radiusY; y++) {
                for (int z = -radiusXZ; z <= radiusXZ; z++) {
                    BlockPos checkPos = center.offset(x, y, z);
                    if (level.getBlockState(checkPos).getBlock() instanceof ResourcesSaplingBlock) {
                        BlockEntity entity = level.getBlockEntity(checkPos);
                        if (entity instanceof ResourcesTypesBlockEntity be && (be.getResourcesType() == null || Objects.equals(be.getResourcesType(), ResourcesTypes.EMPTY))){
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
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                MenuScreens.register(ModMenuTypes.TREE_SIMULATOR.get(), TreeSimulatorScreen::new);
            });
        }

        @SubscribeEvent
        public static void onRegisterColorHandlers(RegisterColorHandlersEvent.Block event) {
            ItemTintSources.ID_MAPPER.put(ResourceLocation.fromNamespaceAndPath(MODID, "resources_type_tint"), ResourcesTypeTintSource.MAP_CODEC);
            event.register((blockState, blockAndTintGetter, blockPos, i) -> {
                        if (blockAndTintGetter != null){
                            BlockEntity blockEntity = blockAndTintGetter.getBlockEntity(blockPos);
                            if (blockEntity instanceof ResourcesTypesBlockEntity be){
                                return be.getColor();
                            }
                        }
                        return -12012264;
                    },
                    ModBlocks.RESOURCES_OAK_SAPLING.get(),
                    ModBlocks.RESOURCES_SPRUCE_SAPLING.get(),
                    ModBlocks.RESOURCES_BIRCH_SAPLING.get(),
                    ModBlocks.RESOURCES_JUNGLE_SAPLING.get(),
                    ModBlocks.RESOURCES_ACACIA_SAPLING.get(),
                    ModBlocks.RESOURCES_DARK_OAK_SAPLING.get(),
                    ModBlocks.RESOURCES_CHERRY_SAPLING.get(),
                    ModBlocks.RESOURCES_PALE_OAK_SAPLING.get(),
                    ModBlocks.RESOURCES_OAK_LEAVES.get(),
                    ModBlocks.RESOURCES_SPRUCE_LEAVES.get(),
                    ModBlocks.RESOURCES_BIRCH_LEAVES.get(),
                    ModBlocks.RESOURCES_JUNGLE_LEAVES.get(),
                    ModBlocks.RESOURCES_ACACIA_LEAVES.get(),
                    ModBlocks.RESOURCES_DARK_OAK_LEAVES.get(),
                    ModBlocks.RESOURCES_CHERRY_LEAVES.get(),
                    ModBlocks.RESOURCES_PALE_OAK_LEAVES.get()
            );
        }

        @SubscribeEvent
        public static void onEntityRenderersRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.TREE_SIMULATOR_BE.get(), TreeSimulatorBlockEntityRenderer::new);
        }
    }
}

package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.block.entity.custom.TreeSimulatorBlockEntity;
import com.coolerpromc.resourcestrees.compat.treeharvester.TreeHarvesterCompat;
import com.coolerpromc.resourcestrees.network.packet.ResourceTypeSyncS2CPacket;
import com.coolerpromc.resourcestrees.platform.NeoForgeRegistryHelper;
import com.coolerpromc.resourcestrees.platform.Services;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.HashSet;

@Mod(Constants.MODID)
public class ResourcesTrees {
    public ResourcesTrees(IEventBus modEventBus, ModContainer modContainer) {
        NeoForgeRegistryHelper.register(modEventBus);
        CommonClass.init();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onRegisterPayloadHandlers);
        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            TreeSimulatorBlockEntity.CONFIG.load();
            // Add custom Resources Leaves and Sapling to Resources Type BE if added via KubeJS
            if (!CommonClass.blocks().isEmpty()){
                ModBlockEntities.RESOURCES_TYPE_BE.get().validBlocks = new HashSet<>(ModBlockEntities.RESOURCES_TYPE_BE.get().validBlocks);
                ModBlockEntities.RESOURCES_TYPE_BE.get().validBlocks.addAll(CommonClass.blocks());
            }
        });
    }

    private void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(ResourceTypeSyncS2CPacket.TYPE, ResourceTypeSyncS2CPacket.STREAM_CODEC, (packet, context) -> {
            context.enqueueWork(() -> packet.handleOnClient(context.player().level()));
        });
    }

    @SubscribeEvent
    public void onOnDatapackSync(OnDatapackSyncEvent event) {
        event.sendRecipes(ModRecipes.TREE_SIMULATOR_TYPE.get());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!Services.PLATFORM.isModLoaded("treeharvester")) return;
        Entity entity = event.getEntity();
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!(entity instanceof ItemEntity itemEntity)) return;

        level.getServer().schedule(new TickTask(5, () -> TreeHarvesterCompat.handleItemEntitySpawn(itemEntity, level)));
    }

    public static Identifier id(String path){
        return Constants.id(path);
    }
}

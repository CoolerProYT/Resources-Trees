package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.block.entity.custom.TreeSimulatorBlockEntity;
import com.coolerpromc.resourcestrees.platform.NeoForgeRegistryHelper;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

@Mod(Constants.MODID)
public class ResourcesTrees {
    public ResourcesTrees(IEventBus modEventBus, ModContainer modContainer) {
        NeoForgeRegistryHelper.register(modEventBus);
        CommonClass.init();

        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(TreeSimulatorBlockEntity.CONFIG::load);
    }

    @SubscribeEvent
    public void onOnDatapackSync(OnDatapackSyncEvent event) {
        event.sendRecipes(ModRecipes.TREE_SIMULATOR_TYPE.get());
    }

    public static Identifier id(String path){
        return Constants.id(path);
    }
}

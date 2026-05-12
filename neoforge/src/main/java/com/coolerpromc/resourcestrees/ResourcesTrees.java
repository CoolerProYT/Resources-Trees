package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.platform.NeoForgeRegistryHelper;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@Mod(Constants.MODID)
public class ResourcesTrees {
    public ResourcesTrees(IEventBus modEventBus, ModContainer modContainer) {
        NeoForgeRegistryHelper.register(modEventBus);
        CommonClass.init();

        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::onAddPackFinders);
    }

    @SubscribeEvent
    public void onOnDatapackSync(OnDatapackSyncEvent event) {
        event.sendRecipes(ModRecipes.TREE_SIMULATOR_TYPE.get());
    }

    public void onAddPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.SERVER_DATA) return;
        event.addRepositorySource(consumer -> consumer.accept(Constants.getInMemoryDataPack()));
    }

    @Deprecated
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        event.getEntity().sendSystemMessage(
                Component.literal("[Resources Trees] ")
                        .withStyle(ChatFormatting.GOLD)
                        .append(Component.literal("Resources Type datapacks no longer work from 26.1.2.100 and are only kept for legacy migration. Please see the wiki for the new config-based system.").withStyle(ChatFormatting.YELLOW))
        );
    }
}

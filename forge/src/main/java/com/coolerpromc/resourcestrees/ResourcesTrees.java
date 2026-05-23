package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.client.tint.ResourcesTypeTintSource;
import com.coolerpromc.resourcestrees.platform.ForgeRegistryHelper;
import com.coolerpromc.resourcestrees.platform.util.BlockRegistryHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static com.coolerpromc.resourcestrees.Constants.MODID;

@Mod(Constants.MODID)
public class ResourcesTrees {
    public ResourcesTrees(FMLJavaModLoadingContext context) {
        ForgeRegistryHelper.register(context.getModBusGroup());
        CommonClass.init();

        AddPackFindersEvent.BUS.addListener(this::onAddPackFinders);
        PlayerEvent.PlayerLoggedInEvent.BUS.addListener(this::onPlayerLoggedIn);

        ItemTintSources.ID_MAPPER.put(Identifier.fromNamespaceAndPath(MODID, "resources_type_tint"), ResourcesTypeTintSource.MAP_CODEC);

        FMLCommonSetupEvent.getBus(context.getModBusGroup()).addListener(e -> e.enqueueWork(() -> {
            for (BlockRegistryHandler<? extends Block> block : ModBlocks.SAPLINGS){
                ComposterBlock.COMPOSTABLES.put(block.asItem(), 0.3F);
            }

            for (BlockRegistryHandler<? extends Block> block : ModBlocks.LEAVES){
                ComposterBlock.COMPOSTABLES.put(block.asItem(), 0.3F);
            }
        }));
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
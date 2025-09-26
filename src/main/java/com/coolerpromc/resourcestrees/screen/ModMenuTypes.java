package com.coolerpromc.resourcestrees.screen;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.screen.custom.TreeSimulatorMenu;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;

public class ModMenuTypes {
    public static final ScreenHandlerType<TreeSimulatorMenu> TREE_SIMULATOR = registerMenu("tree_simulator", TreeSimulatorMenu::new);

    public static <T extends ScreenHandler> ScreenHandlerType<T> registerMenu(String name, ExtendedScreenHandlerType.ExtendedFactory<T, BlockPos> menuType){
        return Registry.register(Registries.SCREEN_HANDLER, ResourcesTrees.id(name), new ExtendedScreenHandlerType<>(menuType, BlockPos.PACKET_CODEC));
    }

    public static void register(){
        ResourcesTrees.LOGGER.info("Registering menu types.");
    }
}

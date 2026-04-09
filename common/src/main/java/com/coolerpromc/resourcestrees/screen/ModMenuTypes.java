package com.coolerpromc.resourcestrees.screen;

import com.coolerpromc.resourcestrees.platform.Services;
import com.coolerpromc.resourcestrees.platform.util.RegistryHandler;
import com.coolerpromc.resourcestrees.screen.custom.TreeSimulatorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;

public class ModMenuTypes {
    public static final RegistryHandler<MenuType<TreeSimulatorMenu>> TREE_SIMULATOR = Services.REGISTRY.registerMenu("tree_simulator", TreeSimulatorMenu::new, BlockPos.STREAM_CODEC);

    public static void init() {
        // Force class loading to trigger static initializers
    }
}

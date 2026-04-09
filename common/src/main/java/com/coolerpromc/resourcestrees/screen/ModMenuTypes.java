package com.coolerpromc.resourcestrees.screen;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.screen.custom.TreeSimulatorMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, ResourcesTrees.MODID);

    public static final Supplier<MenuType<TreeSimulatorMenu>> TREE_SIMULATOR = registerMenu("tree_simulator", TreeSimulatorMenu::new);

    public static <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerMenu(String name, IContainerFactory<T> menuType){
        return MENUS.register(name, () -> IMenuTypeExtension.create(menuType));
    }

    public static void register(IEventBus eventBus){
        MENUS.register(eventBus);
    }
}

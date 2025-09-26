package com.coolerpromc.resourcestrees.screen;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.screen.custom.TreeSimulatorMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, ResourcesTrees.MODID);

    public static final Supplier<MenuType<TreeSimulatorMenu>> TREE_SIMULATOR = registerMenu("tree_simulator", TreeSimulatorMenu::new);

    public static <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerMenu(String name, IContainerFactory<T> menuType){
        return MENUS.register(name, () -> IForgeMenuType.create(menuType));
    }

    public static void register(IEventBus eventBus){
        MENUS.register(eventBus);
    }
}

package com.coolerpromc.resourcestrees;

import com.coolerpromc.resourcestrees.api.IResourcesTreesPlugin;
import com.coolerpromc.resourcestrees.api.ResourcesTreesPlugin;
import com.coolerpromc.resourcestrees.platform.NeoForgeRegistryHelper;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import net.minecraft.server.packs.PackType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mod(Constants.MODID)
public class NeoForgeResourcesTrees {
    private static final Type TYPE = Type.getType(ResourcesTreesPlugin.class);
    public static final List<IResourcesTreesPlugin> PLUGINS = new ArrayList<>();

    public NeoForgeResourcesTrees(IEventBus modEventBus, ModContainer modContainer) {
        List<ModFileScanData.AnnotationData> annotations = ModList.get().getAllScanData().stream().map(ModFileScanData::getAnnotations).flatMap(Collection::stream).filter(a -> TYPE.equals(a.annotationType())).toList();

        for (ModFileScanData.AnnotationData annotationData : annotations){
            String targetClassName = annotationData.memberName();

            try {
                Class<?> targetClass = Class.forName(targetClassName, false, IResourcesTreesPlugin.class.getClassLoader());

                if (IResourcesTreesPlugin.class.isAssignableFrom(targetClass)) {
                    IResourcesTreesPlugin plugin = (IResourcesTreesPlugin) targetClass.getDeclaredConstructor().newInstance();
                    PLUGINS.add(plugin);
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        NeoForgeRegistryHelper.register(modEventBus);
        ResourcesTrees.init();

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
}

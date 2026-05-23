package com.coolerpromc.resourcestrees.platform;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.item.custom.ModBlockItem;
import com.coolerpromc.resourcestrees.platform.services.IRegistryHelper;
import com.coolerpromc.resourcestrees.platform.util.BlockEntityTypeFactory;
import com.coolerpromc.resourcestrees.platform.util.BlockRegistryHandler;
import com.coolerpromc.resourcestrees.platform.util.MenuFactory;
import com.coolerpromc.resourcestrees.platform.util.RegistryHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class ForgeRegistryHelper implements IRegistryHelper {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MODID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Constants.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Constants.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Constants.MODID);
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Constants.MODID);

    @Override
    public <T extends Block> BlockRegistryHandler<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> func, BlockBehaviour.Properties p) {
        RegistryObject<T> block = BLOCKS.register(name, () -> func.apply(p.setId(IRegistryHelper.blockKey(name))));
        registerItem(name, properties -> new ModBlockItem(block.get(), properties.useBlockDescriptionPrefix()));

        return new BlockRegistryHandler<>() {
            @Override
            public Identifier id() {
                return block.getId();
            }

            @Override
            public Holder<T> holder() {
                return block.getHolder().orElse(null);
            }

            @Override
            public T get() {
                return block.get();
            }

            @Override
            public Item asItem() {
                return get().asItem();
            }
        };
    }

    @Override
    public <T extends Item> RegistryHandler<T> registerItem(String name, Function<Item.Properties, T> func) {
        RegistryObject<T> item = ITEMS.register(name, () -> func.apply(new Item.Properties().setId(IRegistryHelper.itemKey(name))));

        return new RegistryHandler<T>() {
            @Override
            public Identifier id() {
                return item.getId();
            }

            @Override
            public Holder<T> holder() {
                return item.getHolder().orElse(null);
            }

            @Override
            public T get() {
                return item.get();
            }
        };
    }

    @Override
    public <T extends BlockEntity> RegistryHandler<BlockEntityType<T>> registerBlockEntity(String name, BlockEntityTypeFactory<T> factory, Supplier<? extends Block>... blocks) {
        RegistryObject<BlockEntityType<T>> blockEntity = BLOCK_ENTITIES.register(name, () -> new BlockEntityType<>(factory::create, Arrays.stream(blocks).map(Supplier::get).collect(Collectors.toSet())));

        return new RegistryHandler<>() {
            @Override
            public Identifier id() {
                return blockEntity.getId();
            }

            @Override
            public Holder<BlockEntityType<T>> holder() {
                return blockEntity.getHolder().orElse(null);
            }

            @Override
            public BlockEntityType<T> get() {
                return blockEntity.get();
            }
        };
    }

    @Override
    public RegistryHandler<CreativeModeTab> registerCreativeTab(String name, Supplier<ItemStack> icon, Component title, Function<CreativeModeTab.ItemDisplayParameters, ItemStack[]> func) {
        RegistryObject<CreativeModeTab> tab = CREATIVE_TABS.register(name, () -> CreativeModeTab.builder().icon(icon).title(title).displayItems(((param, output) -> Arrays.stream(func.apply(param)).forEach(output::accept))).build());

        return new RegistryHandler<>() {
            @Override
            public Identifier id() {
                return tab.getId();
            }

            @Override
            public Holder<CreativeModeTab> holder() {
                return tab.getHolder().orElse(null);
            }

            @Override
            public CreativeModeTab get() {
                return tab.get();
            }
        };
    }

    @Override
    public <T extends AbstractContainerMenu, D> RegistryHandler<MenuType<T>> registerMenu(String name, MenuFactory<T, D> factory, StreamCodec<? super ByteBuf, D> data) {
        RegistryObject<MenuType<T>> menu = MENUS.register(name, () -> IForgeMenuType.create((id, inv, buf) -> factory.create(id, inv, data.decode(buf))));

        return new RegistryHandler<>() {
            @Override
            public Identifier id() {
                return menu.getId();
            }

            @Override
            public Holder<MenuType<T>> holder() {
                return menu.getHolder().orElse(null);
            }

            @Override
            public MenuType<T> get() {
                return menu.get();
            }
        };
    }

    @Override
    public <T extends Recipe<?>> RegistryHandler<RecipeSerializer<T>> registerRecipeSerializer(String name, RecipeSerializer<T> serializer) {
        RegistryObject<RecipeSerializer<T>> holder = RECIPE_SERIALIZERS.register(name, () -> serializer);

        return new RegistryHandler<>() {
            @Override
            public Identifier id() {
                return holder.getId();
            }

            @Override
            public Holder<RecipeSerializer<T>> holder() {
                return holder.getHolder().orElse(null);
            }

            @Override
            public RecipeSerializer<T> get() {
                return holder.get();
            }
        };
    }

    @Override
    public <T extends Recipe<?>> RegistryHandler<RecipeType<T>> registerRecipeType(String name) {
        RegistryObject<RecipeType<T>> holder = RECIPE_TYPES.register(name, () -> RecipeType.simple(Constants.id(name)));

        return new RegistryHandler<>() {
            @Override
            public Identifier id() {
                return holder.getId();
            }

            @Override
            public Holder<RecipeType<T>> holder() {
                return holder.getHolder().orElse(null);
            }

            @Override
            public RecipeType<T> get() {
                return holder.get();
            }
        };
    }

    @Override
    public <T> RegistryHandler<DataComponentType<T>> registerDataComponent(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        RegistryObject<DataComponentType<T>> component = DATA_COMPONENTS.register(name, () -> builder.apply(DataComponentType.builder()).build());

        return new RegistryHandler<>() {
            @Override
            public Identifier id() {
                return component.getId();
            }

            @Override
            public Holder<DataComponentType<T>> holder() {
                return component.getHolder().orElse(null);
            }

            @Override
            public DataComponentType<T> get() {
                return component.get();
            }
        };
    }

    public static void register(BusGroup eventBus){
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
        CREATIVE_TABS.register(eventBus);
        MENUS.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);
        DATA_COMPONENTS.register(eventBus);
    }
}

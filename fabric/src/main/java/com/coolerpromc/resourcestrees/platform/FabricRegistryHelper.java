package com.coolerpromc.resourcestrees.platform;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.platform.services.IRegistryHelper;
import com.coolerpromc.resourcestrees.platform.util.BlockEntityTypeFactory;
import com.coolerpromc.resourcestrees.platform.util.BlockRegistryHandler;
import com.coolerpromc.resourcestrees.platform.util.MenuFactory;
import com.coolerpromc.resourcestrees.platform.util.RegistryHandler;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
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

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class FabricRegistryHelper implements IRegistryHelper {
    @Override
    public <T extends Block> BlockRegistryHandler<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> func, BlockBehaviour.Properties p) {
        ResourceKey<Block> key = IRegistryHelper.blockKey(name);
        Identifier id = key.identifier();
        Holder<T> holder = Registry.registerForHolder(BuiltInRegistries.BLOCK, id, func.apply(p.setId(key)));
        Item item = registerItem(name, properties -> new BlockItem(holder.value(), properties.useBlockDescriptionPrefix())).get();

        return new BlockRegistryHandler<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public Holder<T> holder() {
                return holder;
            }

            @Override
            public T get() {
                return holder.value();
            }

            @Override
            public Item asItem() {
                return item;
            }
        };
    }

    @Override
    public <T extends Item> RegistryHandler<T> registerItem(String name, Function<Item.Properties, T> func) {
        ResourceKey<Item> key = IRegistryHelper.itemKey(name);
        Identifier id = key.identifier();
        Holder<T> holder = Registry.registerForHolder(BuiltInRegistries.ITEM, id, func.apply(new Item.Properties().setId(key)));

        return new RegistryHandler<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public Holder<T> holder() {
                return holder;
            }

            @Override
            public T get() {
                return holder.value();
            }
        };
    }

    @Override
    public <T extends BlockEntity> RegistryHandler<BlockEntityType<T>> registerBlockEntity(String name, BlockEntityTypeFactory<T> factory, Supplier<? extends Block>... blocks) {
        Identifier id = Constants.id(name);
        Holder<BlockEntityType<T>> holder = Registry.registerForHolder(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.create(factory::create, Arrays.stream(blocks).map(Supplier::get).toArray(Block[]::new)).build());

        return new RegistryHandler<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public Holder<BlockEntityType<T>> holder() {
                return holder;
            }

            @Override
            public BlockEntityType<T> get() {
                return holder.value();
            }
        };
    }

    @Override
    public RegistryHandler<CreativeModeTab> registerCreativeTab(String name, Supplier<ItemStack> icon, Component title, Function<CreativeModeTab.ItemDisplayParameters, ItemStack[]> func) {
        Identifier id = Constants.id(name);
        Holder<CreativeModeTab> holder = Registry.registerForHolder(BuiltInRegistries.CREATIVE_MODE_TAB, id, FabricCreativeModeTab.builder().icon(icon).title(title).displayItems((parameters, output) -> Arrays.stream(func.apply(parameters)).forEach(output::accept)).build());

        return new RegistryHandler<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public Holder<CreativeModeTab> holder() {
                return holder;
            }

            @Override
            public CreativeModeTab get() {
                return holder.value();
            }
        };
    }

    @Override
    public <T extends AbstractContainerMenu, D> RegistryHandler<MenuType<T>> registerMenu(String name, MenuFactory<T, D> factory, StreamCodec<? super RegistryFriendlyByteBuf, D> data) {
        Identifier id = Constants.id(name);
        Holder<MenuType<T>> holder = Registry.registerForHolder(BuiltInRegistries.MENU, id, new ExtendedMenuType<>(factory::create, data));

        return new RegistryHandler<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public Holder<MenuType<T>> holder() {
                return holder;
            }

            @Override
            public MenuType<T> get() {
                return holder.value();
            }
        };
    }

    @Override
    public <T extends Recipe<?>> RegistryHandler<RecipeSerializer<T>> registerRecipeSerializer(String name, RecipeSerializer<T> serializer) {
        Identifier id = Constants.id(name);
        Holder<RecipeSerializer<T>> holder = Registry.registerForHolder(BuiltInRegistries.RECIPE_SERIALIZER, id, serializer);

        return new RegistryHandler<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public Holder<RecipeSerializer<T>> holder() {
                return holder;
            }

            @Override
            public RecipeSerializer<T> get() {
                return holder.value();
            }
        };
    }

    @Override
    public <T extends Recipe<?>> RegistryHandler<RecipeType<T>> registerRecipeType(String name) {
        Identifier id = Constants.id(name);
        Holder<RecipeType<T>> holder = Registry.registerForHolder(BuiltInRegistries.RECIPE_TYPE, id, new RecipeType<T>() {
            @Override
            public String toString() {
                return id.toString();
            }
        });

        return new RegistryHandler<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public Holder<RecipeType<T>> holder() {
                return holder;
            }

            @Override
            public RecipeType<T> get() {
                return holder.value();
            }
        };
    }

    @Override
    public <T> RegistryHandler<DataComponentType<T>> registerDataComponent(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        Identifier id = Constants.id(name);
        Holder<DataComponentType<T>> holder = Registry.registerForHolder(BuiltInRegistries.DATA_COMPONENT_TYPE, id, builder.apply(DataComponentType.builder()).build());

        return new RegistryHandler<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public Holder<DataComponentType<T>> holder() {
                return holder;
            }

            @Override
            public DataComponentType<T> get() {
                return holder.value();
            }
        };
    }
}

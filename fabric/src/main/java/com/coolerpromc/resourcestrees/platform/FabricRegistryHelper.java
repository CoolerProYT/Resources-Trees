package com.coolerpromc.resourcestrees.platform;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.item.custom.ModBlockItem;
import com.coolerpromc.resourcestrees.platform.services.IRegistryHelper;
import com.coolerpromc.resourcestrees.platform.util.BlockEntityTypeFactory;
import com.coolerpromc.resourcestrees.platform.util.BlockRegistryHandler;
import com.coolerpromc.resourcestrees.platform.util.MenuFactory;
import com.coolerpromc.resourcestrees.platform.util.RegistryHandler;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class FabricRegistryHelper implements IRegistryHelper {
    @Override
    public <T extends Block> BlockRegistryHandler<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> func, BlockBehaviour.Properties p) {
        ResourceKey<Block> key = IRegistryHelper.blockKey(name);
        Identifier id = key.identifier();
        Holder<T> holder = Registry.registerForHolder(BuiltInRegistries.BLOCK, id, func.apply(p.setId(key)));
        Item item = registerItem(name, properties -> new ModBlockItem(holder.value(), properties.useBlockDescriptionPrefix())).get();

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
    public Ingredient createCustomIngredient(HolderSet<Item> items, DataComponentPatch components, boolean exhaustive) {
        return new FabricResourcesTypeIngredient(items, components, exhaustive).toVanilla();
    }

    @Override
    public boolean isResourcesTypeIngredient(Ingredient ingredient) {
        return ingredient.getCustomIngredient() instanceof FabricResourcesTypeIngredient;
    }

    /**
     * Fabric-specific CustomIngredient wrapper for ResourcesTypeIngredient.
     */
    public static class FabricResourcesTypeIngredient implements CustomIngredient {
        public static final CustomIngredientSerializer<FabricResourcesTypeIngredient> SERIALIZER =
                new CustomIngredientSerializer<>() {
                    @Override
                    public Identifier getIdentifier() {
                        return Constants.id("resources_type");
                    }

                    @Override
                    public MapCodec<FabricResourcesTypeIngredient> getCodec() {
                        return com.coolerpromc.resourcestrees.recipe.ingredient.ResourcesTypeIngredient.CODEC.xmap(
                                ri -> new FabricResourcesTypeIngredient(ri.base(), ri.components(), ri.exhaustive()),
                                fi -> new com.coolerpromc.resourcestrees.recipe.ingredient.ResourcesTypeIngredient(fi.base, fi.components, fi.exhaustive));
                    }

                    @Override
                    public StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, FabricResourcesTypeIngredient> getStreamCodec() {
                        return StreamCodec.composite(
                                net.minecraft.network.codec.ByteBufCodecs.holderSet(net.minecraft.core.registries.Registries.ITEM), fi -> fi.base,
                                DataComponentPatch.STREAM_CODEC, fi -> fi.components,
                                net.minecraft.network.codec.ByteBufCodecs.BOOL, fi -> fi.exhaustive,
                                FabricResourcesTypeIngredient::new
                        );
                    }
                };

        private final HolderSet<Item> base;
        private final DataComponentPatch components;
        private final boolean exhaustive;

        public FabricResourcesTypeIngredient(HolderSet<Item> base, DataComponentPatch components, boolean exhaustive) {
            this.base = base;
            this.components = components;
            this.exhaustive = exhaustive;
        }

        private com.coolerpromc.resourcestrees.recipe.ingredient.ResourcesTypeIngredient toCommon() {
            return new com.coolerpromc.resourcestrees.recipe.ingredient.ResourcesTypeIngredient(base, components, exhaustive);
        }

        @Override
        public boolean test(ItemStack stack) {
            return toCommon().test(stack);
        }

        @Override
        public Stream<Holder<Item>> items() {
            return toCommon().items();
        }

        @Override
        public boolean requiresTesting() {
            return true;
        }

        @Override
        public CustomIngredientSerializer<?> getSerializer() {
            return SERIALIZER;
        }

        @Override
        public SlotDisplay display() {
            return toCommon().display();
        }
    }
}

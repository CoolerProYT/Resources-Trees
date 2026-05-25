package com.coolerpromc.resourcestrees.platform.services;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.platform.util.BlockEntityTypeFactory;
import com.coolerpromc.resourcestrees.platform.util.BlockRegistryHandler;
import com.coolerpromc.resourcestrees.platform.util.MenuFactory;
import com.coolerpromc.resourcestrees.platform.util.RegistryHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface IRegistryHelper {
    <T extends Block> BlockRegistryHandler<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> func, BlockBehaviour.Properties p);
    <T extends Item> RegistryHandler<T> registerItem(String name, Function<Item.Properties, T> func);
    <T extends BlockEntity> RegistryHandler<BlockEntityType<T>> registerBlockEntity(String name, BlockEntityTypeFactory<T> factory, Supplier<? extends Block>... blocks);
    RegistryHandler<CreativeModeTab> registerCreativeTab(String name, Supplier<ItemStack> icon, Component title, Function<CreativeModeTab.ItemDisplayParameters, ItemStack[]> func);
    <T extends AbstractContainerMenu, D> RegistryHandler<MenuType<T>> registerMenu(String name, MenuFactory<T, D> factory, StreamCodec<? super RegistryFriendlyByteBuf, D> data);
    <T extends Recipe<?>> RegistryHandler<RecipeSerializer<T>> registerRecipeSerializer(String name, RecipeSerializer<T> serializer);
    <T extends Recipe<?>> RegistryHandler<RecipeType<T>> registerRecipeType(String name);
    @Deprecated(forRemoval = true)
    <T> RegistryHandler<DataComponentType<T>> registerDataComponent(String name, UnaryOperator<DataComponentType.Builder<T>> builder);

    static ResourceKey<Block> blockKey(String name) {
        return ResourceKey.create(Registries.BLOCK, Constants.id(name));
    }
    static ResourceKey<Item> itemKey(String name) {
        return ResourceKey.create(Registries.ITEM, Constants.id(name));
    }
}
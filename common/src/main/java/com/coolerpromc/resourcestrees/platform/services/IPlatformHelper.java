package com.coolerpromc.resourcestrees.platform.services;

import com.coolerpromc.resourcestrees.api.IResourcesTreesPlugin;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.Item;

import java.nio.file.Path;
import java.util.List;
import java.util.ServiceLoader;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }

    /**
     * Gets the config directory for the current platform.
     *
     * @return The path to the config directory.
     */
    Path getConfigDir();

    /**
     * Opens a menu with extra data (BlockPos) sent to the client.
     */
    void openMenu(ServerPlayer player, MenuProvider provider, BlockPos pos);

    /**
     * Sends a custom packet payload to all connected players.
     */
    void sendToAllPlayers(CustomPacketPayload packet, ServerLevel level);

    TagKey<Item> getShearTag();

    List<IResourcesTreesPlugin> plugins();
}
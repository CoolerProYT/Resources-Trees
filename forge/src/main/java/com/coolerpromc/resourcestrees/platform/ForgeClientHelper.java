package com.coolerpromc.resourcestrees.platform;

import com.coolerpromc.resourcestrees.platform.services.IClientHelper;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import org.joml.Matrix4fc;

import java.util.List;

public class ForgeClientHelper implements IClientHelper {
    @Override
    public CuboidItemModelWrapper cuboidItemModelWrapper(List<ItemTintSource> tints, QuadCollection quads, ModelRenderProperties properties, Matrix4fc transformation) {
        return new CuboidItemModelWrapper(tints, quads, properties, transformation);
    }
}

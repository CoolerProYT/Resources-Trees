package com.coolerpromc.resourcestrees.mixin;

import com.coolerpromc.resourcestrees.api.tree.TreeTypes;
import com.coolerpromc.resourcestrees.client.pack.ResourcesTreesModelPack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.world.flag.FeatureFlagSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

@Mixin(FolderRepositorySource.class)
public class FolderRepositorySourceMixin {
	@Inject(method = "loadPacks", at = @At("HEAD"))
	public void register(Consumer<Pack> result, CallbackInfo ci) throws ExecutionException, InterruptedException {
		ResourcesTreesModelPack pack = new ResourcesTreesModelPack();
		TreeTypes.getTypes().forEach(treeType -> {
			pack.addLeavesModel(treeType.leavesTexture());
			pack.addSaplingBlockModel(treeType.saplingTexture());
			pack.addSaplingItemModel(treeType.saplingTexture());
		});

		Pack p = new Pack(
				pack.location(),
				new Pack.ResourcesSupplier() {
					@Override
					public PackResources openPrimary(PackLocationInfo info) { return pack; }
					@Override
					public PackResources openFull(PackLocationInfo info, Pack.Metadata meta) { return pack; }
				},
				new Pack.Metadata(pack.getDescription(), PackCompatibility.COMPATIBLE, FeatureFlagSet.of(), List.of()),
				new PackSelectionConfig(true, Pack.Position.TOP, false)
		);
		result.accept(p);
	}
}
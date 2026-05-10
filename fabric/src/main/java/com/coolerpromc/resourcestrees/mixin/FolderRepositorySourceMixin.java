package com.coolerpromc.resourcestrees.mixin;

import com.coolerpromc.resourcestrees.Constants;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.Pack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(FolderRepositorySource.class)
public class FolderRepositorySourceMixin {
	@Inject(method = "loadPacks", at = @At("HEAD"))
	public void register(Consumer<Pack> result, CallbackInfo ci) {
		result.accept(Constants.getInMemoryPack());
	}
}
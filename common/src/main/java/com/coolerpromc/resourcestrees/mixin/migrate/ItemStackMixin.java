package com.coolerpromc.resourcestrees.mixin.migrate;

import com.coolerpromc.resourcestrees.util.LegacyItemMigrator;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Deprecated(forRemoval = true)
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Mutable
    @Shadow
    @Final
    @Deprecated
    private @Nullable Holder<Item> item;

    @Shadow
    private int count;

    @Mutable
    @Shadow
    @Final
    private PatchedDataComponentMap components;

    @Inject(method = "<init>(Lnet/minecraft/core/Holder;ILnet/minecraft/core/component/PatchedDataComponentMap;)V", at = @At("RETURN"))
    private void migrateLegacyItem(Holder<Item> item, int count, PatchedDataComponentMap components, CallbackInfo ci) {
        Item newItem = LegacyItemMigrator.migrate(item, components);
        if (newItem != Items.AIR){
            this.item = newItem.builtInRegistryHolder();
            this.count = count;
            this.components = PatchedDataComponentMap.fromPatch(newItem.components(), DataComponentPatch.EMPTY);
        }
    }
}
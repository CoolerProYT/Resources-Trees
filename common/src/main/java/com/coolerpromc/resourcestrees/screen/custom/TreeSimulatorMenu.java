package com.coolerpromc.resourcestrees.screen.custom;

import com.coolerpromc.resourcestrees.block.entity.custom.TreeSimulatorBlockEntity;
import com.coolerpromc.resourcestrees.screen.ModMenuTypes;
import com.coolerpromc.resourcestrees.screen.container.OutputSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TreeSimulatorMenu extends AbstractContainerMenu {
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int TE_INVENTORY_SLOT_COUNT = 11;

    protected final ContainerData data;

    public TreeSimulatorMenu(int containerId, Inventory playerInventory, BlockPos pos) {
        this(containerId, playerInventory, blockEntityAt(playerInventory, pos));
    }

    private TreeSimulatorMenu(int containerId, Inventory playerInventory, TreeSimulatorBlockEntity be) {
        this(containerId, playerInventory, be, be.getData());
    }

    private static TreeSimulatorBlockEntity blockEntityAt(Inventory inv, BlockPos pos) {
        return (TreeSimulatorBlockEntity) inv.player.level().getBlockEntity(pos);
    }

    public TreeSimulatorMenu(int containerId, Inventory playerInventory, TreeSimulatorBlockEntity blockEntity, ContainerData data){
        super(ModMenuTypes.TREE_SIMULATOR.get(), containerId);
        this.data = data;

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addDataSlots(this.data);

        this.addSlot(new Slot(blockEntity.getInputHandler(), 0, 26, 35));

        for (int i = 0; i < blockEntity.getOutputHandler().getContainerSize(); i++){
            this.addSlot(new OutputSlot(blockEntity.getOutputHandler(), i, 98 + 18 * (i % 3), 17 + (i / 3) * 18));
        }

        this.addSlot(new Slot(blockEntity.getAxeHandler(), 0, 62, 59));
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(pPlayer, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 102 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 160));
        }
    }

    public int getCurrentProgress(){
        return data.get(0);
    }

    public int getMaxProgress(){
        return data.get(1);
    }

    public int getProgress(){
        return Math.round((((float) getCurrentProgress() / (float) getMaxProgress()) * 22));
    }
}

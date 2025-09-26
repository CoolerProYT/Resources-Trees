package com.coolerpromc.resourcestrees.screen.custom;

import com.coolerpromc.resourcestrees.block.entity.custom.TreeSimulatorBlockEntity;
import com.coolerpromc.resourcestrees.screen.ModMenuTypes;
import com.coolerpromc.resourcestrees.screen.container.OutputSlot;
import com.coolerpromc.resourcestrees.util.ExtendedSimpleInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class TreeSimulatorMenu extends ScreenHandler {
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int TE_INVENTORY_SLOT_COUNT = 11;

    protected final PropertyDelegate data;

    public TreeSimulatorMenu(int containerId, PlayerInventory playerInventory, BlockPos blockPos) {
        this(containerId, playerInventory, (TreeSimulatorBlockEntity) playerInventory.player.getWorld().getBlockEntity(blockPos), new ArrayPropertyDelegate(2));
    }

    public TreeSimulatorMenu(int containerId, PlayerInventory playerInventory, TreeSimulatorBlockEntity blockEntity, PropertyDelegate data){
        super(ModMenuTypes.TREE_SIMULATOR, containerId);
        this.data = data;

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addProperties(this.data);

        ExtendedSimpleInventory inputHandler = blockEntity.getInputHandler();
        this.addSlot(new Slot(inputHandler, 0, 26, 35));

        ExtendedSimpleInventory outputHandler = blockEntity.getOutputHandler();
        for (int i = 0; i < blockEntity.getOutputHandler().getSlots(); i ++){
            this.addSlot(new OutputSlot(outputHandler, i, 98 + 18 * (i % 3), 17 + (i / 3) * 18, itemStack -> false));
        }

        ExtendedSimpleInventory axeHandler = blockEntity.getAxeHandler();
        this.addSlot(new Slot(axeHandler, 0, 62, 59));
    }


    @Override
    public ItemStack quickMove(PlayerEntity player, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (!sourceSlot.hasStack()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getStack();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!insertItem(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            if (!insertItem(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        if (sourceStack.getCount() == 0) {
            sourceSlot.setStackNoCallbacks(ItemStack.EMPTY);
        } else {
            sourceSlot.markDirty();
        }
        sourceSlot.onTakeItem(player, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 102 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
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

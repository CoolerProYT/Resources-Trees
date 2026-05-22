package com.coolerpromc.resourcestrees.block.entity.custom;

import com.coolerpromc.resourcestrees.Constants;
import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.config.ResourcesTreesConfig;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.recipe.custom.TreeSimulatorRecipe;
import com.coolerpromc.resourcestrees.recipe.input.TreeSimulatorRecipeInput;
import com.coolerpromc.resourcestrees.recipe.output.TreeSimulatorOutput;
import com.coolerpromc.resourcestrees.screen.custom.TreeSimulatorMenu;
import com.coolerpromc.resourcestrees.util.ExtendedSimpleInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TreeSimulatorBlockEntity extends BlockEntity implements MenuProvider{
    public int growTicks = 0;
    public int maxGrowTicks = 0;

    private final ExtendedSimpleInventory inputHandler = new ExtendedSimpleInventory(1){
        @Override
        public int getMaxStackSize() {
            return 1;
        }
    };

    private final ExtendedSimpleInventory outputHandler = new ExtendedSimpleInventory(9);

    private final ExtendedSimpleInventory axeHandler = new ExtendedSimpleInventory(1){
        @Override
        public boolean canPlaceItem(int slot, ItemStack stack) {
            return stack.getItem() instanceof AxeItem;
        }

        @Override
        public void setChanged() {
            if (getItem(0).isEmpty()){
                TreeSimulatorBlockEntity.this.growTicks = 0;
                TreeSimulatorBlockEntity.this.setChanged();
                TreeSimulatorBlockEntity.this.level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
            }
        }
    };

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int i) {
            return switch (i){
                case 0 -> growTicks;
                case 1 -> maxGrowTicks;
                default -> 0;
            };
        }

        @Override
        public void set(int i, int value) {
            switch (i){
                case 0 -> growTicks = value;
                case 1 -> maxGrowTicks = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public TreeSimulatorBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.TREE_SIMULATOR_BE.get(), pos, blockState);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Tree Simulator");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new TreeSimulatorMenu(syncId, playerInventory, this, this.data);
    }

    @Override
    protected void saveAdditional(ValueOutput view) {
        super.saveAdditional(view);
        ContainerHelper.saveAllItems(view.child("input"), inputHandler.getItems());
        ContainerHelper.saveAllItems(view.child("output"), outputHandler.getItems());
        ContainerHelper.saveAllItems(view.child("axe"), axeHandler.getItems());
        view.putInt("growTicks", growTicks);
        view.putInt("maxGrowTicks", maxGrowTicks);
    }

    @Override
    protected void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        ContainerHelper.loadAllItems(view.childOrEmpty("input"), inputHandler.getItems());
        ContainerHelper.loadAllItems(view.childOrEmpty("output"), outputHandler.getItems());
        ContainerHelper.loadAllItems(view.childOrEmpty("axe"), axeHandler.getItems());
        this.data.set(0, view.getIntOr("growTicks", 0));
        this.data.set(1, view.getIntOr("maxGrowTicks", 0));
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    public void tick(Level level, BlockPos pos, BlockState state){
        if (level.isClientSide()) return;
        setGrowTick(level);

        if (hasRecipe()){
            increaseGrowTicks();

            if (treeGrown()){
                harvest(level);
                resetGrowTicks();
            }
        }
        else {
            resetGrowTicks();
        }
    }

    private void harvest(Level level){
        Optional<RecipeHolder<TreeSimulatorRecipe>> recipe = getCurrentRecipe();
        if (recipe.isPresent()) {
            List<ItemStack> results = new ArrayList<>();

            recipe.get().value().drops().forEach(output -> {
                int rolls = output.getRolls(level.getRandom());
                for (int i = 0; i < rolls; i++){
                    if (level.getRandom().nextFloat() < output.chance()){
                        results.add(output.output().create());
                    }
                }
            });

            if (!isAxeUnbreakable()){
                ItemStack axe = getAxe();
                axe.hurtAndBreak(1, (ServerLevel) level, null, _ -> {});
            }

            for (ItemStack result : results) {
                int outputSlot = findSuitableOutputSlot(result);
                if (outputSlot != -1) {
                    this.outputHandler.insertItem(outputSlot, result, false);
                } else {
                    Constants.LOG.warn("No suitable output slot found for item: {} at {}", result, getBlockPos());
                }
            }

            setChanged();
        }
    }

    private boolean treeGrown(){
        return growTicks >= maxGrowTicks && maxGrowTicks > 0;
    }

    private void increaseGrowTicks(){
        this.growTicks++;
        setChanged();
        if (growTicks % 5 == 0) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
        }
    }

    private void resetGrowTicks(){
        boolean wasZero = this.growTicks == 0;
        this.growTicks = 0;
        setChanged();
        if (!wasZero){
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
        }
    }

    private void setMaxGrowTicks(int tick){
        this.maxGrowTicks = tick;
    }

    private int findSuitableOutputSlot(ItemStack result) {
        for (int i = 0; i < this.outputHandler.getSlots(); i++) {
            ItemStack stackInSlot = this.outputHandler.getItem(i);
            if (stackInSlot.isEmpty() || (ItemStack.isSameItemSameComponents(stackInSlot, result) && stackInSlot.getCount() + result.getCount() <= stackInSlot.getMaxStackSize())) {
                return i;
            }
        }
        return -1;
    }

    private boolean hasRecipe() {
        Optional<RecipeHolder<TreeSimulatorRecipe>> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) {
            return false;
        }

        List<ItemStack> results = recipe.get().value().drops().stream().map(TreeSimulatorOutput::output).map(ItemStackTemplate::create).toList();

        for (ItemStack result : results) {
            if (!canInsertAmountIntoOutputSlot(result) || !canInsertItemIntoOutputSlot(result)) {
                return false;
            }
        }

        return checkSlot(results);
    }

    private boolean checkSlot(List<ItemStack> results){
        int count = results.size();
        int emptyCount = 0;

        for (int i = 0; i < this.outputHandler.getSlots(); i++) {
            ItemStack stackInSlot = this.outputHandler.getItem(i);
            if(!stackInSlot.isEmpty()){
                for (ItemStack result : results){
                    if(stackInSlot.getItem() == result.getItem()){
                        if(stackInSlot.getCount() + result.getCount() <= 64){
                            emptyCount++;
                        }
                    }
                }
            }
            else {
                emptyCount++;
            }
        }

        return emptyCount >= count;
    }

    private boolean canInsertAmountIntoOutputSlot(ItemStack result) {
        for (int i = 0; i < this.outputHandler.getSlots(); i++) {
            ItemStack stackInSlot = this.outputHandler.getItem(i);
            if (stackInSlot.isEmpty() || (ItemStack.isSameItemSameComponents(stackInSlot, result) && stackInSlot.getCount() + result.getCount() <= stackInSlot.getMaxStackSize())) {
                return true;
            }
        }
        return false;
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack item) {
        for (int i = 0; i < this.outputHandler.getSlots(); i++) {
            ItemStack stackInSlot = this.outputHandler.getItem(i);
            if (stackInSlot.isEmpty() || ItemStack.isSameItemSameComponents(stackInSlot, item)) {
                return true;
            }
        }
        return false;
    }

    private Optional<RecipeHolder<TreeSimulatorRecipe>> getCurrentRecipe(){
        if (level instanceof ServerLevel serverLevel){
            return serverLevel.recipeAccess().getRecipeFor(ModRecipes.TREE_SIMULATOR_TYPE.get(), new TreeSimulatorRecipeInput(getSapling()), serverLevel);
        }
        return Optional.empty();
    }

    public ExtendedSimpleInventory getInputHandler() {
        return inputHandler;
    }

    public ExtendedSimpleInventory getOutputHandler() {
        return outputHandler;
    }

    public ItemStack getSapling(){
        return inputHandler.getItem(0);
    }

    public ContainerData getData() {
        return data;
    }

    public ExtendedSimpleInventory getAxeHandler() {
        return axeHandler;
    }

    public ItemStack getAxe(){
        return axeHandler.getItem(0);
    }

    public boolean isAxeUnbreakable(){
        return getAxe().has(DataComponents.UNBREAKABLE);
    }

    private void setGrowTick(Level level){
        Optional<RecipeHolder<TreeSimulatorRecipe>> recipeHolder = getCurrentRecipe();
        if (recipeHolder.isPresent() && isAxeValid()){
            TreeSimulatorRecipe recipe = recipeHolder.get().value();
            int tick = recipe.ticksToGrow();
            double GROW_TICK_BY_AXE = ResourcesTreesConfig.get(getAxe().typeHolder().getRegisteredName());
            if (GROW_TICK_BY_AXE != 0.0){
                tick = (int) (recipe.ticksToGrow() / GROW_TICK_BY_AXE);
            }

            int efficiencyLevel = getAxe().getEnchantments().getLevel(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.EFFICIENCY));

            if (efficiencyLevel > 0){
                tick = (int) (tick / (1.0 + efficiencyLevel * 0.2));
            }

            setMaxGrowTicks(tick);
        }
        else{
            setMaxGrowTicks(-1);
        }
    }

    private boolean isAxeValid() {
        return axeHandler.getItem(0).getItem() instanceof AxeItem;
    }

    public Container getHandlerForSide(@org.jspecify.annotations.Nullable Direction direction) {
        if (direction == Direction.DOWN) return outputHandler;
        if (direction == Direction.UP) return inputHandler;
        return axeHandler;
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        SimpleContainer container = new SimpleContainer(11);
        container.addItem(inputHandler.getItem(0));
        for (int i = 0; i < outputHandler.getSlots();i ++){
            container.addItem(outputHandler.getItem(i));
        }
        container.addItem(axeHandler.getItem(0));

        Containers.dropContents(this.level, pos, container);
    }
}

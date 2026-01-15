package com.coolerpromc.resourcestrees.block.entity.custom;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.recipe.custom.TreeSimulatorRecipe;
import com.coolerpromc.resourcestrees.recipe.input.TreeSimulatorRecipeInput;
import com.coolerpromc.resourcestrees.recipe.output.TreeSimulatorOutput;
import com.coolerpromc.resourcestrees.screen.custom.TreeSimulatorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TreeSimulatorBlockEntity extends BlockEntity implements MenuProvider {
    public static final Map<ResourcesSaplingBlock, Item> LOG_BY_SAPLINGS = Map.of(
            ModBlocks.RESOURCES_OAK_SAPLING.get(), Items.OAK_LOG,
            ModBlocks.RESOURCES_SPRUCE_SAPLING.get(), Items.SPRUCE_LOG,
            ModBlocks.RESOURCES_BIRCH_SAPLING.get(), Items.BIRCH_LOG,
            ModBlocks.RESOURCES_JUNGLE_SAPLING.get(), Items.JUNGLE_LOG,
            ModBlocks.RESOURCES_ACACIA_SAPLING.get(), Items.ACACIA_LOG,
            ModBlocks.RESOURCES_DARK_OAK_SAPLING.get(), Items.DARK_OAK_LOG,
            ModBlocks.RESOURCES_CHERRY_SAPLING.get(), Items.CHERRY_LOG
    );

    public int growTicks = 0;
    public int maxGrowTicks = 0;

    private final ItemStackHandler inputHandler = new ItemStackHandler(1){
        @Override
        protected void onContentsChanged(int slot) {
            Optional<TreeSimulatorRecipe> recipeHolder = getCurrentRecipe();
            if (recipeHolder.isPresent()){
                TreeSimulatorRecipe recipe = recipeHolder.get();
                setMaxGrowTicks(recipe.ticksToGrow());
            }
            else{
                setMaxGrowTicks(0);
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };

    private final ItemStackHandler outputHandler = new ItemStackHandler(9);

    private final ItemStackHandler axeHandler = new ItemStackHandler(1){
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.getItem() instanceof AxeItem;
        }

        protected void onContentsChanged(int index) {
            setGrowTick();
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
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new TreeSimulatorMenu(i, inventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("input", inputHandler.serializeNBT());
        tag.put("output", outputHandler.serializeNBT());
        tag.put("axe", axeHandler.serializeNBT());
        tag.putInt("growTicks", growTicks);
        tag.putInt("maxGrowTicks", maxGrowTicks);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        inputHandler.deserializeNBT(tag.getCompound("input"));
        outputHandler.deserializeNBT(tag.getCompound("output"));
        axeHandler.deserializeNBT(tag.getCompound("axe"));
        this.data.set(0, tag.getInt("growTicks"));
        this.data.set(1, tag.getInt("maxGrowTicks"));
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        load(tag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        load(pkt.getTag());
    }

    public void tick(Level level, BlockPos pos, BlockState state){
        if (level.isClientSide()) return;

        if (hasRecipe() && isAxeValid()){
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
        Optional<TreeSimulatorRecipe> recipe = getCurrentRecipe();
        if (recipe.isPresent()) {
            List<ItemStack> results = new ArrayList<>();

            recipe.get().drops().forEach(output -> {
                int rolls = output.getRolls(level.random);
                for (int i = 0; i < rolls; i++){
                    if (level.random.nextFloat() < output.chance()){
                        results.add(output.output().copy());
                    }
                }
            });

            if (!isAxeUnbreakable()){
                ItemStack axe = getAxe();
                int damage = axe.getDamageValue();
                axe.setDamageValue(damage + 1);
            }

            for (ItemStack result : results) {
                int outputSlot = findSuitableOutputSlot(result);
                if (outputSlot != -1) {
                    this.outputHandler.insertItem(outputSlot, result, false);
                } else {
                    ResourcesTrees.LOGGER.warn("No suitable output slot found for item: {} at {}", result, getBlockPos());
                }
            }
            setChanged();
        }
    }

    private boolean treeGrown(){
        return growTicks >= maxGrowTicks;
    }

    private void increaseGrowTicks(){
        this.growTicks++;
        setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
    }

    private void resetGrowTicks(){
        this.growTicks = 0;
        setChanged();
    }

    private void setMaxGrowTicks(int tick){
        this.maxGrowTicks = tick;
    }

    private int findSuitableOutputSlot(ItemStack result) {
        for (int i = 0; i < this.outputHandler.getSlots(); i++) {
            ItemStack stackInSlot = this.outputHandler.getStackInSlot(i);
            if (stackInSlot.isEmpty() || (ResourcesTypes.isSameItemSameType(stackInSlot, result) && stackInSlot.getCount() + result.getCount() <= stackInSlot.getMaxStackSize())) {
                return i;
            }
        }
        return -1;
    }

    private boolean hasRecipe() {
        Optional<TreeSimulatorRecipe> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) {
            return false;
        }

        List<ItemStack> results = recipe.get().drops().stream().map(TreeSimulatorOutput::output).toList();

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
            ItemStack stackInSlot = this.outputHandler.getStackInSlot(i);
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
            ItemStack stackInSlot = this.outputHandler.getStackInSlot(i);
            if (stackInSlot.isEmpty() || (ResourcesTypes.isSameItemSameType(stackInSlot, result) && stackInSlot.getCount() + result.getCount() <= stackInSlot.getMaxStackSize())) {
                return true;
            }
        }
        return false;
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack item) {
        for (int i = 0; i < this.outputHandler.getSlots(); i++) {
            ItemStack stackInSlot = this.outputHandler.getStackInSlot(i);
            if (stackInSlot.isEmpty() || ResourcesTypes.isSameItemSameType(stackInSlot, item)) {
                return true;
            }
        }
        return false;
    }

    private Optional<TreeSimulatorRecipe> getCurrentRecipe(){
        if (level instanceof ServerLevel serverLevel){
            Optional<TreeSimulatorRecipe> recipe = serverLevel.getRecipeManager().getRecipeFor(ModRecipes.TREE_SIMULATOR_TYPE.get(), new TreeSimulatorRecipeInput(getSapling()), serverLevel);
            if (recipe.isPresent()){
                return recipe;
            }
            else if (Block.byItem(getSapling().getItem()) instanceof ResourcesSaplingBlock block && getSapling().hasTag() && getSapling().getTag().contains("type")){
                ResourceLocation type = new ResourceLocation(getSapling().getTag().getString("type"));
                Holder<ResourcesTypes> value = ResourcesTypes.asHolder(serverLevel, type);
                ItemStack leaf = ModItems.LEAF_FRAGMENT.get().getDefaultInstance();
                leaf.getOrCreateTag().putString("type", type.toString());
                if (value != null && value.value() != ResourcesTypes.EMPTY){
                    List<TreeSimulatorOutput> drops = new ArrayList<>();
                    drops.add(TreeSimulatorOutput.of(TreeSimulatorBlockEntity.LOG_BY_SAPLINGS.get(block).getDefaultInstance(), 0.5f, 1, 4));
                    drops.add(TreeSimulatorOutput.of(leaf, 1, 1, 1));
                    drops.add(TreeSimulatorOutput.of(leaf, value.value().leafDropChance(), 1, 4));
                    drops.add(TreeSimulatorOutput.of(getSapling(), value.value().saplingDropChance(), 1, 1));
                    drops.add(TreeSimulatorOutput.of(Items.STICK.getDefaultInstance(), 0.1f, 1, 2));
                    drops.add(TreeSimulatorOutput.of(Items.APPLE.getDefaultInstance(), 0.05f, 1, 1));
                    TreeSimulatorRecipe newRecipe = new TreeSimulatorRecipe(getSapling(), drops, value.value().treeSimulatorTicks(), value.unwrapKey().get().location().withSuffix(BuiltInRegistries.BLOCK.getKey(block).getPath().substring(9)).withPrefix("tree_simulator/"));
                    return Optional.of(newRecipe);
                }
            }
        }
        return Optional.empty();
    }

    public ItemStackHandler getInputHandler() {
        return inputHandler;
    }

    public ItemStackHandler getOutputHandler() {
        return outputHandler;
    }

    public ItemStack getSapling(){
        return inputHandler.getStackInSlot(0);
    }

    public ContainerData getData() {
        return data;
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (side == Direction.DOWN && cap == ForgeCapabilities.ITEM_HANDLER){
            return LazyOptional.of(() -> outputHandler).cast();
        }
        if (cap == ForgeCapabilities.ITEM_HANDLER){
            return LazyOptional.of(() -> inputHandler).cast();
        }
        return super.getCapability(cap, side);
    }

    public ItemStackHandler getAxeHandler() {
        return axeHandler;
    }

    public ItemStack getAxe(){
        return axeHandler.getStackInSlot(0);
    }

    public boolean isAxeUnbreakable(){
        return (getAxe().hasTag() && getAxe().getTag().contains("Unbreakable")) || getAxe().getMaxDamage() == 0 && getAxe() != ItemStack.EMPTY;
    }

    public boolean isAxeValid(){
        ItemStack axe = getAxe();
        int damage = axe.getDamageValue();
        int maxDamage = axe.getMaxDamage();
        if (damage >= maxDamage && axe != ItemStack.EMPTY){
            axeHandler.extractItem(0, 1, false);
        }
        return damage < maxDamage || isAxeUnbreakable();
    }

    private void setGrowTick(){
        Optional<TreeSimulatorRecipe> recipeHolder = getCurrentRecipe();
        if (recipeHolder.isPresent() && isAxeValid()){
            TreeSimulatorRecipe recipe = recipeHolder.get();
            int tick = recipe.ticksToGrow();
            double GROW_TICK_BY_AXE = ResourcesTrees.CONFIG.get(getAxe().getItemHolder().unwrapKey().get().location().toString());
            if (GROW_TICK_BY_AXE != 0.0){
                tick = (int) (recipe.ticksToGrow() / GROW_TICK_BY_AXE);
            }
            setMaxGrowTicks(tick);
        }
        else{
            setMaxGrowTicks(0);
        }
    }
}

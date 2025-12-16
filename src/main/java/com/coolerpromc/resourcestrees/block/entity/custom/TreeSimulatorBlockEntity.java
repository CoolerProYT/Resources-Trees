package com.coolerpromc.resourcestrees.block.entity.custom;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.block.ModBlocks;
import com.coolerpromc.resourcestrees.block.custom.ResourcesSaplingBlock;
import com.coolerpromc.resourcestrees.block.entity.ModBlockEntities;
import com.coolerpromc.resourcestrees.core.ResourcesTypes;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.datagen.ModRecipeProvider;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.recipe.ModRecipes;
import com.coolerpromc.resourcestrees.recipe.custom.TreeSimulatorRecipe;
import com.coolerpromc.resourcestrees.recipe.input.TreeSimulatorRecipeInput;
import com.coolerpromc.resourcestrees.recipe.output.TreeSimulatorOutput;
import com.coolerpromc.resourcestrees.screen.custom.TreeSimulatorMenu;
import com.coolerpromc.resourcestrees.util.ExtendedSimpleInventory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

public class TreeSimulatorBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPos>, SidedInventory {
    public static final Map<ResourcesSaplingBlock, Item> LOG_BY_SAPLINGS = Map.of(
            ModBlocks.RESOURCES_OAK_SAPLING, Items.OAK_LOG,
            ModBlocks.RESOURCES_SPRUCE_SAPLING, Items.SPRUCE_LOG,
            ModBlocks.RESOURCES_BIRCH_SAPLING, Items.BIRCH_LOG,
            ModBlocks.RESOURCES_JUNGLE_SAPLING, Items.JUNGLE_LOG,
            ModBlocks.RESOURCES_ACACIA_SAPLING, Items.ACACIA_LOG,
            ModBlocks.RESOURCES_DARK_OAK_SAPLING, Items.DARK_OAK_LOG,
            ModBlocks.RESOURCES_CHERRY_SAPLING, Items.CHERRY_LOG,
            ModBlocks.RESOURCES_PALE_OAK_SAPLING,  Items.PALE_OAK_LOG
    );

    public int growTicks = 0;
    public int maxGrowTicks = 0;

    private final ExtendedSimpleInventory inputHandler = new ExtendedSimpleInventory(1){
        @Override
        public void setStack(int slot, ItemStack stack) {
            super.setStack(slot, stack);
            Optional<RecipeEntry<TreeSimulatorRecipe>> recipeHolder = getCurrentRecipe();
            if (recipeHolder.isPresent()){
                TreeSimulatorRecipe recipe = recipeHolder.get().value();
                setMaxGrowTicks(recipe.ticksToGrow());
            }
            else{
                setMaxGrowTicks(0);
            }
        }

        @Override
        public boolean canExtract(int slot, ItemStack stack, Direction dir) {
            return false;
        }
    };

    private final ExtendedSimpleInventory outputHandler = new ExtendedSimpleInventory(9){
        @Override
        public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
            return false;
        }
    };

    private final ExtendedSimpleInventory axeHandler = new ExtendedSimpleInventory(1){
        @Override
        public boolean isValid(int slot, ItemStack stack) {
            return stack.getItem() instanceof AxeItem;
        }

        @Override
        public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
            return false;
        }

        @Override
        public boolean canExtract(int slot, ItemStack stack, Direction dir) {
            return false;
        }
    };

    private final PropertyDelegate data = new PropertyDelegate() {
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
        public int size() {
            return 2;
        }
    };

    public TreeSimulatorBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.TREE_SIMULATOR_BE, pos, blockState);
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Tree Simulator");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new TreeSimulatorMenu(syncId, playerInventory, this, this.data);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        return this.pos;
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        Inventories.writeData(view.get("input"), inputHandler.getHeldStacks());
        Inventories.writeData(view.get("output"), outputHandler.getHeldStacks());
        Inventories.writeData(view.get("axe"), axeHandler.getHeldStacks());
        view.putInt("growTicks", growTicks);
        view.putInt("maxGrowTicks", maxGrowTicks);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        Inventories.readData(view.getReadView("input"), inputHandler.getHeldStacks());
        Inventories.readData(view.getReadView("output"), outputHandler.getHeldStacks());
        Inventories.readData(view.getReadView("axe"), axeHandler.getHeldStacks());
        this.data.set(0, view.getInt("growTicks", 0));
        this.data.set(1, view.getInt("maxGrowTicks", 0));
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        NbtCompound tag = super.toInitialChunkDataNbt(registries);
        tag.putInt("growTicks", growTicks);
        tag.putInt("maxGrowTicks", maxGrowTicks);
        return tag;
    }

    public void tick(World level, BlockPos pos, BlockState state){
        if (level.isClient()) return;

        if (hasRecipe() && isAxeValid()){
            increaseGrowTicks();
            markDirty(level, pos, state);

            if (treeGrown()){
                harvest(level);
                resetGrowTicks();
            }
        }
        else {
            resetGrowTicks();
        }

        markDirty();
        level.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
    }

    private void harvest(World level){
        Optional<RecipeEntry<TreeSimulatorRecipe>> recipe = getCurrentRecipe();
        if (recipe.isPresent()) {
            List<ItemStack> results = new ArrayList<>();

            recipe.get().value().drops().forEach(output -> {
                int rolls = output.getRolls(level.random);
                for (int i = 0; i < rolls; i++){
                    if (level.random.nextFloat() < output.chance()){
                        results.add(output.output().copy());
                    }
                }
            });

            if (!isAxeUnbreakable()){
                ItemStack axe = getAxe();
                Integer damage = axe.get(DataComponentTypes.DAMAGE);
                if (damage != null){
                    axe.set(DataComponentTypes.DAMAGE, damage + 1);
                }
            }

            for (ItemStack result : results) {
                int outputSlot = findSuitableOutputSlot(result);
                if (outputSlot != -1) {
                    this.outputHandler.insertItem(outputSlot, result, false);
                } else {
                    ResourcesTrees.LOGGER.warn("No suitable output slot found for item: {} at {}", result, getPos());
                }
            }
        }
    }

    private boolean treeGrown(){
        return growTicks >= maxGrowTicks;
    }

    private void increaseGrowTicks(){
        this.growTicks++;
    }

    private void resetGrowTicks(){
        this.growTicks = 0;
    }

    private void setMaxGrowTicks(int tick){
        this.data.set(1, tick);
    }

    private int findSuitableOutputSlot(ItemStack result) {
        for (int i = 0; i < this.outputHandler.getSlots(); i++) {
            ItemStack stackInSlot = this.outputHandler.getStack(i);
            if (stackInSlot.isEmpty() || (ResourcesTypes.isSameItemSameType(stackInSlot, result) && stackInSlot.getCount() + result.getCount() <= stackInSlot.getMaxCount())) {
                return i;
            }
        }
        return -1;
    }

    private boolean hasRecipe() {
        Optional<RecipeEntry<TreeSimulatorRecipe>> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) {
            return false;
        }

        List<ItemStack> results = recipe.get().value().drops().stream().map(TreeSimulatorOutput::output).toList();

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
            ItemStack stackInSlot = this.outputHandler.getStack(i);
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
            ItemStack stackInSlot = this.outputHandler.getStack(i);
            if (stackInSlot.isEmpty() || (ResourcesTypes.isSameItemSameType(stackInSlot, result) && stackInSlot.getCount() + result.getCount() <= stackInSlot.getMaxCount())) {
                return true;
            }
        }
        return false;
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack item) {
        for (int i = 0; i < this.outputHandler.getSlots(); i++) {
            ItemStack stackInSlot = this.outputHandler.getStack(i);
            if (stackInSlot.isEmpty() || ResourcesTypes.isSameItemSameType(stackInSlot, item)) {
                return true;
            }
        }
        return false;
    }

    private Optional<RecipeEntry<TreeSimulatorRecipe>> getCurrentRecipe(){
        if (world instanceof ServerWorld serverLevel){
            Optional<RecipeEntry<TreeSimulatorRecipe>> recipe = serverLevel.getRecipeManager().getFirstMatch(ModRecipes.TREE_SIMULATOR_TYPE, new TreeSimulatorRecipeInput(getSapling()), serverLevel);
            if (recipe.isPresent()){
                return recipe;
            }
            else if (Block.getBlockFromItem(getSapling().getItem()) instanceof ResourcesSaplingBlock block){
                RegistryEntry<ResourcesTypes> type = getSapling().get(ModDataComponents.TYPE);

                ItemStack leaf = ModItems.LEAF_FRAGMENT.getDefaultStack();
                leaf.set(ModDataComponents.TYPE, type);
                if (type != null){
                    ResourcesTypes value = type.value();
                    List<TreeSimulatorOutput> drops = new ArrayList<>();
                    drops.add(TreeSimulatorOutput.of(TreeSimulatorBlockEntity.LOG_BY_SAPLINGS.get(block).getDefaultStack(), 1, 2, 4));
                    drops.add(TreeSimulatorOutput.of(leaf, 1, 1, 1));
                    drops.add(TreeSimulatorOutput.of(leaf, value.leafDropChance(), 1, 1));
                    drops.add(TreeSimulatorOutput.of(getSapling(), value.saplingDropChance(), 1, 1));
                    drops.add(TreeSimulatorOutput.of(Items.STICK.getDefaultStack(), 0.1f, 1, 2));
                    drops.add(TreeSimulatorOutput.of(Items.APPLE.getDefaultStack(), 0.05f, 1, 1));
                    drops.add(TreeSimulatorOutput.of(ModRecipeProvider.SAPLINGS_BY_SAPLINGS.get(block).getDefaultStack(), 0.1f, 1, 1));
                    TreeSimulatorRecipe newRecipe = new TreeSimulatorRecipe(getSapling(), drops, 1200);
                    RegistryKey<Recipe<?>> key = RegistryKey.of(RegistryKeys.RECIPE, type.getKey().get().getValue().withSuffixedPath(Registries.BLOCK.getId(block).getPath().substring(9)).withPrefixedPath("tree_simulator/"));
                    return Optional.of(new RecipeEntry<>(key, newRecipe));
                }
            }
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
        return inputHandler.getStack(0);
    }

    public PropertyDelegate getData() {
        return data;
    }

    public ExtendedSimpleInventory getAxeHandler() {
        return axeHandler;
    }

    public ItemStack getAxe(){
        return axeHandler.getStack(0);
    }

    public boolean isAxeUnbreakable(){
        return getAxe().contains(DataComponentTypes.UNBREAKABLE);
    }

    public boolean isAxeValid(){
        ItemStack axe = getAxe();
        Integer damage = axe.get(DataComponentTypes.DAMAGE);
        Integer maxDamage = axe.get(DataComponentTypes.MAX_DAMAGE);
        if (damage != null && maxDamage != null){
            if (damage >= maxDamage){
                axeHandler.removeStack(0, 1);
            }
            return damage < maxDamage || isAxeUnbreakable();
        }
        return isAxeUnbreakable();
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.DOWN) {
            return IntStream.range(inputHandler.size(), inputHandler.size() + outputHandler.size()).toArray();
        } else {
            return IntStream.range(0, inputHandler.size()).toArray();
        }
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return inputHandler.canInsert(slot, stack, dir);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return outputHandler.canExtract(slot, stack, dir);
    }

    @Override
    public int size() {
        return inputHandler.size() + outputHandler.size() + axeHandler.size();
    }

    @Override
    public boolean isEmpty() {
        return inputHandler.isEmpty() && outputHandler.isEmpty() && axeHandler.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return getHandlerForSlot(slot).getStack(getLocalSlot(slot));
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return getHandlerForSlot(slot).removeStack(getLocalSlot(slot), amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return getHandlerForSlot(slot).removeStack(getLocalSlot(slot));
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        getHandlerForSlot(slot).setStack(getLocalSlot(slot), stack);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        inputHandler.clear();
        outputHandler.clear();
        axeHandler.clear();
    }

    private Inventory getHandlerForSlot(int slot) {
        if (slot < inputHandler.size()) return inputHandler;
        slot -= inputHandler.size();
        if (slot < outputHandler.size()) return outputHandler;
        slot -= outputHandler.size();
        return axeHandler;
    }

    private int getLocalSlot(int globalSlot) {
        if (globalSlot < inputHandler.size()) return globalSlot;
        globalSlot -= inputHandler.size();
        if (globalSlot < outputHandler.size()) return globalSlot;
        globalSlot -= outputHandler.size();
        return globalSlot;
    }
}

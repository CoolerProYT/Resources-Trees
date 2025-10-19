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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
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
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
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
            Optional<RecipeHolder<TreeSimulatorRecipe>> recipeHolder = getCurrentRecipe();
            if (recipeHolder.isPresent()){
                TreeSimulatorRecipe recipe = recipeHolder.get().value();
                setMaxGrowTicks(recipe.ticksToGrow());
            }
            else{
                setMaxGrowTicks(0);
            }
        }
    };

    private final ItemStackHandler outputHandler = new ItemStackHandler(9);

    private final ItemStackHandler axeHandler = new ItemStackHandler(1){
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.getItem() instanceof AxeItem;
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
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("input", inputHandler.serializeNBT(provider));
        tag.put("output", outputHandler.serializeNBT(provider));
        tag.put("axe", axeHandler.serializeNBT(provider));
        tag.putInt("growTicks", growTicks);
        tag.putInt("maxGrowTicks", maxGrowTicks);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        inputHandler.deserializeNBT(provider, tag.getCompound("input"));
        outputHandler.deserializeNBT(provider, tag.getCompound("output"));
        axeHandler.deserializeNBT(provider, tag.getCompound("axe"));
        this.data.set(0, tag.getInt("growTicks"));
        this.data.set(1, tag.getInt("maxGrowTicks"));
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);
        loadAdditional(tag, lookupProvider);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = saveWithoutMetadata(registries);
        tag.putInt("growTicks", growTicks);
        tag.putInt("maxGrowTicks", maxGrowTicks);
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        super.onDataPacket(net, pkt, lookupProvider);
        loadAdditional(pkt.getTag(), lookupProvider);
    }

    public void tick(Level level, BlockPos pos, BlockState state){
        if (level.isClientSide()) return;

        if (hasRecipe() && isAxeValid()){
            increaseGrowTicks();
            setChanged(level, pos, state);

            if (treeGrown()){
                harvest(level);
                resetGrowTicks();
            }
        }
        else {
            resetGrowTicks();
        }

        setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }

    private void harvest(Level level){
        Optional<RecipeHolder<TreeSimulatorRecipe>> recipe = getCurrentRecipe();
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
                Integer damage = axe.get(DataComponents.DAMAGE);
                if (damage != null){
                    axe.set(DataComponents.DAMAGE, damage + 1);
                }
            }

            for (ItemStack result : results) {
                int outputSlot = findSuitableOutputSlot(result);
                if (outputSlot != -1) {
                    this.outputHandler.insertItem(outputSlot, result, false);
                } else {
                    ResourcesTrees.LOGGER.warn("No suitable output slot found for item: {} at {}", result, getBlockPos());
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
            ItemStack stackInSlot = this.outputHandler.getStackInSlot(i);
            if (stackInSlot.isEmpty() || (ResourcesTypes.isSameItemSameType(stackInSlot, result) && stackInSlot.getCount() + result.getCount() <= stackInSlot.getMaxStackSize())) {
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

    private Optional<RecipeHolder<TreeSimulatorRecipe>> getCurrentRecipe(){
        if (level instanceof ServerLevel serverLevel){
            Optional<RecipeHolder<TreeSimulatorRecipe>> recipe = serverLevel.getRecipeManager().getRecipeFor(ModRecipes.TREE_SIMULATOR_TYPE.get(), new TreeSimulatorRecipeInput(getSapling()), serverLevel);
            if (recipe.isPresent()){
                return recipe;
            }
            else if (Block.byItem(getSapling().getItem()) instanceof ResourcesSaplingBlock block){
                Holder<ResourcesTypes> type = getSapling().get(ModDataComponents.TYPE);
                ItemStack leaf = ModItems.LEAF_FRAGMENT.toStack();
                leaf.set(ModDataComponents.TYPE, type);
                if (type != null && type.value() != ResourcesTypes.EMPTY){
                    ResourcesTypes value = type.value();
                    List<TreeSimulatorOutput> drops = new ArrayList<>();
                    drops.add(TreeSimulatorOutput.of(TreeSimulatorBlockEntity.LOG_BY_SAPLINGS.get(block).getDefaultInstance(), 1, 2, 4));
                    drops.add(TreeSimulatorOutput.of(leaf, 1, 1, 1));
                    drops.add(TreeSimulatorOutput.of(leaf, value.secondaryDropChance(), 1, 1));
                    drops.add(TreeSimulatorOutput.of(getSapling(), value.saplingChance(), 1, 1));
                    drops.add(TreeSimulatorOutput.of(Items.STICK.getDefaultInstance(), 0.1f, 1, 2));
                    drops.add(TreeSimulatorOutput.of(Items.APPLE.getDefaultInstance(), 0.05f, 1, 1));
                    drops.add(TreeSimulatorOutput.of(ModRecipeProvider.SAPLINGS_BY_SAPLINGS.get(block).getDefaultInstance(), 0.1f, 1, 1));
                    TreeSimulatorRecipe newRecipe = new TreeSimulatorRecipe(getSapling(), drops, 1200);
                    ResourceKey<Recipe<?>> key = ResourceKey.create(Registries.RECIPE, type.getKey().location().withSuffix(BuiltInRegistries.BLOCK.getKey(block).getPath().substring(9)).withPrefix("tree_simulator/"));
                    return Optional.of(new RecipeHolder<>(key.location(), newRecipe));
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

    public IItemHandler getCapability(Direction direction){
        if (direction == Direction.DOWN){
            return outputHandler;
        }
        return inputHandler;
    }

    public ItemStackHandler getAxeHandler() {
        return axeHandler;
    }

    public ItemStack getAxe(){
        return axeHandler.getStackInSlot(0);
    }

    public boolean isAxeUnbreakable(){
        return getAxe().has(DataComponents.UNBREAKABLE);
    }

    public boolean isAxeValid(){
        ItemStack axe = getAxe();
        Integer damage = axe.get(DataComponents.DAMAGE);
        Integer maxDamage = axe.get(DataComponents.MAX_DAMAGE);
        if (damage != null && maxDamage != null){
            if (damage >= maxDamage){
                axeHandler.extractItem(0, 1, false);
            }
            return damage < maxDamage || isAxeUnbreakable();
        }
        return isAxeUnbreakable();
    }
}

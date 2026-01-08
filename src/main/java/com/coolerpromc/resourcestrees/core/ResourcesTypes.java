package com.coolerpromc.resourcestrees.core;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.registry.ModRegistries;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public record ResourcesTypes(Either<Identifier, TagKey<Item>> material, int color, int weight, float saplingDropChance, float leafDropChance, int treeSimulatorTicks){
    public static final Codec<Either<Identifier, TagKey<Item>>> MATERIAL_CODEC = Codec.either(
            Identifier.CODEC,
            TagKey.codec(RegistryKeys.ITEM)
    );

    public static final PacketCodec<ByteBuf, Either<Identifier, TagKey<Item>>> MATERIAL_STREAM_CODEC = PacketCodecs.either(
            Identifier.PACKET_CODEC,
            Identifier.PACKET_CODEC.xmap(identifier -> TagKey.of(RegistryKeys.ITEM, identifier), TagKey::id)
    );

    public static final Codec<ResourcesTypes> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MATERIAL_CODEC.fieldOf("material").forGetter(ResourcesTypes::material),
            Codec.INT.fieldOf("color").forGetter(ResourcesTypes::color),
            Codec.INT.fieldOf("weight").forGetter(ResourcesTypes::weight),
            Codec.FLOAT.fieldOf("saplingDropChance").forGetter(ResourcesTypes::saplingDropChance),
            Codec.FLOAT.fieldOf("leafDropChance").forGetter(ResourcesTypes::leafDropChance),
            Codec.INT.fieldOf("treeSimulatorTicks").forGetter(ResourcesTypes::treeSimulatorTicks)
    ).apply(instance, ResourcesTypes::new));

    public static final PacketCodec<RegistryByteBuf, ResourcesTypes> STREAM_CODEC = PacketCodec.tuple(
            MATERIAL_STREAM_CODEC,
            ResourcesTypes::material,
            PacketCodecs.INTEGER,
            ResourcesTypes::color,
            PacketCodecs.INTEGER,
            ResourcesTypes::weight,
            PacketCodecs.FLOAT,
            ResourcesTypes::saplingDropChance,
            PacketCodecs.FLOAT,
            ResourcesTypes::leafDropChance,
            PacketCodecs.INTEGER,
            ResourcesTypes::treeSimulatorTicks,
            ResourcesTypes::new
    );

    public static final RegistryKey<ResourcesTypes> STONE = register("stone");
    public static final RegistryKey<ResourcesTypes> COAL = register("coal");
    public static final RegistryKey<ResourcesTypes> IRON = register("iron");
    public static final RegistryKey<ResourcesTypes> COPPER = register("copper");
    public static final RegistryKey<ResourcesTypes> GOLD = register("gold");
    public static final RegistryKey<ResourcesTypes> LAPIS = register("lapis");
    public static final RegistryKey<ResourcesTypes> EMERALD = register("emerald");
    public static final RegistryKey<ResourcesTypes> DIAMOND = register("diamond");
    public static final RegistryKey<ResourcesTypes> OBSIDIAN = register("obsidian");
    public static final RegistryKey<ResourcesTypes> AMETHYST = register("amethyst");
    public static final RegistryKey<ResourcesTypes> NETHERITE = register("netherite");
    public static final RegistryKey<ResourcesTypes> WOOD = register("wood");
    public static final RegistryKey<ResourcesTypes> QUARTZ = register("quartz");
    public static final RegistryKey<ResourcesTypes> PRISMARINE = register("prismarine");
    public static final RegistryKey<ResourcesTypes> GLOWSTONE = register("glowstone");
    public static final RegistryKey<ResourcesTypes> REDSTONE = register("redstone");
    public static final RegistryKey<ResourcesTypes> DEEPSLATE = register("deepslate");
    public static final RegistryKey<ResourcesTypes> DIRT = register("dirt");
    public static final RegistryKey<ResourcesTypes> FIRE = register("fire");
    public static final RegistryKey<ResourcesTypes> NETHER = register("nether");
    public static final RegistryKey<ResourcesTypes> END = register("end");
    public static final RegistryKey<ResourcesTypes> NATURE = register("nature");
    public static final RegistryKey<ResourcesTypes> WATER = register("water");
    public static final RegistryKey<ResourcesTypes> ICE = register("ice");
    public static final RegistryKey<ResourcesTypes> BEE = register("bee");
    public static final RegistryKey<ResourcesTypes> SLIME = register("slime");
    public static final RegistryKey<ResourcesTypes> SCULK = register("sculk");
    public static final RegistryKey<ResourcesTypes> SKELETON = register("skeleton");
    public static final RegistryKey<ResourcesTypes> SPIDER = register("spider");
    public static final RegistryKey<ResourcesTypes> CHICKEN = register("chicken");
    public static final RegistryKey<ResourcesTypes> COW = register("cow");
    public static final RegistryKey<ResourcesTypes> RABBIT = register("rabbit");
    public static final RegistryKey<ResourcesTypes> SQUID = register("squid");
    public static final RegistryKey<ResourcesTypes> TURTLE = register("turtle");
    public static final RegistryKey<ResourcesTypes> BLAZE = register("blaze");
    public static final RegistryKey<ResourcesTypes> BREEZE = register("breeze");
    public static final RegistryKey<ResourcesTypes> NETHER_STAR = register("nether_star");
    public static final RegistryKey<ResourcesTypes> ENDER_PEARL = register("ender_pearl");
    public static final RegistryKey<ResourcesTypes> SHULKER = register("shulker");
    public static final RegistryKey<ResourcesTypes> DYE = register("dye");
    public static final RegistryKey<ResourcesTypes> GUNPOWDER = register("gunpowder");
    public static final RegistryKey<ResourcesTypes> GHAST = register("ghast");
    public static final RegistryKey<ResourcesTypes> PIG = register("pig");
    public static final RegistryKey<ResourcesTypes> SHEEP = register("sheep");
    public static final RegistryKey<ResourcesTypes> FISH = register("fish");
    public static final RegistryKey<ResourcesTypes> ZOMBIE = register("zombie");

    private static RegistryKey<ResourcesTypes> register(String name){
        return RegistryKey.of(ModRegistries.RESOURCES_TYPES_KEY, ResourcesTrees.id(name));
    }

    public static void bootstrap(Registerable<ResourcesTypes> context){
        context.register(STONE, new Builder(Items.COBBLESTONE, 0xFF4D4B49).treeSimulatorTicks(800).build());
        context.register(COAL, new Builder(Items.COAL_BLOCK, 0xFF000000).treeSimulatorTicks(1000).build());
        context.register(IRON, new Builder(Items.IRON_BLOCK, 0xFFB0BEC5).build());
        context.register(COPPER, new Builder(Items.COPPER_BLOCK, 0xFFD46D44).build());
        context.register(GOLD, new Builder(Items.GOLD_BLOCK, 0xFFFFD600).build());
        context.register(LAPIS, new Builder(Items.LAPIS_BLOCK, 0xFF3F51B5).build());
        context.register(EMERALD, new Builder(Items.EMERALD_BLOCK, 0xFF00C853).treeSimulatorTicks(1400).build());
        context.register(DIAMOND, new Builder(Items.DIAMOND_BLOCK, 0xFF40C4FF).weight(3).saplingDropChance(0.1f).leafDropChance(0.2f).treeSimulatorTicks(1600).build());
        context.register(OBSIDIAN, new Builder(Items.OBSIDIAN, 0xFF2E1A47).weight(4).build());
        context.register(AMETHYST, new Builder(Items.AMETHYST_BLOCK, 0xFF9C27B0).treeSimulatorTicks(800).build());
        context.register(NETHERITE, new Builder(Items.NETHERITE_BLOCK, 0xFF3E3E3E).weight(2).saplingDropChance(0.075f).leafDropChance(0.15f).treeSimulatorTicks(1800).build());
        context.register(WOOD, new Builder(ItemTags.LOGS, 0xFF8D6E63).treeSimulatorTicks(800).build());
        context.register(QUARTZ, new Builder(Items.QUARTZ_BLOCK, 0xFFF5F5F5).treeSimulatorTicks(800).build());
        context.register(PRISMARINE, new Builder(Items.PRISMARINE, 0xFF5EC8C8).treeSimulatorTicks(800).build());
        context.register(GLOWSTONE, new Builder(Items.GLOWSTONE, 0xFFFFF176).treeSimulatorTicks(800).build());
        context.register(REDSTONE, new Builder(Items.REDSTONE_BLOCK, 0xFFFF1744).treeSimulatorTicks(1000).build());
        context.register(DEEPSLATE, new Builder(Items.DEEPSLATE, 0xFF2B2B24).treeSimulatorTicks(800).build());
        context.register(DIRT, new Builder(Items.DIRT, 0xFF9B7653).treeSimulatorTicks(800).build());
        context.register(FIRE, new Builder(ModItems.FIRE_ESSENCE, 0xFFE45323).treeSimulatorTicks(800).build());
        context.register(NETHER, new Builder(Items.NETHERRACK, 0xFF511515).treeSimulatorTicks(800).build());
        context.register(END, new Builder(ModItems.END_ESSENCE, 0xFFC5BE8B).treeSimulatorTicks(800).build());
        context.register(NATURE, new Builder(ModItems.NATURE_ESSENCE, 0xFF1a6e08).treeSimulatorTicks(800).build());
        context.register(WATER, new Builder(ModItems.WATER_ESSENCE, 0xFF1787D4).treeSimulatorTicks(800).build());
        context.register(ICE, new Builder(Items.ICE, 0xFFb9e8ea).treeSimulatorTicks(800).build());
        context.register(BEE, new Builder(ModItems.BEE_ESSENCE, 0xFFEDC343).build());
        context.register(SLIME, new Builder(Items.SLIME_BLOCK, 0xFF6aa84f).build());
        context.register(SCULK, new Builder(ModItems.SCULK_ESSENCE, 0xFF041820).treeSimulatorTicks(1400).build());
        context.register(SKELETON, new Builder(ModItems.SKELETON_ESSENCE, 0xFFeeeeee).build());
        context.register(SPIDER, new Builder(ModItems.SPIDER_ESSENCE, 0xFF1a0c20).build());
        context.register(CHICKEN, new Builder(ModItems.CHICKEN_ESSENCE, 0xFFA1A1A1).build());
        context.register(COW, new Builder(ModItems.COW_ESSENCE, 0xFF543936).build());
        context.register(RABBIT, new Builder(ModItems.RABBIT_ESSENCE, 0xFF8B5A2B).build());
        context.register(SQUID, new Builder(ModItems.SQUID_ESSENCE, 0xFF223B4D).build());
        context.register(TURTLE, new Builder(ModItems.TURTLE_ESSENCE, 0xFF315410).build());
        context.register(BLAZE, new Builder(ModItems.BLAZE_ESSENCE, 0xFFd4ae37).build());
        context.register(BREEZE, new Builder(ModItems.BREEZE_ESSENCE, 0xFFd5d6ff).build());
        context.register(NETHER_STAR, new Builder(Items.NETHER_STAR, 0xFF211f1f).treeSimulatorTicks(2000).build());
        context.register(ENDER_PEARL, new Builder(Items.ENDER_PEARL, 0xFF032620).treeSimulatorTicks(1400).build());
        context.register(SHULKER, new Builder(Items.SHULKER_SHELL, 0xFFcfc2d6).treeSimulatorTicks(1400).build());
        context.register(DYE, new Builder(ModItems.DYE_ESSENCE, 0xFF72d4b3).treeSimulatorTicks(800).build());
        context.register(GUNPOWDER, new Builder(Items.GUNPOWDER, 0xFF414257).build());
        context.register(GHAST, new Builder(ModItems.GHAST_ESSENCE, 0xFFF9F9F9).build());
        context.register(PIG, new Builder(ModItems.PIG_ESSENCE, 0xFFF9A195).build());
        context.register(SHEEP, new Builder(ModItems.SHEEP_ESSENCE, 0xFFFFFFFF).build());
        context.register(FISH, new Builder(ModItems.FISH_ESSENCE, 0xFFC1A76A).build());
        context.register(ZOMBIE, new Builder(ModItems.ZOMBIE_ESSENCE, 0xFF3e692d).build());
    }

    public static Map<Identifier, RegistryEntry<ResourcesTypes>> getAllResourcesTypes(RegistryWrapper.WrapperLookup provider){
        Stream<RegistryEntry.Reference<ResourcesTypes>> registry = provider.getWrapperOrThrow(ModRegistries.RESOURCES_TYPES_KEY).streamEntries();
        Map<Identifier, RegistryEntry<ResourcesTypes>> types = new HashMap<>();
        registry.forEachOrdered(entry -> {
            if (types.containsKey(entry.registryKey().getValue())){
                throw new IllegalStateException("Duplicate resource index " + entry.registryKey().getValue() + " for " + entry.registryKey().getValue());
            }
            types.put(entry.registryKey().getValue(), entry);
        });
        ResourcesTrees.LOGGER.debug("Getting all resources types: {}", types);
        return types;
    }

    public static boolean isSameItemSameType(ItemStack stack, ItemStack other) {
        if (!ItemStack.areItemsEqual(stack, other)) {
            return false;
        }

        boolean hasType1 = stack.contains(ModDataComponents.TYPE);
        boolean hasType2 = other.contains(ModDataComponents.TYPE);

        if (hasType1 && hasType2) {
            return Objects.equals(stack.get(ModDataComponents.TYPE), other.get(ModDataComponents.TYPE));
        }

        return !hasType1 && !hasType2;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ResourcesTypes that = (ResourcesTypes) o;
        return color() == that.color() && weight() == that.weight() && Float.compare(saplingDropChance(), that.saplingDropChance()) == 0 && Float.compare(leafDropChance(), that.leafDropChance()) == 0 && Objects.equals(material(), that.material());
    }

    @Override
    public int hashCode() {
        return Objects.hash(material(), color(), weight(), saplingDropChance(), leafDropChance());
    }

    public static class Builder{
        private final Either<Identifier, TagKey<Item>> material;
        private final int color;
        private int weight;
        private float saplingDropChance;
        private float leafDropChance;
        private int treeSimulatorTicks;

        public Builder(Item material, int color){
            this.material = Either.left(Registries.ITEM.getId(material));
            this.color = color;
            this.weight = 5;
            this.saplingDropChance = 0.125f;
            this.leafDropChance = 0.25f;
            this.treeSimulatorTicks = 1200;
        }

        public Builder(TagKey<Item> material, int color){
            this.material = Either.right(material);
            this.color = color;
            this.weight = 5;
            this.saplingDropChance = 0.125f;
            this.leafDropChance = 0.25f;
            this.treeSimulatorTicks = 1200;
        }
        
        public Builder weight(int weight){
            this.weight = weight;
            return this;
        }
        
        public Builder saplingDropChance(float saplingDropChance){
            this.saplingDropChance = saplingDropChance;
            return this;
        }
        
        public Builder leafDropChance(float leafDropChance) {
            this.leafDropChance = leafDropChance;
            return this;
        }
        
        public Builder treeSimulatorTicks(int treeSimulatorTicks) {
            this.treeSimulatorTicks = treeSimulatorTicks;
            return this;
        }

        public ResourcesTypes build(){
            return new ResourcesTypes(material, color, weight, saplingDropChance, leafDropChance, treeSimulatorTicks);
        }
    }
}

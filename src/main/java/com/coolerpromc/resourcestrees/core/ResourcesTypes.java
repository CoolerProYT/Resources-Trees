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

public record ResourcesTypes(Either<Identifier, TagKey<Item>> material, int color, String translationKey, int weight, float saplingDropChance, float leafDropChance){
    public ResourcesTypes(Item material, int color, String translationKey, int weight, float saplingDropChance, float leafDropChance) {
        this(Either.left(Registries.ITEM.getId(material)), color, translationKey, weight, saplingDropChance, leafDropChance);
    }

    public ResourcesTypes(TagKey<Item> material, int color, String translationKey, int weight, float saplingDropChance, float leafDropChance) {
        this(Either.right(material), color, translationKey, weight, saplingDropChance, leafDropChance);
    }

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
            Codec.STRING.fieldOf("translationKey").forGetter(ResourcesTypes::translationKey),
            Codec.INT.fieldOf("weight").forGetter(ResourcesTypes::weight),
            floatFieldWithLegacy("saplingDropChance", "saplingChance").forGetter(ResourcesTypes::saplingDropChance),
            floatFieldWithLegacy("leafDropChance", "secondaryDropChance").forGetter(ResourcesTypes::leafDropChance)
    ).apply(instance, ResourcesTypes::new));

    private static MapCodec<Float> floatFieldWithLegacy(String newName, String oldName) {
        return new MapCodec<Float>() {
            @Override
            public <T> Stream<T> keys(DynamicOps<T> ops) {
                return Stream.of(ops.createString(newName));
            }

            @Override
            public <T> DataResult<Float> decode(DynamicOps<T> ops, MapLike<T> input) {
                T newValue = input.get(newName);
                if (newValue != null) {
                    return ops.getNumberValue(newValue).map(Number::floatValue);
                }

                T oldValue = input.get(oldName);
                if (oldValue != null) {
                    return ops.getNumberValue(oldValue).map(Number::floatValue);
                }

                return DataResult.error(() -> "Missing field: " + newName + " or " + oldName);
            }

            @Override
            public <T> RecordBuilder<T> encode(Float input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
                return prefix.add(newName, ops.createFloat(input));
            }
        };
    }

    public static final PacketCodec<RegistryByteBuf, ResourcesTypes> STREAM_CODEC = PacketCodec.tuple(
            MATERIAL_STREAM_CODEC,
            ResourcesTypes::material,
            PacketCodecs.INTEGER,
            ResourcesTypes::color,
            PacketCodecs.STRING,
            ResourcesTypes::translationKey,
            PacketCodecs.INTEGER,
            ResourcesTypes::weight,
            PacketCodecs.FLOAT,
            ResourcesTypes::saplingDropChance,
            PacketCodecs.FLOAT,
            ResourcesTypes::leafDropChance,
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

    private static RegistryKey<ResourcesTypes> register(String name){
        return RegistryKey.of(ModRegistries.RESOURCES_TYPES_KEY, ResourcesTrees.id(name));
    }

    public static void bootstrap(Registerable<ResourcesTypes> context){
        context.register(STONE, new ResourcesTypes(Items.COBBLESTONE, 0xFF4D4B49, "item.resourcestrees.stone", 5, 0.125f, 0.25f));
        context.register(COAL, new ResourcesTypes(Items.COAL_BLOCK, 0xFF000000, "item.resourcestrees.coal", 5, 0.125f, 0.25f));
        context.register(IRON, new ResourcesTypes(Items.IRON_BLOCK, 0xFFB0BEC5, "item.resourcestrees.iron", 5, 0.125f, 0.25f));
        context.register(COPPER, new ResourcesTypes(Items.COPPER_BLOCK, 0xFFD46D44, "item.resourcestrees.copper", 5, 0.125f, 0.25f));
        context.register(GOLD, new ResourcesTypes(Items.GOLD_BLOCK, 0xFFFFD600, "item.resourcestrees.gold", 5, 0.125f, 0.25f));
        context.register(LAPIS, new ResourcesTypes(Items.LAPIS_BLOCK, 0xFF3F51B5, "item.resourcestrees.lapis", 5, 0.125f, 0.25f));
        context.register(EMERALD, new ResourcesTypes(Items.EMERALD_BLOCK, 0xFF00C853, "item.resourcestrees.emerald", 5, 0.125f, 0.25f));
        context.register(DIAMOND, new ResourcesTypes(Items.DIAMOND_BLOCK, 0xFF40C4FF, "item.resourcestrees.diamond", 3, 0.10f, 0.2f));
        context.register(OBSIDIAN, new ResourcesTypes(Items.OBSIDIAN, 0xFF2E1A47, "item.resourcestrees.obsidian", 4, 0.125f, 0.25f));
        context.register(AMETHYST, new ResourcesTypes(Items.AMETHYST_BLOCK, 0xFF9C27B0, "item.resourcestrees.amethyst", 5, 0.125f, 0.25f));
        context.register(NETHERITE, new ResourcesTypes(Items.NETHERITE_BLOCK, 0xFF3E3E3E, "item.resourcestrees.netherite", 2, 0.075f, 0.15f));
        context.register(WOOD, new ResourcesTypes(ItemTags.LOGS, 0xFF8D6E63, "item.resourcestrees.wood", 5, 0.125f, 0.25f));
        context.register(QUARTZ, new ResourcesTypes(Items.QUARTZ_BLOCK, 0xFFF5F5F5, "item.resourcestrees.quartz", 5, 0.125f, 0.25f));
        context.register(PRISMARINE, new ResourcesTypes(Items.PRISMARINE, 0xFF5EC8C8, "item.resourcestrees.prismarine", 5, 0.125f, 0.25f));
        context.register(GLOWSTONE, new ResourcesTypes(Items.GLOWSTONE, 0xFFFFF176, "item.resourcestrees.glowstone", 5, 0.125f, 0.25f));
        context.register(REDSTONE, new ResourcesTypes(Items.REDSTONE_BLOCK, 0xFFFF1744, "item.resourcestrees.redstone", 5, 0.125f, 0.25f));
        context.register(DEEPSLATE, new ResourcesTypes(Items.DEEPSLATE, 0xFF2B2B24, "item.resourcestrees.deepslate", 5, 0.125f, 0.25f));
        context.register(DIRT, new ResourcesTypes(Items.DIRT, 0xFF9B7653, "item.resourcestrees.dirt", 5, 0.25f, 0.125f));
        context.register(FIRE, new ResourcesTypes(ModItems.FIRE_ESSENCE, 0xFFE45323 , "item.resourcestrees.fire", 5, 0.125f, 0.25f));
        context.register(NETHER, new ResourcesTypes(Items.NETHERRACK, 0xFF511515 , "item.resourcestrees.nether", 5, 0.125f, 0.25f));
        context.register(END, new ResourcesTypes(ModItems.END_ESSENCE, 0xFFC5BE8B , "item.resourcestrees.end", 5, 0.125f, 0.25f));
        context.register(NATURE, new ResourcesTypes(ModItems.NATURE_ESSENCE, 0xFF1a6e08 , "item.resourcestrees.nature", 5, 0.125f, 0.25f));
        context.register(WATER, new ResourcesTypes(ModItems.WATER_ESSENCE, 0xFF1787D4, "item.resourcestrees.water", 5, 0.125f, 0.25f));
        context.register(ICE, new ResourcesTypes(Items.ICE, 0xFFb9e8ea, "item.resourcestrees.ice", 5, 0.25f, 0.125f));
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
        return color() == that.color() && weight() == that.weight() && Float.compare(saplingDropChance(), that.saplingDropChance()) == 0 && Float.compare(leafDropChance(), that.leafDropChance()) == 0 && Objects.equals(translationKey(), that.translationKey()) && Objects.equals(material(), that.material());
    }

    @Override
    public int hashCode() {
        return Objects.hash(material(), color(), translationKey(), weight(), saplingDropChance(), leafDropChance());
    }
}

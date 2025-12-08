package com.coolerpromc.resourcestrees.core;

import com.coolerpromc.resourcestrees.ResourcesTrees;
import com.coolerpromc.resourcestrees.datacomponent.ModDataComponents;
import com.coolerpromc.resourcestrees.item.ModItems;
import com.coolerpromc.resourcestrees.registry.ModRegistries;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public record ResourcesTypes(Either<Identifier, TagKey<Item>> material, int color, String translationKey, int weight, float saplingChance, float secondaryDropChance){
    public ResourcesTypes(Item material, int color, String translationKey, int weight, float saplingChance, float secondaryDropChance) {
        this(Either.left(BuiltInRegistries.ITEM.getKey(material)), color, translationKey, weight, saplingChance, secondaryDropChance);
    }

    public ResourcesTypes(TagKey<Item> material, int color, String translationKey, int weight, float saplingChance, float secondaryDropChance) {
        this(Either.right(material), color, translationKey, weight, saplingChance, secondaryDropChance);
    }

    public static final Codec<Either<Identifier, TagKey<Item>>> MATERIAL_CODEC = Codec.either(
            Identifier.CODEC,
            TagKey.hashedCodec(Registries.ITEM)
    );

    public static final Codec<ResourcesTypes> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MATERIAL_CODEC.fieldOf("material").forGetter(ResourcesTypes::material),
            Codec.INT.fieldOf("color").forGetter(ResourcesTypes::color),
            Codec.STRING.fieldOf("translationKey").forGetter(ResourcesTypes::translationKey),
            Codec.INT.fieldOf("weight").forGetter(ResourcesTypes::weight),
            Codec.FLOAT.fieldOf("saplingChance").forGetter(ResourcesTypes::saplingChance),
            Codec.FLOAT.fieldOf("secondaryDropChance").forGetter(ResourcesTypes::secondaryDropChance)
    ).apply(instance, ResourcesTypes::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ResourcesTypes> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.either(Identifier.STREAM_CODEC, TagKey.streamCodec(Registries.ITEM)),
            ResourcesTypes::material,
            ByteBufCodecs.INT,
            ResourcesTypes::color,
            ByteBufCodecs.STRING_UTF8,
            ResourcesTypes::translationKey,
            ByteBufCodecs.INT,
            ResourcesTypes::weight,
            ByteBufCodecs.FLOAT,
            ResourcesTypes::saplingChance,
            ByteBufCodecs.FLOAT,
            ResourcesTypes::secondaryDropChance,
            ResourcesTypes::new
    );

    public static final ResourcesTypes EMPTY = new ResourcesTypes(Items.AIR, 0xFF141414, "item.resourcestrees.empty", 0, 0, 0);

    public static final ResourceKey<ResourcesTypes> STONE = register("stone");
    public static final ResourceKey<ResourcesTypes> COAL = register("coal");
    public static final ResourceKey<ResourcesTypes> IRON = register("iron");
    public static final ResourceKey<ResourcesTypes> COPPER = register("copper");
    public static final ResourceKey<ResourcesTypes> GOLD = register("gold");
    public static final ResourceKey<ResourcesTypes> LAPIS = register("lapis");
    public static final ResourceKey<ResourcesTypes> EMERALD = register("emerald");
    public static final ResourceKey<ResourcesTypes> DIAMOND = register("diamond");
    public static final ResourceKey<ResourcesTypes> OBSIDIAN = register("obsidian");
    public static final ResourceKey<ResourcesTypes> AMETHYST = register("amethyst");
    public static final ResourceKey<ResourcesTypes> NETHERITE = register("netherite");
    public static final ResourceKey<ResourcesTypes> WOOD = register("wood");
    public static final ResourceKey<ResourcesTypes> QUARTZ = register("quartz");
    public static final ResourceKey<ResourcesTypes> PRISMARINE = register("prismarine");
    public static final ResourceKey<ResourcesTypes> GLOWSTONE = register("glowstone");
    public static final ResourceKey<ResourcesTypes> REDSTONE = register("redstone");
    public static final ResourceKey<ResourcesTypes> DEEPSLATE = register("deepslate");
    public static final ResourceKey<ResourcesTypes> DIRT = register("dirt");
    public static final ResourceKey<ResourcesTypes> FIRE = register("fire");
    public static final ResourceKey<ResourcesTypes> NETHER = register("nether");
    public static final ResourceKey<ResourcesTypes> END = register("end");
    public static final ResourceKey<ResourcesTypes> NATURE = register("nature");
    public static final ResourceKey<ResourcesTypes> WATER = register("water");
    public static final ResourceKey<ResourcesTypes> ICE = register("ice");

    private static ResourceKey<ResourcesTypes> register(String name){
        return ResourceKey.create(ModRegistries.RESOURCES_TYPES_KEY, Identifier.fromNamespaceAndPath(ResourcesTrees.MODID, name));
    }

    public static void bootstrap(BootstrapContext<ResourcesTypes> context){
        context.register(STONE, new ResourcesTypes(Items.COBBLESTONE, 0xFF4D4B49, "item.resourcestrees.stone", 5, 0.25f, 0.5f));
        context.register(COAL, new ResourcesTypes(Items.COAL_BLOCK, 0xFF000000, "item.resourcestrees.coal", 5, 0.25f, 0.5f));
        context.register(IRON, new ResourcesTypes(Items.IRON_BLOCK, 0xFFB0BEC5, "item.resourcestrees.iron", 5, 0.25f, 0.5f));
        context.register(COPPER, new ResourcesTypes(Items.COPPER_BLOCK, 0xFFD46D44, "item.resourcestrees.copper", 5, 0.25f, 0.5f));
        context.register(GOLD, new ResourcesTypes(Items.GOLD_BLOCK, 0xFFFFD600, "item.resourcestrees.gold", 5, 0.25f, 0.5f));
        context.register(LAPIS, new ResourcesTypes(Items.LAPIS_BLOCK, 0xFF3F51B5, "item.resourcestrees.lapis", 5, 0.25f, 0.5f));
        context.register(EMERALD, new ResourcesTypes(Items.EMERALD_BLOCK, 0xFF00C853, "item.resourcestrees.emerald", 5, 0.25f, 0.5f));
        context.register(DIAMOND, new ResourcesTypes(Items.DIAMOND_BLOCK, 0xFF40C4FF, "item.resourcestrees.diamond", 3, 0.20f, 0.4f));
        context.register(OBSIDIAN, new ResourcesTypes(Items.OBSIDIAN, 0xFF2E1A47, "item.resourcestrees.obsidian", 4, 0.25f, 0.5f));
        context.register(AMETHYST, new ResourcesTypes(Items.AMETHYST_BLOCK, 0xFF9C27B0, "item.resourcestrees.amethyst", 5, 0.25f, 0.5f));
        context.register(NETHERITE, new ResourcesTypes(Items.NETHERITE_BLOCK, 0xFF3E3E3E, "item.resourcestrees.netherite", 2, 0.15f, 0.3f));
        context.register(WOOD, new ResourcesTypes(ItemTags.LOGS, 0xFF8D6E63, "item.resourcestrees.wood", 5, 0.25f, 0.5f));
        context.register(QUARTZ, new ResourcesTypes(Items.QUARTZ_BLOCK, 0xFFF5F5F5, "item.resourcestrees.quartz", 5, 0.25f, 0.5f));
        context.register(PRISMARINE, new ResourcesTypes(Items.PRISMARINE, 0xFF5EC8C8, "item.resourcestrees.prismarine", 5, 0.25f, 0.5f));
        context.register(GLOWSTONE, new ResourcesTypes(Items.GLOWSTONE, 0xFFFFF176, "item.resourcestrees.glowstone", 5, 0.25f, 0.5f));
        context.register(REDSTONE, new ResourcesTypes(Items.REDSTONE_BLOCK, 0xFFFF1744, "item.resourcestrees.redstone", 5, 0.25f, 0.5f));
        context.register(DEEPSLATE, new ResourcesTypes(Items.DEEPSLATE, 0xFF2B2B24, "item.resourcestrees.deepslate", 5, 0.25f, 0.5f));
        context.register(DIRT, new ResourcesTypes(Items.DIRT, 0xFF9B7653, "item.resourcestrees.dirt", 5, 0.25f, 0.5f));
        context.register(FIRE, new ResourcesTypes(ModItems.FIRE_ESSENCE.get(), 0xFFE45323 , "item.resourcestrees.fire", 5, 0.25f, 0.5f));
        context.register(NETHER, new ResourcesTypes(Items.NETHERRACK, 0xFF511515 , "item.resourcestrees.nether", 5, 0.25f, 0.5f));
        context.register(END, new ResourcesTypes(ModItems.END_ESSENCE.get(), 0xFFC5BE8B , "item.resourcestrees.end", 5, 0.25f, 0.5f));
        context.register(NATURE, new ResourcesTypes(ModItems.NATURE_ESSENCE.get(), 0xFF1a6e08 , "item.resourcestrees.nature", 5, 0.25f, 0.5f));
        context.register(WATER, new ResourcesTypes(ModItems.WATER_ESSENCE.get(), 0xFF1787D4, "item.resourcestrees.water", 5, 0.25f, 0.5f));
        context.register(ICE, new ResourcesTypes(Items.ICE, 0xFFb9e8ea, "item.resourcestrees.ice", 5, 0.25f, 0.5f));
    }

    public static Map<Identifier, Holder<ResourcesTypes>> getAllResourcesTypes(HolderLookup.Provider provider){
        Stream<Holder.Reference<ResourcesTypes>> registry = provider.lookupOrThrow(ModRegistries.RESOURCES_TYPES_KEY).listElements();
        Map<Identifier, Holder<ResourcesTypes>> types = new HashMap<>();
        registry.forEachOrdered(entry -> {
            if (types.containsKey(entry.key().identifier())){
                throw new IllegalStateException("Duplicate resource index " + entry.key().identifier() + " for " + entry.key().identifier());
            }
            types.put(entry.key().identifier(), entry);
        });
        return types;
    }

    public Holder<ResourcesTypes> asHolder(Level level) {
        return getAllResourcesTypes(level.registryAccess()).values().stream().filter((holder) -> holder.value().equals(this)).findFirst().get();
    }

    public boolean isEmpty(){
        return this.equals(EMPTY);
    }

    public static boolean isSameItemSameType(ItemStack stack, ItemStack other) {
        if (!ItemStack.isSameItem(stack, other)) {
            return false;
        }

        boolean hasType1 = stack.has(ModDataComponents.TYPE);
        boolean hasType2 = other.has(ModDataComponents.TYPE);

        if (hasType1 && hasType2) {
            return Objects.equals(stack.get(ModDataComponents.TYPE), other.get(ModDataComponents.TYPE));
        }

        return !hasType1 && !hasType2;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ResourcesTypes that = (ResourcesTypes) o;
        return color() == that.color() && weight() == that.weight() && Float.compare(saplingChance(), that.saplingChance()) == 0 && Float.compare(secondaryDropChance(), that.secondaryDropChance()) == 0 && Objects.equals(translationKey(), that.translationKey()) && Objects.equals(material(), that.material());
    }

    @Override
    public int hashCode() {
        return Objects.hash(material(), color(), translationKey(), weight(), saplingChance(), secondaryDropChance());
    }
}
